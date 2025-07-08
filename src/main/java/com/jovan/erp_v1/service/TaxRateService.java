package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.TaxType;
import com.jovan.erp_v1.exception.TaxRateErrorException;
import com.jovan.erp_v1.mapper.TaxRateMapper;
import com.jovan.erp_v1.model.TaxRate;
import com.jovan.erp_v1.repository.TaxRateRepository;
import com.jovan.erp_v1.request.TaxRateRequest;
import com.jovan.erp_v1.response.TaxRateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxRateService implements ITaxRateService {

    private final TaxRateRepository taxRateRepository;
    private final TaxRateMapper taxRateMapper;

    @Transactional
    @Override
    public TaxRateResponse create(TaxRateRequest request) {
        boolean overlapping = taxRateRepository.existsByTaxNameAndTypeAndDateRangeOverlap(
                request.taxName(), request.type(), request.startDate(), request.endDate());
        if (overlapping) {
            throw new TaxRateErrorException("Overlapping tax rate for the same type and period already exists.");
        }
        if (request.startDate().isAfter(request.endDate())) {
            throw new TaxRateErrorException("Start date must be before end date.");
        }
        if (ChronoUnit.YEARS.between(request.startDate(), request.endDate()) > 10) {
            throw new TaxRateErrorException("Tax period cannot be longer than 10 years.");
        }
        if (request.endDate().isBefore(LocalDate.now())) {
            throw new TaxRateErrorException("End date must not be in the past.");
        }
        if (request.startDate().isAfter(LocalDate.now().plusYears(5))) {
            throw new TaxRateErrorException("Start date is too far in the future.");
        }
        TaxRate tax = taxRateMapper.toEntity(request);
        TaxRate saved = taxRateRepository.save(tax);
        return taxRateMapper.toResponse(taxRateRepository.save(saved));
    }

    @Transactional
    @Override
    public TaxRateResponse update(Long id, TaxRateRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        boolean overlapping = taxRateRepository.existsByTaxNameAndTypeAndDateRangeOverlap(
                request.taxName(), request.type(), request.startDate(), request.endDate());
        if (overlapping) {
            throw new TaxRateErrorException("Overlapping tax rate for the same type and period already exists.");
        }
        if (request.startDate().isAfter(request.endDate())) {
            throw new TaxRateErrorException("Start date must be before end date.");
        }
        if (ChronoUnit.YEARS.between(request.startDate(), request.endDate()) > 10) {
            throw new TaxRateErrorException("Tax period cannot be longer than 10 years.");
        }
        if (request.endDate().isBefore(LocalDate.now())) {
            throw new TaxRateErrorException("End date must not be in the past.");
        }
        if (request.startDate().isAfter(LocalDate.now().plusYears(5))) {
            throw new TaxRateErrorException("Start date is too far in the future.");
        }
        TaxRate tax = taxRateRepository.findById(id)
                .orElseThrow(() -> new TaxRateErrorException("TaxRate not found with id: " + id));
        taxRateMapper.toUpdateEntity(tax, request);
        return taxRateMapper.toResponse(taxRateRepository.save(tax));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!taxRateRepository.existsById(id)) {
            throw new TaxRateErrorException("TaxRate not found with id: " + id);
        }
        taxRateRepository.deleteById(id);
    }

    @Override
    public TaxRateResponse findOne(Long id) {
        TaxRate tax = taxRateRepository.findById(id)
                .orElseThrow(() -> new TaxRateErrorException("TaxRate not found with id: " + id));
        return new TaxRateResponse(tax);
    }

    @Override
    public List<TaxRateResponse> findAll() {
        return taxRateRepository.findAll().stream()
                .map(TaxRateResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByType(TaxType type) {
        return taxRateRepository.findByType(type).stream()
                .map(TaxRateResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByTaxName(String taxName) {
        return taxRateRepository.findByTaxName(taxName).stream()
                .map(TaxRateResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByPercentage(BigDecimal percentage) {
        return taxRateRepository.findByPercentage(percentage).stream()
                .map(TaxRateResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByTaxNameAndPercentage(String taxName, BigDecimal percentage) {
        return taxRateRepository.findByTaxNameAndPercentage(taxName, percentage).stream()
                .map(TaxRateResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByStartDateBeforeAndEndDateAfter(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            throw new TaxRateErrorException("Both dates must be provided.");
        }
        if (date1.isAfter(date2)) {
            throw new TaxRateErrorException("First date must be before or equal to second date.");
        }
        List<TaxRate> rates = taxRateRepository.findByStartDateBeforeAndEndDateAfter(date1, date2);
        return taxRateMapper.toResponseList(rates);
    }

    @Override
    public List<TaxRateResponse> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate date1,
            LocalDate date2) {
        if (date1 == null || date2 == null) {
            throw new TaxRateErrorException("Both dates must be provided.");
        }
        if (date1.isAfter(date2)) {
            throw new TaxRateErrorException("First date must be before or equal to second date.");
        }
        List<TaxRate> rates = taxRateRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date1, date2);
        return taxRateMapper.toResponseList(rates);
    }

    @Override
    public List<TaxRateResponse> findOverlapping(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new TaxRateErrorException("Both dates must be provided.");
        }
        if (start.isAfter(end)) {
            throw new TaxRateErrorException("Start date must be before end date.");
        }
        List<TaxRate> overlapping = taxRateRepository.findOverlapping(start, end);
        return taxRateMapper.toResponseList(overlapping);
    }

    @Override
    public List<TaxRateResponse> findByStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new TaxRateErrorException("Start date must be provided.");
        }
        List<TaxRate> result = taxRateRepository.findByStartDate(startDate);
        return taxRateMapper.toResponseList(result);
    }

    @Override
    public List<TaxRateResponse> findByEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new TaxRateErrorException("End date must be provided.");
        }
        List<TaxRate> rates = taxRateRepository.findByEndDate(endDate);
        return taxRateMapper.toResponseList(rates);
    }

    @Override
    public List<TaxRateResponse> findActiveByType(TaxType type, LocalDate date) {
        if (type == null || date == null) {
            throw new TaxRateErrorException("Type and date must be provided.");
        }
        List<TaxRate> result = taxRateRepository
                .findByTypeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(type, date, date);
        return taxRateMapper.toResponseList(result);
    }

    @Override
    public List<TaxRateResponse> findByTypeAndPeriod(TaxType type, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new TaxRateErrorException("Start date must be before or equal to end date.");
        }
        List<TaxRate> found = taxRateRepository.findByTypeAndDateRange(type, startDate, endDate);
        return taxRateMapper.toResponseList(found);
    }

}
