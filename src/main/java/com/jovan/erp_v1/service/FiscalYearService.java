package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.exception.FiscalYearErrorException;
import com.jovan.erp_v1.mapper.FiscalYearMapper;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.repository.FiscalYearRepository;
import com.jovan.erp_v1.request.FiscalYearRequest;
import com.jovan.erp_v1.response.FiscalYearResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FiscalYearService implements IFiscalYearService {

    private final FiscalYearRepository fiscalYearRepository;
    private final FiscalYearMapper fiscalYearMapper;

    @Override
    public FiscalYearResponse create(FiscalYearRequest request) {
        FiscalYear year = fiscalYearMapper.toEntity(request);
        FiscalYear saved = fiscalYearRepository.save(year);
        return fiscalYearMapper.toResponse(fiscalYearRepository.save(saved));
    }

    @Override
    public FiscalYearResponse update(Long id, FiscalYearRequest request) {
        FiscalYear year = fiscalYearRepository.findById(id)
                .orElseThrow(() -> new FiscalYearErrorException("Fiscal-Year not found with id:" + id));
        fiscalYearMapper.updateEntity(year, request);
        return fiscalYearMapper.toResponse(fiscalYearRepository.save(year));
    }

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
        return fiscalYearRepository.findBetweenStartAndEndDates(start, end).stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public FiscalYearResponse findByYear(int year) {
        FiscalYear y = fiscalYearRepository.findByYear(year)
                .orElseThrow(() -> new FiscalYearErrorException("Fiscal year not found with year: " + year));
        return new FiscalYearResponse(y);
    }

    @Override
    public FiscalYearResponse findByYearStatusAndYear(FiscalYearStatus status, Integer year) {
        FiscalYear y = fiscalYearRepository.findByYearStatusAndYear(status, year)
                .orElseThrow(() -> new FiscalYearErrorException("Status and year not found"));
        return fiscalYearMapper.toResponse(y);
    }

    @Override
    public FiscalYearResponse findFirstByYearStatusOrderByStartDateDesc(FiscalYearStatus status) {
        FiscalYear year = fiscalYearRepository.findFirstByYearStatusOrderByStartDateDesc(status)
                .orElseThrow(() -> new FiscalYearErrorException("FiscalYear status not found " + status));
        return new FiscalYearResponse(year);
    }

    @Override
    public List<FiscalYearResponse> findByStartDateAfter(LocalDate date) {
        return fiscalYearRepository.findByStartDateAfter(date).stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalYearResponse> findByEndDateBefore(LocalDate date) {
        return fiscalYearRepository.findByEndDateBefore(date).stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalYearResponse> findByYearStatus(FiscalYearStatus yearStatus) {
        return fiscalYearRepository.findByYearStatus(yearStatus).stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalYearResponse> findByQuarterStatus(FiscalQuarterStatus quarterStatus) {
        return fiscalYearRepository.findByQuarterStatus(quarterStatus).stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalYearResponse> findByQuarterLessThan(FiscalQuarterStatus quarterStatus) {
        return fiscalYearRepository.findByQuarterLessThan(quarterStatus).stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalYearResponse> findByQuarterGreaterThan(FiscalQuarterStatus quarterStatus) {
        return fiscalYearRepository.findByQuarterGreaterThan(quarterStatus).stream()
                .map(FiscalYearResponse::new)
                .collect(Collectors.toList());
    }

}
