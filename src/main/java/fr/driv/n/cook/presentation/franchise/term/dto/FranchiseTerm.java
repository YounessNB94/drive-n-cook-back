package fr.driv.n.cook.presentation.franchise.term.dto;

public record FranchiseTerm(
        String version,
        String entryFeeText,
        String royaltyText,
        String supplyRuleText,
        String content
) {
}
