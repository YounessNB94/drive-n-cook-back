package fr.driv.n.cook.service.truck;

import fr.driv.n.cook.presentation.incident.dto.Incident;
import fr.driv.n.cook.presentation.truck.dto.MaintenanceRecord;
import fr.driv.n.cook.presentation.truck.dto.Truck;
import fr.driv.n.cook.repository.incident.IncidentRepository;
import fr.driv.n.cook.repository.truck.MaintenanceRecordRepository;
import fr.driv.n.cook.repository.truck.TruckRepository;
import fr.driv.n.cook.repository.truck.entity.TruckEntity;
import fr.driv.n.cook.service.truck.mapper.TruckMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class TruckService {

    @Inject
    TruckRepository truckRepository;

    @Inject
    IncidentRepository incidentRepository;

    @Inject
    MaintenanceRecordRepository maintenanceRecordRepository;

    @Inject
    TruckMapper mapper;

    public Truck getTruck(Long truckId) {
        return mapper.toDto(fetchTruck(truckId));
    }

    public List<Incident> listIncidents(Long truckId) {
        fetchTruck(truckId); // ensure exists
        return incidentRepository.listByTruck(truckId).stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<MaintenanceRecord> listMaintenance(Long truckId) {
        fetchTruck(truckId);
        return maintenanceRecordRepository.listByTruck(truckId).stream()
                .map(mapper::toDto)
                .toList();
    }

    private TruckEntity fetchTruck(Long truckId) {
        return truckRepository.findByIdOptional(truckId)
                .orElseThrow(() -> new NotFoundException("Camion introuvable"));
    }
}

