package com.jovan.erp_v1.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.DefectStatus;
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
	
	@Transactional
	@Override
	public DefectResponse confirmDefect(Long id) {
		Defect defect = defectRepository.findById(id).orElseThrow(() -> new ValidationException("Defect not found with id "+id));
		defect.setConfirmed(true);
		defect.setStatus(DefectStatus.CONFIRMED);
		defect.getInspections().stream()
			.filter(ins -> ins.getQuantityAffected() > 0)
			.forEach(ins -> ins.setConfirmed(true));
		defectRepository.save(defect);
		return new DefectResponse(defect);
	}
	
	@Transactional
	@Override
	public DefectResponse closeDefect(Long id) {
		Defect defect = defectRepository.findById(id).orElseThrow(() -> new ValidationException("Defect not found with id "+id));
		if (defect.getStatus() != DefectStatus.CONFIRMED) {
	        throw new ValidationException("Only CONFIRMED defects can be closed");
	    }
	    defect.setStatus(DefectStatus.CLOSED);
	    return new DefectResponse(defectRepository.save(defect));
	}

	@Transactional
	@Override
	public DefectResponse cancelDefect(Long id) {
		Defect defect = defectRepository.findById(id).orElseThrow(() -> new ValidationException("Defect not found with id "+id));
		if (defect.getStatus() != DefectStatus.NEW && defect.getStatus() != DefectStatus.CONFIRMED) {
		    throw new ValidationException("Only NEW or CONFIRMED defects can be cancelled");
		}
		defect.setStatus(DefectStatus.CANCELLED);
		return new DefectResponse(defect);
	}
	
	//genericka metoda za dodavanje novih defekt-statusa
	@Transactional
	@Override
	public DefectResponse changeStatus(Long id, DefectStatus newStatus) {
		validateDefectStatus(newStatus);
		Defect defect = defectRepository.findById(id)
		        .orElseThrow(() -> new ValidationException("Defect not found with id " + id));
		if (defect.getStatus() == DefectStatus.CLOSED) {
            throw new ValidationException("Closed defects cannot change status");
        }
        if (newStatus == DefectStatus.CONFIRMED) {
            if (defect.getStatus() != DefectStatus.NEW) {
                throw new ValidationException("Only NEW defects can be confirmed");
            }
            defect.setConfirmed(true);
            defect.getInspections().forEach(ins -> ins.setConfirmed(true));
        }
        defect.setStatus(newStatus);
        return new DefectResponse(defectRepository.save(defect));
	}
	
	@Override
	public List<DefectResponse> searchDefects(SeverityLevel severity, String descPart, DefectStatus status,
			Boolean confirmed) {
		if (severity != null) validaSeverityLevel(severity);
	    if (descPart != null && !descPart.trim().isEmpty()) validateString(descPart);
	    if (status != null) validateDefectStatus(status);
	    if (severity == null && (descPart == null || descPart.trim().isEmpty()) 
	            && status == null && confirmed == null) {
	        return defectRepository.findAll().stream()
	                .map(defectMapper::toResponse)
	                .collect(Collectors.toList());
	    }
	    List<Defect> items = defectRepository.searchDefects(severity, descPart, status, confirmed);
	    return items.stream()
	            .map(defectMapper::toResponse)
	            .collect(Collectors.toList());
	}
	
	@Override
	public List<DefectResponse> generalSearch(Long id, Long idFrom, Long idTo, String code, String name, String description,
			SeverityLevel severity, DefectStatus status, Boolean confirmed) {
		// Ako je trazen samo jedan ID odmah vrati rezultat za taj ID
	    if (id != null) {
	        validateDefectId(id);
	        return defectRepository.findById(id)
	                .map(defectMapper::toResponse)
	                .map(Collections::singletonList)
	                .orElseThrow(() -> new NoDataFoundException("Defect not found with id " + id));
	    }
	    //Validacija za range ID
	    if (idFrom != null || idTo != null) {
	        if (idFrom == null || idTo == null) {
	            throw new ValidationException("Both idFrom and idTo must be provided for range search");
	        }
	        if (idFrom > idTo) {
	            throw new ValidationException("idFrom must not be greater than idTo");
	        }
	    }
	    // Validacija za ostale filtere
	    if (code != null) validateCodeExist(code, "Code");
	    if (name != null) validateNameExist(name, "Name");
	    if (description != null) validateString(description);
	    if (severity != null) validaSeverityLevel(severity);
	    if (status != null) validateDefectStatus(status);
	    // Ako nije prosledjen nijedan filter vrati sve defekte
	    if (idFrom == null && idTo == null &&
	        (code == null || code.trim().isEmpty()) &&
	        (name == null || name.trim().isEmpty()) &&
	        (description == null || description.trim().isEmpty()) &&
	        severity == null && status == null && confirmed == null) {
	        return defectRepository.findAll().stream()
	                .map(defectMapper::toResponse)
	                .collect(Collectors.toList());
	    }
	    List<Defect> items = defectRepository.generalSearch(id,idFrom, idTo, code, name, description, severity, status, confirmed);
	    if (items.isEmpty()) {
	        String msg = String.format("No defects found for given filters: idFrom=%s, idTo=%s, code=%s, name=%s, desc=%s, severity=%s, status=%s, confirmed=%s",
	                idFrom, idTo, code, name, description, severity, status, confirmed);
	        throw new NoDataFoundException(msg);
	    }
	    return items.stream()
	            .map(defectMapper::toResponse)
	            .collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	@Override
	public DefectResponse trackDefect(Long id) {
		List<Defect> defects = defectRepository.trackDefect(id);
	    if (defects.isEmpty()) {
	        throw new NoDataFoundException("Defect with id " + id + " not found");
	    }
	    Defect defect = defects.get(0);
	    return new DefectResponse(defect);
	}
	
	private List<Long> findByRangeId(Long from, Long to) {
		if(from == null || to == null) {
			throw new ValidationException("IdFrom & idTo must not be null");
		}
		if(from > to) {
			throw new ValidationException("IdFrom must not be greater than idTo");
		}
		if ((to - from) > 10000) { // za limit
	        throw new ValidationException("Range is too large, please narrow down your search");
	    }
		List<Long> idsRange = new ArrayList<>();
		for(var i = from; i <= to; i++) {
			idsRange.add(i);
		}
		return idsRange;
	}
	
	private Defect validateDefectId(Long id) {
		if(id == null) {
			throw new ValidationException("Defect ID must not be null");
		}
		return defectRepository.findById(id).orElseThrow(() -> new ValidationException("Defect not found with id "+id));
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
	
	private void validateDefectStatus(DefectStatus newStatus) {
		Optional.ofNullable(newStatus)
			.orElseThrow(() -> new ValidationException("DefectStatus newStatus must not be null"));
	}

}
