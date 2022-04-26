package se.poket.microservices.core.order.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.poket.ib.client.IbBroker;
import se.poket.microservices.api.core.order.IbOrder;
import se.poket.microservices.api.core.order.OrderService;
import se.poket.microservices.api.event.Event;
import se.poket.microservices.api.exceptions.EventProcessingException;

import java.util.Random;
import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final OrderService orderService;

    private IbBroker ibBroker;

    private int orderId = 0;
    private Random rand = new Random();

    @Autowired
    public MessageProcessorConfig(OrderService orderService) {
        this.orderService = orderService;
        this.ibBroker = new IbBroker(orderService);
    }

    @Bean
    public Consumer<Event<Integer, IbOrder>> messageProcessor() {
        return event -> {
            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case CREATE:
                    IbOrder order = event.getData();
                    LOG.info("Create order with ID: {}", order.getOrderId());
                    IbOrder savedOrder = orderService.createOrder(order).block();
                    if (IbBroker.getClientSocket().isConnected())
                        LOG.info("IB Connected: Yes");
                    else {
                        try {
                            this.ibBroker.setClientId(rand.nextInt(15 - 1) + 1);
                            this.ibBroker.connect();
                            LOG.info("IB Status: Connecting...");
                        } catch (InterruptedException e) {
                            LOG.error(e.getMessage());
                        }
                    }

                    if (savedOrder != null) {
                        if (!ibBroker.isInitialized()) {
                            LOG.info("broker not yet initialized. Initializing now...");
                            try {
                                ibBroker.initialize();
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                LOG.error(e.getMessage());
                                throw new EventProcessingException(e.getLocalizedMessage());
                            }
                        }

                        try {
                            ibBroker.setOrderId(++orderId);
                            savedOrder.setClientId(this.ibBroker.getClientId());
                            savedOrder = ibBroker.buyOrder(savedOrder);
                        } catch (Exception e) {
                            LOG.error("exception: " + e + ": " + e.getMessage());
                            throw new EventProcessingException(e.getLocalizedMessage());
                        }
                    }
                    break;

                case DELETE:
                    break;

                default:
                    String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
                    LOG.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
            }

            LOG.info("Message processing done!");
        };
    }
}
