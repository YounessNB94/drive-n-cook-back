package fr.driv.n.cook.service.franchisee;

import fr.driv.n.cook.presentation.franchisee.dto.Franchisee;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseePatch;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseeRegistration;
import fr.driv.n.cook.repository.franchisee.FranchiseeRepository;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.service.franchisee.mapper.FranchiseeMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

@ApplicationScoped
public class FranchiseeService {

    @Inject
    FranchiseeRepository repository;

    @Inject
    FranchiseeMapper mapper;

    @Transactional
    public Franchisee register(FranchiseeRegistration registration) {
        repository.findByEmail(registration.email()).ifPresent(existing -> {
            throw new BadRequestException("Un franchisé existe déjà avec cet e-mail");
        });

        FranchiseeEntity entity = mapper.toEntity(registration);
        entity.setPasswordHash(hashPassword(registration.password()));
        repository.persist(entity);
        return mapper.toDto(entity);
    }

    public Franchisee getById(Long franchiseeId) {
        return mapper.toDto(fetchFranchisee(franchiseeId));
    }

    @Transactional
    public Franchisee updateProfile(Long franchiseeId, FranchiseePatch patch) {
        FranchiseeEntity entity = fetchFranchisee(franchiseeId);
        mapper.updateEntityFromPatch(patch, entity);
        return mapper.toDto(entity);
    }

    public List<Franchisee> searchByCompanyName(String fragment) {
        if (fragment == null || fragment.isBlank()) {
            return List.of();
        }
        return repository.searchByCompanyName(fragment).stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<Franchisee> listAll() {
        return repository.listAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    private FranchiseeEntity fetchFranchisee(Long franchiseeId) {
        return repository.findByIdOptional(franchiseeId)
                .orElseThrow(() -> new NotFoundException("Franchisé introuvable"));
    }

    private String hashPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algorithme SHA-256 indisponible", e);
        }
    }
}
