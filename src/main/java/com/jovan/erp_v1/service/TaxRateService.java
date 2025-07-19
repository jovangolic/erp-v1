package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.TaxType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.TaxRateErrorException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.TaxRateMapper;
import com.jovan.erp_v1.model.TaxRate;
import com.jovan.erp_v1.repository.TaxRateRepository;
import com.jovan.erp_v1.request.TaxRateRequest;
import com.jovan.erp_v1.response.TaxRateResponse;
import com.jovan.erp_v1.util.DateValidator;

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
        validateTaxRateRequest(request);
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
        validateTaxRateRequest(request);
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
        List<TaxRate> items = taxRateRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("TaxRate items are not found");
    	}
    	return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByType(TaxType type) {
        validateTaxType(type);
    	List<TaxRate> items = taxRateRepository.findByType(type);
    	if(items.isEmpty()) {
    		String msg = String.format("TaxRate for tax type %s is not found", type);
    		throw new NoDataFoundException(msg);
    	}
    	return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByTaxName(String taxName) {
        validateString(taxName);
    	List<TaxRate> items = taxRateRepository.findByTaxName(taxName);
    	if(items.isEmpty()) {
    		String msg = String.format("TaxRate for tax-name %s is not found", taxName);
    		throw new NoDataFoundException(msg);
    	}
    	return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByPercentage(BigDecimal percentage) {
        validateBigDecimal(percentage);
        List<TaxRate> items = taxRateRepository.findByPercentage(percentage);
        if(items.isEmpty()) {
        	String msg = String.format("TaxRate for percentage %s, is not found", percentage);
        	throw new NoDataFoundException(msg);
        }
    	return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByTaxNameAndPercentage(String taxName, BigDecimal percentage) {
        validateString(taxName);
        validateBigDecimal(percentage);
        List<TaxRate> items = taxRateRepository.findByTaxNameAndPercentage(taxName, percentage);
    	if(items.isEmpty()) {
    		String msg = String.format("TaxRate for tax-name %s and percentage %s, is not found", taxName,percentage);
    		throw new NoDataFoundException(msg);
    	}
    	return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByStartDateBeforeAndEndDateAfter(LocalDate date1, LocalDate date2) {
        DateValidator.validateRange(date1, date2);
        List<TaxRate> items = taxRateRepository.findByStartDateBeforeAndEndDateAfter(date1, date2);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("TaxRate for start-date %s before and end-date %s after, is not found",
        			date1.format(formatter), date2.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate date1,
            LocalDate date2) {
        DateValidator.validateRange(date1, date2);
        List<TaxRate> items = taxRateRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date1, date2);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("TaxRate for start-date %s less than and end-date %s greater than, is not found",
    				date1.format(formatter),date2.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
    	return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findOverlapping(LocalDate start, LocalDate end) {
        DateValidator.validateRange(start, end);
        List<TaxRate> items = taxRateRepository.findOverlapping(start, end);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("TaxRate for over-lapping dates between %s and %s is not found",
        			start.format(formatter),end.format(formatter));
        	throw new NoDataFoundException(msg);
        }
    	return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByStartDate(LocalDate startDate) {
        DateValidator.validateNotInFuture(startDate, "Start-date");
        List<TaxRate> items = taxRateRepository.findByStartDate(startDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("TaxRate for start-date %s is not found", startDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByEndDate(LocalDate endDate) {
        DateValidator.validateNotInPast(endDate, "End date");
        List<TaxRate> items = taxRateRepository.findByEndDate(endDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("TaxRate for end-date %s is not found", endDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findActiveByType(TaxType type, LocalDate date) {
        validateTaxType(type);
        DateValidator.validateNotInPast(date, "Date");
        List<TaxRate> items = taxRateRepository.findActiveByType(type, date);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("TaxRate for type active %s and date %s is not found",
        			type,date.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaxRateResponse> findByTypeAndPeriod(TaxType type, LocalDate startDate, LocalDate endDate) {
    	validateTaxType(type);
    	DateValidator.validateRange(startDate, endDate);
        List<TaxRate> items = taxRateRepository.findByTypeAndPeriod(type, startDate, endDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("TaxRate for type %s and period between %s and %s is not found",
        			type,startDate.format(formatter),endDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
    }

	@Override
	public List<TaxRateResponse> findByVat() {
		List<TaxRate> items = taxRateRepository.findByVat();
		if(items.isEmpty()) {
			throw new NoDataFoundException("TaxRate type for 'Vat' is not found");
		}
		return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TaxRateResponse> findByIncome_Tax() {
		List<TaxRate> items = taxRateRepository.findByIncome_Tax();
		if(items.isEmpty()) {
			throw new NoDataFoundException("TaxRate type for 'Income_Tax' is not found");
		}
		return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TaxRateResponse> findBySales_Tax() {
		List<TaxRate> items = taxRateRepository.findBySales_Tax();
		if(items.isEmpty()) {
			throw new NoDataFoundException("TaxRate type for 'Sales_Tax' is not found");
		}
		return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TaxRateResponse> findByCustom() {
		List<TaxRate> items = taxRateRepository.findByCustom();
		if(items.isEmpty()) {
			throw new NoDataFoundException("TaxRate type for 'Custom' is not found");
		}
		return items.stream().map(taxRateMapper::toResponse).collect(Collectors.toList());
	}
	
	private void validateBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Number must not be null nor negative");
		}
	}
	
	private void validateTaxType(TaxType type) {
		if(type == null) {
			throw new ValidationException("TaxType type must not be null");
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}
	
	private void validateTaxRateRequest(TaxRateRequest request) {
		if(request == null) {
			throw new ValidationException("TaxRateRequest must not be null");
		}
		validateString(request.taxName());
		validateBigDecimal(request.percentage());
		validateTaxType(request.type());
	}

}
