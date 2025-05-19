package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.ShelfRequest;
import com.jovan.erp_v1.response.ShelfResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShelfMapper {

	private final StorageRepository storageRepository;
	private final GoodsRepository goodsRepository;
	
	public Shelf toEntity(ShelfRequest request) {
		Shelf shelf = new Shelf();
		shelf.setRowCount(request.rowCount());
		shelf.setCols(request.cols());
		Storage storage = storageRepository.findById(request.storageId()).orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + request.storageId()));
		shelf.setStorage(storage);
		if(request.goods() != null && !request.goods().isEmpty()) {
			List<Goods> goods = goodsRepository.findAllById(request.goods());
			shelf.setGoods(goods);
		}
		else {
			shelf.setGoods(new ArrayList<>());
		}
		return shelf;
	}
	
	public ShelfResponse toResponse(Shelf shelf) {
		ShelfResponse response = new ShelfResponse();
		response.setRowCount(shelf.getRowCount());
		response.setCols(shelf.getCols());
		if(shelf.getStorage() != null) {
			response.setStorageId(shelf.getStorage().getId());;
		}
		if(shelf.getGoods() != null&& !shelf.getGoods().isEmpty()) {
			List<Long> goodsId = shelf.getGoods().stream()
					.map(Goods::getId)
					.collect(Collectors.toList());
			response.setGoods(goodsId);
		}
		else {
			response.setGoods(new ArrayList<>());
		}
		return response;
	}
	
	public List<ShelfResponse> toResponseList(List<Shelf> shelves){
		return shelves.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
}
