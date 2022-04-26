package se.poket.microservices.composite.order.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.poket.microservices.api.composite.IbOrderAggregate;
import se.poket.microservices.api.composite.OrderCompositeService;
import se.poket.microservices.api.composite.ServiceAddresses;
import se.poket.microservices.api.core.order.IbOrder;
import se.poket.microservices.util.http.ServiceUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.logging.Level.FINE;

@SuppressWarnings("rawtypes")
@RestController
public class OrderCompositeServiceImpl implements OrderCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderCompositeServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final OrderCompositeIntegration integration;

    @Autowired
    public OrderCompositeServiceImpl(ServiceUtil serviceUtil, OrderCompositeIntegration integration) {

        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public Mono<IbOrderAggregate> getOrder(int orderId) {
        LOG.info("Will get composite order info for order.id={}", orderId);
        return Mono.zip(values -> createOrderAggregate((IbOrder) values[0], serviceUtil.getServiceAddress()), integration.getOrder(orderId)).doOnError(ex -> LOG.warn("getCompositeOrder failed: {}", ex.toString())).log(LOG.getName(), FINE);
    }

    @Override
    public Mono<Void> createOrder(IbOrderAggregate body) {
        try {
            List<Mono> monoList = new ArrayList<>();

            LOG.debug("createCompositeOrder: creates a new composite entity for orderId: {}", body.getOrderId());

            IbOrder order = new IbOrder(body.getOrderId(), 0, body.getSymbol(), body.getExchange(), body.getCurrency(), body.getType(), body.getLastFillPrice(), body.getNumContracts(), "Pending", null);
            order.setCreatedDate(new Date());
            monoList.add(integration.createOrder(order));

            LOG.debug("createCompositeOrder: composite entities created for orderId: {}", body.getOrderId());

            return Mono.zip(r -> "", monoList.toArray(new Mono[0])).doOnError(ex -> LOG.warn("createCompositeOrder failed: {}", ex.toString())).then();

        } catch (RuntimeException re) {
            LOG.warn("createCompositeOrder failed: {}", re.toString());
            throw re;
        }
    }

    @Override
    public Mono<Void> createOrders(List<IbOrderAggregate> body) {
        try {
            List<Mono> monoList = new ArrayList<>();

            for (IbOrderAggregate order : body) {
                LOG.debug("createCompositeOrder: creates a new composite entity for orderId: {}", order.getOrderId());

                IbOrder newOrder = new IbOrder(order.getOrderId(), 0, order.getSymbol(), order.getExchange(), order.getCurrency(), order.getType(), order.getLastFillPrice(), order.getNumContracts(), "Pending", null);
                newOrder.setCreatedDate(new Date());
                monoList.add(integration.createOrder(newOrder));

                LOG.debug("createCompositeOrder: composite entities created for orderId: {}", order.getOrderId());
            }

            return Mono.zip(r -> "", monoList.toArray(new Mono[0])).doOnError(ex -> LOG.warn("createCompositeOrder failed: {}", ex.toString())).then();

        } catch (RuntimeException re) {
            LOG.warn("createCompositeOrder failed: {}", re.toString());
            throw re;
        }
    }

    private IbOrderAggregate createOrderAggregate(IbOrder order, String serviceAddress) {

        // 1. Setup order info
        int orderId = order.getOrderId();
        String symbol = order.getSymbol();
        String exchange = order.getExchange();
        String currency = order.getCurrency();
        String type = order.getType();
        Double lastFillPrice = order.getLastFillPrice();
        int numContracts = order.getNumContracts();
        Date createdDate = order.getCreatedDate();
        Date sentDate = order.getSentDate();
        Date lastUpdateDate = order.getLastUpdateDate();
        String status = order.getStatus();

        // 4. Create info regarding the involved microservices addresses
        String orderAddress = order.getServiceAddress();
        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, orderAddress);

        return new IbOrderAggregate(orderId, symbol, exchange, currency, type, lastFillPrice, numContracts, status, createdDate, sentDate, lastUpdateDate, serviceAddresses);
    }
}
