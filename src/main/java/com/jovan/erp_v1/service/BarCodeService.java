package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.response.BarCodeResponse;
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
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		BarCode barCode = barCodeRepository.findById(id)
				.orElseThrow(() -> new BarCodeNotFoundException("Bar-code not found with id: " + id));
		validateUniqueCode(request.code());
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

	
}
