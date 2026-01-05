package fr.driv.n.cook.service.franchise.application;

import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplication;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplicationPatch;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplicationRequest;
import fr.driv.n.cook.repository.franchise.application.FranchiseApplicationRepository;
import fr.driv.n.cook.repository.franchise.application.entity.FranchiseApplicationEntity;
import fr.driv.n.cook.repository.franchisee.FranchiseeRepository;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.service.franchise.application.mapper.FranchiseApplicationMapper;
import fr.driv.n.cook.shared.FranchiseApplicationStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class FranchiseApplicationService {

    @Inject
    FranchiseApplicationRepository repository;

    @Inject
    FranchiseeRepository franchiseeRepository;

    @Inject
    FranchiseApplicationMapper mapper;

    @Transactional
    public FranchiseApplication submit(Long franchiseeId, FranchiseApplicationRequest request) {
        FranchiseeEntity franchisee = fetchFranchisee(franchiseeId);
        FranchiseApplicationEntity entity = mapper.toEntity(request);
        entity.setFranchisee(franchisee);
        entity.setStatus(FranchiseApplicationStatus.PENDING);
        repository.persist(entity);
        return mapper.toDto(entity);
    }

    public List<FranchiseApplication> listForFranchisee(Long franchiseeId) {
        FranchiseeEntity franchisee = fetchFranchisee(franchiseeId);
        return repository.list("franchisee", franchisee).stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<FranchiseApplication> listAll(FranchiseApplicationStatus status) {
        List<FranchiseApplicationEntity> entities = status != null
                ? repository.listByStatus(status)
                : repository.listAll();
        return entities.stream().map(mapper::toDto).toList();
    }

    @Transactional
    public FranchiseApplication updatePayment(Long applicationId, FranchiseApplicationPatch patch) {
        FranchiseApplicationEntity entity = fetchApplication(applicationId);
        mapper.updateEntityFromPatch(patch, entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public FranchiseApplication updateStatus(Long applicationId, FranchiseApplicationStatus status) {
        FranchiseApplicationEntity entity = fetchApplication(applicationId);
        if (status.equals(FranchiseApplicationStatus.APPROVED) && !entity.isPaid()) {
            throw new BadRequestException("La demande doit être payée avant validation");
        }
        entity.setStatus(status);
        return mapper.toDto(entity);
    }

    private FranchiseeEntity fetchFranchisee(Long franchiseeId) {
        return franchiseeRepository.findByIdOptional(franchiseeId)
                .orElseThrow(() -> new NotFoundException("Franchisé introuvable"));
    }

    private FranchiseApplicationEntity fetchApplication(Long applicationId) {
        return repository.findByIdOptional(applicationId)
                .orElseThrow(() -> new NotFoundException("Demande introuvable"));
    }

    public FranchiseApplication getById(Long applicationId) {
        FranchiseApplicationEntity entity = fetchApplication(applicationId);
        return mapper.toDto(entity);
    }
}
