package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.NoSuchShiftErrorException;
import com.jovan.erp_v1.exception.NoSuchShiftReportFoundException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.mapper.ShiftReportMapper;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.ShiftReport;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.ShiftReportRepository;
import com.jovan.erp_v1.repository.ShiftRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.ShiftReportRequest;
import com.jovan.erp_v1.response.ShiftReportResponse;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftReportService implements IShiftReportService {

	private final ShiftReportRepository shiftReportRepository;
	private final ShiftReportMapper shiftReportMapper;
	private final UserRepository userRepository;
	private final ShiftRepository shiftRepository;

	@Transactional
	@Override
	public ShiftReport save(ShiftReportRequest request) {
		User creator = fetchUserId(request.createdById());
		Shift shift = fetchShiftId(request.relatedShiftId());
		validateString(request.description());
		validateString(request.filePath());
		ShiftReport report = shiftReportMapper.toEntity(request);
		report.setCreatedBy(creator);
		report.setRelatedShift(shift);
		report.setCreatedAt(LocalDateTime.now());
		return shiftReportRepository.save(report);
	}

	@Transactional
	@Override
	public ShiftReport update(Long id, ShiftReportRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		ShiftReport report = shiftReportRepository.findById(id)
				.orElseThrow(() -> new NoSuchShiftReportFoundException("Report not found with id: " + id));
		Shift shift = fetchShiftId(request.relatedShiftId());
		User creator = fetchUserId(request.createdById());
		validateString(request.description());
		validateString(request.filePath());
		report.setCreatedBy(creator);
		report.setRelatedShift(shift);
		report.setDescription(request.description());
		report.setFilePath(request.filePath());
		return shiftReportRepository.save(report);
	}

	@Override
	public List<ShiftReportResponse> getAll() {
		return shiftReportRepository.findAll().stream()
				.map(ShiftReportResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public ShiftReportResponse getById(Long id) {
		ShiftReport report = shiftReportRepository.findById(id)
				.orElseThrow(() -> new NoSuchShiftReportFoundException("Report not found"));
		return new ShiftReportResponse(report);
	}

	@Override
	public List<ShiftReportResponse> getByShiftId(Long shiftId) {
		return shiftReportRepository.findByRelatedShiftId(shiftId).stream()
				.map(ShiftReportResponse::new)
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if (!shiftReportRepository.existsById(id)) {
			throw new NoSuchShiftReportFoundException("Shift-report not found");
		}
		shiftReportRepository.deleteById(id);
	}

	@Override
	public List<ShiftReportResponse> findByDescription(String description) {
		if(!shiftReportRepository.existsByDescription(description)) {
			throw new IllegalArgumentException("Description doesn't exist");
		}
		validateString(description);
		return shiftReportRepository.findByDescription(description).stream()
				.map(shiftReportMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		return shiftReportRepository.findByCreatedAtBetween(start, end).stream()
				.map(shiftReportMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByCreatedAtAfterOrEqual(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Date");
		return shiftReportRepository.findByCreatedAtAfterOrEqual(date).stream()
				.map(shiftReportMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByCreatedBy_EmailLikeIgnoreCase(String email) {
		validateString(email);
		return shiftReportRepository.findByCreatedBy_EmailLikeIgnoreCase(email).stream()
				.map(shiftReportMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByCreatedBy_PhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		return shiftReportRepository.findByCreatedBy_PhoneNumberLikeIgnoreCase(phoneNumber).stream()
				.map(shiftReportMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByCreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(String firstName,
			String lastName) {
		validateString(firstName);
		validateString(lastName);
		return shiftReportRepository.findByCreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(firstName, lastName).stream()
				.map(shiftReportMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByRelatedShift_EndTimeBefore(LocalDateTime time) {
		DateValidator.validateNotInFuture(time, "End time");
		List<ShiftReport> reports = shiftReportRepository.findByRelatedShift_EndTimeBefore(time);
		if(reports.isEmpty()) {
			throw new NoDataFoundException("There are no related-shifts that ended before the given time");
		}
		return reports.stream().map(shiftReportMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByRelatedShift_EndTimeAfter(LocalDateTime time) {
		DateValidator.validateFutureOrPresent(time, "End time");
		List<ShiftReport> reports = shiftReportRepository.findByRelatedShift_EndTimeAfter(time);
		if(reports.isEmpty()) {
			throw new NoDataFoundException("There are no related-shifts that ended after the given time");
		}
		return reports.stream().map(shiftReportMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByRelatedShift_StartTimeAfter(LocalDateTime time) {
		DateValidator.validateFutureOrPresent(time, "Time start");
		List<ShiftReport> reports = shiftReportRepository.findByRelatedShift_StartTimeAfter(time);
		if(reports.isEmpty()) {
			throw new NoDataFoundException("There are no related-shifts that started after the given time");
		}
		return reports.stream().map(shiftReportMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByRelatedShift_StartTimeBefore(LocalDateTime time) {
		DateValidator.validatePastOrPresent(time, "Time");
		List<ShiftReport> reports = shiftReportRepository.findByRelatedShift_StartTimeAfter(time);
		if(reports.isEmpty()) {
			throw new NoDataFoundException("There are no related-shifts that started before the given time");
		}
		return reports.stream().map(shiftReportMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByRelatedShift_EndTimeBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<ShiftReport> reports = shiftReportRepository.findByRelatedShift_EndTimeBetween(start, end);
		if(reports.isEmpty()) {
			throw new NoDataFoundException("There are no related-shifts that ended between the given time");
		}
		return reports.stream().map(shiftReportMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findRelatedShift_ActiveShifts() {
		if(!shiftReportRepository.existsByRelatedShift_EndTimeIsNull()) {
			throw new IllegalArgumentException("Active shifts for related-shift cannot be found");
		}
		return shiftReportRepository.findRelatedShift_ActiveShifts().stream()
				.map(ShiftReportResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByRelatedShift_EndTimeIsNull() {
		if(!shiftReportRepository.existsByRelatedShift_EndTimeIsNull()) {
			throw new IllegalArgumentException("End time for related-shift must not be null");
		}
		return shiftReportRepository.findByRelatedShift_EndTimeIsNull().stream()
				.map(ShiftReportResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByRelatedShift_ShiftSupervisorIdAndStartTimeBetween(Long supervisorId,
			LocalDateTime start, LocalDateTime end) {
		fetchUserId(supervisorId);
		DateValidator.validateRange(start, end);
		return shiftReportRepository.findByRelatedShift_ShiftSupervisorIdAndStartTimeBetween(supervisorId, start, end).stream()
				.map(ShiftReportResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByRelatedShift_ShiftSupervisorIdAndEndTimeIsNull(Long supervisorId) {
		fetchUserId(supervisorId);
		return shiftReportRepository.findByRelatedShift_ShiftSupervisorIdAndEndTimeIsNull(supervisorId).stream()
				.map(ShiftReportResponse::new)
				.collect(Collectors.toList());
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new IllegalArgumentException("Textal character/s must not be null nor empty");
		}
	}
	
	private User fetchUserId(Long userId) {
    	if(userId == null) {
    		throw new UserNotFoundException("User ID must not be null");
    	}
    	return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id "+ userId));
    }
    
    private Shift fetchShiftId(Long shiftId) {
    	if(shiftId == null) {
    		throw new NoSuchShiftErrorException("Shift ID must not be null");
    	}
    	return shiftRepository.findById(shiftId).orElseThrow(() -> new NoSuchShiftErrorException("Shift not found with id "+shiftId));
    }

}
