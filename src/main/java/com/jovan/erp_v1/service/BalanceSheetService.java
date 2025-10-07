package com.jovan.erp_v1.service;

import java.math.BigDecimal;
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

import com.jovan.erp_v1.enumeration.BalanceSheetStatus;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.exception.BalanceSheetErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.BalanceSheetMapper;
import com.jovan.erp_v1.model.BalanceSheet;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.repository.BalanceSheetRepository;
import com.jovan.erp_v1.repository.FiscalYearRepository;
import com.jovan.erp_v1.repository.specification.BalanceSheetSpecification;
import com.jovan.erp_v1.request.BalanceSheetRequest;
import com.jovan.erp_v1.request.BalanceSheetSearchRequest;
import com.jovan.erp_v1.response.BalanceSheetResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.BalanceSheetSaveAsRequest;
import com.jovan.erp_v1.search_request.BalanceSheetGeneralSearchRequest;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BalanceSheetService implements IBalanceSheetService {

    private final BalanceSheetRepository balanceSheetRepository;
    private final BalanceSheetMapper balanceSheetMapper;
    private final FiscalYearRepository fiscalYearRepository;

    @Transactional
    @Override
    public BalanceSheetResponse create(BalanceSheetRequest request) {
    	DateValidator.validateNotNull(request.date(), "Datum ne sme biti null");
        validateBigDecimal(request.totalAssets());
        validateBigDecimal(request.totalLiabilities());
        validateBigDecimal(request.totalEquity());
        validateBalanceSheetStatus(request.status());
        FiscalYear year = fetchFiscalYearId(request.fiscalYearId());
        BalanceSheet sheet = balanceSheetMapper.toEntity(request,year);
        BalanceSheet saved = balanceSheetRepository.save(sheet);
        return balanceSheetMapper.toResponse(balanceSheetRepository.save(saved));
    }

    @Transactional
    @Override
    public BalanceSheetResponse update(Long id, BalanceSheetRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        BalanceSheet sheet = balanceSheetRepository.findById(id)
                .orElseThrow(() -> new BalanceSheetErrorException("BalanceSheet not found with id " + id));
        DateValidator.validateNotNull(request.date(), "Datum ne sme biti null");
        validateBigDecimal(request.totalAssets());
        validateBigDecimal(request.totalLiabilities());
        validateBigDecimal(request.totalEquity());
        validateBalanceSheetStatus(request.status());
        FiscalYear year = sheet.getFiscalYear();
        if(request.fiscalYearId() != null && (sheet.getFiscalYear() == null || !request.fiscalYearId().equals(sheet.getFiscalYear().getId()))) {
        	year = fetchFiscalYearId(request.fiscalYearId());
        }
        balanceSheetMapper.toEntityUpdate(sheet, request,year);
        return balanceSheetMapper.toResponse(balanceSheetRepository.save(sheet));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!balanceSheetRepository.existsById(id)) {
            throw new BalanceSheetErrorException("BalanceSheet not found with id " + id);
        }
        balanceSheetRepository.deleteById(id);
    }

    @Override
    public BalanceSheetResponse findOne(Long id) {
        BalanceSheet sheet = balanceSheetRepository.findById(id)
                .orElseThrow(() -> new BalanceSheetErrorException("BalanceSheet not found with id " + id));
        return new BalanceSheetResponse(sheet);
    }

    @Override
    public List<BalanceSheetResponse> findAll() {
    	List<BalanceSheet> items = balanceSheetRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("List of items for balance sheet is empty");
    	}
        return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByTotalAssets(BigDecimal totalAssets) {
    	validateBigDecimal(totalAssets);
    	List<BalanceSheet> items = balanceSheetRepository.findByTotalAssets(totalAssets);
    	if(items.isEmpty()) {
    		String msg = String.format("No balance sheet for total assets %s is found", totalAssets);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public BalanceSheetResponse findByDate(LocalDate date) {
    	DateValidator.validateNotNull(date, "Datum ne sme biti null");
        BalanceSheet sheet = balanceSheetRepository.findByDate(date)
                .orElseThrow(() -> new BalanceSheetErrorException("BalanceSheet for given date not found: " + date));
        return balanceSheetMapper.toResponse(sheet);
    }

    @Override
    public List<BalanceSheetResponse> findByDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
    	List<BalanceSheet> items = balanceSheetRepository.findByDateBetween(start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No balance sheet for date between %s and %s is found",
    				start.format(formatter), end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByTotalLiabilities(BigDecimal totalLiabilities) {
    	validateBigDecimal(totalLiabilities);
    	List<BalanceSheet> items = balanceSheetRepository.findByTotalLiabilities(totalLiabilities);
    	if(items.isEmpty()) {
    		String msg = String.format("No balance sheet for total liabilities %s is found", totalLiabilities);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByTotalEquity(BigDecimal totalEquity) {
    	validateBigDecimal(totalEquity);
    	List<BalanceSheet> items = balanceSheetRepository.findByTotalEquity(totalEquity);
    	if(items.isEmpty()) {
    		String msg = String.format("No balance sheet for total equity %s is found", totalEquity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_Id(Long id) {
    	validatefiscalYear(id);
    	List<BalanceSheet> items = balanceSheetRepository.findByFiscalYear_Id(id);
    	if(items.isEmpty()) {
    		String msg = String.format("No balance sheet found for fiscal year id %d", id);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_Year(Integer year) {
    	validateInteger(year);
    	List<BalanceSheet> items = balanceSheetRepository.findByFiscalYear_Year(year);
    	if(items.isEmpty()) {
    		String msg = String.format("No balance sheet found for given fiscal year %d", year);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus) {
    	validateFiscalYearStatus(yearStatus);
    	List<BalanceSheet> items = balanceSheetRepository.findByFiscalYear_YearStatus(yearStatus);
    	if(items.isEmpty()) {
    		String msg = String.format("No balance sheet with fiscal year status %s is not found", yearStatus);
    		throw new NoDataFoundException(msg);
    	}
        return balanceSheetRepository.findByFiscalYear_YearStatus(yearStatus).stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus) {
    	validateFiscalQuarterStatus(quarterStatus);
    	List<BalanceSheet> items = balanceSheetRepository.findByFiscalYear_QuarterStatus(quarterStatus);
    	if(items.isEmpty()) {
    		String msg = String.format("No balance sheet with fiscal year quarter status %s is not found", quarterStatus);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByStatusAndDateRange(FiscalYearStatus status, LocalDate start,
            LocalDate end) {
    	validateFiscalYearStatus(status);
    	DateValidator.validateRange(start, end);
    	List<BalanceSheet> items = balanceSheetRepository.findByStatusAndDateRange(status, start, end);
    	if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No balance sheet with status %s and date range between %s and %s is not found",
					status,start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
        return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
	public List<BalanceSheetResponse> findByTotalAssetsGreaterThan(BigDecimal totalAssets) {
		validateBigDecimal(totalAssets);
		List<BalanceSheet> items = balanceSheetRepository.findByTotalAssetsGreaterThan(totalAssets);
		if(items.isEmpty()) {
			String msg = String.format("Balance sheet with total assets greater than %s is not found", totalAssets);
			throw new NoDataFoundException(msg);
		}
		return balanceSheetRepository.findByTotalAssetsGreaterThan(totalAssets).stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
	}

	@Override
	public List<BalanceSheetResponse> findByTotalAssetsLessThan(BigDecimal totalAssets) {
		validateBigDecimalNonNegative(totalAssets);
		List<BalanceSheet> items = balanceSheetRepository.findByTotalAssetsLessThan(totalAssets);
		if(items.isEmpty()) {
			String msg = String.format("Balance sheet with total assets less than %s is not found", totalAssets);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
	}
		

	@Override
	public List<BalanceSheetResponse> findByTotalEquityGreaterThan(BigDecimal totalEquity) {
		validateBigDecimal(totalEquity);
		List<BalanceSheet> items = balanceSheetRepository.findByTotalEquityGreaterThan(totalEquity);
		if(items.isEmpty()) {
			String msg = String.format("Balance sheet with total equity greater than %s is not found", totalEquity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
	}

	@Override
	public List<BalanceSheetResponse> findByTotalEquityLessThan(BigDecimal totalEquity) {
		validateBigDecimalNonNegative(totalEquity);
		List<BalanceSheet> items = balanceSheetRepository.findByTotalEquityLessThan(totalEquity);
		if(items.isEmpty()) {
			String msg = String.format("Balance sheet with total equity less than %s is not found", totalEquity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
	}
	
	@Override
	public List<BalanceSheetResponse> searchBalanceSheets(LocalDate startDate, LocalDate endDate, Long fiscalYearId, BigDecimal minAssets) {
	    Specification<BalanceSheet> spec = Specification.where(null);
	    if (fiscalYearId != null) {
	        spec = spec.and(BalanceSheetSpecification.hasFiscalYearId(fiscalYearId));
	    }
	    if (startDate != null || endDate != null) {
	        spec = spec.and(BalanceSheetSpecification.hasDateBetween(startDate, endDate));
	    }
	    if (minAssets != null) {
	        spec = spec.and(BalanceSheetSpecification.assetsGreaterThan(minAssets));
	    }
	    List<BalanceSheet> items = balanceSheetRepository.findAll(spec);
	    return items.stream().map(balanceSheetMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<BalanceSheetResponse> searchBalanceSheets(BalanceSheetSearchRequest request) {
        Specification<BalanceSheet> spec = Specification.where(null);
        if (request.fiscalYearId() != null) {
            spec = spec.and(BalanceSheetSpecification.hasFiscalYearId(request.fiscalYearId()));
        }
        if (request.startDate() != null || request.endDate() != null) {
            spec = spec.and(BalanceSheetSpecification.hasDateBetween(request.startDate(), request.endDate()));
        }
        if (request.minAssets() != null) {
            spec = spec.and(BalanceSheetSpecification.assetsGreaterThan(request.minAssets()));
        }
        if (request.minEquity() != null) {
            spec = spec.and(BalanceSheetSpecification.equityGreaterThan(request.minEquity()));
        }
        if (request.minLiabilities() != null) {
            spec = spec.and(BalanceSheetSpecification.liabilitiesGreaterThan(request.minLiabilities()));
        }
        if (Boolean.TRUE.equals(request.onlySolvent())) {
            spec = spec.and(BalanceSheetSpecification.isSolvent());
        }
        List<BalanceSheet> items =  balanceSheetRepository.findAll(spec);
        return items.stream().map(balanceSheetMapper::toResponse).collect(Collectors.toList());
    }
	
	@Override
	public List<BalanceSheetResponse> findByTotalLiabilitiesLessThan(BigDecimal totalLiabilities) {
		validateBigDecimalNonNegative(totalLiabilities);
		List<BalanceSheet> items = balanceSheetRepository.findByTotalLiabilitiesLessThan(totalLiabilities);
		if(items.isEmpty()) {
			String msg = String.format("No BalanceSheet found for total liabilities less tha %s", totalLiabilities);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(balanceSheetMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BalanceSheetResponse> findByTotalLiabilitiesGreaterThan(BigDecimal totalLiabilities) {
		validateBigDecimal(totalLiabilities);
		List<BalanceSheet> items = balanceSheetRepository.findByTotalLiabilitiesGreaterThan(totalLiabilities);
		if(items.isEmpty()) {
			String msg = String.format("No BalanceSheet found for total liabilities greater tha %s", totalLiabilities);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(balanceSheetMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<BalanceSheetResponse> findSolventBalanceSheets() {
		List<BalanceSheet> items = balanceSheetRepository.findSolventBalanceSheets();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Solvent balance-sheets are not found");
		}
		return items.stream().map(balanceSheetMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public BalanceSheetResponse findFirstByOrderByDateDesc() {
		BalanceSheet items = balanceSheetRepository.findFirstByOrderByDateDesc();
		return new BalanceSheetResponse(items);
	}

	@Override
	@Transactional(readOnly = true)
	public BalanceSheetResponse trackBalanceSheet(Long id) {
		BalanceSheet bs = balanceSheetRepository.findById(id).orElseThrow(() -> new ValidationException("BalanceSheet not found with id "+id));
		return new BalanceSheetResponse(bs);
	}

	@Transactional
	@Override
	public BalanceSheetResponse confirmBalanceSheet(Long id) {
		BalanceSheet bs = balanceSheetRepository.findById(id).orElseThrow(() -> new ValidationException("BalanceSheet not found with id "+id));
		bs.setConfirmed(true);
		bs.setStatus(BalanceSheetStatus.CONFIRMED);
		balanceSheetRepository.save(bs);
		return new BalanceSheetResponse(bs);
	}

	@Transactional
	@Override
	public BalanceSheetResponse closeBalanceSheet(Long id) {
		BalanceSheet bs = balanceSheetRepository.findById(id).orElseThrow(() -> new ValidationException("BalanceSheet not found with id "+id));
		if(bs.getStatus() != BalanceSheetStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED balance-sheets can be closed");
		}
		bs.setStatus(BalanceSheetStatus.CLOSED);
		return new BalanceSheetResponse(balanceSheetRepository.save(bs));
	}

	@Transactional
	@Override
	public BalanceSheetResponse cancelBalanceSheet(Long id) {
		BalanceSheet bs = balanceSheetRepository.findById(id).orElseThrow(() -> new ValidationException("BalanceSheet not found with id "+id));
		if(bs.getStatus() != BalanceSheetStatus.CONFIRMED && bs.getStatus() != BalanceSheetStatus.NEW) {
			throw new ValidationException("Only NEW or CONFIRMED defects can be cancelled");
		}
		bs.setStatus(BalanceSheetStatus.CANCELLED);
		return new BalanceSheetResponse(balanceSheetRepository.save(bs));
	}

	@Transactional
	@Override
	public BalanceSheetResponse changeStatus(Long id, BalanceSheetStatus status) {
		BalanceSheet bs = balanceSheetRepository.findById(id).orElseThrow(() -> new ValidationException("BalanceSheet not found with id "+id));
		validateBalanceSheetStatus(status);
		if(bs.getStatus() == BalanceSheetStatus.CLOSED) {
			throw new ValidationException("Closed balance-sheets cannot change status");
		}
		if(status == BalanceSheetStatus.CONFIRMED) {
			if(bs.getStatus() != BalanceSheetStatus.NEW) {
				throw new ValidationException("Only NEW defects can be confirmed");
			}
			bs.setConfirmed(true);
		}
		bs.setStatus(status);
		return new BalanceSheetResponse(balanceSheetRepository.save(bs));
	}

	@Transactional
	@Override
	public BalanceSheetResponse saveBalanceSheet(BalanceSheetRequest request) {
		BalanceSheet bs = BalanceSheet.builder()
				.id(request.id())
				.totalAssets(request.totalAssets())
				.totalEquity(request.totalEquity())
				.totalLiabilities(request.totalLiabilities())
				.fiscalYear(validatefiscalYear(request.fiscalYearId()))
				.confirmed(request.confirmed())
				.status(request.status())
				.build();
		BalanceSheet saved = balanceSheetRepository.save(bs);
		return new BalanceSheetResponse(saved);
	}
	
	private final AbstractSaveAsService<BalanceSheet, BalanceSheetResponse> saveAsHelper = new AbstractSaveAsService<BalanceSheet, BalanceSheetResponse>() {
		
		@Override
		protected BalanceSheetResponse toResponse(BalanceSheet entity) {
			return new BalanceSheetResponse(entity);
		}
		
		@Override
		protected JpaRepository<BalanceSheet, Long> getRepository() {
			return balanceSheetRepository;
		}
		
		@Override
		protected BalanceSheet copyAndOverride(BalanceSheet source, Map<String, Object> overrides) {
			return BalanceSheet.builder()
					.totalAssets((BigDecimal)overrides.getOrDefault("Total-assets", source.getTotalAssets()))
					.totalEquity((BigDecimal)overrides.getOrDefault("Total-equity", source.getTotalEquity()))
					.totalLiabilities((BigDecimal) overrides.getOrDefault("Total-liabilities", source.getTotalLiabilities()))
					.fiscalYear(validatefiscalYear(source.getFiscalYear().getId()))
					.confirmed(source.getConfirmed())
					.status(source.getStatus())
					.build();
		}
	};
	
	private final AbstractSaveAllService<BalanceSheet, BalanceSheetResponse> saveAllHelper = new AbstractSaveAllService<BalanceSheet, BalanceSheetResponse>() {
		
		@Override
		protected Function<BalanceSheet, BalanceSheetResponse> toResponse() {
			return BalanceSheetResponse::new;
		}
		
		@Override
		protected JpaRepository<BalanceSheet, Long> getRepository() {
			return balanceSheetRepository;
		}
	};

	@Transactional
	@Override
	public BalanceSheetResponse saveAs(BalanceSheetSaveAsRequest request) {
		Map<String, Object> overrides = new HashMap<String, Object>();
		if(request.totalAssets() != null) overrides.put("Total-assets",request.totalAssets());
		if(request.totalEquity() != null) overrides.put("Total-equity", request.totalEquity());
		if(request.totalLiabilities() != null) overrides.put("Total-liabilities", request.totalLiabilities());
		if(request.fiscalYearId() != null) overrides.put("Fiscal-year ID", request.fiscalYearId());
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}

	@Transactional
	@Override
	public List<BalanceSheetResponse> saveAll(List<BalanceSheetRequest> requests) {
		List<BalanceSheet> items = requests.stream()
				.map(req -> BalanceSheet.builder()
						.id(req.id())
						.totalAssets(req.totalAssets())
						.totalEquity(req.totalEquity())
						.totalLiabilities(req.totalLiabilities())
						.fiscalYear(validatefiscalYear(req.fiscalYearId()))
						.status(req.status())
						.confirmed(req.confirmed())
						.build())
				.collect(Collectors.toList());
		return saveAllHelper.saveAll(items);
	}

	@Override
	public List<BalanceSheetResponse> generalSearch(BalanceSheetGeneralSearchRequest req) {
		Specification<BalanceSheet> spec = BalanceSheetSpecification.fromRequest(req);
		List<BalanceSheet> items = balanceSheetRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No BalanceSheet found for given criteria");
		}
		return items.stream().map(balanceSheetMapper::toResponse).collect(Collectors.toList());		
	}
	
	private void validateBalanceSheetStatus(BalanceSheetStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("BalanceSheetStatus status must not be null"));
	}
    
    private void validateInteger(Integer num) {
        if (num == null || num <= 0) {
            throw new ValidationException("Vrednost mora biti pozitivan ceo broj");
        }
    }
    
    public FiscalYear validatefiscalYear(Long fiscalYearId) {
    	if(fiscalYearId == null) {
    		throw new ValidationException("FiscalYear za ID "+fiscalYearId+" me postoji");
    	}
    	return fiscalYearRepository.findById(fiscalYearId).orElseThrow(() -> new ValidationException("FiscalYear not found with id "+fiscalYearId));
    }
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Mora biti pozitivan broj");
        }
    }

    private void validateFiscalYearStatus(FiscalYearStatus status) {
    	if(status == null) {
    		throw new ValidationException("Status za FiscalYearStatus ne sme biti null");
    	}
    }
    
    private void validateFiscalQuarterStatus(FiscalQuarterStatus quarterStatus) {
    	if(quarterStatus == null) {
    		throw new ValidationException("QuarterStatus za FiscalQuarterStatus ne sme biti null");
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
    
    private FiscalYear fetchFiscalYearId(Long fiscalYearId) {
    	if(fiscalYearId == null) {
    		throw new ValidationException("Fiscal Year ID must not be null");
    	}
    	return fiscalYearRepository.findById(fiscalYearId).orElseThrow(() -> new ValidationException("Fiscal Year not found with id "+fiscalYearId));
    }

}
