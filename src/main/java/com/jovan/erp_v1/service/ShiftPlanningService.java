package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.ShiftType;
import com.jovan.erp_v1.exception.ShiftPlanningErrorException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.mapper.ShiftPlanningMapper;
import com.jovan.erp_v1.model.ShiftPlanning;
import com.jovan.erp_v1.repository.ShiftPlanningRepository;
import com.jovan.erp_v1.request.ShiftPlanningRequest;
import com.jovan.erp_v1.response.ShiftPlanningResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftPlanningService implements IShiftPlanningService {

    private final ShiftPlanningRepository shiftPlanningRepository;
    private final ShiftPlanningMapper shiftPlanningMapper;

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
        return shiftPlanningRepository.findByWorkCenter_NameContainingIgnoreCase(name).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByWorkCenter_Capacity(BigDecimal capacity) {
        return shiftPlanningRepository.findByWorkCenter_Capacity(capacity).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByWorkCenter_LocationContainingIgnoreCase(String location) {
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
        return shiftPlanningRepository.findByEmployee_Email(email).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_UsernameContainingIgnoreCase(String username) {
        return shiftPlanningRepository.findByEmployee_UsernameContainingIgnoreCase(username).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployeeFirstAndLastName(String firstName, String lastName) {
        return shiftPlanningRepository.findByEmployeeFirstAndLastName(firstName, lastName).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_PhoneNumber(String phoneNumber) {
        return shiftPlanningRepository.findByEmployee_PhoneNumber(phoneNumber).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByDate(LocalDate date) {
        return shiftPlanningRepository.findByDate(date).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByDateBetween(LocalDate start, LocalDate end) {
        return shiftPlanningRepository.findByDateBetween(start, end).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByDateGreaterThanEqual(LocalDate date) {
        return shiftPlanningRepository.findByDateGreaterThanEqual(date).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findOrdersWithStartDateAfterOrEqual(LocalDate date) {
        return shiftPlanningRepository.findOrdersWithStartDateAfterOrEqual(date).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByShiftType(ShiftType shiftType) {
        return shiftPlanningRepository.findByShiftType(shiftType).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByAssigned(boolean assigned) {
        return shiftPlanningRepository.findByAssigned(assigned).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_IdAndAssignedTrue(Long employeeId) {
        return shiftPlanningRepository.findByEmployee_IdAndAssignedTrue(employeeId).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByEmployee_IdAndShiftType(Long employeeId, ShiftType shiftType) {
        return shiftPlanningRepository.findByEmployee_IdAndShiftType(employeeId, shiftType).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findByWorkCenter_IdAndDateAfterAndAssignedFalse(Long workCenterId,
            LocalDate date) {
        return shiftPlanningRepository.findByWorkCenter_IdAndDateAfterAndAssignedFalse(workCenterId, date).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftPlanningResponse> findShiftsForEmployeeBetweenDates(Long employeeId, LocalDate start,
            LocalDate end) {
        return shiftPlanningRepository.findShiftsForEmployeeBetweenDates(employeeId, start, end).stream()
                .map(ShiftPlanningResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmployee_IdAndDateAndShiftType(Long employeeId, LocalDate date, ShiftType shiftType) {
        return shiftPlanningRepository.existsByEmployee_IdAndDateAndShiftType(employeeId, date, shiftType);
    }

    private void validateRequest(ShiftPlanningRequest request) {
        Objects.requireNonNull(request.workCenterId(), "WorkCenter ID must not be null");
        Objects.requireNonNull(request.userId(), "User ID must not be null");
        Objects.requireNonNull(request.date(), "Date must not be null");
    }

}
