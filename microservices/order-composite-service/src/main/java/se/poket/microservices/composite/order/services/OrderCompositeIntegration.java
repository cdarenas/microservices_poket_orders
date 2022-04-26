package se.poket.microservices.composite.order.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import se.poket.microservices.api.core.order.IbOrder;
import se.poket.microservices.api.core.order.OrderService;
import se.poket.microservices.api.event.Event;
import se.poket.microservices.api.exceptions.InvalidInputException;
import se.poket.microservices.api.exceptions.NotFoundException;
import se.poket.microservices.util.http.HttpErrorInfo;

import java.io.IOException;

import static java.util.logging.Level.FINE;
import static se.poket.microservices.api.event.Event.Type.CREATE;

@Component
public class OrderCompositeIntegration implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderCompositeIntegration.class);

    private static final String ORDER_SERVICE_URL = "http://order";
    private static final String PORTFOLIO_SERVICE_URL = "http://portfolio";

    private final WebClient webClient;
    private final ObjectMapper mapper;

    private final StreamBridge streamBridge;

    private final Scheduler publishEventScheduler;

    @Autowired
    public OrderCompositeIntegration(@Qualifier("publishEventScheduler") Scheduler publishEventScheduler, WebClient.Builder webClient, ObjectMapper mapper, StreamBridge streamBridge) {

        this.publishEventScheduler = publishEventScheduler;
        this.webClient = webClient.build();
        this.mapper = mapper;
        this.streamBridge = streamBridge;
    }

    @Override
    public Mono<IbOrder> getOrder(int orderId) throws NotFoundException {
        String url = ORDER_SERVICE_URL + "/order/" + orderId;
        LOG.debug("Will call the getOrder API on URL: {}", url);

        return webClient.get().uri(url).retrieve().bodyToMono(IbOrder.class).log(LOG.getName(), FINE).onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
    }

    @Override
    public Mono<IbOrder> createOrder(IbOrder body) {
        return Mono.fromCallable(() -> {
            sendMessage("orders-out-0", new Event(CREATE, body.getOrderId(), body));
            return body;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public IbOrder updateOrder(IbOrder body) {
        return null;
    }

    @Override
    public IbOrder updateOrderStatus(IbOrder body) {
        return null;
    }

    public Mono<Health> getOrderHealth() {
        return getHealth(ORDER_SERVICE_URL);
    }

    private Mono<Health> getHealth(String url) {
        url += "/actuator/health";
        LOG.debug("Will call the Health API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
                .map(s -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
                .log(LOG.getName(), FINE);
    }

    private void sendMessage(String bindingName, Event event) {
        LOG.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message message = MessageBuilder.withPayload(event)
                .setHeader("partitionKey", event.getKey())
                .build();
        streamBridge.send(bindingName, message);
    }

    private Throwable handleException(Throwable ex) {

        if (!(ex instanceof WebClientResponseException)) {
            LOG.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
            return ex;
        }

        WebClientResponseException wcre = (WebClientResponseException) ex;

        switch (wcre.getStatusCode()) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(wcre));

            case UNPROCESSABLE_ENTITY:
                return new InvalidInputException(getErrorMessage(wcre));

            default:
                LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
                LOG.warn("Error body: {}", wcre.getResponseBodyAsString());
                return ex;
        }
    }

    private String getErrorMessage(WebClientResponseException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
