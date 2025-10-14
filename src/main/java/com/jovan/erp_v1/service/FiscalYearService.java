package com.jovan.erp_v1.service;

import java.time.LocalDate;
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

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.enumeration.FiscalYearTypeStatus;
import com.jovan.erp_v1.exception.FiscalYearErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.FiscalYearMapper;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.repository.FiscalYearRepository;
import com.jovan.erp_v1.repository.specification.FiscalYearSpecification;
import com.jovan.erp_v1.request.FiscalQuarterRequest;
import com.jovan.erp_v1.request.FiscalYearRequest;
import com.jovan.erp_v1.response.FiscalYearResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.FiscalYearSaveAsRequest;
import com.jovan.erp_v1.search_request.FiscalYearSearchRequest;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearMonthlyStatDTO;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearQuarterStatDTO;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearStatusStatDTO;
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
    
    @Transactional(readOnly = true)
    @Override
	public FiscalYearResponse trackFiscalYear(Long id) {
		List<FiscalYear> items = fiscalYearRepository.trackFiscalYear(id);
		if(items.isEmpty()) {
			throw new NoDataFoundException("FiscalYear with id "+id+" not found");
		}
		FiscalYear saved = items.get(0);
		return new FiscalYearResponse(fiscalYearRepository.save(saved));
	}

    @Transactional
	@Override
	public FiscalYearResponse confirmFiscalYear(Long id) {
    	FiscalYear year = fiscalYearRepository.findById(id).orElseThrow(() -> new ValidationException("FiscalYear not found with id "+id));
    	year.setConfirmed(true);
    	year.setStatus(FiscalYearTypeStatus.CONFIRMED);
    	year.getQuarters().stream()
    		.forEach(y -> y.setConfirmed(true));
		return new FiscalYearResponse(fiscalYearRepository.save(year));
	}

    @Transactional
	@Override
	public FiscalYearResponse cancelFiscalYear(Long id) {
    	FiscalYear year = fiscalYearRepository.findById(id).orElseThrow(() -> new ValidationException("FiscalYear not found with id "+id));
    	if(year.getStatus() != FiscalYearTypeStatus.CONFIRMED && year.getStatus() != FiscalYearTypeStatus.NEW) {
    		throw new ValidationException("Only NEW or CONFIRMED fiscal-year can be cancelled");
    	}
    	year.setStatus(FiscalYearTypeStatus.CANCELLED);
		return new FiscalYearResponse(fiscalYearRepository.save(year));
	}

    @Transactional
	@Override
	public FiscalYearResponse closeFiscalYear(Long id) {
    	FiscalYear year = fiscalYearRepository.findById(id).orElseThrow(() -> new ValidationException("FiscalYear not found with id "+id));
    	if(year.getStatus() != FiscalYearTypeStatus.CONFIRMED) {
    		throw new ValidationException("Only CONFIRMED fiscal-year can be closed");
    	}
    	year.setStatus(FiscalYearTypeStatus.CLOSED);
		return new FiscalYearResponse(fiscalYearRepository.save(year));
	}

    @Transactional
	@Override
	public FiscalYearResponse changeStatus(Long id, FiscalYearTypeStatus status) {
    	FiscalYear year = fiscalYearRepository.findById(id).orElseThrow(() -> new ValidationException("FiscalYear not found with id "+id));
    	validateFiscalYearTypeStatus(status);
    	if(year.getStatus() == FiscalYearTypeStatus.CLOSED) {
    		throw new ValidationException("Closed fiscal-year cannot change status");
    	}
    	if(status == FiscalYearTypeStatus.CONFIRMED) {
    		if(year.getStatus() != FiscalYearTypeStatus.NEW) {
    			throw new ValidationException("Only NEW fiscal-year can be confirmed");
    		}
    		year.setConfirmed(true);
    		year.getQuarters().forEach(y -> y.setConfirmed(true));
    	}
    	year.setStatus(status);
		return new FiscalYearResponse(fiscalYearRepository.save(year));
	}

    @Transactional
	@Override
	public List<FiscalYearMonthlyStatDTO> countFiscalYearsByYearAndMonth() {
    	List<FiscalYearMonthlyStatDTO> items = fiscalYearRepository.countFiscalYearsByYearAndMonth();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("No FiscalYear found for given year and month");
    	}
		return items.stream()
				.map(item -> {
					Integer year = item.getYear();
					Integer month = item.getMonth();
					Long count = item.getCount();
					return new FiscalYearMonthlyStatDTO(year,month,count);
				})
				.toList();
	}

	@Override
	public List<FiscalYearStatusStatDTO> countByFiscalYearStatus() {
		List<FiscalYearStatusStatDTO> items = fiscalYearRepository.countByFiscalYearStatus();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalYear four given status found");
		}
		return items.stream()
				.map(item -> {
					FiscalYearStatus yearStatus = item.yearStatus();
					Long count = item.count();
					return new FiscalYearStatusStatDTO(yearStatus, count);
				})
				.toList();
	}

	@Override
	public List<FiscalYearQuarterStatDTO> countByFiscalYearQuarterStatus() {
		List<FiscalYearQuarterStatDTO> items = fiscalYearRepository.countByFiscalYearQuarterStatus();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalYear for given quearter-status, found");
		}
		return items.stream()
				.map(item -> {
					FiscalQuarterStatus quarterStatus = item.quarterStatus();
					Long count = item.count();
					return new FiscalYearQuarterStatDTO(quarterStatus, count);
				})
				.toList();
	}

	@Transactional
	@Override
	public FiscalYearResponse saveFiscalYear(FiscalYearRequest request) {
		FiscalYear fy = FiscalYear.builder()
				.id(request.id())
				.year(request.year())
				.endDate(request.endDate())
				.yearStatus(request.yearStatus())
				.quarterStatus(request.quarterStatus())
				.status(request.status())
				.confirmed(request.confirmed())
				.build();
		FiscalYear saved = fiscalYearRepository.save(fy);
		return new FiscalYearResponse(saved);
	}
	
	private final AbstractSaveAsService<FiscalYear, FiscalYearResponse> saveAsHelper = new AbstractSaveAsService<FiscalYear, FiscalYearResponse>() {
		
		@Override
		protected FiscalYearResponse toResponse(FiscalYear entity) {
			return new FiscalYearResponse(entity);
		}
		
		@Override
		protected JpaRepository<FiscalYear, Long> getRepository() {
			return fiscalYearRepository;
		}
		
		@Override
		protected FiscalYear copyAndOverride(FiscalYear source, Map<String, Object> overrides) {
			return FiscalYear.builder()
					.endDate(source.getEndDate())
					.year((Integer) overrides.getOrDefault("Year", source.getYear()))
					.yearStatus(source.getYearStatus())
					.quarterStatus(source.getQuarterStatus())
					.build();
		}
	};
	
	private final AbstractSaveAllService<FiscalYear, FiscalYearResponse> saveAllHelper = new AbstractSaveAllService<FiscalYear, FiscalYearResponse>() {
		
		@Override
		protected Function<FiscalYear, FiscalYearResponse> toResponse() {
			return FiscalYearResponse::new;
		}
		
		@Override
		protected JpaRepository<FiscalYear, Long> getRepository() {
			return fiscalYearRepository;
		}
	};

	@Transactional
	@Override
	public FiscalYearResponse saveAs(FiscalYearSaveAsRequest req) {
		Map<String, Object> overrides = new HashMap<String, Object>();
		if(req.endDate() != null) overrides.put("End-date", req.endDate());
		if(req.year() != null) overrides.put("Year", req.year());
		if(req.yearStatus() != null) overrides.put("Year-status", req.yearStatus());
		if(req.quarterStatus() != null) overrides.put("Quarter-status", req.quarterStatus());
		return saveAsHelper.saveAs(req.sourceId(), overrides);
	}

	@Transactional
	@Override
	public List<FiscalYearResponse> saveAll(List<FiscalYearRequest> request) {
		List<FiscalYear> items = request.stream()
				.map(req -> FiscalYear.builder()
						.id(req.id())
						.endDate(req.endDate())
						.year(req.year())
						.yearStatus(req.yearStatus())
						.quarterStatus(req.quarterStatus())
						.confirmed(req.confirmed())
						.status(req.status())
						.build())
				.collect(Collectors.toList());
		return saveAllHelper.saveAll(items);
	}

	@Override
	public List<FiscalYearResponse> generalSearch(FiscalYearSearchRequest req) {
		Specification<FiscalYear> spec = FiscalYearSpecification.fromRequest(req);
		List<FiscalYear> items = fiscalYearRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalYer found for given criteria");
		}
		return items.stream().map(fiscalYearMapper::toResponse).collect(Collectors.toList());
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

    private void validateFiscalYearTypeStatus(FiscalYearTypeStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(() -> new ValidationException("FiscalYearTypeStatus status must not be null"));
    }
}
