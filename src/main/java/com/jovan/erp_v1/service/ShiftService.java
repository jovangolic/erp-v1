package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.NoSuchShiftErrorException;
import com.jovan.erp_v1.exception.SupervisorNotFoundException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.mapper.ShiftMapper;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.ShiftRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.ShiftRequest;
import com.jovan.erp_v1.response.ShiftResponse;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftService implements IShiftService {

	private final ShiftRepository shiftRepository;
	private final ShiftMapper shiftMapper;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public Shift save(ShiftRequest request) {
		DateValidator.validateNotNull(request.startTime(), "Start time");
	    DateValidator.validateNotNull(request.endTime(), "End time");
	    DateValidator.validateRange(request.startTime(), request.endTime());
	    DateValidator.validatePastOrPresent(request.startTime(), "Start time");
	    DateValidator.validatePastOrPresent(request.endTime(), "End time");
	    //DateValidator.validateNotInFuture(request.startTime(), "Start time");
		User supervisor = validateShiftSupervisorId(request.shiftSupervisorId());
		Shift shift = shiftMapper.toEntity(request);
		shift.setShiftSupervisor(supervisor);
		return shiftRepository.save(shift);
	}

	@Transactional
	@Override
	public Shift update(Long id, ShiftRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		Shift shift = shiftRepository.findById(id)
				.orElseThrow(() -> new NoSuchShiftErrorException("Shift not found with id: " + id));
		DateValidator.validateNotNull(request.startTime(), "Start time");
	    DateValidator.validateNotNull(request.endTime(), "End time");
	    DateValidator.validateRange(request.startTime(), request.endTime());
	    DateValidator.validatePastOrPresent(request.startTime(), "Start time");
	    DateValidator.validatePastOrPresent(request.endTime(), "End time");
		User supervisor = validateShiftSupervisorId(request.shiftSupervisorId());
		shift.setStartTime(request.startTime());
		shift.setEndTime(request.endTime());
		shift.setShiftSupervisor(supervisor);
		return shiftRepository.save(shift);
	}

	@Override
	public List<ShiftResponse> getAll() {
		return shiftRepository.findAll().stream()
				.map(shiftMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public ShiftResponse getById(Long id) {
		Shift shift = shiftRepository.findById(id)
				.orElseThrow(() -> new NoSuchShiftErrorException("Shift not found"));
		return shiftMapper.toResponse(shift);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if (!shiftRepository.existsById(id)) {
			throw new NoSuchShiftErrorException("Shift not found");
		}
		shiftRepository.deleteById(id);
	}

	@Override
	public List<ShiftResponse> findByEndTimeBefore(LocalDateTime time) {
		DateValidator.validateNotInFuture(time, "End time");
		List<Shift> shifts = shiftRepository.findByEndTimeBefore(time);
		if(shifts.isEmpty()) {
			throw new NoDataFoundException("There are no shifts that ended before the given time");
		}
		return shifts.stream()
				.map(shiftMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findByEndTimeAfter(LocalDateTime time) {
		DateValidator.validateFutureOrPresent(time, "End time");
		List<Shift> shifts = shiftRepository.findByEndTimeAfter(time);
		if(shifts.isEmpty()) {
			throw new NoDataFoundException("There are no shifts that ended after the given time");
		}
		return shifts.stream()
				.map(shiftMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findByStartTimeAfter(LocalDateTime time) {
		DateValidator.validateFutureOrPresent(time, "Start date");
		List<Shift> shifts = shiftRepository.findByStartTimeAfter(time);
		if(shifts.isEmpty()) {
			throw new NoDataFoundException("There are no shifts that started after the given time");
		}
		return shifts.stream()
				.map(shiftMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findByStartTimeBefore(LocalDateTime time) {
		DateValidator.validatePastOrPresent(time, "Start date");
		List<Shift> shifts = shiftRepository.findByStartTimeBefore(time);
		if(shifts.isEmpty()) {
			throw new NoDataFoundException("There are no shifts that started before the given time");
		}
		return shifts.stream()
				.map(shiftMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findByEndTimeBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<Shift> shifts = shiftRepository.findByEndTimeBetween(start, end);
		if(shifts.isEmpty()) {
			throw new NoDataFoundException("There are no shifts that ended between the given time");
		}
		return shifts.stream()
				.map(shiftMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findActiveShifts() {
		return shiftRepository.findActiveShifts().stream()
				.map(ShiftResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findByEndTimeIsNull() {
		if(!shiftRepository.existsByEndTimeIsNull()) {
			throw new IllegalArgumentException("There is no active shifts");
		}
		return shiftRepository.findByEndTimeIsNull().stream()
				.map(ShiftResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findByShiftSupervisorIdAndStartTimeBetween(Long supervisorId, LocalDateTime start,
			LocalDateTime end) {
		validateShiftSupervisorId(supervisorId);
		DateValidator.validateRange(start, end);
		return shiftRepository.findByShiftSupervisorIdAndStartTimeBetween(supervisorId, start, end).stream()
				.map(ShiftResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findByShiftSupervisorIdAndEndTimeIsNull(Long supervisorId) {
		validateShiftSupervisorId(supervisorId);
		if (!shiftRepository.existsByShiftSupervisorIdAndEndTimeIsNull(supervisorId)) {
			throw new IllegalArgumentException("Currently, there is not any active shift for this foreman");
		}
		return shiftRepository.findByShiftSupervisorIdAndEndTimeIsNull(supervisorId).stream()
				.map(shiftMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public Integer countShiftsByStartDate(LocalDate date) {
		DateValidator.validateNotNull(date, "Start date");
		DateValidator.validateFutureOrPresent(date, "Date");
		return shiftRepository.countShiftsByStartDate(date);
	}

	@Override
	public Integer countShiftsByEndDate(LocalDate date) {
		DateValidator.validateNotNull(date, "End date");
		DateValidator.validatePastOrPresent(date, "Date");
		return shiftRepository.countShiftsByEndDate(date);
	}

	@Override
	public List<ShiftResponse> findCurrentShiftBySupervisor(Long supervisorId) {
		validateShiftSupervisorId(supervisorId);
		return shiftRepository.findCurrentShiftBySupervisor(supervisorId).stream()
				.map(ShiftResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findShiftsLongerThan(Integer hours) {
		validateInteger(hours);
		return shiftRepository.findShiftsLongerThan(hours).stream()
				.map(ShiftResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findShiftsOverlappingPeriod(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		return shiftRepository.findShiftsOverlappingPeriod(start, end).stream()
				.map(ShiftResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findNightShifts() {
		return shiftRepository.findNightShifts().stream()
				.map(shiftMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findFutureShifts(LocalDateTime now) {
		DateValidator.validateNotNull(now, "Time now");
		List<Shift> shifts = shiftRepository.findByStartTimeAfter(now);
		return shifts.stream().map(shiftMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findAllFutureShifts() {
		return shiftRepository.findAllFutureShifts()
	            .stream()
	            .map(shiftMapper::toResponse)
	            .collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findByShiftSupervisor_EmailLikeIgnoreCase(String email) {
		validateString(email);
		return shiftRepository.findByShiftSupervisor_EmailLikeIgnoreCase(email).stream()
				.map(ShiftResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findByShiftSupervisorPhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		return shiftRepository.findByShiftSupervisorPhoneNumberLikeIgnoreCase(phoneNumber).stream()
				.map(ShiftResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftResponse> findByShiftSupervisor_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(String firstName,
			String lastName) {
		validateString(firstName);
		validateString(lastName);
		return shiftRepository.findByShiftSupervisor_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(firstName, lastName).stream()
				.map(ShiftResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public boolean hasActiveShift(Long supervisorId) {
	    return shiftRepository.existsByShiftSupervisorIdAndEndTimeIsNull(supervisorId);
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new IllegalArgumentException("String must not be null nor empty");
		}
	}
	
	private void validateInteger(Integer num) {
		if (num == null || num < 0) {
		    throw new IllegalArgumentException("Number must be positive or zero");
		}
	}
	
	private User validateShiftSupervisorId(Long shiftSupervisorId) {
		if(shiftSupervisorId == null) {
			throw new UserNotFoundException("Foreman ID must not be null");
		}
		return userRepository.findById(shiftSupervisorId).orElseThrow(() -> new UserNotFoundException("Foreman not found with id "+shiftSupervisorId));
	}
	
	private void validateNoActiveShift(Long supervisorId) {
	    if (hasActiveShift(supervisorId)) {
	        throw new IllegalStateException("Supervisor već ima aktivnu smenu.");
	    }
	}
}
