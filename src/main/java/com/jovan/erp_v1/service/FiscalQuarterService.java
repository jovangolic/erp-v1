package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.exception.FiscalQuarterErrorException;
import com.jovan.erp_v1.mapper.FiscalQuarterMapper;
import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.repository.FiscalQuarterRepository;
import com.jovan.erp_v1.request.FiscalQuarterRequest;
import com.jovan.erp_v1.response.FiscalQuarterResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FiscalQuarterService implements IFiscalQuarterService {

    private final FiscalQuarterRepository fiscalQuarterRepository;
    private final FiscalQuarterMapper fiscalQuarterMapper;

    @Transactional
    @Override
    public FiscalQuarterResponse create(FiscalQuarterRequest request) {
        FiscalQuarter q = fiscalQuarterMapper.toEntity(request);
        FiscalQuarter saved = fiscalQuarterRepository.save(q);
        return fiscalQuarterMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public FiscalQuarterResponse update(Long id, FiscalQuarterRequest request) {
        FiscalQuarter q = fiscalQuarterRepository.findById(id)
                .orElseThrow(() -> new FiscalQuarterErrorException("FiscalQuarter not found with id: " + id));
        fiscalQuarterMapper.updateEntity(q, request);
        return fiscalQuarterMapper.toResponse(fiscalQuarterRepository.save(q));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!fiscalQuarterRepository.existsById(id)) {
            throw new FiscalQuarterErrorException("FiscalQuarter not found with id: " + id);
        }
        fiscalQuarterRepository.deleteById(id);
    }

    @Override
    public FiscalQuarterResponse findOne(Long id) {
        FiscalQuarter q = fiscalQuarterRepository.findById(id)
                .orElseThrow(() -> new FiscalQuarterErrorException("FiscalQuarter not found with id: " + id));
        return new FiscalQuarterResponse(q);
    }

    @Override
    public List<FiscalQuarterResponse> findAll() {
        return fiscalQuarterRepository.findAll().stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByFiscalYear(FiscalYear fiscalYear) {
        return fiscalQuarterRepository.findByFiscalYear(fiscalYear).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByQuarterStatus(FiscalQuarterStatus status) {
        return fiscalQuarterRepository.findByQuarterStatus(status).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByStartDateBetween(LocalDate start, LocalDate end) {
        return fiscalQuarterRepository.findByStartDateBetween(start, end).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByFiscalYearAndQuarterStatus(FiscalYear fiscalYear,
            FiscalQuarterStatus status) {
        return fiscalQuarterRepository.findByFiscalYearAndQuarterStatus(fiscalYear, status).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByStartDateAfter(LocalDate date) {
        return fiscalQuarterRepository.findByStartDateAfter(date).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByStartDateBefore(LocalDate date) {
        return fiscalQuarterRepository.findByStartDateBefore(date).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByFiscalYear_Year(Integer year) {
        return fiscalQuarterRepository.findByFiscalYear_Year(year).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

}
