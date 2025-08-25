package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.DefectMapper;
import com.jovan.erp_v1.model.Defect;
import com.jovan.erp_v1.repository.DefectRepository;
import com.jovan.erp_v1.request.DefectRequest;
import com.jovan.erp_v1.response.DefectResponse;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefectService implements IDefectService {

	private final DefectRepository defectRepository;
	private final DefectMapper defectMapper;

	@Transactional
	@Override
	public DefectResponse create(DefectRequest request) {
		validateCodeExist(request.code(), "Code");
		validateNameExist(request.name(), "Name");
		validateString(request.description());
		validaSeverityLevel(request.severity());
		Defect d = defectMapper.toEntity(request);
		Defect saved = defectRepository.save(d);
		return new DefectResponse(saved);
	}

	@Transactional
	@Override
	public DefectResponse update(Long id, DefectRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		Defect d = defectRepository.findById(id).orElseThrow(() -> new ValidationException("Defect not found with id "+id));
		validateStringsNotEmpty("Name, code, description", request.name(), request.code(), request.description());
		defectMapper.toEntityUpdate(d, request);
		Defect update = defectRepository.save(d);
		return new DefectResponse(update);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if(!defectRepository.existsById(id)) {
			throw new ValidationException("Defect id must not be null");
		}
		defectRepository.deleteById(id);
	}

	@Override
	public DefectResponse findOne(Long id) {
		Defect d = defectRepository.findById(id).orElseThrow(() -> new ValidationException("Defect not found with id "+id));
		return new DefectResponse(d);
	}

	@Override
	public List<DefectResponse> findAll() {
		List<Defect> items = defectRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Defect list is empty");
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<DefectResponse> findByCodeContainingIgnoreCase(String code) {
		validateString(code);
		List<Defect> items = defectRepository.findByCodeContainingIgnoreCase(code);
		if(items.isEmpty()) {
			String msg = String.format("No Defect for code %s is found", code);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<DefectResponse> findByNameContainingIgnoreCase(String name) {
		validateString(name);
		List<Defect> items = defectRepository.findByNameContainingIgnoreCase(name);
		if(items.isEmpty()) {
			String msg = String.format("No Defect for name %s is found", name);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<DefectResponse> findByDescriptionContainingIgnoreCase(String description) {
		validateString(description);
		List<Defect> items = defectRepository.findByDescriptionContainingIgnoreCase(description);
		if(items.isEmpty()) {
			String msg = String.format("No Defect for description %s is found", description);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<DefectResponse> findByCodeContainingIgnoreCaseAndNameContainingIgnoreCase(String code, String name) {
		validateString(code);
		validateString(name);
		List<Defect> items = defectRepository.findByCodeContainingIgnoreCaseAndNameContainingIgnoreCase(code, name);
		if(items.isEmpty()) {
			String msg = String.format("No Defect for code %s and name %s ,is found",code, name);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<DefectResponse> findBySeverity(SeverityLevel severity) {
		validaSeverityLevel(severity);
		List<Defect> items = defectRepository.findBySeverity(severity);
		if(items.isEmpty()) {
			String msg = String.format("No Defect for severity-level %s is found", severity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<DefectResponse> findByCodeContainingIgnoreCaseAndSeverity(String code, SeverityLevel severity) {
		validateString(code);
		validaSeverityLevel(severity);
		List<Defect> items = defectRepository.findByCodeContainingIgnoreCaseAndSeverity(code, severity);
		if(items.isEmpty()) {
			String msg = String.format("No Defect for code %s and severity-level %s, is found", code,severity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<DefectResponse> findByNameContainingIgnoreCaseAndSeverity(String name, SeverityLevel severity) {
		validateString(name);
		validaSeverityLevel(severity);
		List<Defect> items = defectRepository.findByNameContainingIgnoreCaseAndSeverity(name, severity);
		if(items.isEmpty()) {
			String msg = String.format("No Defect for name %s and severity-level %s, is found", name,severity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<DefectResponse> findAllByOrderBySeverityAsc() {
		List<Defect> items = defectRepository.findAllByOrderBySeverityAsc();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Defect for severity-levely in ascending order found");
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<DefectResponse> findAllByOrderBySeverityDesc() {
		List<Defect> items = defectRepository.findAllByOrderBySeverityDesc();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Defect for severity-levely in descending order found");
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<DefectResponse> findBySeverityAndDescriptionContainingIgnoreCase(SeverityLevel severity,
			String descPart) {
		validaSeverityLevel(severity);
		validateString(descPart);
		List<Defect> items = defectRepository.findBySeverityAndDescriptionContainingIgnoreCase(severity, descPart);
		if(items.isEmpty()) {
			String msg = String.format("No Defect for severity-level %s and description %s, is found", 
					severity,descPart);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public Long countBySeverity(SeverityLevel severity) {
		validaSeverityLevel(severity);
		return defectRepository.countBySeverity(severity);
	}

	@Override
	public Long countByCode(String code) {
		validateString(code);
		return defectRepository.countByCode(code);
	}

	@Override
	public Long countByName(String name) {
		validateString(name);
		return defectRepository.countByName(name);
	}

	@Override
	public List<DefectResponse> findBySeverityIn(List<SeverityLevel> levels) {
		validaSeverityList(levels);
		List<Defect> items = defectRepository.findBySeverityIn(levels);
		if(items.isEmpty()) {
			String msg = String.format("No Defect for serverity-lists, %levels, is found", levels);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(defectMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public Boolean existsByNameContainingIgnoreCase(String name) {
		validateString(name);
		return defectRepository.existsByNameContainingIgnoreCase(name);
	}

	@Override
	public Boolean existsByCodeContainingIgnoreCase(String code) {
		validateString(code);
		return defectRepository.existsByCodeContainingIgnoreCase(code);
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}
	
	private void validaSeverityLevel(SeverityLevel severity) {
		Optional.ofNullable(severity)
			.orElseThrow(() -> new ValidationException("SeverityLevel severity must not be null"));
	}
	
	private void validaSeverityList(List<SeverityLevel> levels) {
		if(levels == null || levels.isEmpty()) {
			throw new ValidationException("SeverityLevel list must not be null nor empty");
		}
		//lambda funkcija koja vrsi iteraciju, kao for-petlja
		if (levels.stream().anyMatch(Objects::isNull)) {
	        throw new ValidationException("SeverityLevel list must not contain null items");
	    }
	}
	
	private void validateNameExist(String value, String fieldName) {
		if(value == null || value.trim().isEmpty()) {
	        throw new ValidationException(fieldName + " must not be null or empty");
	    }
		if(defectRepository.existsByNameContainingIgnoreCase(value)) {
			throw new ValidationException("Given name doesn't exist");
		}
	}
	
	private void validateCodeExist(String value, String fieldName) {
		if(value == null || value.trim().isEmpty()) {
	        throw new ValidationException(fieldName + " must not be null or empty");
	    }
		if(defectRepository.existsByCodeContainingIgnoreCase(value)) {
			throw new ValidationException("Given code doesn't exist");
		}
	}
	
	private void validateStringsNotEmpty(String fieldName, String... values) {
	    for (String val : values) {
	        if (val == null || val.trim().isEmpty()) {
	            throw new ValidationException(fieldName + " must not be null or empty");
	        }
	    }
	}

}
