package fr.driv.n.cook.service.franchise.term;

import fr.driv.n.cook.presentation.franchise.term.dto.FranchiseTerm;
import fr.driv.n.cook.repository.franchise.term.FranchiseTermsRepository;
import fr.driv.n.cook.repository.franchise.term.entity.FranchiseTermsEntity;
import fr.driv.n.cook.service.franchise.term.mapper.FranchiseTermMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FranchiseTermService {

    @Inject
    FranchiseTermsRepository repository;

    @Inject
    FranchiseTermMapper mapper;

    public FranchiseTerm getLatest() {
        FranchiseTermsEntity entity = repository.find("order by version desc")
                .firstResultOptional()
                .orElseThrow(() -> new NotFoundException("Aucune condition de franchise disponible"));
        return mapper.toDto(entity);
    }
}
