package se.poket.microservices.core.order.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.poket.microservices.api.core.order.IbOrder;
import se.poket.microservices.api.core.order.OrderService;
import se.poket.microservices.api.exceptions.InvalidInputException;
import se.poket.microservices.api.exceptions.NotFoundException;
import se.poket.microservices.core.order.persistence.IbOrderEntity;
import se.poket.microservices.core.order.persistence.OrderRepository;
import se.poket.microservices.util.http.ServiceUtil;

import static java.util.logging.Level.FINE;

@RestController
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final ServiceUtil serviceUtil;

    private final OrderRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    private final OrderMapper mapper;

    @Autowired
    public OrderServiceImpl(ServiceUtil serviceUtil, OrderRepository repository, ReactiveMongoTemplate mongoTemplate, OrderMapper mapper) {
        this.serviceUtil = serviceUtil;
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;
    }

    @Override
    public Mono<IbOrder> getOrder(int orderId) {
        if (orderId < 1) {
            throw new InvalidInputException("Invalid orderId: " + orderId);
        }

        LOG.info("Will get order info for id={}", orderId);

        return repository.findByOrderId(orderId).switchIfEmpty(Mono.error(new NotFoundException("No order found for orderId: " + orderId))).log(LOG.getName(), FINE).map(e -> mapper.entityToApi(e)).map(e -> setServiceAddress(e));
    }

    @Override
    public Mono<IbOrder> createOrder(IbOrder body) {
        if (body.getOrderId() < 1) {
            throw new InvalidInputException("Invalid orderId: " + body.getOrderId());
        }

        IbOrderEntity entity = mapper.apiToEntity(body);
        return repository.save(entity).log(LOG.getName(), FINE).onErrorMap(DuplicateKeyException.class, ex -> new InvalidInputException("Duplicate key, Order Id: " + body.getOrderId())).map(e -> mapper.entityToApi(e));
    }

    @Override
    public IbOrder updateOrder(IbOrder body) {
        LOG.info("Updating New Order!");
        IbOrderEntity orderFromDb = repository.findByOrderId(body.getOrderId()).block();
        orderFromDb.setClientId(body.getClientId());
        orderFromDb.setIbOrderId(body.getIbOrderId());
        orderFromDb.setStatus(body.getStatus());
        orderFromDb.setLastFillPrice(body.getLastFillPrice());
        orderFromDb.setLastUpdateDate(body.getLastUpdateDate());
        LOG.info("Status: " + orderFromDb.getStatus());
        return repository.save(orderFromDb).map(mapper::entityToApi).block();
    }

    @Override
    public IbOrder updateOrderStatus(IbOrder body) {
        LOG.info("Updating Order Status!");
        Query query = new Query(Criteria.where("ibOrderId").is(body.getIbOrderId()))
                .addCriteria(Criteria.where("clientId").is(body.getClientId()));
        IbOrderEntity orderFromDb = mongoTemplate.find(query, IbOrderEntity.class).blockFirst();
        if (orderFromDb != null) {
            LOG.info("Order Found!");
            orderFromDb.setStatus(body.getStatus());
            orderFromDb.setLastFillPrice(body.getLastFillPrice());
            orderFromDb.setLastUpdateDate(body.getLastUpdateDate());
            LOG.info("Status: " + orderFromDb.getStatus());
            return repository.save(orderFromDb).map(mapper::entityToApi).block();
        }
        LOG.info("Order Not Found!...");
        return null;
    }

    private IbOrder setServiceAddress(IbOrder e) {
        e.setServiceAddress(serviceUtil.getServiceAddress());
        return e;
    }
}
