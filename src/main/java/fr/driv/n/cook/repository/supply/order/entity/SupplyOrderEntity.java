package fr.driv.n.cook.repository.supply.order.entity;

import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.repository.warehouse.entity.WarehouseEntity;
import fr.driv.n.cook.shared.PaymentMethod;
import fr.driv.n.cook.shared.SupplyOrderStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "supply_orders")
@Getter
@Setter
public class SupplyOrderEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SupplyOrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchisee_id", nullable = false)
    private FranchiseeEntity franchisee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_warehouse_id")
    private WarehouseEntity pickupWarehouse;

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

    @OneToMany(mappedBy = "supplyOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SupplyOrderItemEntity> items = new ArrayList<>();
}
