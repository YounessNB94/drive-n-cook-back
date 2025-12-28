package fr.driv.n.cook.repository.loyalty.card;

import fr.driv.n.cook.repository.loyalty.card.entity.LoyaltyCardEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class LoyaltyCardRepository implements PanacheRepositoryBase<LoyaltyCardEntity, Long> {

    public Optional<LoyaltyCardEntity> findByCustomerRef(String customerRef) {
        return find("customerRef", customerRef).firstResultOptional();
    }
}
