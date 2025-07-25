package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.ValidationException;

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
	
	public BigDecimal countAvailableCapacity() {
		if(this.capacity == null || this.usedCapacity == null) {
			return BigDecimal.ZERO;
		}
		if(this.usedCapacity.compareTo(this.capacity) > 0) {
			throw new ValidationException("UsedCapacity must not be greater than capacity");
		}
		if(this.capacity.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Capacity must not be negative number");
		}
		BigDecimal total = this.capacity.subtract(this.usedCapacity).max(BigDecimal.ZERO);
		return total;
	}
	
	public boolean hasCapacityFor(BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Amount must be a positive number.");
		}
		return countAvailableCapacity().compareTo(amount) >= 0;
	}
	
	public void allocateCapacity(BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Amount must be a positive number.");
		}
		if (!hasCapacityFor(amount)) {
			throw new ValidationException("Not enough available capacity in the storage.");
		}
		this.usedCapacity = this.usedCapacity.add(amount);
	}
	
	public void releaseCapacity(BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Amount must be a positive number.");
		}
		if (amount.compareTo(this.usedCapacity) > 0) {
			throw new ValidationException("Cannot release more than currently used capacity.");
		}
		this.usedCapacity = this.usedCapacity.subtract(amount);
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
