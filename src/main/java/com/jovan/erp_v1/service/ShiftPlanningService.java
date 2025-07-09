package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.ShiftType;
import com.jovan.erp_v1.exception.ShiftPlanningErrorException;
import com.jovan.erp_v1.exception.UserNotFoundException;
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
        validateRequest(request);
        ShiftPlanning sp = shiftPlanningMapper.toEntity(request);
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
        shiftPlanningMapper.toUpdateEntity(sp, request);
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
        return shiftPlanningRepository.findAll().stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByWorkCenter_NameContainingIgnoreCase(String name) {
    	validateString(name);
        return shiftPlanningRepository.findByWorkCenter_NameContainingIgnoreCase(name).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByWorkCenter_Capacity(BigDecimal capacity) {
    	validateBigDecimal(capacity);
        return shiftPlanningRepository.findByWorkCenter_Capacity(capacity).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByWorkCenter_LocationContainingIgnoreCase(String location) {
    	validateString(location);
        return shiftPlanningRepository.findByWorkCenter_LocationContainingIgnoreCase(location).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_Id(Long id) {
        return shiftPlanningRepository.findByEmployee_Id(id).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_Email(String email) {
    	validateString(email);
        return shiftPlanningRepository.findByEmployee_Email(email).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_UsernameContainingIgnoreCase(String username) {
    	validateString(username);
        return shiftPlanningRepository.findByEmployee_UsernameContainingIgnoreCase(username).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployeeFirstAndLastName(String firstName, String lastName) {
    	validateString(firstName);
    	validateString(lastName);
        return shiftPlanningRepository.findByEmployeeFirstAndLastName(firstName, lastName).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_PhoneNumber(String phoneNumber) {
    	validateString(phoneNumber);
        return shiftPlanningRepository.findByEmployee_PhoneNumber(phoneNumber).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByDate(LocalDate date) {
    	DateValidator.validateNotNull(date, "Date");
        return shiftPlanningRepository.findByDate(date).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
        return shiftPlanningRepository.findByDateBetween(start, end).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByDateGreaterThanEqual(LocalDate date) {
    	DateValidator.validateNotNull(date, "Date");
        return shiftPlanningRepository.findByDateGreaterThanEqual(date).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findOrdersWithStartDateAfterOrEqual(LocalDate date) {
    	DateValidator.validateNotNull(date, "Date");
        return shiftPlanningRepository.findOrdersWithStartDateAfterOrEqual(date).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByShiftType(ShiftType shiftType) {
    	validateShiftType(shiftType);
        return shiftPlanningRepository.findByShiftType(shiftType).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByAssigned(Boolean assigned) {
        return shiftPlanningRepository.findByAssigned(assigned).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_IdAndAssignedTrue(Long employeeId) {
    	fetchUser(employeeId);
        return shiftPlanningRepository.findByEmployee_IdAndAssignedTrue(employeeId).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_IdAndShiftType(Long employeeId, ShiftType shiftType) {
    	fetchUser(employeeId);
    	validateShiftType(shiftType);
        return shiftPlanningRepository.findByEmployee_IdAndShiftType(employeeId, shiftType).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByWorkCenter_IdAndDateAfterAndAssignedFalse(Long workCenterId,
            LocalDate date) {
    	fetchWorkCenter(workCenterId);
    	DateValidator.validateNotNull(date, "Date");
        return shiftPlanningRepository.findByWorkCenter_IdAndDateAfterAndAssignedFalse(workCenterId, date).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findShiftsForEmployeeBetweenDates(Long employeeId, LocalDate start,
            LocalDate end) {
    	fetchUser(employeeId);
    	DateValidator.validateRange(start, end);
        return shiftPlanningRepository.findShiftsForEmployeeBetweenDates(employeeId, start, end).stream()
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
        fetchWorkCenter(request.workCenterId());
        fetchUser(request.userId());
        DateValidator.validateNotNull(request.date(), "Date");
        validateShiftType(request.shiftType());
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

}
