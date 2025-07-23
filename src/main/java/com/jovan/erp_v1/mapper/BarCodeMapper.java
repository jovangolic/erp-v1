package com.jovan.erp_v1.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.response.BarCodeResponse;
import com.jovan.erp_v1.response.BasicGoodsForBarCodeResponse;
import com.jovan.erp_v1.response.UserResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class BarCodeMapper extends AbstractMapper<BarCodeRequest> {

	public BarCode toEntity(BarCodeRequest request, User scannedBy, Goods goods) {
		Objects.requireNonNull(request, "BarCodeRequest ne sme biti null");
		Objects.requireNonNull(scannedBy, "User ne sme biti null");
		Objects.requireNonNull(goods, "Goods ne sme biti null");
		validateIdForCreate(request, BarCodeRequest::id);
		return BarCode.builder()
		        .code(request.code())
		        .scannedAt(LocalDateTime.now())
		        .scannedBy(scannedBy)
		        .goods(goods)
		        .build();
	}
	
	public BarCode toUpdateEntity(BarCode code, BarCodeRequest request, User scannedBy, Goods goods) {
		Objects.requireNonNull(code, "BarCode ne sme biti null");
		Objects.requireNonNull(request, "BarCodeRequest ne sme biti null");
		Objects.requireNonNull(scannedBy, "User ne sme biti null");
		Objects.requireNonNull(goods, "Goods ne sme biti null");
		validateIdForUpdate(request, BarCodeRequest::id);
		return buildBarCodeFromRequest(code, request,scannedBy,goods);
	}
	
	
	private BarCode buildBarCodeFromRequest(BarCode code, BarCodeRequest request, User scannedBy, Goods goods) {
		code.setGoods(goods);
        code.setCode(request.code());
        code.setScannedAt(request.scannedAt() != null ? request.scannedAt() : LocalDateTime.now());
        code.setScannedBy(scannedBy);
        return code;
	}
	
	
	public BarCodeResponse toResponse(BarCode barCode) {
	    Objects.requireNonNull(barCode, "BarCode ne sme biti null");
	    return BarCodeResponse.builder()
	        .id(barCode.getId())
	        .code(barCode.getCode())
	        .scannedAt(barCode.getScannedAt())
	        .scannedBy(barCode.getScannedBy() != null ? new UserResponse(barCode.getScannedBy()) : null)
	        .basicGoodsForBarCodeResponse(barCode.getGoods() != null ? new BasicGoodsForBarCodeResponse(barCode.getGoods()) : null)
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
	
}
