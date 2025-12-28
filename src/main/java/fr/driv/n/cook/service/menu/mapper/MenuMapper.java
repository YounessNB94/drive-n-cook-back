package fr.driv.n.cook.service.menu.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.menu.dto.Menu;
import fr.driv.n.cook.presentation.menu.dto.MenuItem;
import fr.driv.n.cook.presentation.menu.dto.MenuItemPatch;
import fr.driv.n.cook.presentation.menu.dto.MenuPatch;
import fr.driv.n.cook.repository.menu.entity.MenuEntity;
import fr.driv.n.cook.repository.menu.entity.MenuItemEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ApplicationMapperConfig.class)
public interface MenuMapper {

    Menu toDto(MenuEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    void updateEntityFromPatch(MenuPatch patch, @MappingTarget MenuEntity entity);

    @Mapping(target = "menuId", source = "menu.id")
    MenuItem toDto(MenuItemEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "priceCash", source = "priceCash")
    @Mapping(target = "pointsPrice", source = "pointsPrice")
    @Mapping(target = "available", source = "available")
    void updateEntityFromPatch(MenuItemPatch patch, @MappingTarget MenuItemEntity entity);
}

