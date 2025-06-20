package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.exception.BalanceSheetErrorException;
import com.jovan.erp_v1.mapper.BalanceSheetMapper;
import com.jovan.erp_v1.model.BalanceSheet;
import com.jovan.erp_v1.repository.BalanceSheetRepository;
import com.jovan.erp_v1.request.BalanceSheetRequest;
import com.jovan.erp_v1.response.BalanceSheetResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BalanceSheetService implements IBalanceSheetService {

    private final BalanceSheetRepository balanceSheetRepository;
    private final BalanceSheetMapper balanceSheetMapper;

    @Transactional
    @Override
    public BalanceSheetResponse create(BalanceSheetRequest request) {
        BalanceSheet sheet = balanceSheetMapper.toEntity(request);
        BalanceSheet saved = balanceSheetRepository.save(sheet);
        return balanceSheetMapper.toResponse(balanceSheetRepository.save(saved));
    }

    @Transactional
    @Override
    public BalanceSheetResponse update(Long id, BalanceSheetRequest request) {
        BalanceSheet sheet = balanceSheetRepository.findById(id)
                .orElseThrow(() -> new BalanceSheetErrorException("BalanceSheet not found with id " + id));
        balanceSheetMapper.toEntityUpdate(sheet, request);
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
        return balanceSheetRepository.findAll().stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByTotalAssets(BigDecimal totalAssets) {
        return balanceSheetRepository.findByTotalAssets(totalAssets).stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public BalanceSheetResponse findByDate(LocalDate date) {
        BalanceSheet sheet = balanceSheetRepository.findByDate(date)
                .orElseThrow(() -> new BalanceSheetErrorException("BalanceSheet for given date not found: " + date));
        return balanceSheetMapper.toResponse(sheet);
    }

    @Override
    public List<BalanceSheetResponse> findByDateBetween(LocalDate start, LocalDate end) {
        return balanceSheetRepository.findByDateBetween(start, end).stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByTotalLiabilities(BigDecimal totalLiabilities) {
        return balanceSheetRepository.findByTotalLiabilities(totalLiabilities).stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByTotalEquity(BigDecimal totalEquity) {
        return balanceSheetRepository.findByTotalEquity(totalEquity).stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_Id(Long id) {
        return balanceSheetRepository.findByFiscalYear_Id(id).stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_Year(Integer year) {
        return balanceSheetRepository.findByFiscalYear_Year(year).stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus) {
        return balanceSheetRepository.findByFiscalYear_YearStatus(yearStatus).stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus) {
        return balanceSheetRepository.findByFiscalYear_QuarterStatus(quarterStatus).stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceSheetResponse> findByStatusAndDateRange(FiscalYearStatus status, LocalDate start,
            LocalDate end) {
        return balanceSheetRepository.findByStatusAndDateRange(status, start, end).stream()
                .map(BalanceSheetResponse::new)
                .collect(Collectors.toList());
    }

}
