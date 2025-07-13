package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.Storage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageResponse {

	private Long id;
	private String name;
	private String location;
	private BigDecimal capacity;
	private StorageType type;
	private List<GoodsResponse> goods;
	private List<ShelfResponse> shelves;
	private List<ShipmentResponse> outgoingShipments;
	private List<StockTransferResponse> outgoingTransfers;
	private List<StockTransferResponse> incomingTransfers;
	private List<MaterialResponse> materialResponses;
	private List<WorkCenterResponse> workCenterResponses;
	private List<MaterialMovementResponse> outgoingMaterialMovements;
	private List<MaterialMovementResponse> incomingMaterialMovements;
	private StorageStatus status;

	public StorageResponse(Storage storage) {
		this.id = storage.getId();
		this.name = storage.getName();
		this.location = storage.getLocation();
		this.capacity = storage.getCapacity();
		this.type = storage.getType();
		this.goods = storage.getGoods()
				.stream()
				.map(GoodsResponse::new)
				.collect(Collectors.toList());
		this.shelves = storage.getShelves()
				.stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
		this.outgoingShipments = storage.getOutgoingShipments()
				.stream()
				.map(ShipmentResponse::new)
				.collect(Collectors.toList());

		this.outgoingTransfers = storage.getOutgoingTransfers()
				.stream()
				.map(StockTransferResponse::new)
				.collect(Collectors.toList());

		this.incomingTransfers = storage.getIncomingTransfers()
				.stream()
				.map(StockTransferResponse::new)
				.collect(Collectors.toList());
		this.materialResponses = storage.getMaterials().stream()
				.map(MaterialResponse::new)
				.collect(Collectors.toList());
		this.workCenterResponses = storage.getWorkCenters().stream()
				.map(WorkCenterResponse::new)
				.collect(Collectors.toList());
		this.outgoingMaterialMovements = storage.getOutgoingMaterialMovements().stream()
				.map(MaterialMovementResponse::new)
				.collect(Collectors.toList());
		this.incomingMaterialMovements = storage.getIncomingMaterialMovements().stream()
				.map(MaterialMovementResponse::new)
				.collect(Collectors.toList());		this.status = storage.getStatus();
	}
}
