package com.jovan.erp_v1.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelfResponseWithGoods {

	private Long id;
	private Integer rowCount;
	private Integer cols;
	private Long storageId;
	private List<GoodsResponse> goods;
}
