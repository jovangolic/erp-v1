package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.dto.MonthlyNetProfitDTO;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.exception.IncomeStatementErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.IncomeStatementMapper;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.model.IncomeStatement;
import com.jovan.erp_v1.repository.FiscalYearRepository;
import com.jovan.erp_v1.repository.IncomeStatementRepository;
import com.jovan.erp_v1.request.IncomeStatementRequest;
import com.jovan.erp_v1.response.IncomeStatementResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeStatementService implements IntIncomeStatementService {

    private final IncomeStatementRepository incomeStatementRepository;
    private final IncomeStatementMapper incomeStatementMapper;
    private final FiscalYearRepository fiscalYearRepository;

    @Transactional
    @Override
    public IncomeStatementResponse create(IncomeStatementRequest request) {
        validateFieldRequest(request);
        FiscalYear fiscalYear = validateFiscalYear(request.fiscalYearId());
        if (request.periodStart().isBefore(fiscalYear.getStartDate()) ||
                request.periodEnd().isAfter(fiscalYear.getEndDate())) {
            throw new IncomeStatementErrorException("Income statement period must be within the fiscal year period.");
        }
        List<IncomeStatement> existingStatements = incomeStatementRepository.findByFiscalYearId(request.fiscalYearId());
        boolean overlaps = existingStatements.stream()
                .anyMatch(statement -> !(request.periodEnd().isBefore(statement.getPeriodStart()) ||
                        request.periodStart().isAfter(statement.getPeriodEnd())));
        if (overlaps) {
            throw new IncomeStatementErrorException("Income statement period overlaps with an existing statement.");
        }
        IncomeStatement st = incomeStatementMapper.toEntity(request,fiscalYear);
        IncomeStatement saved = incomeStatementRepository.save(st);
        return incomeStatementMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public IncomeStatementResponse update(Long id, IncomeStatementRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        IncomeStatement st = incomeStatementRepository.findById(id)
                .orElseThrow(() -> new IncomeStatementErrorException("Income statement not found"));
        validateFieldRequest(request);
        FiscalYear fiscalYear = st.getFiscalYear();
        if (request.periodStart().isBefore(fiscalYear.getStartDate()) ||
                request.periodEnd().isAfter(fiscalYear.getEndDate())) {
            throw new IncomeStatementErrorException("Income statement period must be within the fiscal year period.");
        }
        List<IncomeStatement> existingStatements = incomeStatementRepository.findByFiscalYearId(request.fiscalYearId());
        boolean overlaps = existingStatements.stream()
                .filter(statement -> !statement.getId().equals(id))
                .anyMatch(statement -> !(request.periodEnd().isBefore(statement.getPeriodStart()) ||
                        request.periodStart().isAfter(statement.getPeriodEnd())));
        if (overlaps) {
            throw new IncomeStatementErrorException("Income statement period overlaps with an existing statement.");
        }
        if(request.fiscalYearId() != null && (st.getFiscalYear() == null || !request.fiscalYearId().equals(st.getFiscalYear().getId()))) {
        	fiscalYear = validateFiscalYear(request.fiscalYearId());
        }
        incomeStatementMapper.toEntityUpdate(st, request, fiscalYear);
        return incomeStatementMapper.toResponse(incomeStatementRepository.save(st));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!incomeStatementRepository.existsById(id)) {
            throw new IncomeStatementErrorException("Income-statement not found with id: " + id);
        }
        incomeStatementRepository.deleteById(id);
    }

    @Override
    public IncomeStatementResponse findOne(Long id) {
        IncomeStatement st = incomeStatementRepository.findById(id)
                .orElseThrow(() -> new IncomeStatementErrorException("Income statement not found"));
        return new IncomeStatementResponse(st);
    }

    @Override
    public List<IncomeStatementResponse> findAll() {
    	List<IncomeStatement> items = incomeStatementRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("IncomeStatement list is empty");
    	}
        return items.stream()
                .map(IncomeStatementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeStatementResponse> findByTotalRevenue(BigDecimal totalRevenue) {
    	validateBigDecimal(totalRevenue);
    	List<IncomeStatement> items = incomeStatementRepository.findByTotalRevenue(totalRevenue);
    	if(items.isEmpty()) {
    		String msg = String.format("No IncomeStatement found for total revenue %s", totalRevenue);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(IncomeStatementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeStatementResponse> findByTotalExpenses(BigDecimal totalExpenses) {
    	validateBigDecimal(totalExpenses);
    	List<IncomeStatement> items = incomeStatementRepository.findByTotalExpenses(totalExpenses);
    	if(items.isEmpty()) {
    		String msg = String.format("No IncomeStatement found for total expenses %s", totalExpenses);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(IncomeStatementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeStatementResponse> findByNetProfit(BigDecimal netProfit) {
    	validateBigDecimal(netProfit);
    	List<IncomeStatement> items = incomeStatementRepository.findByNetProfit(netProfit);
    	if(items.isEmpty()) {
    		String msg = String.format("No IncomeStatement found for net profit %s", netProfit);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(IncomeStatementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeStatementResponse> findByFiscalYear_Year(Integer year) {
    	validateInteger(year);
    	List<IncomeStatement> items = incomeStatementRepository.findByFiscalYear_Year(year);
    	if(items.isEmpty()) {
    		String msg = String.format("No IncomeStatement found for fiscal year %d", year);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(IncomeStatementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeStatementResponse> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus) {
    	validateFiscalQuarterStatus(quarterStatus);
    	List<IncomeStatement> items = incomeStatementRepository.findByFiscalYear_QuarterStatus(quarterStatus);
    	if(items.isEmpty()) {
    		String msg = String.format("No IncomeStatement found for quarter status %s", quarterStatus);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(IncomeStatementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeStatementResponse> findByPeriodStartBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IncomeStatementErrorException("Dates must be provided");
        }
        if (start.isAfter(end)) {
            throw new IncomeStatementErrorException("Start date must be before end date");
        }
        List<IncomeStatement> st = incomeStatementRepository.findByPeriodStartBetween(start, end);
        if(st.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No IncomeStatement found for period start between %s and %s", 
        			start.format(formatter),end.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return incomeStatementMapper.toResponseList(st);
    }

    @Override
    public List<IncomeStatementResponse> findByPeriodEndBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IncomeStatementErrorException("Dates must be provided");
        }
        if (start.isAfter(end)) {
            throw new IncomeStatementErrorException("Start date must be before end date");
        }
        List<IncomeStatement> st = incomeStatementRepository.findByPeriodEndBetween(start, end);
        if(st.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No IncomeStatement found for period end between %s and %s", 
        			start.format(formatter),end.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return incomeStatementMapper.toResponseList(st);
    }

    @Override
    public List<IncomeStatementResponse> findWithinPeriod(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IncomeStatementErrorException("Dates must be provided");
        }
        if (start.isAfter(end)) {
            throw new IncomeStatementErrorException("Start date must be before end date");
        }
        List<IncomeStatement> st = incomeStatementRepository.findWithinPeriod(start, end);
        if(st.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No IncomeStatement found for within period between %s and %s", 
        			start.format(formatter),end.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return incomeStatementMapper.toResponseList(st);
    }

    @Override
    public List<IncomeStatementResponse> findByDateWithinPeriod(LocalDate date) {
        if (date == null) {
            throw new IncomeStatementErrorException("Dates must be provided");
        }
        List<IncomeStatement> st = incomeStatementRepository.findByDateWithinPeriod(date);
        if(st.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No IncomeStatement found for date within period %s",date.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return incomeStatementMapper.toResponseList(st);
    }
    
    @Override
    public List<MonthlyNetProfitDTO> getMonthlyNetProfitForYear(Integer year) {
        List<Object[]> results = incomeStatementRepository.findMonthlyNetProfitByYear(year);
        List<MonthlyNetProfitDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            int month = ((Integer) row[0]);
            int yr = ((Integer) row[1]);
            BigDecimal netProfit = (BigDecimal) row[2];
            dtos.add(new MonthlyNetProfitDTO(month, yr, netProfit));
        }
        return dtos;
    }
    
    @Override
	public BigDecimal calculateTotalNetProfitByFiscalYear(Long fiscalYearId) {
		validateFiscalYear(fiscalYearId);
		return incomeStatementRepository.calculateTotalNetProfitByFiscalYear(fiscalYearId);
	}

	@Override
	public BigDecimal findTotalNetProfitByFiscalYear(Long fiscalYearId) {
		validateFiscalYear(fiscalYearId);
		return incomeStatementRepository.findTotalNetProfitByFiscalYear(fiscalYearId);
	}

	@Override
	public List<IncomeStatementResponse> findByTotalRevenueGreaterThan(BigDecimal totalRevenue) {
		validateBigDecimal(totalRevenue);
		List<IncomeStatement> items = incomeStatementRepository.findByTotalRevenueGreaterThan(totalRevenue);
		if(items.isEmpty()) {
			String msg = String.format("No IncomeStatement found for total revenue greater than %s", totalRevenue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<IncomeStatementResponse> findByTotalExpensesGreaterThan(BigDecimal totalExpenses) {
		validateBigDecimal(totalExpenses);
		List<IncomeStatement> items = incomeStatementRepository.findByTotalExpensesGreaterThan(totalExpenses);
		if(items.isEmpty()) {
			String msg = String.format("No IncomeStatement found for total expenses greater than %s", totalExpenses);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<IncomeStatementResponse> findByNetProfitGreaterThan(BigDecimal netProfit) {
		validateBigDecimal(netProfit);
		List<IncomeStatement> items = incomeStatementRepository.findByNetProfitGreaterThan(netProfit);
		if(items.isEmpty()) {
			String msg = String.format("No IncomeStatement found for net profit greater than %s", netProfit);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<IncomeStatementResponse> findByTotalRevenueLessThan(BigDecimal totalRevenue) {
		validateBigDecimalNonNegative(totalRevenue);
		List<IncomeStatement> items = incomeStatementRepository.findByTotalRevenueLessThan(totalRevenue);
		if(items.isEmpty()) {
			String msg = String.format("No IncomeStatement found for total revenue less than %s", totalRevenue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<IncomeStatementResponse> findByTotalExpensesLessThan(BigDecimal totalExpenses) {
		validateBigDecimalNonNegative(totalExpenses);
		List<IncomeStatement> items = incomeStatementRepository.findByTotalExpensesLessThan(totalExpenses);
		if(items.isEmpty()) {
			String msg = String.format("No IncomeStatement found for total expenses less than %s", totalExpenses);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<IncomeStatementResponse> findByNetProfitLessThan(BigDecimal netProfit) {
		validateBigDecimalNonNegative(netProfit);
		List<IncomeStatement> items = incomeStatementRepository.findByNetProfitLessThan(netProfit);
		if(items.isEmpty()) {
			String msg = String.format("No IncomeStatement found for net profit less than %s", netProfit);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<IncomeStatementResponse> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus) {
		validateFiscalYearStatus(yearStatus);
		List<IncomeStatement> items = incomeStatementRepository.findByFiscalYear_YearStatus(yearStatus);
		if(items.isEmpty()) {
			String msg = String.format("No IncomeStatement for fiscal year status %s is found", yearStatus);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<IncomeStatementResponse> findByFiscalYear_QuarterStatusAndYearStatus(FiscalYearStatus yearStatus,
			FiscalQuarterStatus quarterStatus) {
		validateFiscalYearStatus(yearStatus);
		validateFiscalQuarterStatus(quarterStatus);
		List<IncomeStatement> items = incomeStatementRepository.findByFiscalYear_QuarterStatusAndYearStatus(yearStatus, quarterStatus);
		if(items.isEmpty()) {
			String msg = String.format("No IncomeStatement for fiscal quarter status %s and year status %s is found",
					yearStatus, quarterStatus);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public BigDecimal sumTotalRevenueBetweenDates(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		return incomeStatementRepository.sumTotalRevenueBetweenDates(start, end);
	}

	@Override
	public BigDecimal sumTotalExpensesBetweenDates(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		return incomeStatementRepository.sumTotalExpensesBetweenDates(start, end);
	}

	@Override
	public BigDecimal sumNetProfitBetweenDates(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		return incomeStatementRepository.sumNetProfitBetweenDates(start, end);
	}

	@Override
	public BigDecimal sumNetProfitByQuarterStatus(FiscalQuarterStatus quarterStatus) {
		validateFiscalQuarterStatus(quarterStatus);
		return incomeStatementRepository.sumNetProfitByQuarterStatus(quarterStatus);
	}

	@Override
	public List<IncomeStatementResponse> findByQuarterStatusAndMinRevenue(FiscalQuarterStatus quarterStatus,
			BigDecimal minRevenue) {
		validateFiscalQuarterStatus(quarterStatus);
		validateBigDecimalNonNegative(minRevenue);
		List<IncomeStatement> items = incomeStatementRepository.findByQuarterStatusAndMinRevenue(quarterStatus, minRevenue);
		if(items.isEmpty()) {
			String msg = String.format("No IncomeStatement for quarter status %s and min-revenue %s is found",
					quarterStatus,minRevenue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public BigDecimal sumRevenueByFiscalYearStatus(FiscalYearStatus yearStatus) {
		validateFiscalYearStatus(yearStatus);
		return incomeStatementRepository.sumRevenueByFiscalYearStatus(yearStatus);
	}

	@Override
	public List<IncomeStatementResponse> findByFiscalYear_StartDate(LocalDate startDate) {
		DateValidator.validateNotNull(startDate, "Start date");
		List<IncomeStatement> items = incomeStatementRepository.findByFiscalYear_StartDate(startDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No IncomeStatement found fiscal year start-date %s", startDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<IncomeStatementResponse> findByFiscalYear_EndDate(LocalDate endDate) {
		DateValidator.validateNotInPast(endDate, "End date");
		List<IncomeStatement> items = incomeStatementRepository.findByFiscalYear_EndDate(endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No IncomeStatement found fiscal year end-date %s", endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public BigDecimal sumTotalRevenue(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		return incomeStatementRepository.sumTotalRevenue(start, end);
	}

	@Override
	public BigDecimal sumTotalExpenses(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		return incomeStatementRepository.sumTotalExpenses(start, end);
	}

	@Override
	public BigDecimal sumNetProfit(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		return incomeStatementRepository.sumNetProfit(start, end);
	}

	@Override
	public BigDecimal sumNetProfitByYearStatus(FiscalYearStatus yearStatus) {
		validateFiscalYearStatus(yearStatus);
		return incomeStatementRepository.sumNetProfitByYearStatus(yearStatus);
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }
    
    private void validateInteger(Integer num) {
    	if(num == null || num < 0) {
    		throw new IllegalArgumentException("Must be positive number");
    	}
    }
    
    private void validateFiscalYearStatus(FiscalYearStatus yearStatus) {
    	Optional.ofNullable(yearStatus)
    		.orElseThrow(() -> new ValidationException("FiscalYearStatus yearStatus must not be null"));
    }
    
    private void validateFiscalQuarterStatus(FiscalQuarterStatus quarterStatus) {
    	if(quarterStatus == null) {
    		throw new IllegalArgumentException("quarterStatus for FiscalQuarterStatus must not be null");
    	}
    }
    
    private void validateFieldRequest(IncomeStatementRequest request) {
    	if (request.periodStart() == null || request.periodEnd() == null) {
            throw new IncomeStatementErrorException("Start and end dates must be provided.");
        }
        if (request.periodStart().isAfter(request.periodEnd())) {
            throw new IncomeStatementErrorException("Start date cannot be after end date.");
        }
        if(request.periodEnd().compareTo(request.periodStart()) < 0) {
        	throw new IncomeStatementErrorException("End date cannot be before start date.");
        }
        validateBigDecimal(request.totalRevenue());
        validateBigDecimal(request.totalExpenses());
        validateBigDecimal(request.netProfit());
    }
    
    private FiscalYear validateFiscalYear(Long yearId) {
    	if(yearId == null) {
    		throw new ValidationException("FiscalYear ID must not be null");
    	}
    	return fiscalYearRepository.findById(yearId).orElseThrow(() -> new ValidationException("FiscalYear not found with id"+yearId));
    }

}
