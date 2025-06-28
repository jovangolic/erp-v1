package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.model.Supply;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplyResponse {

	private Long id;
	private String storageName;
	private String storageLocation;
	private BigDecimal storageCapacity;
	private BigDecimal quantity;
	private LocalDateTime updates;
	private List<GoodsResponse> goods; // ako je potrebno samo odkomentarisi

	public SupplyResponse(Supply supply) {
		this.id = supply.getId();
		this.storageName = supply.getStorage().getName();
		this.storageLocation = supply.getStorage().getLocation();
		this.storageCapacity = supply.getStorage().getCapacity();
		this.quantity = supply.getQuantity();
		this.updates = supply.getUpdates();
		this.goods = supply.getGoods()
				.stream()
				.map(GoodsResponse::new)
				.collect(Collectors.toList());
	}

}
