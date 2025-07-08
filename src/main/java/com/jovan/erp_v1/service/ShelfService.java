package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.ShelfNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.ShelfRequest;
import com.jovan.erp_v1.response.GoodsResponse;
import com.jovan.erp_v1.response.ShelfResponse;
import com.jovan.erp_v1.response.ShelfResponseWithGoods;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShelfService implements IShelfService {

	private final StorageRepository storageRepository;
	private final ShelfRepository shelfRepository;
	private final GoodsRepository goodsRepository;

	@Transactional
	@Override
	public ShelfResponse createShelf(ShelfRequest request) {
		Shelf shelf = new Shelf();
		validateInteger(request.rowCount());
		validateInteger(request.cols());
		Storage storage = fetchStorageId(request.storageId());
		validateGoodsList(request.goods());
		shelf.setRowCount(request.rowCount());
		shelf.setCols(request.cols());
		shelf.setStorage(storage);
		if (request.goods() != null && !request.goods().isEmpty()) {
			List<Goods> goods = goodsRepository.findAllById(request.goods());
			for (Goods g : goods) {
				g.setShelf(shelf); // ✳️ Bitan korak
			}
			shelf.setGoods(goods);
		}
		Shelf saved = shelfRepository.save(shelf);
		return new ShelfResponse(saved);
	}

	@Transactional
	@Override
	public ShelfResponse updateShelf(Long id, ShelfRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		Shelf shelf = shelfRepository.findById(id)
				.orElseThrow(() -> new ShelfNotFoundException("Shelf not found with id: " + id));
		validateInteger(request.rowCount());
		validateInteger(request.cols());
		Storage storage = fetchStorageId(request.storageId());
		shelf.setRowCount(request.rowCount());
		shelf.setCols(request.cols());
		shelf.setStorage(storage);
		if (request.goods() != null && !request.goods().isEmpty()) {
			List<Goods> goodsList = goodsRepository.findAllById(request.goods());
			for (Goods g : goodsList) {
				g.setShelf(shelf); // važno
			}
			shelf.setGoods(goodsList);
		} else {
			shelf.setGoods(new ArrayList<>());
		}
		Shelf updated = shelfRepository.save(shelf);
		return new ShelfResponse(updated);
	}

	@Transactional
	@Override
	public void deleteShelf(Long id) {
		if (!shelfRepository.existsById(id)) {
			throw new ShelfNotFoundException("Shelf not found with id: " + id);
		}
		shelfRepository.deleteById(id);
	}

	@Override
	public boolean existsByRowCountAndStorageId(Integer rows, Long storageId) {
		validateInteger(rows);
		fetchStorageId(storageId);
		return shelfRepository.existsByRowCountAndStorageId(rows, storageId);
	}

	@Override
	public boolean existsByColsAndStorageId(Integer cols, Long storageId) {
		validateInteger(cols);
		fetchStorageId(storageId);
		return shelfRepository.existsByColsAndStorageId(cols, storageId);
	}

	@Override
	public boolean existsByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId) {
		validateInteger(cols);
		validateInteger(rows);
		fetchStorageId(storageId);
		return shelfRepository.existsByRowCountAndColsAndStorageId(rows, cols, storageId);
	}

	@Override
	public List<ShelfResponse> findByStorageId(Long storageId) {
		fetchStorageId(storageId);
		return shelfRepository.findByStorageId(storageId).stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<ShelfResponse> findByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId) {
		validateInteger(cols);
		validateInteger(rows);
		fetchStorageId(storageId);
		return shelfRepository.findByRowCountAndColsAndStorageId(rows, cols, storageId)
				.map(ShelfResponse::new);
	}

	@Override
	public List<ShelfResponse> findByRowCountAndStorageId(Integer rows, Long storageId) {
		fetchStorageId(storageId);
		validateInteger(rows);
		return shelfRepository.findByRowCountAndStorageId(rows, storageId).stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByColsAndStorageId(Integer cols, Long storageId) {
		fetchStorageId(storageId);
		validateInteger(cols);
		return shelfRepository.findByColsAndStorageId(cols, storageId).stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public ShelfResponseWithGoods getShelfWithGoods(Long shelfId) {
		Shelf shelf = shelfRepository.findById(shelfId)
				.orElseThrow(() -> new ShelfNotFoundException("Shelf not found with id: " + shelfId));
		List<GoodsResponse> goodsResponses = shelf.getGoods().stream()
				.map(GoodsResponse::new) // koristiš tvoj postojeći konstruktor
				.collect(Collectors.toList());
		return new ShelfResponseWithGoods(
				shelf.getId(),
				shelf.getRowCount(),
				shelf.getCols(),
				shelf.getStorage().getId(),
				goodsResponses);
	}

	@Override
	public ShelfResponse findOne(Long id) {
		Shelf shelf = shelfRepository.findById(id)
				.orElseThrow(() -> new ShelfNotFoundException("Shelf not found with id: " + id));
		return new ShelfResponse(shelf);
	}

	@Override
	public List<ShelfResponse> findAll() {
		return shelfRepository.findAll().stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_Name(String name) {
		validateString(name);
		return shelfRepository.findByStorage_Name(name).stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_Location(String location) {
		validateString(location);
		return shelfRepository.findByStorage_Location(location).stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_Type(StorageType type) {
		validateStorageType(type);
		return shelfRepository.findByStorage_Type(type).stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_CapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		return shelfRepository.findByStorage_CapacityGreaterThan(capacity).stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByStorage_NameAndStorage_Type(String name, StorageType type) {
		validateString(name);
		validateStorageType(type);
		return shelfRepository.findByStorage_NameAndStorage_Type(name, type).stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}
	
	private void validateGoodsList(List<Long> goods) {
		if(goods == null || goods.isEmpty()) {
			throw new IllegalArgumentException("Goods list must not be empty nor null");
		}
		for(Long goodId: goods) {
			if(goodId == null || goodId <= 0) {
				throw new IllegalArgumentException("Each good ID must be a positive non-null number");
			}
		}
	}
	
	private void validateInteger(Integer num) {
		if(num == null || num <= 0) {
			throw new IllegalArgumentException("Number must be positive");
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new IllegalArgumentException("String must not be null nor empty");
		}
	}
	
	private void validateBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Number must be positive");
		}
	}
	
	private void validateStorageType(StorageType type) {
		if(type == null) {
			throw new StorageNotFoundException("StorageType type must not be null");
		}
	}
	
	private Storage fetchStorageId(Long storageId) {
		if(storageId == null) {
			throw new StorageNotFoundException("Storage ID must not be null");
		}
		return storageRepository.findById(storageId).orElseThrow(() -> new StorageNotFoundException("Storage not found with id "+storageId));
	}

}
