package fr.driv.n.cook.repository.franchise.term.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "franchise_terms")
@Getter
@Setter
public class FranchiseTermsEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String version;

    @Column(name = "entry_fee_text", nullable = false, length = 100)
    private String entryFeeText;

    @Column(name = "royalty_text", nullable = false, length = 100)
    private String royaltyText;

    @Column(name = "supply_rule_text", nullable = false, length = 150)
    private String supplyRuleText;

    @Lob
    @Column(nullable = false)
    private String content;
}
