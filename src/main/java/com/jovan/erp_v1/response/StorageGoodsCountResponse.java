package com.jovan.erp_v1.response;

import com.jovan.erp_v1.dto.StorageGoodsCountDTO;

public record StorageGoodsCountResponse(String storageName, Long goodsCount) {

	public StorageGoodsCountResponse(StorageGoodsCountDTO dto) {
		this(dto.storageName(), dto.goodsCount());
	}
}
