package fr.driv.n.cook.service.loyalty.card.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.loyalty.card.dto.LoyaltyCard;
import fr.driv.n.cook.repository.loyalty.card.entity.LoyaltyCardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = ApplicationMapperConfig.class)
public interface LoyaltyCardMapper {

    @Mapping(target = "franchiseeId", source = "franchisee.id")
    LoyaltyCard toDto(LoyaltyCardEntity entity);
}
