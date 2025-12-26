package fr.driv.n.cook.repository.warehouse.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
public class InventoryItemEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private WarehouseEntity warehouse;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 30)
    private String unit;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;
}

