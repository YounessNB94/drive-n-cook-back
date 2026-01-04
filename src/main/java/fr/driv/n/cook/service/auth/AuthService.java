package fr.driv.n.cook.service.auth;

import fr.driv.n.cook.presentation.auth.dto.AuthTokenResponse;
import fr.driv.n.cook.presentation.auth.dto.FranchiseeLoginRequest;
import fr.driv.n.cook.presentation.franchisee.dto.Franchisee;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseeRegistration;
import fr.driv.n.cook.repository.franchisee.FranchiseeRepository;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.service.franchisee.mapper.FranchiseeMapper;
import fr.driv.n.cook.shared.FranchiseeRole;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Inject
    FranchiseeRepository franchiseeRepository;

    @Inject
    FranchiseeMapper franchiseeMapper;

    @ConfigProperty(name = "app.auth.token.ttl-seconds", defaultValue = "259200")
    long tokenTtlSeconds;

    @ConfigProperty(name = "app.auth.token.issuer", defaultValue = "driv-n-cook")
    String tokenIssuer;

    public AuthTokenResponse login(FranchiseeLoginRequest request) {
        FranchiseeEntity franchisee = franchiseeRepository.findByEmail(request.email())
                .orElseThrow(this::invalidCredentials);
        if (!BcryptUtil.matches(request.password(), franchisee.getPasswordHash())) {
            throw invalidCredentials();
        }
        return issueToken(franchisee.getId(), franchisee.getEmail(), franchisee.getRole());
    }

    @Transactional
    public AuthTokenResponse signup(FranchiseeRegistration registration) {
        franchiseeRepository.findByEmail(registration.email()).ifPresent(existing -> {
            throw new BadRequestException("Un franchisé existe déjà avec cet e-mail");
        });

        FranchiseeEntity entity = franchiseeMapper.toEntity(registration);
        entity.setPasswordHash(hashPassword(registration.password()));
        franchiseeRepository.persist(entity);
        Franchisee created = franchiseeMapper.toDto(entity);
        return issueToken(created.id(), created.email(), entity.getRole());
    }

    @Transactional
    public Franchisee promoteFranchisee(Long franchiseeId) {
        FranchiseeEntity entity = franchiseeRepository.findByIdOptional(franchiseeId)
                .orElseThrow(() -> new NotFoundException("Franchisé introuvable"));
        entity.setRole(FranchiseeRole.ADMIN);
        return franchiseeMapper.toDto(entity);
    }

    private AuthTokenResponse issueToken(Long franchiseeId, String email, FranchiseeRole role) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(tokenTtlSeconds);
        String jwt = Jwt.claims()
                .issuer(tokenIssuer)
                .subject(String.valueOf(franchiseeId))
                .upn(email)
                .groups(Set.of(role.name()))
                .issuedAt(now)
                .expiresAt(expiresAt)
                .sign();
        return new AuthTokenResponse(jwt, expiresAt);
    }

    private NotAuthorizedException invalidCredentials() {
        return new NotAuthorizedException("Identifiants invalides");
    }

    private String hashPassword(String rawPassword) {
        return BcryptUtil.bcryptHash(rawPassword);
    }
}
