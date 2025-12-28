package fr.driv.n.cook.service.franchise.application.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplication;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplicationPatch;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplicationRequest;
import fr.driv.n.cook.repository.franchise.application.entity.FranchiseApplicationEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ApplicationMapperConfig.class)
public interface FranchiseApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "franchisee", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "paid", constant = "false")
    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(target = "paymentRef", ignore = true)
    @Mapping(target = "note", source = "note")
    FranchiseApplicationEntity toEntity(FranchiseApplicationRequest request);

    FranchiseApplication toDto(FranchiseApplicationEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "paid", source = "paid")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "paymentRef", source = "paymentRef")
    void updateEntityFromPatch(FranchiseApplicationPatch patch, @MappingTarget FranchiseApplicationEntity entity);
}
