package fr.driv.n.cook.service.incident;

import fr.driv.n.cook.presentation.incident.dto.Incident;
import fr.driv.n.cook.presentation.incident.dto.IncidentPatch;
import fr.driv.n.cook.repository.incident.IncidentRepository;
import fr.driv.n.cook.repository.incident.entity.IncidentEntity;
import fr.driv.n.cook.repository.truck.TruckRepository;
import fr.driv.n.cook.repository.truck.entity.TruckEntity;
import fr.driv.n.cook.service.truck.mapper.TruckMapper;
import fr.driv.n.cook.shared.IncidentStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class IncidentService {

    @Inject
    IncidentRepository incidentRepository;

    @Inject
    TruckRepository truckRepository;

    @Inject
    TruckMapper mapper;

    public Incident getIncident(Long incidentId) {
        return mapper.toDto(fetchIncident(incidentId));
    }

    @Transactional
    public Incident createIncident(Long truckId, Incident incident) {
        TruckEntity truck = fetchTruck(truckId);
        IncidentEntity entity = new IncidentEntity();
        entity.setTruck(truck);
        entity.setDescription(incident.description());
        entity.setStatus(incident.status() != null ? incident.status() : IncidentStatus.OPEN);
        incidentRepository.persist(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public Incident patchIncident(Long incidentId, IncidentPatch patch) {
        IncidentEntity entity = fetchIncident(incidentId);
        if (patch.description() != null) {
            entity.setDescription(patch.description());
        }
        if (patch.status() != null) {
            validateTransition(entity.getStatus(), patch.status());
            entity.setStatus(patch.status());
        }
        return mapper.toDto(entity);
    }

    private void validateTransition(IncidentStatus current, IncidentStatus next) {
        if (current == IncidentStatus.RESOLVED) {
            throw new BadRequestException("Incident déjà résolu");
        }
        if (current == IncidentStatus.OPEN && next == IncidentStatus.IN_PROGRESS) {
            return;
        }
        if (current == IncidentStatus.IN_PROGRESS && next == IncidentStatus.RESOLVED) {
            return;
        }
        throw new BadRequestException("Transition invalide");
    }

    private IncidentEntity fetchIncident(Long incidentId) {
        return incidentRepository.findByIdOptional(incidentId)
                .orElseThrow(() -> new NotFoundException("Incident introuvable"));
    }

    private TruckEntity fetchTruck(Long truckId) {
        return truckRepository.findByIdOptional(truckId)
                .orElseThrow(() -> new NotFoundException("Camion introuvable"));
    }
}
