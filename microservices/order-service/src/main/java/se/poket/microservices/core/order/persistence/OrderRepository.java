package se.poket.microservices.core.order.persistence;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<IbOrderEntity, String> {

    Mono<IbOrderEntity> findByOrderId(int orderId);

    Mono<IbOrderEntity> findByIbOrderId(int ibOrderId);
}
