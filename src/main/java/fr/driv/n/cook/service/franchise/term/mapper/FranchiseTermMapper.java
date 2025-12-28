package fr.driv.n.cook.service.franchise.term.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.franchise.term.dto.FranchiseTerm;
import fr.driv.n.cook.repository.franchise.term.entity.FranchiseTermsEntity;
import org.mapstruct.Mapper;

@Mapper(config = ApplicationMapperConfig.class)
public interface FranchiseTermMapper {

    FranchiseTerm toDto(FranchiseTermsEntity entity);
}

