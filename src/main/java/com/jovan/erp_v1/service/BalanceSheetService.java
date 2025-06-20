package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.repository.BalanceSheetRepository;
import com.jovan.erp_v1.request.BalanceSheetRequest;
import com.jovan.erp_v1.response.BalanceSheetResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BalanceSheetService implements IBalanceSheetService {

    private final BalanceSheetRepository balanceSheetRepository;

    @Override
    public BalanceSheetResponse create(BalanceSheetRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public BalanceSheetResponse update(Long id, BalanceSheetRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public BalanceSheetResponse findOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<BalanceSheetResponse> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<BalanceSheetResponse> findByTotalAssets(BigDecimal totalAssets) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByTotalAssets'");
    }

    @Override
    public BalanceSheetResponse findByDate(LocalDate date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByDate'");
    }

    @Override
    public List<BalanceSheetResponse> findByDateBetween(LocalDate start, LocalDate end) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByDateBetween'");
    }

    @Override
    public List<BalanceSheetResponse> findByTotalLiabilities(BigDecimal totalLiabilities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByTotalLiabilities'");
    }

    @Override
    public List<BalanceSheetResponse> findByTotalEquity(BigDecimal totalEquity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByTotalEquity'");
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_Id(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByFiscalYear_Id'");
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_Year(int year) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByFiscalYear_Year'");
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByFiscalYear_YearStatus'");
    }

    @Override
    public List<BalanceSheetResponse> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByFiscalYear_QuarterStatus'");
    }

    @Override
    public List<BalanceSheetResponse> findByStatusAndDateRange(FiscalYearStatus status, LocalDate start,
            LocalDate end) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByStatusAndDateRange'");
    }

}
