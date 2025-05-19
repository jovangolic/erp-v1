package com.jovan.erp_v1.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.response.BarCodeResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BarCodeMapper {

	private final GoodsRepository goodsRepository;
	
	public BarCode toEntity(BarCodeRequest request) {
		if (request.goodsId() == null) {
            throw new IllegalArgumentException("Goods ID must not be null");
        }
        Goods goods = goodsRepository.findById(request.goodsId())
                .orElseThrow(() -> new GoodsNotFoundException("Goods not found with id: " + request.goodsId()));
        BarCode code = new BarCode();
        code.setId(request.id());
        code.setCode(request.code());
        code.setScannedAt(request.scannedAt() != null ? request.scannedAt() : LocalDateTime.now());
        code.setScannedBy(request.scannedBy());
        code.setGoods(goods);
        return code;
	}
	
	
	public BarCodeResponse toResponse(BarCode barCode) {
		return new BarCodeResponse(
                barCode.getId(),
                barCode.getCode(),
                barCode.getScannedAt(),
                barCode.getScannedBy(),
                barCode.getGoods() != null ? barCode.getGoods().getId() : null,
                barCode.getGoods() != null ? barCode.getGoods().getName() : null,
                barCode.getGoods() != null ? barCode.getGoods().getGoodsType() : null
        );
	}
	
	public List<BarCodeResponse> toResponseList(List<BarCode> codes){
		return codes.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
	
}
