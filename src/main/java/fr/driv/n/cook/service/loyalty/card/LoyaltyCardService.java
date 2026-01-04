package fr.driv.n.cook.service.loyalty.card;

import fr.driv.n.cook.presentation.loyalty.card.dto.LoyaltyCard;
import fr.driv.n.cook.repository.loyalty.card.LoyaltyCardRepository;
import fr.driv.n.cook.repository.loyalty.card.entity.LoyaltyCardEntity;
import fr.driv.n.cook.service.franchisee.FranchiseeService;
import fr.driv.n.cook.service.loyalty.card.mapper.LoyaltyCardMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class LoyaltyCardService {

    @Inject
    LoyaltyCardRepository loyaltyCardRepository;

    @Inject
    LoyaltyCardMapper mapper;

    @Inject
    FranchiseeService franchiseeService;

    @Transactional
    public LoyaltyCard createCard(Long franchiseeId) {
        LoyaltyCardEntity entity = new LoyaltyCardEntity();
        entity.setCustomerRef(generateCustomerRef());
        entity.setPointsBalance(0);
        entity.setFranchisee(franchiseeService.fetchFranchisee(franchiseeId));
        loyaltyCardRepository.persist(entity);
        return mapper.toDto(entity);
    }

    public LoyaltyCard getCard(Long cardId) {
        return mapper.toDto(fetchCard(cardId));
    }

    public List<LoyaltyCard> listAllCards() {
        return loyaltyCardRepository.listAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public LoyaltyCard getByCustomerRef(String customerRef) {
        return loyaltyCardRepository.findByCustomerRef(customerRef)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Carte introuvable"));
    }

    @Transactional
    public LoyaltyCard updatePoints(Long cardId, int delta) {
        LoyaltyCardEntity entity = fetchCard(cardId);
        int newBalance = entity.getPointsBalance() + delta;
        if (newBalance < 0) {
            throw new IllegalArgumentException("Solde insuffisant");
        }
        entity.setPointsBalance(newBalance);
        return mapper.toDto(entity);
    }

    public List<LoyaltyCard> listForFranchisee(Long franchiseeId) {
        return loyaltyCardRepository.find("franchisee.id", franchiseeId).stream()
                .map(mapper::toDto)
                .toList();
    }

    public LoyaltyCard getCardForFranchisee(Long cardId, Long franchiseeId) {
        LoyaltyCardEntity entity = fetchCard(cardId);
        if (!entity.getFranchisee().getId().equals(franchiseeId)) {
            throw new NotFoundException("Carte introuvable");
        }
        return mapper.toDto(entity);
    }

    private LoyaltyCardEntity fetchCard(Long cardId) {
        return loyaltyCardRepository.findByIdOptional(cardId)
                .orElseThrow(() -> new NotFoundException("Carte introuvable"));
    }

    private String generateCustomerRef() {
        // Implémentation de la génération de la référence client a parti la date actuellle et un nombre aléatoire
        return "CUST-" + System.currentTimeMillis();

    }
}
