package fr.driv.n.cook.repository.franchise.application.entity;

import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.shared.FranchiseApplicationStatus;
import fr.driv.n.cook.shared.PaymentMethod;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "franchise_applications")
@Getter
@Setter
public class FranchiseApplicationEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchisee_id", nullable = false)
    private FranchiseeEntity franchisee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FranchiseApplicationStatus status;

    @Column(nullable = false)
    private boolean paid;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 30)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_ref", length = 100)
    private String paymentRef;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(length = 1000)
    private String note;
}
