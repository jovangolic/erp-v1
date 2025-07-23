package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.jovan.erp_v1.request.BalanceSheetRequest;
import com.jovan.erp_v1.response.BalanceSheetResponse;
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

    
    private void validateInteger(Integer num) {
        if (num == null || num <= 0) {
            throw new IllegalArgumentException("Vrednost mora biti pozitivan ceo broj");
        }
    }
    
    public void validatefiscalYear(Long fiscalYearId) {
    	if(fiscalYearId == null) {
    		throw new IllegalArgumentException("FiscalYear za ID "+fiscalYearId+" me postoji");
    	}
    }
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }

    private void validateFiscalYearStatus(FiscalYearStatus status) {
    	if(status == null) {
    		throw new IllegalArgumentException("Status za FiscalYearStatus ne sme biti null");
    	}
    }
    
    private void validateFiscalQuarterStatus(FiscalQuarterStatus quarterStatus) {
    	if(quarterStatus == null) {
    		throw new IllegalArgumentException("QuarterStatus za FiscalQuarterStatus ne sme biti null");
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
