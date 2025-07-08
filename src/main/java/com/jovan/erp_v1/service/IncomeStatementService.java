package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.exception.FiscalYearErrorException;
import com.jovan.erp_v1.exception.IncomeStatementErrorException;
import com.jovan.erp_v1.mapper.IncomeStatementMapper;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.model.IncomeStatement;
import com.jovan.erp_v1.repository.FiscalYearRepository;
import com.jovan.erp_v1.repository.IncomeStatementRepository;
import com.jovan.erp_v1.request.IncomeStatementRequest;
import com.jovan.erp_v1.response.IncomeStatementResponse;

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
        // 1. Validacija da su datumi prisutni (može se preskočiti zbog @NotNull)
        validateFieldRequest(request);
        // 3. Učitavanje i provera da je u okviru fiskalne godine
        FiscalYear fiscalYear = fiscalYearRepository.findById(request.fiscalYearId())
                .orElseThrow(
                        () -> new FiscalYearErrorException("Fiscal year not found with ID: " + request.fiscalYearId()));
        if (request.periodStart().isBefore(fiscalYear.getStartDate()) ||
                request.periodEnd().isAfter(fiscalYear.getEndDate())) {
            throw new IncomeStatementErrorException("Income statement period must be within the fiscal year period.");
        }
        // 4. (Opcionalno) Preklapanje sa drugim izveštajima
        List<IncomeStatement> existingStatements = incomeStatementRepository.findByFiscalYearId(request.fiscalYearId());
        boolean overlaps = existingStatements.stream()
                .anyMatch(statement -> !(request.periodEnd().isBefore(statement.getPeriodStart()) ||
                        request.periodStart().isAfter(statement.getPeriodEnd())));
        if (overlaps) {
            throw new IncomeStatementErrorException("Income statement period overlaps with an existing statement.");
        }
        IncomeStatement st = incomeStatementMapper.toEntity(request);
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
        FiscalYear fiscalYear = fiscalYearRepository.findById(request.fiscalYearId())
                .orElseThrow(
                        () -> new FiscalYearErrorException("Fiscal year not found with ID: " + request.fiscalYearId()));
        if (request.periodStart().isBefore(fiscalYear.getStartDate()) ||
                request.periodEnd().isAfter(fiscalYear.getEndDate())) {
            throw new IncomeStatementErrorException("Income statement period must be within the fiscal year period.");
        }
        List<IncomeStatement> existingStatements = incomeStatementRepository.findByFiscalYearId(request.fiscalYearId());
        boolean overlaps = existingStatements.stream()
                .filter(statement -> !statement.getId().equals(id)) // IGNORIŠI trenutni
                .anyMatch(statement -> !(request.periodEnd().isBefore(statement.getPeriodStart()) ||
                        request.periodStart().isAfter(statement.getPeriodEnd())));
        if (overlaps) {
            throw new IncomeStatementErrorException("Income statement period overlaps with an existing statement.");
        }
        incomeStatementMapper.toEntityUpdate(st, request);
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
        return incomeStatementRepository.findAll().stream()
                .map(IncomeStatementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeStatementResponse> findByTotalRevenue(BigDecimal totalRevenue) {
    	validateBigDecimal(totalRevenue);
        return incomeStatementRepository.findByTotalRevenue(totalRevenue).stream()
                .map(IncomeStatementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeStatementResponse> findByTotalExpenses(BigDecimal totalExpenses) {
    	validateBigDecimal(totalExpenses);
        return incomeStatementRepository.findByTotalExpenses(totalExpenses).stream()
                .map(IncomeStatementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeStatementResponse> findByNetProfit(BigDecimal netProfit) {
    	validateBigDecimal(netProfit);
        return incomeStatementRepository.findByNetProfit(netProfit).stream()
                .map(IncomeStatementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeStatementResponse> findByFiscalYear_Year(Integer year) {
    	validateInteger(year);
        return incomeStatementRepository.findByFiscalYear_Year(year).stream()
                .map(IncomeStatementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeStatementResponse> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus) {
    	validateFiscalQuarterStatus(quarterStatus);
        return incomeStatementRepository.findByFiscalYear_QuarterStatus(quarterStatus).stream()
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
        return incomeStatementMapper.toResponseList(st);
    }

    @Override
    public List<IncomeStatementResponse> findByDateWithinPeriod(LocalDate date) {
        if (date == null) {
            throw new IncomeStatementErrorException("Dates must be provided");
        }
        List<IncomeStatement> st = incomeStatementRepository.findByDateWithinPeriod(date);
        return incomeStatementMapper.toResponseList(st);
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
    
    private void validateFiscalQuarterStatus(FiscalQuarterStatus quarterStatus) {
    	if(quarterStatus == null) {
    		throw new IllegalArgumentException("quarterStatus for FiscalQuarterStatus must not be null");
    	}
    }
    
    private void validateFieldRequest(IncomeStatementRequest request) {
    	if (request.periodStart() == null || request.periodEnd() == null) {
            throw new IncomeStatementErrorException("Start and end dates must be provided.");
        }
        // 2. Start pre end
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

}
