package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Storage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private String location;

	@Column
	private BigDecimal capacity;

	@Enumerated(EnumType.STRING)
	@Column
	private StorageType type;
	
	@Enumerated(EnumType.STRING)
	private StorageStatus status;
	
	@Column
	private BigDecimal usedCapacity = BigDecimal.ZERO;

	@OneToMany(mappedBy = "storage", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Goods> goods;

	@OneToMany(mappedBy = "storage", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Shelf> shelves;

	@OneToMany(mappedBy = "originStorage")
	private List<Shipment> outgoingShipments;

	@OneToMany(mappedBy = "fromStorage")
	private List<StockTransfer> outgoingTransfers;

	@OneToMany(mappedBy = "toStorage")
	private List<StockTransfer> incomingTransfers;

	@OneToMany(mappedBy = "storage", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Material> materials = new ArrayList<>();

	@OneToMany(mappedBy = "fromStorage")
	private List<MaterialMovement> outgoingMaterialMovements;

	@OneToMany(mappedBy = "toStorage")
	private List<MaterialMovement> incomingMaterialMovements;

	@OneToMany(mappedBy = "localStorage")
	private List<WorkCenter> workCenters;

	public Storage() {
		this.goods = new ArrayList<>();
		this.shelves = new ArrayList<>();
	}

	public void addShelf(Shelf shelf) {
		if (shelves == null) {
			shelves = new ArrayList<>();
		}
		shelves.add(shelf);
		shelf.setStorage(this);
	}

	/*
	 * public void addGoods(Goods good, String scannedBy) {
	 * if(goods == null) {
	 * goods = new ArrayList<>();
	 * }
	 * goods.add(good);
	 * good.setStorage(this);
	 * BarCode barCode = new BarCode();
	 * barCode.setCode(BarCodeGenerator.generate());
	 * barCode.setScannedAt(LocalDateTime.now());
	 * barCode.setScannedBy(scannedBy);
	 * barCode.setGoods(good);
	 * if(good.getBarCodes() == null) {
	 * good.setBarCodes(new ArrayList<BarCode>());
	 * }
	 * good.getBarCodes().add(barCode);
	 * }
	 */

	public void addGoods(Goods good, User scannedBy) {
		if (goods == null) {
			goods = new ArrayList<>();
		}
		goods.add(good);
		good.setStorage(this);
		BarCode barCode = BarCode.builder()
				.code(BarCodeGenerator.generate())
				.scannedAt(LocalDateTime.now())
				.scannedBy(scannedBy)
				.goods(good)
				.build();
		good.addBarCode(barCode);
	}
}
