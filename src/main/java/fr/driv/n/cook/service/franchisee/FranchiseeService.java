package fr.driv.n.cook.service.franchisee;

import fr.driv.n.cook.presentation.franchisee.dto.Franchisee;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseePatch;
import fr.driv.n.cook.repository.franchisee.FranchiseeRepository;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.service.franchisee.mapper.FranchiseeMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FranchiseeService {

    @Inject
    FranchiseeRepository repository;

    @Inject
    FranchiseeMapper mapper;

    public Franchisee getById(Long franchiseeId) {
        return mapper.toDto(fetchFranchisee(franchiseeId));
    }

    @Transactional
    public Franchisee updateProfile(Long franchiseeId, FranchiseePatch patch) {
        FranchiseeEntity entity = fetchFranchisee(franchiseeId);
        mapper.updateEntityFromPatch(patch, entity);
        return mapper.toDto(entity);
    }

    public List<Franchisee> listAll() {
        return repository.listAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public FranchiseeEntity fetchFranchisee(Long franchiseeId) {
        return repository.findByIdOptional(franchiseeId)
                .orElseThrow(() -> new NotFoundException("Franchisé introuvable"));
    }

    public Optional<FranchiseeEntity> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
