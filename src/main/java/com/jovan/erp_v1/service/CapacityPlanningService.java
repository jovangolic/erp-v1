package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.CapacityPlanningErrorException;
import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.mapper.CapacityPlanningMapper;
import com.jovan.erp_v1.model.CapacityPlanning;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.CapacityPlanningRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.request.CapacityPlanningRequest;
import com.jovan.erp_v1.response.CapacityPlanningResponse;
import com.jovan.erp_v1.util.DateValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CapacityPlanningService implements ICapacityPlanningService {

    private final CapacityPlanningRepository capacityPlanningRepository;
    private final CapacityPlanningMapper capacityPlanningMapper;
    private final WorkCenterRepository workCenterRepository;

    @Transactional
    @Override
    public CapacityPlanningResponse create(CapacityPlanningRequest request) {
        WorkCenter workCenter = workCenterRepository.findById(request.workCenterId())
                .orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found"));
        DateValidator.validateNotNull(request.date(), "Datum ne sme biti null");
        validateAvailableCapacity(request.availableCapacity());
        validatePlannedLoad(request.plannedLoad());
        calculateRemainingCapacity(request.availableCapacity(), request.plannedLoad());
        CapacityPlanning entity = capacityPlanningMapper.toEntity(request, workCenter);
        CapacityPlanning saved = capacityPlanningRepository.save(entity);
        return capacityPlanningMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public CapacityPlanningResponse update(Long id, CapacityPlanningRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        CapacityPlanning cp = capacityPlanningRepository.findById(id)
                .orElseThrow(() -> new CapacityPlanningErrorException("CapacityPlanning not found with id: " + id));
        WorkCenter workCenter = workCenterRepository.findById(request.workCenterId())
                .orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found"));
        DateValidator.validateNotNull(request.date(), "Datum ne sme biti null");
        validateAvailableCapacity(request.availableCapacity());
        validatePlannedLoad(request.plannedLoad());
        calculateRemainingCapacity(request.availableCapacity(), request.plannedLoad());
        capacityPlanningMapper.toUpdateEntity(cp, request, workCenter);
        return capacityPlanningMapper.toResponse(capacityPlanningRepository.save(cp));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!capacityPlanningRepository.existsById(id)) {
            throw new CapacityPlanningErrorException("CapacityPlanning not found with id: " + id);
        }
        capacityPlanningRepository.deleteById(id);
    }

    @Override
    public CapacityPlanningResponse findOne(Long id) {
        CapacityPlanning cp = capacityPlanningRepository.findById(id)
                .orElseThrow(() -> new CapacityPlanningErrorException("CapacityPlanning not found with id: " + id));
        return capacityPlanningMapper.toResponse(cp);
    }

    @Override
    public List<CapacityPlanningResponse> findAll() {
        return capacityPlanningRepository.findAll().stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByWorkCenter_Id(Long id) {
    	validateWorkCenterId(id);
        return capacityPlanningRepository.findByWorkCenter_Id(id).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByWorkCenter_NameContainingIgnoreCase(String name) {
    	validateString(name);
        return capacityPlanningRepository.findByWorkCenter_NameContainingIgnoreCase(name).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByWorkCenter_LocationContainingIgnoreCase(String location) {
    	validateString(location);
        return capacityPlanningRepository.findByWorkCenter_LocationContainingIgnoreCase(location).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
        return capacityPlanningRepository.findByDateBetween(start, end).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByDate(LocalDate date) {
    	DateValidator.validateNotNull(date, "Datun ne sme biti null");
        return capacityPlanningRepository.findByDate(date).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByDateGreaterThanEqual(LocalDate date) {
    	DateValidator.validateNotNull(date, "Datun ne sme biti null");
        return capacityPlanningRepository.findByDateGreaterThanEqual(date).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByAvailableCapacity(BigDecimal availableCapacity) {
    	validateAvailableCapacity(availableCapacity);
        return capacityPlanningRepository.findByAvailableCapacity(availableCapacity).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByAvailableCapacityGreaterThan(BigDecimal availableCapacity) {
    	validateAvailableCapacity(availableCapacity);
        return capacityPlanningRepository.findByAvailableCapacityGreaterThan(availableCapacity).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByAvailableCapacityLessThan(BigDecimal availableCapacity) {
    	validateAvailableCapacity(availableCapacity);
        return capacityPlanningRepository.findByAvailableCapacityLessThan(availableCapacity).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByPlannedLoad(BigDecimal plannedLoad) {
    	validatePlannedLoad(plannedLoad);
        return capacityPlanningRepository.findByPlannedLoad(plannedLoad).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByPlannedLoadGreaterThan(BigDecimal plannedLoad) {
    	validatePlannedLoad(plannedLoad);
        return capacityPlanningRepository.findByPlannedLoadGreaterThan(plannedLoad).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByPlannedLoadLessThan(BigDecimal plannedLoad) {
    	validatePlannedLoad(plannedLoad);
        return capacityPlanningRepository.findByPlannedLoadLessThan(plannedLoad).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByPlannedLoadAndAvailableCapacity(BigDecimal plannedLoad,
            BigDecimal availableCapacity) {
    	validateDoubleBigDecimal(availableCapacity, plannedLoad);
        return capacityPlanningRepository.findByPlannedLoadAndAvailableCapacity(plannedLoad, availableCapacity).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByRemainingCapacity(BigDecimal remainingCapacity) {
    	validateRemainingCapacity(remainingCapacity);
        return capacityPlanningRepository.findByRemainingCapacity(remainingCapacity).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findWhereRemainingCapacityIsLessThanAvailableCapacity() {
        return capacityPlanningRepository.findWhereRemainingCapacityIsLessThanAvailableCapacity().stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findWhereRemainingCapacityIsGreaterThanAvailableCapacity() {
        return capacityPlanningRepository.findWhereRemainingCapacityIsGreaterThanAvailableCapacity().stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findAllOrderByUtilizationDesc() {
        return capacityPlanningRepository.findAllOrderByUtilizationDesc().stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findWhereLoadExceedsCapacity() {
        return capacityPlanningRepository.findWhereLoadExceedsCapacity().stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByUtilizationGreaterThan(BigDecimal threshold) {
        return capacityPlanningRepository.findByUtilizationGreaterThan(threshold).stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }
    
    private void validateRemainingCapacity(BigDecimal remainingCapacity) {
    	if(remainingCapacity == null) {
    		throw new IllegalArgumentException("Remaining capacity ne sme biti null.");
    	}
    	if(remainingCapacity.compareTo(BigDecimal.ZERO) < 0) {
    		throw new IllegalArgumentException("Remaining capacity mora biti 0 ili veći.");
    	}
    }
    
    private BigDecimal calculateRemainingCapacity(BigDecimal available, BigDecimal planned) {
        BigDecimal remaining = available.subtract(planned);
        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Planned load ne može biti veći od available capacity.");
        }
        return remaining;
    }
    
    private void validateDoubleBigDecimal(BigDecimal availableCapacity, BigDecimal plannedLoad) {
    	if (plannedLoad == null || plannedLoad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Planned load mora biti veći od nule.");
        }
        if (availableCapacity == null || availableCapacity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Available capacity mora biti 0 ili veći.");
        }
        if (plannedLoad.compareTo(availableCapacity) > 0) {
            throw new IllegalArgumentException("Planned load ne može biti veći od available capacity.");
        }
    }
    
    
    private void validateWorkCenterId(Long workCenterId) {
    	if(workCenterId == null) {
    		throw new WorkCenterErrorException("WorkCenter ID "+workCenterId+" ne postoji");
    	}
    }
    
    private void validateAvailableCapacity(BigDecimal availableCapacity) {
    	if (availableCapacity == null || availableCapacity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Available capacity mora biti 0 ili veći.");
        }
    }
    
    private void validatePlannedLoad(BigDecimal plannedLoad) {
    	if (plannedLoad == null || plannedLoad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Planned load mora biti veći od nule.");
        }
    }
    
    private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Tekstualni karakter ne sme biti null ili prazan");
        }
    }

}
