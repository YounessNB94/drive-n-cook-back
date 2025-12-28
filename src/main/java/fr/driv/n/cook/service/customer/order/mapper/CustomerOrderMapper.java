package fr.driv.n.cook.service.customer.order.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrder;
import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrderItem;
import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrderPatch;
import fr.driv.n.cook.repository.customer.order.entity.CustomerOrderEntity;
import fr.driv.n.cook.repository.customer.order.entity.CustomerOrderItemEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ApplicationMapperConfig.class)
public interface CustomerOrderMapper {

    @Mapping(target = "loyaltyCardId", source = "loyaltyCard.id")
    CustomerOrder toDto(CustomerOrderEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "paid", source = "paid")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    void updateEntityFromPatch(CustomerOrderPatch patch, @MappingTarget CustomerOrderEntity entity);

    @Mapping(target = "customerOrderId", source = "customerOrder.id")
    @Mapping(target = "menuItemId", source = "menuItem.id")
    CustomerOrderItem toDto(CustomerOrderItemEntity entity);
}

