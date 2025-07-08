package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.BarCodeNotFoundException;
import com.jovan.erp_v1.exception.BarCodeNotUniqueException;
import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.mapper.BarCodeMapper;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.repository.BarCodeRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
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
	

	@Transactional
	@Override
	public BarCodeResponse createBarCode(BarCodeRequest request) {
		validateUniqueCode(request.code());
		DateValidator.validateNotNull(request.scannedAt(), "Datum i vreme ne smeju biti null");
		validateScannedById(request.scannedById());
		validateGoodsId(request.goodsId());
		BarCode code = barCodeMapper.toEntity(request);
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
		validateScannedById(request.scannedById());
		validateGoodsId(request.goodsId());
		barCodeMapper.toUpdateEntity(barCode, request);
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
		return barCodeRepository.findAll().stream()
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
		return barCodeRepository.findByGoods_Id(goodsId).stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BarCodeResponse> findByGoods_Name(String goodsName) {
		validateString(goodsName);
		return barCodeRepository.findByGoods_Name(goodsName).stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BarCodeResponse> findByScannedAtBetween(LocalDateTime from, LocalDateTime to) {
		DateValidator.validateRange(from, to);
		return barCodeRepository.findByScannedAtBetween(from, to).stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BarCodeResponse> findByScannedBy_Id(Long scannedById) {
		validateScannedById(scannedById);
		return barCodeRepository.findByScannedBy_Id(scannedById).stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BarCodeResponse> findByScannedBy_FirstNameContainingIgnoreCaseAndScannedBy_LastNameContainingIgnoreCase(
			String userFirstName, String userLastName) {
		validateDoubleString(userFirstName, userLastName);
		return barCodeRepository.findByScannedBy_FirstNameContainingIgnoreCaseAndScannedBy_LastNameContainingIgnoreCase(userFirstName, userLastName).stream()
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
	
	private void validateGoodsId(Long goodsId) {
		if(goodsId == null) {
			throw new GoodsNotFoundException("Goods ID "+goodsId+" ne postoji");
		}
	}
	
	private void validateUniqueCode(String code) {
	    if (code == null || code.trim().isEmpty()) {
	        throw new IllegalArgumentException("Code ne sme biti null ili prazan");
	    }
	    if (barCodeRepository.existsByCode(code)) {
	        throw new IllegalArgumentException("Code već postoji: " + code);
	    }
	}
	
	private void validateScannedById(Long scannedById) {
		if(scannedById == null) {
			throw new IllegalArgumentException("User ID "+scannedById+" ne postoji");
		}
	}

	
}
