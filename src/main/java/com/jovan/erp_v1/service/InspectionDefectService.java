package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionDefectStatus;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.InspectionDefectMapper;
import com.jovan.erp_v1.model.Batch;
import com.jovan.erp_v1.model.Defect;
import com.jovan.erp_v1.model.Inspection;
import com.jovan.erp_v1.model.InspectionDefect;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.QualityCheck;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.BatchRepository;
import com.jovan.erp_v1.repository.DefectRepository;
import com.jovan.erp_v1.repository.InspectionDefectRepository;
import com.jovan.erp_v1.repository.InspectionRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.QualityCheckRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.specification.InspectionDefectSpecification;
import com.jovan.erp_v1.request.InspectionDefectRequest;
import com.jovan.erp_v1.response.InspectionDefectResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.InspectionDefectSaveAsRequest;
import com.jovan.erp_v1.search_request.InspectionDefectSearchRequest;
import com.jovan.erp_v1.statistics.inspection_defect.InspectionDefectQuantityAffectedSummaryDTO;
import com.jovan.erp_v1.statistics.inspection_defect.QuantityAffectedByDefectStatDTO;
import com.jovan.erp_v1.statistics.inspection_defect.QuantityAffectedByInspectionStatDTO;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspectionDefectService implements InfInspectionDefectService {

	private final InspectionDefectRepository inspectionDefectRepository;
	private final InspectionRepository inspectionRepository;
	private final DefectRepository defectRepository;
	private final InspectionDefectMapper inspectionDefectMapper;
	private final ProductRepository productRepository;
	private final BatchRepository batchRepository;
	private final UserRepository userRepository;
	private final QualityCheckRepository qualityCheckRepository;
	
	@Transactional
	@Override
	public InspectionDefectResponse create(InspectionDefectRequest request) {
		validateInteger(request.quantityAffected());
		Inspection inspection = validateInspectionId(request.inspectionId());
		Defect defect = validateDefectId(request.defectId());
		InspectionDefect df = inspectionDefectMapper.toEntity(request, inspection, defect);
		InspectionDefect saved = inspectionDefectRepository.save(df);
		return new InspectionDefectResponse(saved);
	}
	
	@Transactional
	@Override
	public InspectionDefectResponse update(Long id, InspectionDefectRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		InspectionDefect def = inspectionDefectRepository.findById(id).orElseThrow(() -> new ValidationException("No InspectionDefect found with id "+id));
		validateInteger(request.quantityAffected());
		Inspection inspection = def.getInspection();
		if(request.inspectionId() != null && (inspection.getId() == null || !request.inspectionId().equals(inspection.getId()))) {
			inspection = validateInspectionId(request.inspectionId());
		}
		Defect defect =def.getDefect();
		if(request.defectId() != null && (defect.getId() == null || !request.defectId().equals(defect.getId()))) {
			defect = validateDefectId(request.defectId());
		}
		inspectionDefectMapper.toEntityUpdate(def, request, inspection, defect);
		InspectionDefect saved = inspectionDefectRepository.save(def);
		return new InspectionDefectResponse(saved);
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		if(!inspectionDefectRepository.existsById(id)) {
			throw new NoDataFoundException("No InspectionDefect found with id "+id);
		}
		inspectionDefectRepository.deleteById(id);
	}
	
	@Override
	public InspectionDefectResponse findOne(Long id) {
		InspectionDefect def = inspectionDefectRepository.findById(id).orElseThrow(() -> new ValidationException("No InspectionDefect found with id "+id));
		return new InspectionDefectResponse(def);
	}
	
	@Override
	public List<InspectionDefectResponse> findAll() {
		List<InspectionDefect> items = inspectionDefectRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("InspectionDefect list is empty");
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByQuantityAffected(Integer quantityAffected) {
		validateInteger(quantityAffected);
		List<InspectionDefect> items = inspectionDefectRepository.findByQuantityAffected(quantityAffected);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-affected %s, found", quantityAffected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByQuantityAffectedGreaterThan(Integer quantityAffected) {
		validateInteger(quantityAffected);
		List<InspectionDefect> items = inspectionDefectRepository.findByQuantityAffectedGreaterThan(quantityAffected);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-affected greater than %s, found", quantityAffected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByQuantityAffectedLessThan(Integer quantityAffected) {
		validateMinInteger(quantityAffected);
		List<InspectionDefect> items = inspectionDefectRepository.findByQuantityAffectedLessThan(quantityAffected);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-affected less than %s, found", quantityAffected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByQuantityAffectedBetween(Integer min, Integer max) {
		validateMinAndMaxInteger(min, max);
		List<InspectionDefect> items = inspectionDefectRepository.findByQuantityAffectedBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-affected between % and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspectionId(Long inspectionId) {
		validateInspectionId(inspectionId);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspectionId(inspectionId);
		if(items.isEmpty()) {
			String msg = String.format("No inspection-id %d, found", inspectionId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public boolean existsByInspectionCode(String inspectionCode) {
		validateString(inspectionCode);
		return inspectionDefectRepository.existsByInspectionCode(inspectionCode);
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_CodeLikeIgnoreCase(String inspectionCode) {
		validateString(inspectionCode);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_CodeLikeIgnoreCase(inspectionCode);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for inspection-code %s, found", inspectionCode);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Type(InspectionType type) {
		validateInspectionType(type);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Type(type);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for inspection-type %s, found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Result(InspectionResult result) {
		validateInspectionResult(result);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Result(result);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for inspection-result %s, found", result);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Notes(String notes) {
		validateString(notes);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Notes(notes);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for notes %s, found", notes);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_TypeAndInspection_Result(InspectionType type,
			InspectionResult result) {
		validateInspectionType(type);
		validateInspectionResult(result);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_TypeAndInspection_Result(type, result);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for insepction-type %s and inspection-result %s, found", type, result);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_NotesAndInspection_Type(String notes, InspectionType type) {
		validateString(notes);
		validateInspectionType(type);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_NotesAndInspection_Type(notes, type);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for notes %s and inspection-type %s, found", notes,type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_NotesAndInspection_Result(String notes,
			InspectionResult result) {
		validateString(notes);
		validateInspectionResult(result);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_NotesAndInspection_Result(notes, result);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for notes %s and inspection-result %s, found", notes,result);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_InspectionDate(LocalDateTime inspectionDate) {
		DateValidator.validateNotNull(inspectionDate, "Inspection date");
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_InspectionDate(inspectionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection for inspection-date %s found", inspectionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_InspectionDateBefore(LocalDateTime inspectionDate) {
		DateValidator.validateNotInFuture(inspectionDate, "Inspection date before");
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_InspectionDateBefore(inspectionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection for inspection date before %s, found", inspectionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_InspectionDateAfter(LocalDateTime inspectionDate) {
		DateValidator.validateNotInPast(inspectionDate, "Inspection date after");
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_InspectionDateAfter(inspectionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection for inspection date after %s, found", inspectionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_InspectionDateBetween(LocalDateTime start,
			LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_InspectionDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection for inspection date between %s and %s, found", 
					start.format(formatter), end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_InspectionDateAndInspection_Result(
			LocalDateTime inspectionDate, InspectionResult result) {
		DateValidator.validateNotNull(inspectionDate, "Inspection-date");
		validateInspectionResult(result);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_InspectionDateAndInspection_Result(inspectionDate, result);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection for inspection-date %s and inspection-result %s, found", 
					inspectionDate.format(formatter), result);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_InspectorId(Long inspectorId) {
		validateUserId(inspectorId);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_InspectorId(inspectorId);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for inspector-id %d, found", inspectorId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_InspectorFirstNameContainingIgnoreCaseAndInspection_InspectorLastNameContainingIgnoreCase(
			String firstName, String lastName) {
		validateString(firstName);
		validateString(lastName);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_InspectorFirstNameContainingIgnoreCaseAndInspection_InspectorLastNameContainingIgnoreCase(firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for inspector frst-name %s and last-name %s, found", firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_InspectorEmailLikeIgnoreCase(String inspectorEmail) {
		validateString(inspectorEmail);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_InspectorEmailLikeIgnoreCase(inspectorEmail);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for inspector email %s, found", inspectorEmail);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_InspectorPhoneNumberLikeIgnoreCase(
			String inspectorPhoneNumber) {
		validateString(inspectorPhoneNumber);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_InspectorPhoneNumberLikeIgnoreCase(inspectorPhoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for inspector phone-number %s, found", inspectorPhoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityInspected(Integer quantityInspected) {
		validateInteger(quantityInspected);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityInspected(quantityInspected);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-inspected %s, found", quantityInspected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityInspectedGreaterThan(Integer quantityInspected) {
		validateInteger(quantityInspected);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityInspectedGreaterThan(quantityInspected);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-inspected greater than %s, found", quantityInspected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityInspectedLessThan(Integer quantityInspected) {
		validateMinInteger(quantityInspected);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityInspectedLessThan(quantityInspected);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-inspected less than %s, found", quantityInspected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityInspectedBetween(Integer min, Integer max) {
		validateMinAndMaxInteger(min, max);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityInspectedBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-inspected between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityAccepted(Integer quantityAccepted) {
		validateInteger(quantityAccepted);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityAccepted(quantityAccepted);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-accepted %s, found", quantityAccepted);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityAcceptedGreaterThan(Integer quantityAccepted) {
		validateInteger(quantityAccepted);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityAcceptedGreaterThan(quantityAccepted);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-accepted greater than %s, found", quantityAccepted);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityAcceptedLessThan(Integer quantityAccepted) {
		validateMinInteger(quantityAccepted);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityAcceptedLessThan(quantityAccepted);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-accepted less than %s, found", quantityAccepted);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityAcceptedBetween(Integer min, Integer max) {
		validateMinAndMaxInteger(min, max);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityAcceptedBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-accepted between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityRejected(Integer quantityRejected) {
		validateInteger(quantityRejected);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityRejected(quantityRejected);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-rejected %s, found", quantityRejected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityRejectedGreaterThan(Integer quantityRejected) {
		validateInteger(quantityRejected);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityRejectedGreaterThan(quantityRejected);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-rejected greater than %s, found", quantityRejected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityRejectedLessThan(Integer quantityRejected) {
		validateMinInteger(quantityRejected);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityRejectedLessThan(quantityRejected);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-rejected less than %s, found", quantityRejected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QuantityRejectedBetween(Integer min, Integer max) {
		validateMinAndMaxInteger(min, max);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QuantityRejectedBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-rejected between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_Id(Long qualityCheckId) {
		validateQualityCheckId(qualityCheckId);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_Id(qualityCheckId);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quality-check id %d, found", qualityCheckId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_LocDate(LocalDateTime locDate) {
		DateValidator.validateNotNull(locDate, "Date-time");
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_LocDate(locDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection for quality-check date %s, found", locDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_LocDateAfter(LocalDateTime locDate) {
		DateValidator.validateNotInPast(locDate, "Date-after");
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_LocDateAfter(locDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection for quality-check date after %s, found", locDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_LocDateBefore(LocalDateTime locDate) {
		DateValidator.validateNotInFuture(locDate, "Date-before");
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_LocDateBefore(locDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection for quality-check date before %s, found", locDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_LocDateBetween(LocalDateTime start,
			LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_LocDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection for quality-check date between %s and %s, found", 
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_Notes(String notes) {
		validateString(notes);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_Notes(notes);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quality-check notes %s, found", notes);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_ReferenceId(Long referenceId) {
		validateReferenceId(referenceId);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_ReferenceId(referenceId);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quality check reference-id %d, found", referenceId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_ReferenceType(ReferenceType referenceType) {
		validateReferenceType(referenceType);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_ReferenceType(referenceType);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quality-check, reference-type %s, found", referenceType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_CheckType(QualityCheckType checkType) {
		validateQualityCheckType(checkType);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_CheckType(checkType);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quality check type %s, found", checkType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_Status(QualityCheckStatus status) {
		validateQualityCheckStatus(status);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quality check-status %s, found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_CheckType(
			ReferenceType referenceType, QualityCheckType checkType) {
		validateReferenceType(referenceType);
		validateQualityCheckType(checkType);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_CheckType(referenceType, checkType);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for reference-type %s and check-type %s, found",
					referenceType,checkType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_Status(
			ReferenceType referenceType, QualityCheckStatus status) {
		validateReferenceType(referenceType);
		validateQualityCheckStatus(status);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_Status(referenceType, status);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for reference-type %s and check-status %s, found", 
					referenceType,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_QualityCheck_CheckTypeAndInspection_QualityCheck_Status(
			QualityCheckType checkType, QualityCheckStatus status) {
		validateQualityCheckType(checkType);
		validateQualityCheckStatus(status);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_QualityCheck_CheckTypeAndInspection_QualityCheck_Status(checkType, status);
		if(items.isEmpty()){
			String msg = String.format("No inspection for check-type %s, and check-status %s, found",
					checkType,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Product_Id(Long productId) {
		validateProductId(productId);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Product_Id(productId);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for product's id %d, found", productId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Product_CurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Product_CurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for product's current-quantity %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Product_CurrentQuantityGreaterThan(
			BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Product_CurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for product's current-quantity greater than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Product_CurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Product_CurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for product's current-quantity less than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Product_CurrentQuantityBetween(BigDecimal min,
			BigDecimal max) {
		validateMinAndMax(min, max);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Product_CurrentQuantityBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for product's current-quantity between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Product_NameContainingIgnoreCase(String productName) {
		validateString(productName);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Product_NameContainingIgnoreCase(productName);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for product's name %s, found", productName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Product_UnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Product_UnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for product's unit-measure %s, found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Product_SupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Product_SupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for product's supplier-type %s, found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Product_StorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Product_StorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for product's storage-type %s, found", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Product_GoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Product_GoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for product's goods-type %s, found", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByInspection_Batch_Id(Long batchId) {
		validateBatchId(batchId);
		List<InspectionDefect> items = inspectionDefectRepository.findByInspection_Batch_Id(batchId);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for batch-id %d, found", batchId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByDefect_CodeContainingIgnoreCase(String code) {
		validateString(code);
		List<InspectionDefect> items = inspectionDefectRepository.findByDefect_CodeContainingIgnoreCase(code);
		if(items.isEmpty()) {
			String msg = String.format("No defect code %s, found", code);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByDefect_NameContainingIgnoreCase(String name) {
		validateString(name);
		List<InspectionDefect> items = inspectionDefectRepository.findByDefect_NameContainingIgnoreCase(name);
		if(items.isEmpty()) {
			String msg = String.format("No defect name %s, found", name);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByDefect_DescriptionContainingIgnoreCase(String description) {
		validateString(description);
		List<InspectionDefect> items = inspectionDefectRepository.findByDefect_DescriptionContainingIgnoreCase(description);
		if(items.isEmpty()) {
			String msg = String.format("No defect description %s, found", description);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByDefect_CodeContainingIgnoreCaseAndDefect_NameContainingIgnoreCase(
			String code, String name) {
		validateString(code);
		validateString(name);
		List<InspectionDefect> items = inspectionDefectRepository.findByDefect_CodeContainingIgnoreCaseAndDefect_NameContainingIgnoreCase(code, name);
		if(items.isEmpty()) {
			String msg = String.format("No defect for code %s and name %s, found", code,name);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByDefect_Severity(SeverityLevel severity) {
		validateSeverityLevel(severity);
		List<InspectionDefect> items = inspectionDefectRepository.findByDefect_Severity(severity);
		if(items.isEmpty()) {
			String msg = String.format("No defect-severity %s, found", severity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByDefect_CodeContainingIgnoreCaseAndDefect_Severity(String code,
			SeverityLevel severity) {
		validateString(code);
		validateSeverityLevel(severity);
		List<InspectionDefect> items = inspectionDefectRepository.findByDefect_CodeContainingIgnoreCaseAndDefect_Severity(code, severity);
		if(items.isEmpty()) {
			String msg = String.format("No defect-code %s and defect-severity %s, found", code,severity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByDefect_NameContainingIgnoreCaseAndDefect_Severity(String name,
			SeverityLevel severity) {
		validateString(name);
		validateSeverityLevel(severity);
		List<InspectionDefect> items = inspectionDefectRepository.findByDefect_NameContainingIgnoreCaseAndDefect_Severity(name, severity);
		if(items.isEmpty()) {
			String msg = String.format("No defect-severity %s and defect-name %s, found", 
					name,severity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByDefect_SeverityAndDefect_DescriptionContainingIgnoreCase(
			SeverityLevel severity, String descPart) {
		validateSeverityLevel(severity);
		validateString(descPart);
		List<InspectionDefect> items = inspectionDefectRepository.findByDefect_SeverityAndDefect_DescriptionContainingIgnoreCase(severity, descPart);
		if(items.isEmpty()) {
			String msg = String.format("No defect-severity %s and defect-description %s, found",
					severity,descPart);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public Long countByDefect_Severity(SeverityLevel severity) {
		validateSeverityLevel(severity);
		return inspectionDefectRepository.countByDefect_Severity(severity);
	}
	
	@Override
	public Long countByDefect_Code(String code) {
		validateString(code);
		return inspectionDefectRepository.countByDefect_Code(code);
	}
	
	@Override
	public Long countByDefect_Name(String name) {
		validateString(name);
		return inspectionDefectRepository.countByDefect_Name(name);
	}
	
	@Override
	public Boolean existsByDefect_Code(String code) {
		validateString(code);
		return inspectionDefectRepository.existsByDefect_Code(code);
	}
	
	@Override
	public Boolean existsByDefect_Name(String name) {
		validateString(name);
		return inspectionDefectRepository.existsByDefect_Name(name);
	}
	
	@Override
	public List<InspectionDefectResponse> findByConfirmed(Boolean confirmed) {
		List<InspectionDefect> items = inspectionDefectRepository.findByConfirmed(confirmed);
		if(items.isEmpty()) {
			String msg = String.format("No InspectionDefect for confirmed %b, found", confirmed);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<InspectionDefectResponse> findByDefectIdAndConfirmed(Long defectId, Boolean confirmed) {
		validateDefectId(defectId);
		List<InspectionDefect> items = inspectionDefectRepository.findByDefectIdAndConfirmed(defectId, confirmed);
		if(items.isEmpty()) {
			String msg = String.format("No InspectionDefect for defect-id %d and confirmed %b, found",
					defectId,confirmed);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	@Override
	public InspectionDefectResponse trackInspectionDefec(Long id) {
		InspectionDefect idef = inspectionDefectRepository.trackInspectionDefect(id).orElseThrow(() -> new ValidationException("InspectionDefect not found with id "+id));
		return new InspectionDefectResponse(idef);
	}

	@Transactional
	@Override
	public InspectionDefectResponse confirmInspectionDefect(Long id) {
		InspectionDefect idef = inspectionDefectRepository.trackInspectionDefect(id).orElseThrow(() -> new ValidationException("InspectionDefect not found with id "+id));
		idef.setConfirmed(true);
		idef.setStatus(InspectionDefectStatus.CONFIRMED);;
		return new InspectionDefectResponse(inspectionDefectRepository.save(idef));
	}

	@Transactional
	@Override
	public InspectionDefectResponse cancelInspectionDefect(Long id) {
		InspectionDefect idef = inspectionDefectRepository.trackInspectionDefect(id).orElseThrow(() -> new ValidationException("InspectionDefect not found with id "+id));
		if(idef.getStatus() != InspectionDefectStatus.NEW && idef.getStatus() != InspectionDefectStatus.CONFIRMED) {
			throw new ValidationException("Only NEW or CONFIRMED InspectionDefects can be cancelled");
		}
		idef.setStatus(InspectionDefectStatus.CANCELLED);
		return new InspectionDefectResponse(inspectionDefectRepository.save(idef));  
	}

	@Transactional
	@Override
	public InspectionDefectResponse closeInspectionDefect(Long id) {
		InspectionDefect idef = inspectionDefectRepository.trackInspectionDefect(id).orElseThrow(() -> new ValidationException("InspectionDefect not found with id "+id));
		if(idef.getStatus() != InspectionDefectStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED InspectionDefects can be closed");
		}
		idef.setStatus(InspectionDefectStatus.CLOSED);
		return new InspectionDefectResponse(inspectionDefectRepository.save(idef));  
	}

	@Transactional
	@Override
	public InspectionDefectResponse changeStatus(Long id, InspectionDefectStatus status) {
		InspectionDefect idef = inspectionDefectRepository.trackInspectionDefect(id).orElseThrow(() -> new ValidationException("InspectionDefect not found with id "+id));
		validateInspectionDefectStatus(status);
		if(idef.getStatus() == InspectionDefectStatus.CLOSED) {
			throw new ValidationException("Closed InspectionDefects cannot change status");
		}
		if(status == InspectionDefectStatus.CONFIRMED) {
			if(idef.getStatus() != InspectionDefectStatus.NEW) {
				throw new ValidationException("Only NEW InspectionDefects can be confirmed");
			}
			idef.setConfirmed(true);
		}
		idef.setStatus(status);
		return new InspectionDefectResponse(inspectionDefectRepository.save(idef));
	}

	@Transactional
	@Override
	public InspectionDefectResponse saveInspectionDefect(InspectionDefectRequest request) {
		InspectionDefect idef = InspectionDefect.builder()
				.id(request.id())
				.quantityAffected(request.quantityAffected())
				.inspection(validateInspectionId(request.inspectionId()))
				.defect(validateDefectId(request.defectId()))
				.build();
		InspectionDefect saved = inspectionDefectRepository.save(idef);
		return new InspectionDefectResponse(saved);
	}
	
	private final AbstractSaveAsService<InspectionDefect, InspectionDefectResponse> saveAsHelper = new AbstractSaveAsService<InspectionDefect, InspectionDefectResponse>() {
		
		@Override
		protected InspectionDefectResponse toResponse(InspectionDefect entity) {
			return new InspectionDefectResponse(entity); 
		}
		
		@Override
		protected JpaRepository<InspectionDefect, Long> getRepository() {
			return inspectionDefectRepository;
		}
		
		@Override
		protected InspectionDefect copyAndOverride(InspectionDefect source, Map<String, Object> overrides) {
			return InspectionDefect.builder()
					.quantityAffected((Integer) overrides.getOrDefault("Quantity-affected", source.getQuantityAffected()))
					.inspection(validateInspectionId(source.getInspection().getId()))
					.defect(validateDefectId(source.getDefect().getId()))
					.build();
		}
	};
	
	private final AbstractSaveAllService<InspectionDefect, InspectionDefectResponse> saveAllHelper = new AbstractSaveAllService<InspectionDefect, InspectionDefectResponse>() {
		
		@Override
		protected Function<InspectionDefect, InspectionDefectResponse> toResponse() {
			return InspectionDefectResponse::new;
		}
		
		@Override
		protected JpaRepository<InspectionDefect, Long> getRepository() {
			return inspectionDefectRepository;
		}
	};

	@Transactional
	@Override
	public InspectionDefectResponse saveAs(InspectionDefectSaveAsRequest request) {
		Map<String, Object> overrides = new HashMap<String, Object>();
		if(request.quantityAffected() != null) overrides.put("Quantity-affected", request.quantityAffected());
		if(request.inspectionId() != null) overrides.put("Inspection-ID", validateInspectionId(request.inspectionId()));
		if(request.defectId() != null) overrides.put("Defect-ID", validateDefectId(request.defectId()));
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}

	@Transactional
	@Override
	public List<InspectionDefectResponse> saveAll(List<InspectionDefectRequest> request) {
		List<InspectionDefect> items = request.stream()
				.map(req -> InspectionDefect.builder()
						.id(req.id())
						.quantityAffected(req.quantityAffected())
						.inspection(validateInspectionId(req.inspectionId()))
						.defect(validateDefectId(req.defectId()))
						.build())
				.toList();		
		return saveAllHelper.saveAll(items);
	}

	@Override
	public List<InspectionDefectResponse> generalSearch(InspectionDefectSearchRequest request) {
		Specification<InspectionDefect> spec = InspectionDefectSpecification.formRequest(request);
		List<InspectionDefect> items = inspectionDefectRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InspectionDefect found for given criteria");
		}
		return items.stream().map(inspectionDefectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public InspectionDefectQuantityAffectedSummaryDTO getQuantityAffectedSummary() {
		InspectionDefectQuantityAffectedSummaryDTO items = inspectionDefectRepository.getQuantityAffectedSummary();
		return new InspectionDefectQuantityAffectedSummaryDTO(1L, items.getQuantityAffected());
	}

	@Override
	public List<QuantityAffectedByInspectionStatDTO> countQuantityAffectedByInspection() {
		List<QuantityAffectedByInspectionStatDTO> items = inspectionDefectRepository.countQuantityAffectedByInspection();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InspectionDefect for quantity-affected by inspection, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityAffected = item.getQuantityAffected();
					Long inspectionId = item.getInspectionId();
					return new QuantityAffectedByInspectionStatDTO(count, quantityAffected, inspectionId);
				})
				.toList();
	}

	@Override
	public List<QuantityAffectedByDefectStatDTO> countQuantityAffectedByDefect() {
		List<QuantityAffectedByDefectStatDTO> items = inspectionDefectRepository.countQuantityAffectedByDefect();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InspectionDefect for quantity-affected by defect, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityAffected = item.getQuantityAffected();
					Long defectId = item.getDefectId();
					return new QuantityAffectedByDefectStatDTO(count, quantityAffected, defectId);
				})
				.toList();
	}
	
	private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new ValidationException("Tekstualni karakter ne sme biti null ili prazan");
        }
    }

    private void validateMinAndMax(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new ValidationException("Min i Max ne smeju biti null");
        }

        if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Min mora biti >= 0, a Max mora biti > 0");
        }

        if (min.compareTo(max) > 0) {
            throw new ValidationException("Min ne može biti veći od Max");
        }
    }
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Mora biti pozitivan broj");
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
    
    private void validateMinInteger(Integer num) {
		if(num == null || num < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
	}
    
    private void validateMinAndMaxInteger(Integer min, Integer max) {
		if(min == null || max == null) {
			throw new ValidationException("Both min and max must not be null.");
		}
		if(min < 0) {
			throw new ValidationException("Min must not be negative");
		}
		if(max <= 0) {
			throw new ValidationException("Max  must be greater than zero");
		}
		if(min > max) {
			throw new ValidationException("Min must not be greater than Max");
		}
	}
    
    private void validateInteger(Integer num) {
		if(num == null || num <= 0) {
			throw new ValidationException("Number must be positive");
		}
	}
    
    private Inspection validateInspectionId(Long inspectionId) {
    	if(inspectionId == null) {
    		throw new ValidationException("Inspection ID must not be null");
    	}
    	return inspectionRepository.findById(inspectionId).orElseThrow(() -> new ValidationException("Inspection not found with id "+inspectionId));
    }
    
    private Defect validateDefectId(Long defectId) {
    	if(defectId == null) {
    		throw new ValidationException("Defect ID must not be null");
    	}
    	return defectRepository.findById(defectId).orElseThrow(() -> new ValidationException("Defect not found with id "+defectId));
    }
    
    private void validateUnitMeasure(UnitMeasure unitMeasure) {
		Optional.ofNullable(unitMeasure)
			.orElseThrow(() -> new ValidationException("UnitMeasure unitMeasure must not be null"));
	}
	
	private void validateSupplierType(SupplierType supplierType) {
		Optional.ofNullable(supplierType)
			.orElseThrow(() -> new ValidationException("SupplierType supplierType must not be null"));
	}
	
	private void validateStorageType(StorageType storageType) {
		Optional.ofNullable(storageType)
			.orElseThrow(() -> new ValidationException("StorageType storageType"));
	}
	
	private void validateGoodsType(GoodsType goodsType) {
		Optional.ofNullable(goodsType)
			.orElseThrow(() -> new ValidationException("GoodsType goodsType must not be null"));
	}
	
	private void validateInspectionType(InspectionType type) {
		Optional.ofNullable(type)
			.orElseThrow(() -> new ValidationException("InspectionType type must not be null"));
	}
	
	private void validateInspectionResult(InspectionResult result) {
		Optional.ofNullable(result)
			.orElseThrow(() -> new ValidationException("InspectionResult result must not be null"));
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
	
	private void validateSeverityLevel(SeverityLevel severity) {
		Optional.ofNullable(severity)
			.orElseThrow(() -> new ValidationException("SeverityLevel severity must not be null"));
	}
	
	private void validateInspectionDefectStatus(InspectionDefectStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("InspectionDefectStatus status must not be null"));
	}
	
	private Product validateProductId(Long productId) {
		if(productId == null) {
			throw new ValidationException("Product ID must not be null");
		}
		return productRepository.findById(productId).orElseThrow(() -> new ValidationException("Product not found with id "+productId));
	}
	
	private User validateUserId(Long userId) {
		if(userId == null) {
			throw new ValidationException("Inspector ID must not be null");
		}
		return userRepository.findById(userId).orElseThrow(() -> new ValidationException("Inspector not found with id "+userId));
	}
	
	private void validateReferenceId(Long referenceId) {
		if(referenceId == null) {
			throw new ValidationException("Reference-id, must not be null");
		}
	}
	
	private QualityCheck validateQualityCheckId(Long qualityCheckId) {
		if(qualityCheckId == null) {
			throw new ValidationException("QualityCheck ID must not be null");
		}
		return qualityCheckRepository.findById(qualityCheckId).orElseThrow(() -> new ValidationException("QualityCheck not found with id "+qualityCheckId));
	}
	
	private Batch validateBatchId(Long batchId) {
		if(batchId == null) {
			throw new ValidationException("Batch ID must not be null");
		}
		return batchRepository.findById(batchId).orElseThrow(() -> new ValidationException("Batch not found with id "+batchId));
	}

}
