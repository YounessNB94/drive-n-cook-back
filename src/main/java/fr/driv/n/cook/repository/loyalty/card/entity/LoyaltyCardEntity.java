package fr.driv.n.cook.repository.loyalty.card.entity;

import fr.driv.n.cook.repository.customer.order.entity.CustomerOrderEntity;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loyalty_cards")
@Getter
@Setter
public class LoyaltyCardEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_ref", nullable = false, length = 150)
    private String customerRef;

    @Column(name = "points_balance", nullable = false)
    private Integer pointsBalance;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "loyaltyCard", fetch = FetchType.LAZY)
    private List<CustomerOrderEntity> orders = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchisee_id", nullable = false)
    private FranchiseeEntity franchisee;

}
