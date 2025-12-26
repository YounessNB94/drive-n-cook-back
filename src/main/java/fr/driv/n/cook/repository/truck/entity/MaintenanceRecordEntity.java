package fr.driv.n.cook.repository.truck.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_records")
@Getter
@Setter
public class MaintenanceRecordEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false)
    private TruckEntity truck;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, length = 500)
    private String description;
}

