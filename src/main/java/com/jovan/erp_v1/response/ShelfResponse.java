package com.jovan.erp_v1.response;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Shelf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelfResponse {

	private Long id;
	private Integer rowCount;
	private Integer cols;
	private Long storageId;
	private List<Long> goods;
	
	public ShelfResponse(Shelf shelf) {
        this.id = shelf.getId();
        this.rowCount = shelf.getRowCount();
        this.cols = shelf.getCols();
        this.storageId = shelf.getStorage() != null ? shelf.getStorage().getId() : null;
        this.goods = shelf.getGoods() != null
            ? shelf.getGoods().stream().map(Goods::getId).collect(Collectors.toList())
            : new ArrayList<>();
    }
}
