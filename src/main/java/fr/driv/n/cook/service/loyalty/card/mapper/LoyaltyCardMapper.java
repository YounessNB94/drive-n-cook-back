package fr.driv.n.cook.service.loyalty.card.mapper;

import fr.driv.n.cook.config.ApplicationMapperConfig;
import fr.driv.n.cook.presentation.loyalty.card.dto.LoyaltyCard;
import fr.driv.n.cook.repository.loyalty.card.entity.LoyaltyCardEntity;
import org.mapstruct.Mapper;

@Mapper(config = ApplicationMapperConfig.class)
public interface LoyaltyCardMapper {

    LoyaltyCard toDto(LoyaltyCardEntity entity);
}

