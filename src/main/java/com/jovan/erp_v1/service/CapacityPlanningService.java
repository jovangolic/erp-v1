package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.CapacityPlanningErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.mapper.CapacityPlanningMapper;
import com.jovan.erp_v1.model.CapacityPlanning;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.CapacityPlanningRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.request.CapacityPlanningRequest;
import com.jovan.erp_v1.response.CapacityPlanningResponse;
import com.jovan.erp_v1.util.DateValidator;
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
    	List<CapacityPlanning> items = capacityPlanningRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("List of capacity-planning is empty");
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByWorkCenter_Id(Long id) {
    	validateWorkCenterId(id);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByWorkCenter_Id(id);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-planning for work-center id %d is found", id);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByWorkCenter_NameContainingIgnoreCase(String name) {
    	validateString(name);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByWorkCenter_NameContainingIgnoreCase(name);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-planning for work-center name %s is found", name);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByWorkCenter_LocationContainingIgnoreCase(String location) {
    	validateString(location);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByWorkCenter_LocationContainingIgnoreCase(location);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-planning for work-center location %s is found", location);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByDateBetween(start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No Capacity-planning with date between %s and %s ,is found", 
    				start.format(formatter), end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByDate(LocalDate date) {
    	DateValidator.validateNotNull(date, "Datun ne sme biti null");
    	List<CapacityPlanning> items = capacityPlanningRepository.findByDate(date);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No Capacity-planning with date %s ,is found", date.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByDateGreaterThanEqual(LocalDate date) {
    	DateValidator.validateNotNull(date, "Datun ne sme biti null");
    	List<CapacityPlanning> items = capacityPlanningRepository.findByDateGreaterThanEqual(date);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No Capacity-planning with date greater than %s ,is found", date.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByAvailableCapacity(BigDecimal availableCapacity) {
    	validateAvailableCapacity(availableCapacity);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByAvailableCapacity(availableCapacity);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-planning with available capacity  %s, is found", availableCapacity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByAvailableCapacityGreaterThan(BigDecimal availableCapacity) {
    	validateAvailableCapacity(availableCapacity);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByAvailableCapacityGreaterThan(availableCapacity);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-planning with available capacity greater than %s, is found", availableCapacity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByAvailableCapacityLessThan(BigDecimal availableCapacity) {
    	validateAvailableCapacity(availableCapacity);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByAvailableCapacityLessThan(availableCapacity);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-planning with available capacity less than %s, is found", availableCapacity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByPlannedLoad(BigDecimal plannedLoad) {
    	validatePlannedLoad(plannedLoad);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByPlannedLoad(plannedLoad);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-planning with planned load %s, is found", plannedLoad);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByPlannedLoadGreaterThan(BigDecimal plannedLoad) {
    	validatePlannedLoad(plannedLoad);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByPlannedLoadGreaterThan(plannedLoad);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-planning with planned load greater than %s, is found", plannedLoad);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByPlannedLoadLessThan(BigDecimal plannedLoad) {
    	validatePlannedLoad(plannedLoad);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByPlannedLoadLessThan(plannedLoad);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-planning with planned load less than %s, is found", plannedLoad);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByPlannedLoadAndAvailableCapacity(BigDecimal plannedLoad,
            BigDecimal availableCapacity) {
    	validateDoubleBigDecimal(availableCapacity, plannedLoad);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByPlannedLoadAndAvailableCapacity(plannedLoad, availableCapacity);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-plannig with planned load %s and available capacity %s is found",
    				plannedLoad,availableCapacity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByRemainingCapacity(BigDecimal remainingCapacity) {
    	validateBigDecimalNonNegative(remainingCapacity);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByRemainingCapacity(remainingCapacity);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-planning with remaining capacity %s, is found", remainingCapacity);
    		throw new NoDataFoundException(msg);
    	}
    	validateRemainingCapacity(remainingCapacity);
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findWhereRemainingCapacityIsLessThanAvailableCapacity() {
    	List<CapacityPlanning> items = capacityPlanningRepository.findWhereRemainingCapacityIsLessThanAvailableCapacity();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("No Capacity-planning where remaining capacity is less than available capacity, is found");
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findWhereRemainingCapacityIsGreaterThanAvailableCapacity() {
    	List<CapacityPlanning> items = capacityPlanningRepository.findWhereRemainingCapacityIsGreaterThanAvailableCapacity();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("No Capacity-planning where remaining capacity is greater than available capacity, is found");
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findAllOrderByUtilizationDesc() {
    	List<CapacityPlanning> items = capacityPlanningRepository.findAllOrderByUtilizationDesc();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("No Capacity-planning with utilization in descending order, found");
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findWhereLoadExceedsCapacity() {
    	List<CapacityPlanning> items = capacityPlanningRepository.findWhereLoadExceedsCapacity();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("No Capacity-planning where load exceeds capacity is found");
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CapacityPlanningResponse> findByUtilizationGreaterThan(BigDecimal threshold) {
    	validateBigDecimal(threshold);
    	List<CapacityPlanning> items = capacityPlanningRepository.findByUtilizationGreaterThan(threshold);
    	if(items.isEmpty()) {
    		String msg = String.format("No Capacity-planning for utilization greater than  with given threshold %d found", threshold);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(CapacityPlanningResponse::new)
                .collect(Collectors.toList());
    }
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }
    
    private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
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
