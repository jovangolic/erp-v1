package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.SupplyRequest;
import com.jovan.erp_v1.response.GoodsResponse;
import com.jovan.erp_v1.response.SupplyResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SupplyMapper {

	private final StorageRepository storageRepository;
	private final GoodsRepository goodsRepository;
	
	
	public Supply toEntity(SupplyRequest request) {
		Supply supply = new Supply();
		Storage storage = storageRepository.findById(request.storageId()).orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + request.storageId()));
		/*List<Goods> goods = request.goodsIds().stream()
				.map(id -> goodsRepository.findById(id).orElseThrow(() -> new GoodsNotFoundException("Goods not found with id: " + id)))
				.peek(g -> g.setSupply(supply))
				.collect(Collectors.toList());*/
		List<Goods> goodsList = new ArrayList<>();
		if (request.goodsIds() != null) {
		    goodsList = request.goodsIds().stream()
		        .map((Long id) -> goodsRepository.findById(id)
		            .orElseThrow(() -> new GoodsNotFoundException("Goods not found with id: " + id)))
		        .peek(g -> g.setSupply(supply)) // ako koristiš bidirekcionu vezu
		        .collect(Collectors.toList());
		}
		supply.setGoods(goodsList);
		supply.setId(request.id());
		supply.setStorage(storage);
		supply.setQuantity(request.quantity());
		supply.setUpdates(request.updates());
		supply.setGoods(goodsList);
		return supply;
	}
	
	public SupplyResponse toResponse(Supply supply) {
		SupplyResponse response = new SupplyResponse();
		response.setId(supply.getId());
		response.setStorageName(supply.getStorage().getName());
		response.setStorageLocation(supply.getStorage().getLocation());
		response.setStorageCapacity(supply.getStorage().getCapacity());
		response.setQuantity(supply.getQuantity());
		response.setUpdates(supply.getUpdates());
		if(supply.getGoods() != null) {
			List<GoodsResponse> responses = supply.getGoods().stream()
					.map(GoodsResponse::new)
					.collect(Collectors.toList());
			response.setGoods(responses);
		}
		else {
			response.setGoods(new ArrayList<>());
		}
		return response;
	}
	
	
	
	public List<SupplyResponse> toResponseList(List<Supply> supplies){
		return supplies.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
	
}
