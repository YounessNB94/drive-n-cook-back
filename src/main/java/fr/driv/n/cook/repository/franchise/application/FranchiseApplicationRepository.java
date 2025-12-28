package fr.driv.n.cook.repository.franchise.application;

import fr.driv.n.cook.repository.franchise.application.entity.FranchiseApplicationEntity;
import fr.driv.n.cook.shared.FranchiseApplicationStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FranchiseApplicationRepository implements PanacheRepositoryBase<FranchiseApplicationEntity, Long> {

    public List<FranchiseApplicationEntity> listByFranchiseeId(Long franchiseeId) {
        return list("franchisee.id", franchiseeId);
    }

    public List<FranchiseApplicationEntity> listByStatus(FranchiseApplicationStatus status) {
        return list("status", status);
    }
}
