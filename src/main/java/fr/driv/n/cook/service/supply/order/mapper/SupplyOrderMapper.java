package fr.driv.n.cook.service.supply.order.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrder;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderItem;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderItemPatch;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderPatch;
import fr.driv.n.cook.repository.supply.order.entity.SupplyOrderEntity;
import fr.driv.n.cook.repository.supply.order.entity.SupplyOrderItemEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ApplicationMapperConfig.class)
public interface SupplyOrderMapper {

    @Mapping(target = "pickupWarehouseId", source = "pickupWarehouse.id")
    @Mapping(target = "franchiseeId", source = "franchisee.id")
    SupplyOrder toDto(SupplyOrderEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pickupWarehouse", ignore = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "paid", source = "paid")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "paymentRef", source = "paymentRef")
    void updateEntityFromPatch(SupplyOrderPatch patch, @MappingTarget SupplyOrderEntity entity);

    @Mapping(target = "supplyOrderId", source = "supplyOrder.id")
    @Mapping(target = "inventoryItemId", source = "inventoryItem.id")
    SupplyOrderItem toDto(SupplyOrderItemEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "quantity", source = "quantity")
    void updateEntityFromPatch(SupplyOrderItemPatch patch, @MappingTarget SupplyOrderItemEntity entity);
}
