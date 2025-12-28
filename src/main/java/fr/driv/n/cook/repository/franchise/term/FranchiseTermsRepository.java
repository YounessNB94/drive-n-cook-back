package fr.driv.n.cook.repository.franchise.term;

import fr.driv.n.cook.repository.franchise.term.entity.FranchiseTermsEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class FranchiseTermsRepository implements PanacheRepositoryBase<FranchiseTermsEntity, Long> {

    public Optional<FranchiseTermsEntity> findByVersion(String version) {
        return find("version", version).firstResultOptional();
    }
}

