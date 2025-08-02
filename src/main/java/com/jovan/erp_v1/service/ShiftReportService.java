package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	public ShiftReportResponse save(ShiftReportRequest request) {
		User creator = fetchUserId(request.createdById());
		Shift shift = fetchShiftId(request.relatedShiftId());
		validateString(request.description());
		validateString(request.filePath());
		ShiftReport report = shiftReportMapper.toEntity(request,creator,shift);
		ShiftReport saved = shiftReportRepository.save(report);
		return new ShiftReportResponse(saved);
	}

	@Transactional
	@Override
	public ShiftReportResponse update(Long id, ShiftReportRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		ShiftReport report = shiftReportRepository.findById(id)
				.orElseThrow(() -> new NoSuchShiftReportFoundException("Report not found with id: " + id));
		User creator = report.getCreatedBy();
		if(request.createdById() != null && (creator.getId() == null || !request.createdById().equals(creator.getId()))) {
			creator = fetchUserId(request.createdById());
		}
		Shift shift = report.getRelatedShift();
		if(request.relatedShiftId() != null && (shift.getId() == null || !request.relatedShiftId().equals(shift.getId()))) {
			shift = fetchShiftId(request.relatedShiftId());
		}
		validateString(request.description());
		validateString(request.filePath());
		shiftReportMapper.toUpdateEntity(report, request, creator, shift);
		ShiftReport saved = shiftReportRepository.save(report);
		return new ShiftReportResponse(saved);
	}

	@Override
	public List<ShiftReportResponse> getAll() {
		List<ShiftReport> items = shiftReportRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("ShiftReport list is empty");
		}
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
		fetchShiftId(shiftId);
		List<ShiftReport> items = shiftReportRepository.findByRelatedShiftId(shiftId);
		if(items.isEmpty()) {
			String msg = String.format("No ShiftReport for shift-id %d, found", shiftId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
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
		validateString(description);
		List<ShiftReport> items = shiftReportRepository.findByDescription(description);
		if(items.isEmpty()) {
			String msg = String.format("No ShiftReport for description %s, found", description);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(shiftReportMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<ShiftReport> items = shiftReportRepository.findByCreatedAtBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No ShiftReport for createdAt between %s and %s, found", 
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(shiftReportMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByCreatedAtAfterOrEqual(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Date");
		List<ShiftReport> items = shiftReportRepository.findByCreatedAtAfterOrEqual(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No ShiftReport for createdAt after or equal %s, found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(shiftReportMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByCreatedBy_EmailLikeIgnoreCase(String email) {
		validateString(email);
		List<ShiftReport> items = shiftReportRepository.findByCreatedBy_EmailLikeIgnoreCase(email);
		if(items.isEmpty()) {
			String msg = String.format("No ShiftReport for createdBy's email %s ,found", email);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(shiftReportMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByCreatedBy_PhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		List<ShiftReport> items = shiftReportRepository.findByCreatedBy_PhoneNumberLikeIgnoreCase(phoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No ShiftReport for createdBy's phone-number %s, found", phoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(shiftReportMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByCreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(String firstName,
			String lastName) {
		validateString(firstName);
		validateString(lastName);
		List<ShiftReport> items = shiftReportRepository.findByCreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No ShiftReport for createdBy first-name %s and last-name %s, found",
					firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
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
		List<ShiftReport> items = shiftReportRepository.findRelatedShift_ActiveShifts();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No ShiftReport for active shifts are found");
		}
		return items.stream()
				.map(ShiftReportResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByRelatedShift_EndTimeIsNull() {
		List<ShiftReport> items = shiftReportRepository.findByRelatedShift_EndTimeIsNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No ShiftReport for related-shift's end time is null, found");
		}
		return items.stream()
				.map(ShiftReportResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByRelatedShift_ShiftSupervisorIdAndStartTimeBetween(Long supervisorId,
			LocalDateTime start, LocalDateTime end) {
		fetchUserId(supervisorId);
		DateValidator.validateRange(start, end);
		List<ShiftReport> items = shiftReportRepository.findByRelatedShift_ShiftSupervisorIdAndStartTimeBetween(supervisorId, start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No ShiftReport for related-shift bound to supervisor-id %d and start time between %s and %s is found", 
					supervisorId,start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ShiftReportResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftReportResponse> findByRelatedShift_ShiftSupervisorIdAndEndTimeIsNull(Long supervisorId) {
		fetchUserId(supervisorId);
		List<ShiftReport> items = shiftReportRepository.findByRelatedShift_ShiftSupervisorIdAndEndTimeIsNull(supervisorId);
		if(items.isEmpty()) {
			String msg = String.format("No ShiftReport for related-shift's supervisor-id %d is found", supervisorId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
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
