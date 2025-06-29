package com.jovan.erp_v1.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.response.BarCodeResponse;
import com.jovan.erp_v1.response.UserResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BarCodeMapper extends AbstractMapper<BarCodeRequest> {

	private final GoodsRepository goodsRepository;
	private UserRepository userRepository;
	
	public BarCode toEntity(BarCodeRequest request) {
		Objects.requireNonNull(request, "BarCodeRequest ne sme biti null");
		validateIdForCreate(request, BarCodeRequest::id);
		return buildBarCodeFromRequest(new BarCode(), request);
	}
	
	public BarCode toUpdateEntity(BarCode code, BarCodeRequest request) {
		Objects.requireNonNull(code, "BarCode ne sme biti null");
		Objects.requireNonNull(request, "BarCodeRequest ne sme biti null");
		validateIdForUpdate(request, BarCodeRequest::id);
		return buildBarCodeFromRequest(code, request);
	}
	
	
	private BarCode buildBarCodeFromRequest(BarCode code, BarCodeRequest request) {
		code.setGoods(fetchGoods(request.goodsId()));
        code.setCode(request.code());
        code.setScannedAt(request.scannedAt() != null ? request.scannedAt() : LocalDateTime.now());
        code.setScannedBy(fetchScannedBy(request.scannedById()));
        return code;
	}
	
	
	public BarCodeResponse toResponse(BarCode barCode) {
	    Objects.requireNonNull(barCode, "BarCode ne sme biti null");

	    return BarCodeResponse.builder()
	        .id(barCode.getId())
	        .code(barCode.getCode())
	        .scannedAt(barCode.getScannedAt())
	        .scannedBy(barCode.getScannedBy() != null ? new UserResponse(barCode.getScannedBy()) : null)
	        .goodsId(barCode.getGoods() != null ? barCode.getGoods().getId() : null)
	        .goodsName(barCode.getGoods() != null ? barCode.getGoods().getName() : null)
	        .goodsType(barCode.getGoods() != null ? barCode.getGoods().getGoodsType() : null)
	        .build();
	}
	
	public List<BarCodeResponse> toResponseList(List<BarCode> codes){
		if(codes == null || codes.isEmpty()) {
			return Collections.emptyList();
		}
		return codes.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
	
	private Goods fetchGoods(Long goodsId) {
		if(goodsId == null) {
			throw new GoodsNotFoundException("Goods ID must not be null");
		}
		return goodsRepository.findById(goodsId)
                .orElseThrow(() -> new GoodsNotFoundException("Goods not found with id: " + goodsId));
	}
	
	private User fetchScannedBy(Long scannedById) {
        if (scannedById == null) {
            throw new UserNotFoundException("User id must not be null");
        }
        return userRepository.findById(scannedById)
                .orElseThrow(() -> new UserNotFoundException("user not found with id: " + scannedById));
    }
	
}
