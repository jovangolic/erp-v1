package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.ShiftType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ShiftPlanningErrorException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.mapper.ShiftPlanningMapper;
import com.jovan.erp_v1.model.ShiftPlanning;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.ShiftPlanningRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.request.ShiftPlanningRequest;
import com.jovan.erp_v1.response.ShiftPlanningResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftPlanningService implements IShiftPlanningService {

    private final ShiftPlanningRepository shiftPlanningRepository;
    private final ShiftPlanningMapper shiftPlanningMapper;
    private final UserRepository userRepository;
    private final WorkCenterRepository workCenterRepository;

    @Transactional
    @Override
    public ShiftPlanningResponse create(ShiftPlanningRequest request) {
    	WorkCenter wc = fetchWorkCenter(request.workCenterId());
    	User emp = fetchUser(request.userId());
        validateRequest(request);
        ShiftPlanning sp = shiftPlanningMapper.toEntity(request,wc,emp);
        ShiftPlanning saved = shiftPlanningRepository.save(sp);
        return shiftPlanningMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public ShiftPlanningResponse update(Long id, ShiftPlanningRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        ShiftPlanning sp = shiftPlanningRepository.findById(id)
                .orElseThrow(() -> new ShiftPlanningErrorException("ShiftPlanning not found with id: " + id));
        validateRequest(request);
        WorkCenter wc = sp.getWorkCenter();
        if(request.workCenterId() != null && (wc.getId() == null || !request.workCenterId().equals(wc.getId()))) {
        	wc = fetchWorkCenter(request.workCenterId());
        }
        User emp = sp.getEmployee();
        if(request.userId() != null && (emp.getId() == null || !request.userId().equals(emp.getId()))) {
        	emp = fetchUser(request.userId());
        }
        shiftPlanningMapper.toUpdateEntity(sp, request,wc, emp);
        return shiftPlanningMapper.toResponse(shiftPlanningRepository.save(sp));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!shiftPlanningRepository.existsById(id)) {
            throw new ShiftPlanningErrorException("ShiftPlanning not found with id: " + id);
        }
        shiftPlanningRepository.deleteById(id);
    }

    @Override
    public ShiftPlanningResponse findOne(Long id) {
        ShiftPlanning sp = shiftPlanningRepository.findById(id)
                .orElseThrow(() -> new ShiftPlanningErrorException("ShiftPlanning not found with id: " + id));
        return new ShiftPlanningResponse(sp);
    }

    @Override
    public List<ShiftPlanningResponse> findAll() {
    	List<ShiftPlanning> items = shiftPlanningRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("ShiftPlanning list is empty");
    	}
        return shiftPlanningRepository.findAll().stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByWorkCenter_NameContainingIgnoreCase(String name) {
    	validateString(name);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByWorkCenter_NameContainingIgnoreCase(name);
    	if(items.isEmpty()) {
    		String msg = String.format("No ShiftPlanning for workCenter name %s is foiund", name);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByWorkCenter_Capacity(BigDecimal capacity) {
    	validateBigDecimal(capacity);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByWorkCenter_Capacity(capacity);
    	if(items.isEmpty()) {
    		String msg = String.format("No ShiftPlanning for workCenter capacity %s is found", capacity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
	public List<ShiftPlanningResponse> findByWorkCenter_CapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<ShiftPlanning> items = shiftPlanningRepository.findByWorkCenter_CapacityGreaterThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No ShiftPlanning for workCenter capacity greater than %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
	}

	@Override
	public List<ShiftPlanningResponse> findByWorkCenter_CapacityLessThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		List<ShiftPlanning> items = shiftPlanningRepository.findByWorkCenter_CapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No ShiftPlanning for workCenter capacity less than %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
	}

    @Override
    public List<ShiftPlanningResponse> findByWorkCenter_LocationContainingIgnoreCase(String location) {
    	validateString(location);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByWorkCenter_LocationContainingIgnoreCase(location);
    	if(items.isEmpty()) {
    		String msg = String.format("No ShiftPlanning for work-center location %s is found", location);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_Id(Long id) {
    	fetchUser(id);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByEmployee_Id(id);
    	if(items.isEmpty()) {
    		String msg = String.format("No ShiftPlanning for employee-id %d is found", id);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_Email(String email) {
    	validateString(email);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByEmployee_Email(email);
    	if(items.isEmpty()) {
    		String msg = String.format("No ShiftPlanning for employee's email %s is found", email);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_UsernameContainingIgnoreCase(String username) {
    	validateString(username);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByEmployee_UsernameContainingIgnoreCase(username);
    	if(items.isEmpty()) {
    		String msg = String.format("No ShiftPlanning for employee's username %s is found", username);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployeeFirstContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName) {
    	validateString(firstName);
    	validateString(lastName);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByEmployeeFirstContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
    	if(items.isEmpty()) {
    		String msg = String.format("No ShiftPlanning for employee's first-name %s and last-name %s is found", 
    				firstName,lastName);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_PhoneNumber(String phoneNumber) {
    	validateString(phoneNumber);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByEmployee_PhoneNumber(phoneNumber);
    	if(items.isEmpty()) {
    		String msg = String.format("No ShiftPlanning for employee's phone-number %s is found", phoneNumber);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByDate(LocalDate date) {
    	DateValidator.validateNotNull(date, "Date");
    	List<ShiftPlanning> items = shiftPlanningRepository.findByDate(date);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No ShiftPlanning for date %s is found", date.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByDateBetween(start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No ShiftPlanning for date between %s and %s is found", 
    				start.format(formatter),end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByDateGreaterThanEqual(LocalDate date) {
    	DateValidator.validateNotNull(date, "Date");
    	List<ShiftPlanning> items = shiftPlanningRepository.findByDateGreaterThanEqual(date);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No ShiftPlanning for date greater than or equal to %s is found ", date.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findOrdersWithStartDateAfterOrEqual(LocalDate date) {
    	DateValidator.validateNotNull(date, "Date");
    	List<ShiftPlanning> items = shiftPlanningRepository.findOrdersWithStartDateAfterOrEqual(date);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No ShiftPlanning for orders start date after %s is found", date.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByShiftType(ShiftType shiftType) {
    	validateShiftType(shiftType);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByShiftType(shiftType);
    	if(items.isEmpty()) {
    		String msg = String.format("No ShiftPlanning for shift-type %s is found", shiftType);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByAssigned(Boolean assigned) {
    	List<ShiftPlanning> items = shiftPlanningRepository.findByAssigned(assigned);
    	if (items.isEmpty()) {
            throw new NoDataFoundException(String.format("No ShiftPlanning found with assigned = %b", assigned));
        }
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_IdAndAssignedTrue(Long employeeId) {
    	fetchUser(employeeId);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByEmployee_IdAndAssignedTrue(employeeId);
    	if(items.isEmpty()) {
    		String msg = String.format("No ShiftPlanning for employee-id %d and assigned equal to 'true', found", employeeId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_IdAndShiftType(Long employeeId, ShiftType shiftType) {
    	fetchUser(employeeId);
    	validateShiftType(shiftType);
    	List<ShiftPlanning> items = shiftPlanningRepository.findByEmployee_IdAndShiftType(employeeId, shiftType);
    	if(items.isEmpty()) {
    		String msg = String.format("No ShiftPlanning for employee-id %d and shift-type %s is found",
    				employeeId,shiftType);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByWorkCenter_IdAndDateAfterAndAssignedFalse(Long workCenterId,
            LocalDate date) {
    	fetchWorkCenter(workCenterId);
    	DateValidator.validateNotNull(date, "Date");
    	List<ShiftPlanning> items = shiftPlanningRepository.findByWorkCenter_IdAndDateAfterAndAssignedFalse(workCenterId, date);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No ShiftPlanning for work-center-id %d and date after %s is found",
    				workCenterId,date.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findShiftsForEmployeeBetweenDates(Long employeeId, LocalDate start,
            LocalDate end) {
    	fetchUser(employeeId);
    	DateValidator.validateRange(start, end);
    	List<ShiftPlanning> items = shiftPlanningRepository.findShiftsForEmployeeBetweenDates(employeeId, start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No ShiftPlanning for employee-id %d and dates between %s and %s is found",
    				employeeId,start.format(formatter),end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmployee_IdAndDateAndShiftType(Long employeeId, LocalDate date, ShiftType shiftType) {
    	fetchUser(employeeId);
    	DateValidator.validateNotNull(date, "Date");
    	validateShiftType(shiftType);
        return shiftPlanningRepository.existsByEmployee_IdAndDateAndShiftType(employeeId, date, shiftType);
    }

    private void validateRequest(ShiftPlanningRequest request) {
        DateValidator.validateNotNull(request.date(), "Date");
        validateShiftType(request.shiftType());
        validateAssignedFlag(request.assigned());
    }
    
    private void validateAssignedFlag(Boolean assigned) {
    	if (assigned == null) {
    		throw new ValidationException("Assigned flag must not be null");
    	}
    }
    
    private void validateBigDecimal(BigDecimal capacity) {
    	if(capacity == null || capacity.compareTo(BigDecimal.ZERO) <= 0) {
    		throw new IllegalArgumentException("Capacity must be positive number");
    	}
    }
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new IllegalArgumentException("Text character must not be null nor empty");
    	}
    }
    
    private void validateShiftType(ShiftType shiftType) {
    	if(shiftType == null) {
    		throw new IllegalArgumentException("ShiftType shiftType must not be null");
    	}
    }
    
    private WorkCenter fetchWorkCenter(Long workCenterId) {
        if (workCenterId == null) {
            throw new WorkCenterErrorException("WorkCenter can't be null");
        }
        return workCenterRepository.findById(workCenterId)
                .orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found with id " + workCenterId));
    }

    private User fetchUser(Long userId) {
        if (userId == null) {
            throw new UserNotFoundException("User ID can't be null");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}

}
