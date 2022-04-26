package se.poket.microservices.core.order.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import se.poket.microservices.api.core.order.IbOrder;
import se.poket.microservices.core.order.persistence.IbOrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true)
    })
    IbOrder entityToApi(IbOrderEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true), @Mapping(target = "version", ignore = true)
    })
    IbOrderEntity apiToEntity(IbOrder api);
}