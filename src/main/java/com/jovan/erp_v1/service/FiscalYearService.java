package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.exception.FiscalYearErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.mapper.FiscalYearMapper;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.repository.FiscalYearRepository;
import com.jovan.erp_v1.request.FiscalQuarterRequest;
import com.jovan.erp_v1.request.FiscalYearRequest;
import com.jovan.erp_v1.response.FiscalYearResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FiscalYearService implements IFiscalYearService {

    private final FiscalYearRepository fiscalYearRepository;
    private final FiscalYearMapper fiscalYearMapper;

    @Transactional
    @Override
    public FiscalYearResponse create(FiscalYearRequest request) {
    	validatePositiveInteger(request.year(), "Godina");
        DateValidator.validateRange(request.startDate(), request.endDate());
        validateFiscalYearStatus(request.yearStatus());
        validateFiscalQuarterStatus(request.quarterStatus());
        validateQuarterList(request);
        FiscalYear year = fiscalYearMapper.toEntity(request);
        FiscalYear saved = fiscalYearRepository.save(year);
        return fiscalYearMapper.toResponse(fiscalYearRepository.save(saved));
    }

    @Transactional
    @Override
    public FiscalYearResponse update(Long id, FiscalYearRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        FiscalYear year = fiscalYearRepository.findById(id)
                .orElseThrow(() -> new FiscalYearErrorException("Fiscal-Year not found with id:" + id));
        validatePositiveInteger(request.year(), "Godina");
        DateValidator.validateRange(request.startDate(), request.endDate());
        validateFiscalYearStatus(request.yearStatus());
        validateFiscalQuarterStatus(request.quarterStatus());
        validateQuarterList(request);
        fiscalYearMapper.updateEntity(year, request);
        return fiscalYearMapper.toResponse(fiscalYearRepository.save(year));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!fiscalYearRepository.existsById(id)) {
            throw new FiscalYearErrorException("Fiscla-Year not found with id:" + id);
        }
        fiscalYearRepository.deleteById(id);
    }

    @Override
    public FiscalYearResponse findOne(Long id) {
        FiscalYear year = fiscalYearRepository.findById(id)
                .orElseThrow(() -> new FiscalYearErrorException("Fiscal-Year not found with id:" + id));
        return new FiscalYearResponse(year);
    }

    @Override
    public List<FiscalYearResponse> findAll() {
    	List<FiscalYear> items = fiscalYearRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("FiscalYear list is empty");
    	}
        return fiscalYearRepository.findAll().stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    /*
     * @Override
     * public List<FiscalYearResponse> findByStatus(FiscalYearStatus status) {
     * return fiscalYearRepository.findByStatus(status).stream()
     * .map(FiscalYearResponse::new)
     * .collect(Collectors.toList());
     * }
     */

    @Override
    public List<FiscalYearResponse> findBetweenStartAndEndDates(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
    	List<FiscalYear> items = fiscalYearRepository.findBetweenStartAndEndDates(start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No FiscalYear for start date %s and end date %s is found",
    				start.format(formatter), end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public FiscalYearResponse findByYear(Integer year) {
    	validatePositiveInteger(year, "Godina");
        FiscalYear y = fiscalYearRepository.findByYear(year)
                .orElseThrow(() -> new FiscalYearErrorException("Fiscal year not found with year: " + year));
        return new FiscalYearResponse(y);
    }

    @Override
    public FiscalYearResponse findByYearStatusAndYear(FiscalYearStatus status, Integer year) {
    	validateFiscalYearStatus(status);
    	validatePositiveInteger(year, "Godina");
        FiscalYear y = fiscalYearRepository.findByYearStatusAndYear(status, year)
                .orElseThrow(() -> new FiscalYearErrorException("Status and year not found"));
        return fiscalYearMapper.toResponse(y);
    }

    @Override
    public FiscalYearResponse findFirstByYearStatusOrderByStartDateDesc(FiscalYearStatus status) {
    	validateFiscalYearStatus(status);
        FiscalYear year = fiscalYearRepository.findFirstByYearStatusOrderByStartDateDesc(status)
                .orElseThrow(() -> new FiscalYearErrorException("FiscalYear status not found " + status));
        return new FiscalYearResponse(year);
    }

    @Override
    public List<FiscalYearResponse> findByStartDateAfter(LocalDate date) {
    	DateValidator.validateNotNull(date, "Datum ne sme biti null");
    	List<FiscalYear> items = fiscalYearRepository.findByStartDateAfter(date);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No FiscalYear found for start date after %s", date.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalYearResponse> findByEndDateBefore(LocalDate date) {
    	DateValidator.validateNotNull(date, "Datum ne sme biti null");
    	List<FiscalYear> items = fiscalYearRepository.findByEndDateBefore(date);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No FiscalYear end date before found %s", date.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalYearResponse> findByYearStatus(FiscalYearStatus yearStatus) {
    	validateFiscalYearStatus(yearStatus);
    	List<FiscalYear> items = fiscalYearRepository.findByYearStatus(yearStatus);
    	if(items.isEmpty()) {
    		String msg = String.format("No FiscalYear found for year status %s", yearStatus);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalYearResponse> findByQuarterStatus(FiscalQuarterStatus quarterStatus) {
    	validateFiscalQuarterStatus(quarterStatus);
    	List<FiscalYear> items = fiscalYearRepository.findByQuarterStatus(quarterStatus);
    	if(items.isEmpty()) {
    		String msg = String.format("No FiscalYear found for quarter status %s", quarterStatus);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalYearResponse> findByQuarterLessThan(FiscalQuarterStatus quarterStatus) {
    	validateFiscalQuarterStatus(quarterStatus);
    	List<FiscalYear> items = fiscalYearRepository.findByQuarterLessThan(quarterStatus);
    	if(items.isEmpty()) {
    		String msg = String.format("No FiscalYear found for quarter status less than %s", quarterStatus);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalYearResponse> findByQuarterGreaterThan(FiscalQuarterStatus quarterStatus) {
    	validateFiscalQuarterStatus(quarterStatus);
    	List<FiscalYear> items = fiscalYearRepository.findByQuarterGreaterThan(quarterStatus);
    	if(items.isEmpty()) {
    		String msg = String.format("No FiscalYear found for quarter status greater than %s", quarterStatus);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }
    
    private void validateFiscalYearStatus(FiscalYearStatus yearStatus) {
    	if(yearStatus == null) {
    		throw new IllegalArgumentException("yearStatus za FiscalYearStatus ne sme biti null");
    	}
    }
    
    private void validatePositiveInteger(Integer value, String fieldName) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException(fieldName + " mora biti pozitivan broj.");
        }
    }
    
    private void validateFiscalQuarterStatus(FiscalQuarterStatus quarterStatus) {
    	if(quarterStatus == null) {
    		throw new IllegalArgumentException("quarterStatus za FiscalQuarterStatus ne sme biti null");
    	}
    }
    
    private void validateQuarterList(FiscalYearRequest request) {
    	// Validacija svakog kvartala
    	List<FiscalQuarterRequest> quarter = request.quarters();
    	if(quarter == null || quarter.isEmpty()) {
    		throw new IllegalArgumentException("Request lista ne sme biti prazna");
    	}
    	for(FiscalQuarterRequest fq : quarter) {
    		validateFiscalQuarterStatus(fq.quarterStatus());
    		DateValidator.validateRange(fq.startDate(), fq.endDate());
    	}
    	// Provera preklapanja kvartala unutar godine
    	for(var i = 0; i < quarter.size(); i++) {
    		for(var j = i + 1; j < quarter.size(); j++) {
    			FiscalQuarterRequest fq1 = quarter.get(i);
    			FiscalQuarterRequest fq2 = quarter.get(j);
    			boolean overLaps = !(fq1.endDate().isBefore(fq2.startDate()) || fq2.startDate().isAfter(fq1.endDate()));
    			if(overLaps) {
    				throw new IllegalArgumentException("Kvartali se medjusobno preklapaju");
    			}
    		}
    	}
    }
}
