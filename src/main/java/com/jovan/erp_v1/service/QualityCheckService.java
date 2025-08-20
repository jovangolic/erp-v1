package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.dto.CheckTypeCountResponse;
import com.jovan.erp_v1.dto.DateCountResponse;
import com.jovan.erp_v1.dto.InspectorCountResponse;
import com.jovan.erp_v1.dto.InspectorNameCountResponse;
import com.jovan.erp_v1.dto.ReferenceTypeCountResponse;
import com.jovan.erp_v1.dto.StatusCountResponse;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.QualityCheckMapper;
import com.jovan.erp_v1.model.QualityCheck;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.QualityCheckRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.QualityCheckRequest;
import com.jovan.erp_v1.response.QualityCheckResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QualityCheckService implements IQualityCheckService {

	private final QualityCheckRepository qualityCheckRepository;
	private final UserRepository userRepository;
	private final QualityCheckMapper qualityCheckMapper;

	@Transactional
	@Override
	public QualityCheckResponse create(QualityCheckRequest request) {
		DateValidator.validateNotNull(request.locDate(), "Date-timr");
		User inspector =  validateUserId(request.inspectorId());
		validateReferenceType(request.referenceType());
		validateReferenceId(request.referenceId());
		validateQualityCheckType(request.checkType());
		validateQualityCheckStatus(request.status());
		validateString(request.notes());
		QualityCheck qc = qualityCheckMapper.toEntity(request, inspector);
		QualityCheck saved = qualityCheckRepository.save(qc);
		return new QualityCheckResponse(saved);
	}

	
	@Override
	public QualityCheckResponse update(Long id, QualityCheckRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		QualityCheck qc = validateQualityCheck(id);
		User inspector = qc.getInspector();
		if(request.inspectorId() != null && (inspector.getId() == null || !request.inspectorId().equals(inspector.getId()))) {
			inspector = validateUserId(request.inspectorId());
		}
		validateReferenceType(request.referenceType());
		validateReferenceId(request.referenceId());
		validateQualityCheckType(request.checkType());
		validateQualityCheckStatus(request.status());
		validateString(request.notes());
		qualityCheckMapper.toEntityUpdate(qc, request, inspector);
		QualityCheck saved = qualityCheckRepository.save(qc);
		return new QualityCheckResponse(saved);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if(!qualityCheckRepository.existsById(id)) {
			throw new ValidationException("QualityCheck ID must not be null");
		}
		qualityCheckRepository.deleteById(id);
	}

	@Override
	public QualityCheckResponse findOne(Long id) {
		QualityCheck qc = qualityCheckRepository.findById(id).orElseThrow(() -> new ValidationException("QualityCheck not found with id "+id));
		return new QualityCheckResponse(qc);
	}

	@Override
	public List<QualityCheckResponse> findAll() {
		List<QualityCheck> items = qualityCheckRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("QualityCheck list is empty");
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByReferenceType(ReferenceType referenceType) {
		validateReferenceType(referenceType);
		List<QualityCheck> items = qualityCheckRepository.findByReferenceType(referenceType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for reference-type %s found", referenceType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByCheckType(QualityCheckType checkType) {
		validateQualityCheckType(checkType);
		List<QualityCheck> items = qualityCheckRepository.findByCheckType(checkType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for check-type %s, found", checkType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByStatus(QualityCheckStatus status) {
		validateQualityCheckStatus(status);
		List<QualityCheck> items = qualityCheckRepository.findByStatus(status);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for status %s, found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByCheckTypeAndStatus(QualityCheckType checkType, QualityCheckStatus status) {
		validateQualityCheckType(checkType);
		validateQualityCheckStatus(status);
		List<QualityCheck> items = qualityCheckRepository.findByCheckTypeAndStatus(checkType, status);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for check-type %s and status %s , found", checkType,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByStatusAndReferenceType(QualityCheckStatus status,
			ReferenceType referenceType) {
		validateQualityCheckStatus(status);
		validateReferenceType(referenceType);
		List<QualityCheck> items = qualityCheckRepository.findByStatusAndReferenceType(status, referenceType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for status %s and reference-type %s, found", 
					status,referenceType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByCheckTypeAndReferenceType(QualityCheckType checkType,
			ReferenceType referenceType) {
		validateQualityCheckType(checkType);
		validateReferenceType(referenceType);
		List<QualityCheck> items = qualityCheckRepository.findByCheckTypeAndReferenceType(checkType, referenceType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for check-type %s and reference-type %s, found",
					checkType, referenceType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType) {
		validateReferenceId(referenceId);
		validateReferenceType(referenceType);
		List<QualityCheck> items = qualityCheckRepository.findByReferenceIdAndReferenceType(referenceId, referenceType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for reference-id %d and reference-type %s, found",
					referenceId,referenceType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByReferenceIdAndCheckType(Long referenceId,
			QualityCheckType checkType) {
		validateReferenceId(referenceId);
		validateQualityCheckType(checkType);
		List<QualityCheck> items = qualityCheckRepository.findByReferenceIdAndCheckType(referenceId, checkType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for reference-id %d and check-type %s, found",
					referenceId,checkType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByReferenceIdAndStatus(Long referenceId,
			QualityCheckStatus status) {
		validateReferenceId(referenceId);
		validateQualityCheckStatus(status);
		List<QualityCheck> items = qualityCheckRepository.findByReferenceIdAndStatus(referenceId, status);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for reference-id %d and status %s, found",
					referenceId,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByReferenceTypeIn(List<ReferenceType> referenceType) {
		validateReferenceTypeList(referenceType);
		List<QualityCheck> items = qualityCheckRepository.findByReferenceTypeIn(referenceType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for reference-type list %s, found", referenceType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByCheckTypeIn(List<QualityCheckType> checkType) {
		validateQualityCheckTypeList(checkType);
		List<QualityCheck> items = qualityCheckRepository.findByCheckTypeIn(checkType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for check-type list %s, found", checkType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByStatusIn(List<QualityCheckStatus> status) {
		validateQualityCheckStatusList(status);
		List<QualityCheck> items = qualityCheckRepository.findByStatusIn(status);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for check-status list %s, found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByReferenceIdAndReferenceTypeAndStatus(Long referenceId,
			ReferenceType referenceType, QualityCheckStatus status) {
		validateReferenceId(referenceId);
		validateReferenceType(referenceType);
		validateQualityCheckStatus(status);
		List<QualityCheck> items = qualityCheckRepository.findByReferenceIdAndReferenceTypeAndStatus(referenceId, referenceType, status);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for reference-id %d, reference-type %s and status %s, found", 
					referenceId, referenceType,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByReferenceIdAndReferenceTypeAndCheckType(Long referenceId,
			ReferenceType referenceType, QualityCheckType checkType) {
		validateReferenceId(referenceId);
		validateReferenceType(referenceType);
		validateQualityCheckType(checkType);
		List<QualityCheck> items = qualityCheckRepository.findByReferenceIdAndReferenceTypeAndCheckType(referenceId, referenceType, checkType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for reference-id %d, reference-type %s and check-type %s, found",
					referenceId,referenceType,checkType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByNotes(String notes) {
		validateString(notes);
		List<QualityCheck> items = qualityCheckRepository.findByNotes(notes);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for notes %s, found", notes);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByReferenceId(Long referenceId) {
		validateReferenceId(referenceId);
		List<QualityCheck> items = qualityCheckRepository.findByReferenceId(referenceId);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for reference-id %d, found", referenceId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByLocDate(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Date-time");
		List<QualityCheck> items = qualityCheckRepository.findByLocDate(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No QualityCheck for date %s, found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByLocDateBefore(LocalDateTime date) {
		DateValidator.validateNotInFuture(date, "Date-before");
		List<QualityCheck> items = qualityCheckRepository.findByLocDateBefore(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No QualityCheck for date-before %s, found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByLocDateAfter(LocalDateTime date) {
		DateValidator.validateNotInPast(date, "Date-after");
		List<QualityCheck> items = qualityCheckRepository.findByLocDateAfter(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No QualityCheck for date-after %s, found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByLocDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
		DateValidator.validateRange(startDate, endDate);
		List<QualityCheck> items = qualityCheckRepository.findByLocDateBetween(startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No QualityCheck for date between %s and %s, found",
					startDate.format(formatter),endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByStatusAndLocDateBetween(QualityCheckStatus status, LocalDateTime startDate,
			LocalDateTime endDate) {
		validateQualityCheckStatus(status);
		DateValidator.validateRange(startDate, endDate);
		List<QualityCheck> items = qualityCheckRepository.findByStatusAndLocDateBetween(status, startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No QualityCheck for status %s and date between %s and %s, found", 
					status,startDate.format(formatter), endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByCheckTypeAndLocDateBetween(QualityCheckType checkType, LocalDateTime startDate,
			LocalDateTime endDate) {
		validateQualityCheckType(checkType);
		DateValidator.validateRange(startDate, endDate);
		List<QualityCheck> items = qualityCheckRepository.findByCheckTypeAndLocDateBetween(checkType, startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No QualityCheck for check-type %s and date between %s and %s, found", 
					checkType,startDate.format(formatter),endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByReferenceTypeAndLocDateBetween(ReferenceType referenceType,
			LocalDateTime startDate, LocalDateTime endDate) {
		validateReferenceType(referenceType);
		DateValidator.validateRange(startDate, endDate);
		List<QualityCheck> items = qualityCheckRepository.findByReferenceTypeAndLocDateBetween(referenceType, startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No QualityCheck for reference-id %d and date between %s and %s, found",
					referenceType, startDate.format(formatter), endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByReferenceIdOrderByLocDateDesc(Long referenceId) {
		validateReferenceId(referenceId);
		List<QualityCheck> items = qualityCheckRepository.findByReferenceIdOrderByLocDateDesc(referenceId);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for reference-id %d order by date desc, found", referenceId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByInspectorIdOrderByLocDateDesc(Long inspectorId) {
		validateUserId(inspectorId);
		List<QualityCheck> items = qualityCheckRepository.findByInspectorIdOrderByLocDateDesc(inspectorId);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for inspector-id %d order by date desc, found", inspectorId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public boolean existsByReferenceIdAndReferenceTypeAndStatus(Long referenceId, ReferenceType referenceType,
			QualityCheckStatus status) {
		validateReferenceId(referenceId);
		validateReferenceType(referenceType);
		validateQualityCheckStatus(status);
		return qualityCheckRepository.existsByReferenceIdAndReferenceTypeAndStatus(referenceId, referenceType, status);
	}

	@Override
	public boolean existsByInspectorIdAndLocDateBetween(Long inspectorId, LocalDateTime startDate, LocalDateTime endDate) {
		validateUserId(inspectorId);
		DateValidator.validateRange(startDate, endDate);
		return qualityCheckRepository.existsByInspectorIdAndLocDateBetween(inspectorId, startDate, endDate);
	}

	@Override
	public long countByStatus(QualityCheckStatus status) {
		validateQualityCheckStatus(status);
		return qualityCheckRepository.countByStatus(status);
	}

	@Override
	public long countByCheckType(QualityCheckType checkType) {
		validateQualityCheckType(checkType);
		return qualityCheckRepository.countByCheckType(checkType);
	}

	@Override
	public long countByReferenceType(ReferenceType referenceType) {
		validateReferenceType(referenceType);
		return qualityCheckRepository.countByReferenceType(referenceType);
	}

	@Override
	public long countByInspectorId(Long inspectorId) {
		validateUserId(inspectorId);
		return qualityCheckRepository.countByInspectorId(inspectorId);
	}

	@Override
	public long countByReferenceTypeAndStatus(ReferenceType referenceType, QualityCheckStatus status) {
		validateReferenceType(referenceType);
		validateQualityCheckStatus(status);
		return qualityCheckRepository.countByReferenceTypeAndStatus(referenceType, status);
	}

	@Override
	public long countByLocDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
		DateValidator.validateRange(startDate, endDate);
		return qualityCheckRepository.countByLocDateBetween(startDate, endDate);
	}

	@Override
	public List<QualityCheckResponse> findByInspectorId(Long inspectorId) {
		validateUserId(inspectorId);
		List<QualityCheck> items = qualityCheckRepository.findByInspectorId(inspectorId);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for inspector-id %d, found", inspectorId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByInspectorEmailLikeIgnoreCase(String email) {
		validateString(email);
		List<QualityCheck> items = qualityCheckRepository.findByInspectorEmailLikeIgnoreCase(email);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for inspector's email %s, found", email);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByInspectorPhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		List<QualityCheck> items = qualityCheckRepository.findByInspectorPhoneNumberLikeIgnoreCase(phoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for inspector phone-number %s, found", phoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByInspector_FirstNameContainingIgnoreCaseAndInspector_LastNameContainingIgnoreCase(
			String firstName, String lastName) {
		validateString(firstName);
		validateString(lastName);
		List<QualityCheck> items = qualityCheckRepository.findByInspector_FirstNameContainingIgnoreCaseAndInspector_LastNameContainingIgnoreCase(firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for inspector first-name %s and last-name %s, found",
					firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByInspectorIdAndStatus(Long inspectorId, QualityCheckStatus status) {
		validateUserId(inspectorId);
		validateQualityCheckStatus(status);
		List<QualityCheck> items = qualityCheckRepository.findByInspectorIdAndStatus(inspectorId, status);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for inspector-id %d and status %s, found", 
					inspectorId,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByInspectorIdAndCheckType(Long inspectorId, QualityCheckType checkType) {
		validateUserId(inspectorId);
		validateQualityCheckType(checkType);
		List<QualityCheck> items = qualityCheckRepository.findByInspectorIdAndCheckType(inspectorId, checkType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for inspector-id %d and check-type %s, found", 
					inspectorId,checkType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityCheckResponse> findByInspectorIdAndReferenceType(Long inspectorId, ReferenceType referenceType) {
		validateUserId(inspectorId);
		validateReferenceType(referenceType);
		List<QualityCheck> items = qualityCheckRepository.findByInspectorIdAndReferenceType(inspectorId, referenceType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityCheck for inspector-id %d and refernce-type %s, found",
					inspectorId,referenceType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityCheckMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StatusCountResponse> countByStatusGrouped() {
		List<StatusCountResponse> items = qualityCheckRepository.countByStatusGrouped();
		return items.stream()
				.map(item -> {
					QualityCheckStatus status = item.status();
					Long count = item.count();
					return new StatusCountResponse(status, count);
				})
				.toList();
	}

	@Override
	public List<CheckTypeCountResponse> countByCheckTypeGrouped() {
		List<CheckTypeCountResponse> items = qualityCheckRepository.countByCheckTypeGrouped();
		return items.stream()
				.map(item -> {
					QualityCheckType checkType = item.checkType();
					Long count = item.count();
					return new CheckTypeCountResponse(checkType, count);
				})
				.toList();
	}

	@Override
	public List<ReferenceTypeCountResponse> countByReferenceTypeGrouped() {
		List<ReferenceTypeCountResponse> items = qualityCheckRepository.countByReferenceTypeGrouped();
		return items.stream()
				.map(item -> {
					ReferenceType referenceType = item.referenceType();
					Long count = item.count();
					return new ReferenceTypeCountResponse(referenceType, count);
				})
				.toList();
	}

	@Override
	public List<InspectorCountResponse> countByInspectorGrouped() {
		List<InspectorCountResponse> items = qualityCheckRepository.countByInspectorGrouped();
		return items.stream()
				.map(item -> {
					Long inspectorId = item.inspectorId();
					Long count = item.count();
					return new InspectorCountResponse(inspectorId, count);
				})
				.toList();
	}

	@Override
	public List<InspectorNameCountResponse> countByInspectorNameGrouped() {
		List<InspectorNameCountResponse> items = qualityCheckRepository.countByInspectorNameGrouped();
		return items.stream()
				.map(item -> {
					String fullName = item.fullName();
					Long count = item.count();
					return new InspectorNameCountResponse(fullName, count);
				})
				.toList();
	}

	@Override
	public List<DateCountResponse> countByDateGrouped() {
		List<DateCountResponse> items = qualityCheckRepository.countByDateGrouped();
		return items.stream()
				.map(item -> {
					LocalDate localDate = item.localDate();
					Long count = item.count();
					return new DateCountResponse(localDate,count);
				})
				.toList();
	}
	
	private void validateReferenceType(ReferenceType referenceType) {
		Optional.ofNullable(referenceType)
			.orElseThrow(() -> new ValidationException("ReferenceType referenceType must not be null"));
	}
	
	private void validateQualityCheckType(QualityCheckType checkType) {
		Optional.ofNullable(checkType)
			.orElseThrow(() -> new ValidationException("QualityCheckType checkType must not be null"));
	}
	
	private void validateQualityCheckStatus(QualityCheckStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("QualityCheckStatus status must not be null"));
	}
	
	private void validateReferenceTypeList(List<ReferenceType> referenceType) {
		if(referenceType == null || referenceType.isEmpty()) {
			throw new ValidationException("ReferenceType list must not be empty nor null");
		}
		for(ReferenceType rt: referenceType) {
			if(rt == null) {
				throw new ValidationException("Each referenceType element, must not be null");
			}
		}
	}
	
	private void validateQualityCheckTypeList(List<QualityCheckType> checkType) {
		if(checkType == null || checkType.isEmpty()) {
			throw new ValidationException("QualityCheckType list must not be empty nor null");
		}
		for(QualityCheckType qt: checkType) {
			if(qt == null) {
				throw new ValidationException("Each checkType element, must not be null");
			}
		}
	}
	
	private void validateQualityCheckStatusList(List<QualityCheckStatus> status) {
		if(status == null || status.isEmpty()) {
			throw new ValidationException("QualityCheckStatus list must not be empty nor null");
		}
		for(QualityCheckStatus sta: status) {
			if(sta == null) {
				throw new ValidationException("Each status element, must not be null");
			}
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("Given characters must not be null nor empty");
		}
	}
	
	private User validateUserId(Long userId) {
		if(userId == null) {
			throw new ValidationException("Inspector ID must not be null");
		}
		return userRepository.findById(userId).orElseThrow(() -> new ValidationException("Inspector not found with id "+userId));
	}
	
	private QualityCheck validateQualityCheck(Long qualityCheckId) {
		if(qualityCheckId == null) {
			throw new ValidationException("QualityCheck ID must not be null");
		}
		return qualityCheckRepository.findById(qualityCheckId).orElseThrow(() -> new ValidationException("QualityCheck not found with id "+qualityCheckId));
	}
	
	private void validateReferenceId(Long refId) {
		if(refId == null) {
			throw new ValidationException("Reference-id must not be null");
		}
	}
}
