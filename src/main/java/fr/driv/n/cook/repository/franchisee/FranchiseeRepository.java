package fr.driv.n.cook.repository.franchisee;

import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FranchiseeRepository implements PanacheRepositoryBase<FranchiseeEntity, Long> {

    public Optional<FranchiseeEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public List<FranchiseeEntity> searchByCompanyName(String fragment) {
        return find("LOWER(companyName) LIKE ?1", "%" + fragment.toLowerCase() + "%").list();
    }
}
