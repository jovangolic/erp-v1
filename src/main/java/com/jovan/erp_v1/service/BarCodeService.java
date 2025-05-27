package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.BarCodeNotFoundException;
import com.jovan.erp_v1.exception.BarCodeNotUniqueException;
import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.repository.BarCodeRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.response.BarCodeResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BarCodeService implements IBarcodeService {

	private final BarCodeRepository barCodeRepository;
	private final GoodsRepository goodsRepository;

	@Transactional
	@Override
	public BarCodeResponse createBarCode(BarCodeRequest request) {
		BarCode barCode = new BarCode();
		barCode.setCode(request.code());
		barCode.setScannedAt(request.scannedAt());
		barCode.setScannedBy(request.scannedBy());
		Goods goods = goodsRepository.findById(request.goodsId())
				.orElseThrow(() -> new GoodsNotFoundException("Goods not found with id: " + request.goodsId()));
		barCode.setGoods(goods);
		BarCode saved = barCodeRepository.save(barCode);
		return new BarCodeResponse(saved);
	}

	@Transactional
	@Override
	public BarCodeResponse updateBarCode(Long id, BarCodeRequest request) {
		BarCode barCode = barCodeRepository.findById(id)
				.orElseThrow(() -> new BarCodeNotFoundException("Bar-code not found with id: " + id));
		barCode.setCode(request.code());
		barCode.setScannedAt(request.scannedAt());
		barCode.setScannedBy(request.scannedBy());
		Goods goods = goodsRepository.findById(request.goodsId())
				.orElseThrow(() -> new GoodsNotFoundException("Goods not found with id: " + request.goodsId()));
		barCode.setGoods(goods);
		BarCode update = barCodeRepository.save(barCode);
		return new BarCodeResponse(update);
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
	public List<BarCodeResponse> getByGoods(Long goodsId) {
		Goods goods = goodsRepository.findById(goodsId)
				.orElseThrow(() -> new GoodsNotFoundException("Goods not found with id: " + goodsId));
		return barCodeRepository.findByGoods(goods).stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BarCodeResponse> getByScannedBy(String scannedBy) {
		return barCodeRepository.findByScannedBy(scannedBy).stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BarCodeResponse> getByScannedAtBetween(LocalDateTime from, LocalDateTime to) {
		return barCodeRepository.findByScannedAtBetween(from, to).stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
	}
}
