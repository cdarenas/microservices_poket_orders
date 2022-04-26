package se.poket.microservices.api.core.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import reactor.core.publisher.Mono;
import se.poket.microservices.api.exceptions.NotFoundException;

public interface OrderService {
    @GetMapping(value = "/order/{orderId}", produces = "application/json")
    Mono<IbOrder> getOrder(@PathVariable int orderId) throws NotFoundException;

    @PostMapping(value = "/order", consumes = "application/json", produces = "application/json")
    Mono<IbOrder> createOrder(IbOrder body);

    @PutMapping(value = "/order", consumes = "application/json", produces = "application/json")
    IbOrder updateOrder(IbOrder body);

    @PutMapping(value = "/order/status", consumes = "application/json", produces = "application/json")
    IbOrder updateOrderStatus(IbOrder body);
}
