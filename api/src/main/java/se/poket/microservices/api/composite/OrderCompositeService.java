package se.poket.microservices.api.composite;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "OrderComposite", description = "REST API for composite order information.")
public interface OrderCompositeService {

    @Operation(
            summary = "${api.order-composite.get-composite-order.description}",
            description = "${api.order-composite.get-composite-order.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @GetMapping(
            value = "/order-composite/{orderId}",
            produces = "application/json")
    Mono<IbOrderAggregate> getOrder(@PathVariable int orderId);

    @Operation(
            summary = "${api.order-composite.create-composite-order.description}",
            description = "${api.order-composite.create-composite-order.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(
            value = "/order-composite",
            consumes = "application/json")
    Mono<Void> createOrder(@RequestBody IbOrderAggregate body);

    @Operation(
            summary = "${api.order-composite.create-composite-orders.description}",
            description = "${api.order-composite.create-composite-orders.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(
            value = "/order-composite/orders",
            consumes = "application/json")
    Mono<Void> createOrders(@RequestBody List<IbOrderAggregate> body);
}
