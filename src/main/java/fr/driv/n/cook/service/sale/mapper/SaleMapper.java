package fr.driv.n.cook.service.sale.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.sale.dto.Sale;
import fr.driv.n.cook.repository.sale.entity.SaleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = ApplicationMapperConfig.class)
public interface SaleMapper {

    @Mapping(target = "menuItemId", source = "menuItem.id")
    Sale toDto(SaleEntity entity);
}

