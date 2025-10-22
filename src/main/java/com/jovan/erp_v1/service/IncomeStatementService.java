package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import com.jovan.erp_v1.dto.MonthlyNetProfitDTO;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.enumeration.IncomeStatementStatus;
import com.jovan.erp_v1.exception.IncomeStatementErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.IncomeStatementMapper;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.model.IncomeStatement;
import com.jovan.erp_v1.repository.FiscalYearRepository;
import com.jovan.erp_v1.repository.IncomeStatementRepository;
import com.jovan.erp_v1.repository.specification.IncomeStatementSpecification;
import com.jovan.erp_v1.request.IncomeStatementRequest;
import com.jovan.erp_v1.response.IncomeStatementResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.IncomeStatementSaveAsRequest;
import com.jovan.erp_v1.search_request.IncomeStatementSearchRequest;
import com.jovan.erp_v1.statistics.income_statement.IncomeStatementExpensesStatDTO;
import com.jovan.erp_v1.statistics.income_statement.IncomeStatementNetProfitStatDTO;
import com.jovan.erp_v1.statistics.income_statement.IncomeStatementRevenuStatDTO;
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
	
	@Transactional(readOnly = true)
	@Override
	public IncomeStatementResponse trackIncomeStatement(Long id) {
		IncomeStatement is = incomeStatementRepository.findById(id).orElseThrow(() -> new ValidationException("IncomeStatement not found with id "+id));
		return new IncomeStatementResponse(is);
	}

	@Transactional
	@Override
	public IncomeStatementResponse confirmIncomeStatement(Long id) {
		IncomeStatement is = incomeStatementRepository.findById(id).orElseThrow(() -> new ValidationException("IncomeStatement not found with id "+id));
		is.setConfirmed(true);
		is.setStatus(IncomeStatementStatus.CONFIRMED);
		return new IncomeStatementResponse(incomeStatementRepository.save(is));
	}

	@Transactional
	@Override
	public IncomeStatementResponse cancelIncomeStatement(Long id) {
		IncomeStatement is = incomeStatementRepository.findById(id).orElseThrow(() -> new ValidationException("IncomeStatement not found with id "+id));
		if(is.getStatus() != IncomeStatementStatus.NEW && is.getStatus() != IncomeStatementStatus.CONFIRMED) {
			throw new ValidationException("Only NEW or CONFIRMED incomeStatements can be cancelled");
		}
		is.setStatus(IncomeStatementStatus.CANCELLED);
		return new IncomeStatementResponse(incomeStatementRepository.save(is));
	}

	@Transactional
	@Override
	public IncomeStatementResponse closeIncomeStatement(Long id) {
		IncomeStatement is = incomeStatementRepository.findById(id).orElseThrow(() -> new ValidationException("IncomeStatement not found with id "+id));
		if(is.getStatus() != IncomeStatementStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED incomeStatements can be closed");
		}
		is.setStatus(IncomeStatementStatus.CLOSED);
		return new IncomeStatementResponse(incomeStatementRepository.save(is));
	}

	@Transactional
	@Override
	public IncomeStatementResponse changeStatus(Long id, IncomeStatementStatus status) {
		IncomeStatement is = incomeStatementRepository.findById(id).orElseThrow(() -> new ValidationException("IncomeStatement not found with id "+id));
		validateIncomeStatementStatus(status);
		if(is.getStatus() == IncomeStatementStatus.CLOSED) {
			throw new ValidationException("Closed incomeStatements cannot change status");
		}
		if(status == IncomeStatementStatus.CONFIRMED) {
			if(is.getStatus() != IncomeStatementStatus.NEW) {
				throw new ValidationException("Only NEW incomeStatements can be confirmed");
			}
			is.setConfirmed(true);
		}
		is.setStatus(status);
		return new IncomeStatementResponse(incomeStatementRepository.save(is));
	}

	@Transactional
	@Override
	public IncomeStatementResponse saveIncomeStatement(IncomeStatementRequest request) {
		FiscalYear fy = validateFiscalYear(request.fiscalYearId());
		//provera da li periodStart i periodEnd spadaju u opseg godine
		if(request.periodStart().isBefore(fy.getStartDate()) || request.periodEnd().isAfter(fy.getEndDate())) {
			throw new ValidationException("Income statement period must fall within the fiscal year period");
		}
		if (request.periodEnd().isBefore(request.periodStart())) {
            throw new ValidationException("Period end cannot be before start date");
        }
		if(request.totalRevenue() == null || request.totalExpenses() == null) {
			throw new ValidationException("Total revenue and expenses must not be null");
		}
		if(request.totalRevenue().compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Total revenue canot be negative");
		}
		if(request.totalExpenses().compareTo(request.totalRevenue()) > 0) {
			throw new ValidationException("Expenses cannot exceed revenue");
		}
		BigDecimal expectedProfit = request.totalRevenue().subtract(request.totalExpenses());
		if(request.netProfit() == null || expectedProfit.compareTo(request.netProfit()) != 0) {
			throw new ValidationException("Net profit must equal revenue minus expenses");
		}
		IncomeStatement is = IncomeStatement.builder()
				.id(request.id())
				.periodStart(request.periodStart())
				.periodEnd(request.periodEnd())
				.totalRevenue(request.totalRevenue())
				.totalExpenses(request.totalExpenses())
				.netProfit(request.netProfit())
				.fiscalYear(validateFiscalYear(request.fiscalYearId()))
				.confirmed(request.confirmed())
				.status(request.status())
				.build();
		IncomeStatement saved = incomeStatementRepository.save(is);
		return new IncomeStatementResponse(saved);
	}
	
	private final AbstractSaveAsService<IncomeStatement, IncomeStatementResponse> saveAsHelper = new AbstractSaveAsService<IncomeStatement, IncomeStatementResponse>() {
		
		@Override
		protected IncomeStatementResponse toResponse(IncomeStatement entity) {
			return new IncomeStatementResponse(entity);
		}
		
		@Override
		protected JpaRepository<IncomeStatement, Long> getRepository() {
			return incomeStatementRepository;
		}
		
		@Override
		protected IncomeStatement copyAndOverride(IncomeStatement source, Map<String, Object> overrides) {
			return IncomeStatement.builder()
					.periodStart(source.getPeriodStart())
					.periodEnd(source.getPeriodEnd())
					.totalRevenue((BigDecimal) overrides.getOrDefault("Total-revenue", source.getTotalRevenue()))
					.totalExpenses((BigDecimal) overrides.getOrDefault("Total-expenses", source.getTotalExpenses()))
					.netProfit((BigDecimal) overrides.getOrDefault("Net-profit", source.getNetProfit()))
					.fiscalYear(validateFiscalYear(source.getFiscalYear().getId()))
					.build();
		}
	};

	@Transactional
	@Override
	public IncomeStatementResponse saveAs(IncomeStatementSaveAsRequest request) {
		Map<String, Object> overrides = new HashMap<>();
		FiscalYear fy = validateFiscalYear(request.fiscalYearId());
		if(request.periodStart().isBefore(fy.getStartDate()) || request.periodEnd().isAfter(fy.getEndDate())) {
			throw new ValidationException("Income statement period must fall within the fiscal year period");
		}
		if(request.periodEnd().isBefore(request.periodStart())) {
			throw new ValidationException("Period end date cannot be before start date.");
		}
		BigDecimal expectedProfit = request.totalRevenue().subtract(request.totalExpenses());
		if(request.netProfit() == null || expectedProfit.compareTo(request.netProfit()) != 0) {
			throw new ValidationException("Net profit must equal total revenue minus total expenses.");
		}
		if(request.periodStart() != null) overrides.put("Period-start", request.periodStart());
		if(request.periodEnd() != null) overrides.put("Period-end", request.periodEnd());
		if(request.totalRevenue() != null) overrides.put("Total-revenue", request.totalRevenue());
		if(request.totalExpenses() != null) overrides.put("Total-expenses", request.totalExpenses());
		if(request.netProfit() != null) overrides.put("Net-profit", request.netProfit());
		if(request.fiscalYearId() != null) overrides.put("Fiscal-year ID", validateFiscalYear(request.fiscalYearId()));
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}
	
	private final AbstractSaveAllService<IncomeStatement, IncomeStatementResponse> saveAllHelper = new AbstractSaveAllService<IncomeStatement, IncomeStatementResponse>() {
		
		@Override
		protected Function<IncomeStatement, IncomeStatementResponse> toResponse() {
			return IncomeStatementResponse::new;
		}
		
		@Override
		protected JpaRepository<IncomeStatement, Long> getRepository() {
			return incomeStatementRepository;
		}
	};

	@Transactional
	@Override
	public List<IncomeStatementResponse> saveAll(List<IncomeStatementRequest> requests) {
		List<IncomeStatement> items = requests.stream()
				.map(req -> {
					FiscalYear fy = validateFiscalYear(req.fiscalYearId());
					//provera da li periodStart i periodEnd spadaju u opseg godine
					if(req.periodStart().isBefore(fy.getStartDate()) || req.periodEnd().isAfter(fy.getEndDate())) {
						throw new ValidationException("Income statement period must fall within the fiscal year period for ID: " + req.id());
					}
					if (req.periodEnd().isBefore(req.periodStart())) {
		                throw new ValidationException("Period end cannot be before start date for ID: " + req.id());
		            }
					if(req.totalRevenue() == null || req.totalExpenses() == null) {
						throw new ValidationException("Total revenue and expenses must not be null for ID: " + req.id());
					}
					if(req.totalRevenue().compareTo(BigDecimal.ZERO) < 0) {
						throw new ValidationException("Total revenue canot be negative for ID: "+ req.id());
					}
					if(req.totalExpenses().compareTo(req.totalRevenue()) > 0) {
						throw new ValidationException("Expenses cannot exceed revenue for ID: "+ req.id());
					}
					BigDecimal expectedProfit = req.totalRevenue().subtract(req.totalExpenses());
					if(req.netProfit() == null || expectedProfit.compareTo(req.netProfit()) != 0) {
						throw new ValidationException("Net profit must equal revenue minus expenses for ID: " + req.id());
					}
					return IncomeStatement.builder()
							.id(req.id())
							.periodStart(req.periodStart())
							.periodEnd(req.periodEnd())
							.totalRevenue(req.totalRevenue())
							.totalExpenses(req.totalExpenses())
							.netProfit(req.netProfit())
							.fiscalYear(validateFiscalYear(req.fiscalYearId()))
							.confirmed(req.confirmed())
							.status(req.status())
							.build();
				})
				.toList();
		return saveAllHelper.saveAll(items);
	}

	@Override
	public List<IncomeStatementResponse> generalSearch(IncomeStatementSearchRequest request) {
		Specification<IncomeStatement> spec = IncomeStatementSpecification.fromRequest(request);
		List<IncomeStatement> items = incomeStatementRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No IncomeStatement found for given criteria.");
		}
		return items.stream().map(incomeStatementMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<IncomeStatementRevenuStatDTO> countIncomeStatementRevenuByFiscalYear() {
		List<IncomeStatementRevenuStatDTO> items = incomeStatementRepository.countIncomeStatementRevenuByFiscalYear();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No IncomeStatement for revenue by fiscal-year, is found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					BigDecimal revenue = item.getRevenue();
					Long fiscalYearId = item.getFiscalYearId();
					Integer fiscalYear = item.getFiscalYear();
					return new IncomeStatementRevenuStatDTO(count, revenue, fiscalYearId, fiscalYear);
				})
				.toList();
	}

	@Override
	public List<IncomeStatementExpensesStatDTO> countIncomeStatementExpensesByFiscalYear() {
		List<IncomeStatementExpensesStatDTO> items = incomeStatementRepository.countIncomeStatementExpensesByFiscalYear();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No IncomeStatement fro expenses by fiscal-year, is found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					BigDecimal expenses = item.getExpenses();
					Long fiscalYearId = item.getFiscalYearId();
					Integer fiscalYear = item.getFiscalYear();
					return new IncomeStatementExpensesStatDTO(count, expenses, fiscalYearId, fiscalYear);
				})
				.toList();
	}

	@Override
	public List<IncomeStatementNetProfitStatDTO> countIncomeStatementNetProfitByFiscalYear() {
		List<IncomeStatementNetProfitStatDTO> items = incomeStatementRepository.countIncomeStatementNetProfitByFiscalYear();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No IncomeStatement for net-profit by fiscal-year, is found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					BigDecimal netProfit = item.getNetProfit();
					Long fiscalYearId = item.getFiscalYearId();
					Integer fiscalYear = item.getFiscalYear();
					return new IncomeStatementNetProfitStatDTO(count, netProfit, fiscalYearId, fiscalYear);
				})
				.toList();
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
    
    private void validateIncomeStatementStatus(IncomeStatementStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(() -> new ValidationException("IncomeStatementStatus status must not be null"));
    }

}
