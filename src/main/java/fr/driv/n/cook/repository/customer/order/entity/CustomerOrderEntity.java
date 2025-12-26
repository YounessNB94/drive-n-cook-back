package fr.driv.n.cook.repository.customer.order.entity;

import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.repository.loyalty.card.entity.LoyaltyCardEntity;
import fr.driv.n.cook.repository.menu.entity.MenuItemEntity;
import fr.driv.n.cook.repository.sale.entity.SaleEntity;
import fr.driv.n.cook.shared.CustomerOrderStatus;
import fr.driv.n.cook.shared.CustomerOrderType;
import fr.driv.n.cook.shared.PaymentMethod;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_orders")
@Getter
@Setter
public class CustomerOrderEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CustomerOrderType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CustomerOrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchisee_id", nullable = false)
    private FranchiseeEntity franchisee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loyalty_card_id")
    private LoyaltyCardEntity loyaltyCard;

    @Column(nullable = false)
    private boolean paid;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 30)
    private PaymentMethod paymentMethod;

    @Column(name = "total_cash", precision = 10, scale = 2)
    private BigDecimal totalCash;

    @Column(name = "total_points")
    private Integer totalPoints;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CustomerOrderItemEntity> items = new ArrayList<>();

    @OneToMany(mappedBy = "customerOrder", fetch = FetchType.LAZY)
    private List<SaleEntity> sales = new ArrayList<>();
}

