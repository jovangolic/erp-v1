package com.jovan.erp_v1.mapper;


import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.request.SupplyRequest;
import com.jovan.erp_v1.response.SupplyResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class SupplyMapper extends AbstractMapper<SupplyRequest> {

	public Supply toEntity(SupplyRequest request, Storage storage, List<Goods> goods) {
		Objects.requireNonNull(request, "SupplyRequest must not be null");
		validateIdForCreate(request, SupplyRequest::id);
		Supply s = new Supply();
		s.setId(request.id());
		s.setStorage(storage);
		s.setGoods(goods);
		s.setQuantity(request.quantity());
		s.setUpdates(request.updates());;
		return s;
	}
	
	public Supply toEntityUpdate(Supply supply,SupplyRequest request, Storage storage, List<Goods> goods) {
		Objects.requireNonNull(request, "SupplyRequest must not be null");
		Objects.requireNonNull(supply, "Supply must not be null");
		validateIdForUpdate(request, SupplyRequest::id);
		supply.setStorage(storage);
		supply.setGoods(goods);
		supply.setQuantity(request.quantity());
		supply.setUpdates(request.updates());
		return supply;
	}
	
	public SupplyResponse toResponse(Supply supply) {
		Objects.requireNonNull(supply, "Supply must not be null");
		return new SupplyResponse(supply);
	}
	
	public List<SupplyResponse> toResponseList(List<Supply> supplies){
		if(supplies == null || supplies.isEmpty()) {
			return Collections.emptyList();
		}
		return supplies.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
	
}
