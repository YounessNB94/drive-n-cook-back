package fr.driv.n.cook.repository.truck.entity;

import fr.driv.n.cook.repository.appointment.entity.AppointmentEntity;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.repository.incident.entity.IncidentEntity;
import fr.driv.n.cook.repository.warehouse.entity.WarehouseEntity;
import fr.driv.n.cook.shared.TruckStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trucks")
@Getter
@Setter
public class TruckEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate_number", nullable = false, length = 20)
    private String plateNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TruckStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchisee_id")
    private FranchiseeEntity franchisee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_warehouse_id")
    private WarehouseEntity currentWarehouse;

    @OneToMany(mappedBy = "truck", fetch = FetchType.LAZY)
    private List<AppointmentEntity> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "truck", fetch = FetchType.LAZY)
    private List<IncidentEntity> incidents = new ArrayList<>();

    @OneToMany(mappedBy = "truck", fetch = FetchType.LAZY)
    private List<MaintenanceRecordEntity> maintenanceRecords = new ArrayList<>();
}
