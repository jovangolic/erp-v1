package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.exception.FiscalQuarterErrorException;
import com.jovan.erp_v1.mapper.FiscalQuarterMapper;
import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.repository.FiscalQuarterRepository;
import com.jovan.erp_v1.request.FiscalQuarterRequest;
import com.jovan.erp_v1.response.FiscalQuarterResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FiscalQuarterService implements IFiscalQuarterService {

    private final FiscalQuarterRepository fiscalQuarterRepository;
    private final FiscalQuarterMapper fiscalQuarterMapper;

    @Transactional
    @Override
    public FiscalQuarterResponse create(FiscalQuarterRequest request) {
    	List<FiscalQuarter> existing = fiscalQuarterRepository.findByFiscalYear_Id(request.fiscalYearId());
    	boolean overlaps = existing.stream().anyMatch(q ->
    	    !(request.endDate().isBefore(q.getStartDate()) || request.startDate().isAfter(q.getEndDate()))
    	);
    	if (overlaps) {
    	    throw new IllegalArgumentException("Datum kvartala se preklapa sa postojećim kvartalom.");
    	}
    	validateFiscalQuarterStatus(request.quarterStatus());
    	DateValidator.validateRange(request.startDate(), request.endDate());
        FiscalQuarter q = fiscalQuarterMapper.toEntity(request);
        FiscalQuarter saved = fiscalQuarterRepository.save(q);
        return fiscalQuarterMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public FiscalQuarterResponse update(Long id, FiscalQuarterRequest request) {
        FiscalQuarter q = fiscalQuarterRepository.findById(id)
                .orElseThrow(() -> new FiscalQuarterErrorException("FiscalQuarter not found with id: " + id));
        List<FiscalQuarter> existing = fiscalQuarterRepository.findByFiscalYear_Id(request.fiscalYearId())
                .stream()
                .filter(fq -> !fq.getId().equals(id)) // ignorišemo trenutno ažurirani kvartal
                .toList();
        boolean overlaps = existing.stream().anyMatch(fq ->
        	!(request.endDate().isBefore(fq.getStartDate()) || request.startDate().isAfter(fq.getEndDate()))
	    );
	    if (overlaps) {
	        throw new IllegalArgumentException("Datum kvartala se preklapa sa postojećim kvartalom.");
	    }
        validateFiscalQuarterStatus(request.quarterStatus());
    	DateValidator.validateRange(request.startDate(), request.endDate());
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
    public List<FiscalQuarterResponse> findByFiscalYear_Year(Integer year) {
    	validateInteger(year);
        return fiscalQuarterRepository.findByFiscalYear_Year(year).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByQuarterStatus(FiscalQuarterStatus status) {
    	validateFiscalQuarterStatus(status);
        return fiscalQuarterRepository.findByQuarterStatus(status).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByStartDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
        return fiscalQuarterRepository.findByStartDateBetween(start, end).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByFiscalYearIdAndQuarterStatus(Long fiscalYearId,
            FiscalQuarterStatus status) {
    	validateFiscalYearId(fiscalYearId);
    	validateFiscalQuarterStatus(status);
        return fiscalQuarterRepository.findByFiscalYearIdAndQuarterStatus(fiscalYearId, status).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByStartDateAfter(LocalDate date) {
    	DateValidator.validateNotNull(date, "Datum ne sme biti null");
        return fiscalQuarterRepository.findByStartDateAfter(date).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByStartDateBefore(LocalDate date) {
    	DateValidator.validateNotNull(date, "Datum ne sme biti null");
        return fiscalQuarterRepository.findByStartDateBefore(date).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByFiscalYear_Id(Long id) {
    	validateFiscalYearId(id);
        return fiscalQuarterRepository.findByFiscalYear_Id(id).stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

	@Override
	public List<FiscalQuarterResponse> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus) {
		validateFiscalYearStatus(yearStatus);
		return fiscalQuarterRepository.findByFiscalYear_YearStatus(yearStatus).stream()
				.map(FiscalQuarterResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYear_StartDateBetween(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		return fiscalQuarterRepository.findByFiscalYear_StartDateBetween(start, end).stream()
				.map(FiscalQuarterResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByEndDate(LocalDate endDate) {
		DateValidator.validateNotNull(endDate, "Datum ne sme biti null");
		return fiscalQuarterRepository.findByEndDate(endDate).stream()
				.map(FiscalQuarterResponse::new)
				.collect(Collectors.toList());
	}
	
	private void validateFiscalYearId(Long fiscalYearId) {
		if(fiscalYearId == null) {
			throw new IllegalArgumentException("ID za fiskalnu godinu "+fiscalYearId+" ne postoji");
		}
	}
	
	private void validateFiscalQuarterStatus(FiscalQuarterStatus quarterStatus) {
		if(quarterStatus == null) {
			throw new IllegalArgumentException("Kvartal za fiskalnu godinu ne sme biti null");
		}
	}
	
	private void validateFiscalYearStatus(FiscalYearStatus yearStatus) {
		if(yearStatus == null) {
			throw new IllegalArgumentException("Status za fiskalnu godinu ne sme biti null");
		}
	}
	
	private void validateInteger(Integer num) {
        if (num == null || num <= 0) {
            throw new IllegalArgumentException("Vrednost mora biti pozitivan ceo broj");
        }
    }

}
