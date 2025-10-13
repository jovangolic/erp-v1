package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalQuarterTypeStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.exception.FiscalQuarterErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.FiscalQuarterMapper;
import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.repository.FiscalQuarterRepository;
import com.jovan.erp_v1.repository.FiscalYearRepository;
import com.jovan.erp_v1.repository.specification.FiscalQuarterSpecification;
import com.jovan.erp_v1.request.FiscalQuarterRequest;
import com.jovan.erp_v1.response.FiscalQuarterResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.FiscalQuarterSaveAsRequest;
import com.jovan.erp_v1.search_request.FiscalQuarterSearchRequest;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FiscalQuarterService implements IFiscalQuarterService {

    private final FiscalQuarterRepository fiscalQuarterRepository;
    private final FiscalQuarterMapper fiscalQuarterMapper;
    private final FiscalYearRepository fiscalYearRepository;

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
    	FiscalYear year = validateFiscalYearId(request.fiscalYearId());
        FiscalQuarter q = fiscalQuarterMapper.toEntity(request,year);
        FiscalQuarter saved = fiscalQuarterRepository.save(q);
        return fiscalQuarterMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public FiscalQuarterResponse update(Long id, FiscalQuarterRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        FiscalQuarter q = fiscalQuarterRepository.findById(id)
                .orElseThrow(() -> new FiscalQuarterErrorException("FiscalQuarter not found with id: " + id));
        List<FiscalQuarter> existing = fiscalQuarterRepository.findByFiscalYear_Id(request.fiscalYearId())
                .stream()
                .filter(fq -> !fq.getId().equals(id)) 
                .toList();
        boolean overlaps = existing.stream().anyMatch(fq ->
        	!(request.endDate().isBefore(fq.getStartDate()) || request.startDate().isAfter(fq.getEndDate()))
	    );
	    if (overlaps) {
	        throw new IllegalArgumentException("Datum kvartala se preklapa sa postojećim kvartalom.");
	    }
        validateFiscalQuarterStatus(request.quarterStatus());
    	DateValidator.validateRange(request.startDate(), request.endDate());
    	FiscalYear year = q.getFiscalYear();
    	if(request.fiscalYearId() != null && (q.getFiscalYear() == null || !request.fiscalYearId().equals(q.getFiscalYear().getId()))) {
    		year = validateFiscalYearId(request.fiscalYearId());
    	}
        fiscalQuarterMapper.updateEntity(q, request,year);
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
    	List<FiscalQuarter> items = fiscalQuarterRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("FiscalQuarter list is empty");
    	}
        return fiscalQuarterRepository.findAll().stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByFiscalYear_Year(Integer year) {
    	validateInteger(year);
    	List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYear_Year(year);
    	if(items.isEmpty()) {
    		String msg = String.format("No FiscalQuarter found for fiscal year %d", year);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByQuarterStatus(FiscalQuarterStatus status) {
    	validateFiscalQuarterStatus(status);
    	List<FiscalQuarter> items = fiscalQuarterRepository.findByQuarterStatus(status);
    	if(items.isEmpty()) {
    		String msg = String.format("No FiscalQuarter found for status %s", status);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByStartDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
    	List<FiscalQuarter> items = fiscalQuarterRepository.findByStartDateBetween(start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No FiscalQuarter found for start date between %s and %s",
    				start.format(formatter),end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByFiscalYearIdAndQuarterStatus(Long fiscalYearId,
            FiscalQuarterStatus status) {
    	validateFiscalYearId(fiscalYearId);
    	validateFiscalQuarterStatus(status);
    	List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYearIdAndQuarterStatus(fiscalYearId, status);
    	if(items.isEmpty()) {
    		String msg = String.format("No FiscalQuarter found for fiscal year id %d and quarter status %s",
    				fiscalYearId, status);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByStartDateAfter(LocalDate date) {
    	DateValidator.validateNotNull(date, "Datum ne sme biti null");
    	List<FiscalQuarter> items = fiscalQuarterRepository.findByStartDateAfter(date);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No FiscalQuarter found for start date after %s", date.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByStartDateBefore(LocalDate date) {
    	DateValidator.validateNotNull(date, "Datum ne sme biti null");
    	List<FiscalQuarter> items = fiscalQuarterRepository.findByStartDateBefore(date);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No FiscalQuarter found for start date before %s", date.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FiscalQuarterResponse> findByFiscalYear_Id(Long id) {
    	validateFiscalYearId(id);
    	List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYear_Id(id);
    	if(items.isEmpty()) {
    		String msg = String.format("No FiscalQuarter found for fiscal year id %d", id);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

	@Override
	public List<FiscalQuarterResponse> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus) {
		validateFiscalYearStatus(yearStatus);
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYear_YearStatus(yearStatus);
		if(items.isEmpty()) {
			String msg = String.format("No FiscalQuarter found for fiscal year status %s", yearStatus);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(FiscalQuarterResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYear_StartDateBetween(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYear_StartDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No FiscalQuarter found for fiscal year start date between %s and %s",
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(FiscalQuarterResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByEndDate(LocalDate endDate) {
		DateValidator.validateNotNull(endDate, "Datum ne sme biti null");
		List<FiscalQuarter> items = fiscalQuarterRepository.findByEndDate(endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No FiscalQuarter found for end date %s", endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(FiscalQuarterResponse::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<FiscalQuarterResponse> findByQuarterStatusQ1() {
		List<FiscalQuarter> items = fiscalQuarterRepository.findByQuarterStatusQ1();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalQuarter found for quarter status = 'Q1'");
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByQuarterStatusQ2() {
		List<FiscalQuarter> items = fiscalQuarterRepository.findByQuarterStatusQ2();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalQuarter found for quarter status = 'Q2'");
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByQuarterStatusQ3() {
		List<FiscalQuarter> items = fiscalQuarterRepository.findByQuarterStatusQ3();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalQuarter found for quarter status = 'Q3'");
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByQuarterStatusQ4() {
		List<FiscalQuarter> items = fiscalQuarterRepository.findByQuarterStatusQ4();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalQuarter found for quarter status = 'Q4'");
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYearStatusOpen() {
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYearStatusOpen();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalQuarter found for fiscal year status = 'OPEN'");
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYearStatusClosed() {
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYearStatusClosed();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalQuarter found for fiscal year status = 'CLOSED'");
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYearStatusArchived() {
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYearStatusArchived();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalQuarter found for fiscal year status = 'ARCHIVED'");
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYearStartDate(LocalDate startDate) {
		DateValidator.validateNotInPast(startDate, "Start date");
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYearStartDate(startDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No FiscalQuarter found for fiscal year start date %s", startDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYearStartDateAfter(LocalDate startDate) {
		DateValidator.validateNotInPast(startDate, "Start date after");
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYearStartDateAfter(startDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No FiscalQuarter found for fiscal year start date after %s", startDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYearStartDateBefore(LocalDate startDate) {
		DateValidator.validateNotInFuture(startDate, "Start date before");
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYearStartDateBefore(startDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No FiscalQuarter found for fiscal year start date before %s", startDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYearEndDate(LocalDate endDate) {
		DateValidator.validateNotInPast(endDate, "End date");
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYearEndDate(endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No FiscalQuarter found for fiscal year end date %s", endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYearStartDateBetween(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYearStartDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No FiscalQuarter found for start date between %s and %s",
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYearEndDateBetween(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYearEndDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No FiscalQuarter found for end date between %s and %s",
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus) {
		validateFiscalQuarterStatus(quarterStatus);
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYear_QuarterStatus(quarterStatus);
		if(items.isEmpty()) {
			String msg = String.format("No FiscalQuarter found for quarter status %s", quarterStatus);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findActiveQuarters() {
		List<FiscalQuarter> items = fiscalQuarterRepository.findActiveQuarters();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalQuarter found for active quarters");
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findQuartersEndingSoon(LocalDate date) {
		DateValidator.validateNotInPast(date, "Ending date");
		List<FiscalQuarter> items = fiscalQuarterRepository.findQuartersEndingSoon(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No FiscalQuarter for ending soon date %s is found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYear_YearAndQuarterStatus(Integer year, FiscalQuarterStatus status) {
		validateInteger(year);
		validateFiscalQuarterStatus(status);
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYear_YearAndQuarterStatus(year, status);
		if(items.isEmpty()) {
			String msg = String.format("No FiscalQuarter for fiscal year %d and quarter status %s is found", year,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<FiscalQuarterResponse> findByFiscalYearBetweenYears(Integer start, Integer end) {
		validateDoubleInteger(start, end);
		List<FiscalQuarter> items = fiscalQuarterRepository.findByFiscalYearBetweenYears(start, end);
		if(items.isEmpty()) {
			String msg = String.format("No FiscalQuarter for fiscal year between %d and %d is found", start,end);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	@Override
	public FiscalQuarterResponse trackFiscalQuarter(Long id) {
		FiscalQuarter fq = fiscalQuarterRepository.findById(id).orElseThrow(() -> new ValidationException("FiscalQuarter not found with id "+id));
		return new FiscalQuarterResponse(fq);
	}

	@Transactional
	@Override
	public FiscalQuarterResponse confirmFiscalQuarter(Long id) {
		FiscalQuarter fq = fiscalQuarterRepository.findById(id).orElseThrow(() -> new ValidationException("FiscalQuarter not found with id "+id));
		fq.setConfirmed(true);
		fq.setStatus(FiscalQuarterTypeStatus.CONFIRMED);
		return new FiscalQuarterResponse(fiscalQuarterRepository.save(fq));
	}

	@Transactional
	@Override
	public FiscalQuarterResponse cancelFiscalQuarter(Long id) {
		FiscalQuarter fq = fiscalQuarterRepository.findById(id).orElseThrow(() -> new ValidationException("FiscalQuarter not found with id "+id));
		if(fq.getStatus() != FiscalQuarterTypeStatus.CONFIRMED && fq.getStatus() != FiscalQuarterTypeStatus.NEW) {
			throw new ValidationException("Only NEW or CONFIRMED fiscal-quarter can be cancelled");
		}
		fq.setStatus(FiscalQuarterTypeStatus.CANCELLED);
		return new FiscalQuarterResponse(fiscalQuarterRepository.save(fq)); 
	}

	@Transactional
	@Override
	public FiscalQuarterResponse closeFiscalQuarter(Long id) {
		FiscalQuarter fq = fiscalQuarterRepository.findById(id).orElseThrow(() -> new ValidationException("FiscalQuarter not found with id "+id));
		if(fq.getStatus() != FiscalQuarterTypeStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED fiscal-quarter can be closed");
		}
		fq.setStatus(FiscalQuarterTypeStatus.CLOSED);		
		return new FiscalQuarterResponse(fiscalQuarterRepository.save(fq));
	}
	

	@Transactional
	@Override
	public FiscalQuarterResponse changeStatus(Long id, FiscalQuarterTypeStatus status) {
		FiscalQuarter fq = fiscalQuarterRepository.findById(id).orElseThrow(() -> new ValidationException("FiscalQuarter not found with id "+id));
		validateFiscalQuarterTypeStatus(status);
		if(fq.getStatus() == FiscalQuarterTypeStatus.CLOSED) {
			throw new ValidationException("Closed fiscal-quarter  cannot change status");
		}
		if(status == FiscalQuarterTypeStatus.CONFIRMED) {
			if(fq.getStatus() != FiscalQuarterTypeStatus.NEW) {
				throw new ValidationException("Only NEW fiscal-quarter can be confirmed");
			}
			fq.setConfirmed(true);
		}
		fq.setStatus(status);
		return new FiscalQuarterResponse(fiscalQuarterRepository.save(fq));
	}

	@Transactional
	@Override
	public FiscalQuarterResponse saveFiscalQuarter(FiscalQuarterRequest request) {
		FiscalQuarter items = FiscalQuarter.builder()
				.id(request.id())
				.endDate(request.endDate())
				.fiscalYear(validateFiscalYearId(request.fiscalYearId()))
				.confirmed(request.confirmed())
				.status(request.status())
				.quarterStatus(request.quarterStatus())
				.build();
		FiscalQuarter saved = fiscalQuarterRepository.save(items);
		return new FiscalQuarterResponse(saved);
	}
	
	private final AbstractSaveAsService<FiscalQuarter, FiscalQuarterResponse> saveAsHelper = new AbstractSaveAsService<FiscalQuarter, FiscalQuarterResponse>() {
		
		@Override
		protected FiscalQuarterResponse toResponse(FiscalQuarter entity) {
			return new FiscalQuarterResponse(entity);
		}
		
		@Override
		protected JpaRepository<FiscalQuarter, Long> getRepository() {
			return fiscalQuarterRepository;
		}
		
		@Override
		protected FiscalQuarter copyAndOverride(FiscalQuarter source, Map<String, Object> overrides) {
			return FiscalQuarter.builder()
					.endDate(source.getEndDate())
					.fiscalYear(validateFiscalYearId(source.getFiscalYear().getId()))
					.confirmed(source.getConfirmed())
					.status(source.getStatus())
					.quarterStatus(source.getQuarterStatus())
					.build();
		}
	};
	
	private final AbstractSaveAllService<FiscalQuarter, FiscalQuarterResponse> saveAllHelper = new AbstractSaveAllService<FiscalQuarter, FiscalQuarterResponse>() {
		
		@Override
		protected Function<FiscalQuarter, FiscalQuarterResponse> toResponse() {
			return FiscalQuarterResponse::new;
		}
		
		@Override
		protected JpaRepository<FiscalQuarter, Long> getRepository() {
			return fiscalQuarterRepository;
		}
	};

	@Transactional
	@Override
	public FiscalQuarterResponse saveAs(FiscalQuarterSaveAsRequest request) {
		Map<String, Object> overrides = new HashMap<>();
		if(request.endDate() != null) overrides.put("End-date", request.endDate());
		if(request.fiscalYearId() != null) overrides.put("Fiscal-year ID", validateFiscalYearId(request.fiscalYearId()));
		if(request.quarterStatus() != null) overrides.put("Quarter-status", request.quarterStatus());
		if(request.confirmed() != null) overrides.put("Confirmed", request.confirmed());
		if(request.status() != null) overrides.put("Status", request.status());
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}

	@Transactional
	@Override
	public List<FiscalQuarterResponse> saveAll(List<FiscalQuarterRequest> requests) {
		List<FiscalQuarter> items = requests.stream()
				.map(req -> FiscalQuarter.builder()
						.id(req.id())
						.endDate(req.endDate())
						.fiscalYear(validateFiscalYearId(req.fiscalYearId()))
						.status(req.status())
						.confirmed(req.confirmed())
						.quarterStatus(req.quarterStatus())
						.build())
				.toList();
		return saveAllHelper.saveAll(items);
	}

	@Override
	public List<FiscalQuarterResponse> generalSearch(FiscalQuarterSearchRequest request) {
		Specification<FiscalQuarter> spec = FiscalQuarterSpecification.fromRequest(request);
		List<FiscalQuarter> items = fiscalQuarterRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No FiscalQuarter found for given criteria");
		}
		return items.stream().map(fiscalQuarterMapper::toResponse).collect(Collectors.toList());
	}
	
	private FiscalYear validateFiscalYearId(Long fiscalYearId) {
		if(fiscalYearId == null) {
			throw new IllegalArgumentException("ID za fiskalnu godinu "+fiscalYearId+" ne postoji");
		}
		return fiscalYearRepository.findById(fiscalYearId).orElseThrow(() -> new ValidationException("FiscalYear not found with id "+fiscalYearId));
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
	
	private void validateDoubleInteger(Integer min, Integer max) {
		if(min == null || max == null) {
			throw new ValidationException("Both numbers must not be null");
		}
		if (min < 0) {
	        throw new ValidationException("Min value must be zero or positive.");
	    }
	    if (max <= 0) {
	        throw new ValidationException("Max value must be positive and greater than zero.");
	    }
	    if (min > max) {
	        throw new ValidationException("Min value must not be greater than max value.");
	    }
	}
	
	private void validateFiscalQuarterTypeStatus(FiscalQuarterTypeStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("FiscalQuarterTypeStatus status must not be null"));
	}
}
