package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
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

import com.jovan.erp_v1.enumeration.BarCodeStatus;
import com.jovan.erp_v1.exception.BarCodeNotFoundException;
import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.BarCodeMapper;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.BarCodeRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.specification.BarCodeSpecification;
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.response.BarCodeResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.BarCodeSaveAsRequest;
import com.jovan.erp_v1.search_request.BarCodeSearchRequest;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BarCodeService implements IBarcodeService {

	private final BarCodeRepository barCodeRepository;
	private final GoodsRepository goodsRepository;
	private final BarCodeMapper barCodeMapper;
	private final UserRepository userRepository;
	

	@Transactional
	@Override
	public BarCodeResponse createBarCode(BarCodeRequest request) {
		validateUniqueCode(request.code());
		validateBarCodeStatus(request.status());
		DateValidator.validateNotNull(request.scannedAt(), "Datum i vreme ne smeju biti null");
		Goods goods = validateGoodsId(request.goodsId());
		User scannedBy = validateScannedById(request.scannedById());
		BarCode code = barCodeMapper.toEntity(request,scannedBy,goods);
		BarCode saved = barCodeRepository.save(code);
		return barCodeMapper.toResponse(saved);
	}

	@Transactional
	@Override
	public BarCodeResponse updateBarCode(Long id, BarCodeRequest request) {
		if (!request.id().equals(id)) {
			throw new ValidationException("ID in path and body do not match");
		}
		BarCode barCode = barCodeRepository.findById(id)
				.orElseThrow(() -> new BarCodeNotFoundException("Bar-code not found with id: " + id));
		validateUniqueCode(request.code());
		validateBarCodeStatus(request.status());
		DateValidator.validateNotNull(request.scannedAt(), "Datum i vreme ne smeju biti null");
		User scannedBy = barCode.getScannedBy();
		if (request.scannedById() != null &&
		    (barCode.getScannedBy() == null || !request.scannedById().equals(barCode.getScannedBy().getId()))) {
			scannedBy = validateScannedById(request.scannedById());
		}
		Goods goods = barCode.getGoods();
		if (request.goodsId() != null &&
		    (barCode.getGoods() == null || !request.goodsId().equals(barCode.getGoods().getId()))) {
			goods = validateGoodsId(request.goodsId());
		}
		barCodeMapper.toUpdateEntity(barCode, request, scannedBy, goods);
		return barCodeMapper.toResponse(barCodeRepository.save(barCode));
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if (!barCodeRepository.existsById(id)) {
			throw new BarCodeNotFoundException("BarCode not found with id: " + id);
		}
		barCodeRepository.deleteById(id);
	}

	@Override
	public BarCodeResponse getOne(Long id) {
		BarCode code = barCodeRepository.findById(id)
				.orElseThrow(() -> new BarCodeNotFoundException("BarCode not found with id: " + id));
		return new BarCodeResponse(code);
	}

	@Override
	public List<BarCodeResponse> getAll() {
		List<BarCode> items = barCodeRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("List of bar-codes is empty");
		}
		return items.stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public BarCodeResponse getByCode(String code) {
		BarCode barCode = barCodeRepository.findByCode(code)
				.orElseThrow(() -> new BarCodeNotFoundException("BarCode not found with code: " + code));
		return new BarCodeResponse(barCode);
	}
	
	@Override
	public List<BarCodeResponse> findByGoods_Id(Long goodsId) {
		validateGoodsId(goodsId);
		List<BarCode> items = barCodeRepository.findByGoods_Id(goodsId);
		if(items.isEmpty()) {
			String msg = String.format("No bar-codes for goodsId %d is found", goodsId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BarCodeResponse> findByGoods_Name(String goodsName) {
		validateString(goodsName);
		List<BarCode> items = barCodeRepository.findByGoods_Name(goodsName);
		if(items.isEmpty()) {
			String msg = String.format("No bar-codes with goods name %s is gound", goodsName);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BarCodeResponse> findByScannedAtBetween(LocalDateTime from, LocalDateTime to) {
		DateValidator.validateRange(from, to);
		List<BarCode> items = barCodeRepository.findByScannedAtBetween(from, to);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No bar-codes with scannedAt between %s and %s is found",
					from.format(formatter),to.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BarCodeResponse> findByScannedBy_Id(Long scannedById) {
		validateScannedById(scannedById);
		List<BarCode> items = barCodeRepository.findByScannedBy_Id(scannedById);
		if(items.isEmpty()) {
			String msg = String.format("No bar-codes fr scannedBy ID %d is found", scannedById);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BarCodeResponse> findByScannedBy_FirstNameContainingIgnoreCaseAndScannedBy_LastNameContainingIgnoreCase(
			String userFirstName, String userLastName) {
		validateDoubleString(userFirstName, userLastName);
		List<BarCode> items = barCodeRepository.findByScannedBy_FirstNameContainingIgnoreCaseAndScannedBy_LastNameContainingIgnoreCase(userFirstName, userLastName);
		if(items.isEmpty()) {
			String msg = String.format("No bar-codes with scannedBy user-first-name %s and user-last-name %s is found",
					userFirstName,userLastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	@Override
	public BarCodeResponse trackBarCode(Long id) {
		BarCode bc = barCodeRepository.findById(id).orElseThrow(() -> new ValidationException("BarCode not found with id "+id));
		return new BarCodeResponse(bc);
	}

	@Transactional
	@Override
	public BarCodeResponse confirmBarCode(Long id) {
		BarCode bc = barCodeRepository.findById(id).orElseThrow(() -> new ValidationException("BarCode not found with id "+id));
		bc.setConfirmed(true);
		bc.setStatus(BarCodeStatus.CONFIRMED);
		return new BarCodeResponse(barCodeRepository.save(bc));
	}

	@Transactional
	@Override
	public BarCodeResponse closeBarCode(Long id) {
		BarCode bc = barCodeRepository.findById(id).orElseThrow(() -> new ValidationException("BarCode not found with id "+id));
		if(bc.getStatus() != BarCodeStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED bar-codes can be closed");
		}
		bc.setStatus(BarCodeStatus.CLOSED);
		return new BarCodeResponse(barCodeRepository.save(bc));
	}

	@Transactional
	@Override
	public BarCodeResponse cancelBarCode(Long id) {
		BarCode bc = barCodeRepository.findById(id).orElseThrow(() -> new ValidationException("BarCode not found with id "+id));
		if(bc.getStatus() != BarCodeStatus.NEW && bc.getStatus() != BarCodeStatus.CONFIRMED) {
			throw new ValidationException("Only NEW or CONFIRMED bar-codes can be cancelled");
		}
		bc.setStatus(BarCodeStatus.CLOSED);
		return new BarCodeResponse(barCodeRepository.save(bc));
	}

	@Transactional
	@Override
	public BarCodeResponse changeStatus(Long id, BarCodeStatus status) {
		BarCode bc = barCodeRepository.findById(id).orElseThrow(() -> new ValidationException("BarCode not found with id "+id));
		validateBarCodeStatus(status);
		if(bc.getStatus() == BarCodeStatus.CLOSED) {
			throw new ValidationException("Closed bar-codes cannot change status");
		}
		if(status == BarCodeStatus.CONFIRMED) {
			if(bc.getStatus() != BarCodeStatus.NEW) {
				throw new ValidationException("Only NEW bar-codes can be confirmed");
			}
			bc.setConfirmed(true);
		}
		bc.setStatus(status);
		return new BarCodeResponse(barCodeRepository.save(bc));
	}

	@Transactional
	@Override
	public BarCodeResponse saveBarCode(BarCodeRequest request) {
		BarCode bc = BarCode.builder()
				.code(request.code())
				.scannedBy(validateScannedById(request.scannedById()))
				.goods(validateGoodsId(request.goodsId()))
				.status(request.status())
				.confirmed(request.confirmed())
				.build();
		BarCode saved = barCodeRepository.save(bc);
		return new BarCodeResponse(saved);
	}
	
	private final AbstractSaveAsService<BarCode, BarCodeResponse> saveAsHelper = new AbstractSaveAsService<BarCode, BarCodeResponse>() {
		
		@Override
		protected BarCodeResponse toResponse(BarCode entity) {
			return new BarCodeResponse(entity);
		}
		
		@Override
		protected JpaRepository<BarCode, Long> getRepository() {
			return barCodeRepository;
		}
		
		@Override
		protected BarCode copyAndOverride(BarCode source, Map<String, Object> overrides) {
			return BarCode.builder()
					.code((String) overrides.getOrDefault("code", source.getCode()))
					.scannedBy(validateScannedById(source.getScannedBy().getId()))
					.goods(validateGoodsId(source.getGoods().getId()))
					.status(source.getStatus())
					.confirmed(source.getConfirmed())
					.build();
		}
	};
	
	private final AbstractSaveAllService<BarCode, BarCodeResponse> saveAllHelper = new AbstractSaveAllService<BarCode, BarCodeResponse>() {
		
		@Override
		protected Function<BarCode, BarCodeResponse> toResponse() {
			return BarCodeResponse::new;
		}
		
		@Override
		protected JpaRepository<BarCode, Long> getRepository() {
			return barCodeRepository;
		}
	};

	@Transactional
	@Override
	public BarCodeResponse saveAs(BarCodeSaveAsRequest request) {
		Map<String, Object> overrides = new HashMap<>();
		if(request.code() != null) overrides.put("Code", request.code());
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}

	@Transactional
	@Override
	public List<BarCodeResponse> saveAll(List<BarCodeRequest> request) {
		List<BarCode> items = request.stream()
				.map(req -> BarCode.builder()
						.id(req.id())
						.code(req.code())
						.scannedBy(validateScannedById(req.scannedById()))
						.goods(validateGoodsId(req.goodsId()))
						.status(req.status())
						.confirmed(req.confirmed())
						.build())
				.collect(Collectors.toList());
		return saveAllHelper.saveAll(items);
	}

	@Override
	public List<BarCodeResponse> generalSearch(BarCodeSearchRequest request) {
		Specification<BarCode> spec = BarCodeSpecification.fromRequest(request);
		List<BarCode> items = barCodeRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No BarCodes found for given criteria");
		}
		return items.stream().map(barCodeMapper::toResponse).collect(Collectors.toList());		
	}
	
	private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Naziv robe ne sme biti null ili prazan");
        }
    }

	private void validateDoubleString(String s1, String s2) {
        if (s1 == null || s1.trim().isEmpty() || s2 == null || s2.trim().isEmpty()) {
            throw new IllegalArgumentException("Oba stringa moraju biti ne-null i ne-prazna");
        }
    }
	
	private Goods validateGoodsId(Long goodsId) {
		if(goodsId == null) {
			throw new GoodsNotFoundException("Goods ID "+goodsId+" ne postoji");
		}
		return goodsRepository.findById(goodsId).orElseThrow(() -> new ValidationException("Goods not found with id "+goodsId));
	}
	
	private void validateUniqueCode(String code) {
	    if (code == null || code.trim().isEmpty()) {
	        throw new IllegalArgumentException("Code ne sme biti null ili prazan");
	    }
	    if (barCodeRepository.existsByCode(code)) {
	        throw new IllegalArgumentException("Code već postoji: " + code);
	    }
	}
	
	private User validateScannedById(Long scannedById) {
		if(scannedById == null) {
			throw new IllegalArgumentException("User ID "+scannedById+" ne postoji");
		}
		return userRepository.findById(scannedById).orElseThrow(() -> new ValidationException("User not found with id "+scannedById));
	}
	
	private void validateBarCodeStatus(BarCodeStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("BarCodeStatus status must not be null"));
	}
}
