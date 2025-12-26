package fr.driv.n.cook.repository.appointment.entity;

import fr.driv.n.cook.repository.supply.order.entity.SupplyOrderEntity;
import fr.driv.n.cook.repository.truck.entity.TruckEntity;
import fr.driv.n.cook.repository.warehouse.entity.WarehouseEntity;
import fr.driv.n.cook.shared.AppointmentStatus;
import fr.driv.n.cook.shared.AppointmentType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class AppointmentEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AppointmentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AppointmentStatus status;

    @Column(nullable = false)
    private LocalDateTime datetime;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private WarehouseEntity warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supply_order_id")
    private SupplyOrderEntity supplyOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id")
    private TruckEntity truck;
}

