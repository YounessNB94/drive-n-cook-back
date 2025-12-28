package fr.driv.n.cook.service.franchisee.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.franchisee.dto.Franchisee;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseePatch;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseeRegistration;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ApplicationMapperConfig.class)
public interface FranchiseeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "trucks", ignore = true)
    @Mapping(target = "passwordHash", expression = "java(registration.password())")
    FranchiseeEntity toEntity(FranchiseeRegistration registration);

    Franchisee toDto(FranchiseeEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "address", source = "address")
    void updateEntityFromPatch(FranchiseePatch patch, @MappingTarget FranchiseeEntity entity);
}
