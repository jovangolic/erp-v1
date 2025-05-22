package com.jovan.erp_v1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.ShelfNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.ShelfRequest;
import com.jovan.erp_v1.response.ShelfResponse;

import jakarta.transaction.Transactional;
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
		shelf.setRowCount(request.rowCount());
		shelf.setCols(request.cols());
		Storage storage = storageRepository.findById(request.storageId())
				.orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + request.storageId()));
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
		shelf.setRowCount(request.rowCount());
		shelf.setCols(request.cols());
		Storage storage = storageRepository.findById(request.storageId())
				.orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + request.storageId()));
		shelf.setStorage(storage);
		if (request.goods() != null && !request.goods().isEmpty()) {
			List<Goods> goodsList = goodsRepository.findAllById(request.goods());
			for (Goods g : goodsList) {
				g.setShelf(shelf); // ✳️ važno
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
		return shelfRepository.existsByRowCountAndStorageId(rows, storageId);
	}

	@Override
	public boolean existsByColsAndStorageId(Integer cols, Long storageId) {
		return shelfRepository.existsByColsAndStorageId(cols, storageId);
	}

	@Override
	public boolean existsByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId) {
		return shelfRepository.existsByRowCountAndColsAndStorageId(rows, cols, storageId);
	}

	@Override
	public List<ShelfResponse> findByStorageId(Long storageId) {
		return shelfRepository.findByStorageId(storageId).stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<ShelfResponse> findByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId) {
		return shelfRepository.findByRowCountAndColsAndStorageId(rows, cols, storageId)
				.map(ShelfResponse::new);
	}

	@Override
	public List<ShelfResponse> findByRowCountAndStorageId(Integer rows, Long storageId) {
		return shelfRepository.findByRowCountAndStorageId(rows, storageId).stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShelfResponse> findByColsAndStorageId(Integer cols, Long storageId) {
		return shelfRepository.findByColsAndStorageId(cols, storageId).stream()
				.map(ShelfResponse::new)
				.collect(Collectors.toList());
	}

}
