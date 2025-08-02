package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.request.ShelfRequest;
import com.jovan.erp_v1.response.ShelfResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class ShelfMapper extends AbstractMapper<ShelfRequest> {
	
	public Shelf toEntity(ShelfRequest request, Storage storage) {
		Objects.requireNonNull(request, "ShelfRequest must not be null");
		Objects.requireNonNull(storage, "Storage must not be null");
		validateIdForCreate(request, ShelfRequest::id);
		Shelf shelf = new Shelf();
		shelf.setId(request.id());
		shelf.setRowCount(request.rowCount());
		shelf.setCols(request.cols());
		shelf.setStorage(storage);
		shelf.setGoods(new ArrayList<>());
		return shelf;
	}
	
	public Shelf toEntityUpdate(Shelf shelf, ShelfRequest request, Storage storage) {
		Objects.requireNonNull(shelf, "Shelf must not be null");
		Objects.requireNonNull(request, "ShelfRequest must not be null");
		Objects.requireNonNull(storage, "Storage must not be null");
		shelf.setRowCount(request.rowCount());
		shelf.setCols(request.cols());
		shelf.setStorage(storage);
		shelf.setGoods(new ArrayList<>());
		return shelf;
	}
	
	public ShelfResponse toResponse(Shelf shelf) {
		Objects.requireNonNull(shelf, "Shelf must not be null");
		return new ShelfResponse(shelf);
	}
	
	public List<ShelfResponse> toResponseList(List<Shelf> shelves){
		if(shelves == null || shelves.isEmpty()) {
			return Collections.emptyList();
		}
		return shelves.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
}
