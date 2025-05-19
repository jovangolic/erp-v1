package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.SupplyNotFoundException;
import com.jovan.erp_v1.mapper.SupplyMapper;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.request.SupplyRequest;
import com.jovan.erp_v1.response.SupplyResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplyService implements ISupplyService {

	
	private final SupplyRepository supplyRepository;
	private final SupplyMapper supplyMapper;
	private final StorageRepository storageRepository;
	private final GoodsRepository goodsRepository;
	
	
	@Transactional
	@Override
    public SupplyResponse createSupply(SupplyRequest request) {
        Supply supply = supplyMapper.toEntity(request);
        Storage storage = storageRepository.findById(request.storageId())
            .orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + request.storageId()));
        supply.setStorage(storage);
        if (supply.getUpdates() == null) {
            supply.setUpdates(LocalDateTime.now());
        }
        List<Goods> goodsList = goodsRepository.findAllById(request.goodsIds());
        if (goodsList.size() != request.goodsIds().size()) {
            throw new GoodsNotFoundException("Some Goods not found by given IDs.");
        }
        supply.setGoods(goodsList);
        Supply saved = supplyRepository.save(supply);
        return supplyMapper.toResponse(saved);
    }

	
	@Transactional
    @Override
    public SupplyResponse updateSupply(Long id, SupplyRequest request) {
        Supply existing = supplyRepository.findById(id)
            .orElseThrow(() -> new SupplyNotFoundException("Supply not found with id: " + id));
        existing.setQuantity(request.quantity());
        existing.setUpdates(request.updates());

        Storage storage = storageRepository.findById(request.storageId())
            .orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + request.storageId()));
        existing.setStorage(storage);
        List<Goods> goodsList = goodsRepository.findAllById(request.goodsIds());
        if (goodsList.size() != request.goodsIds().size()) {
            throw new GoodsNotFoundException("Some Goods not found by given IDs.");
        }
        existing.setGoods(goodsList);

        Supply updated = supplyRepository.save(existing);
        return supplyMapper.toResponse(updated);
    }
	
	@Transactional
	@Override
	public void deleteSupply(Long id) {
		if(!supplyRepository.existsById(id)) {
			throw new SupplyNotFoundException("Supply not found with id: "+id);
		}
		supplyRepository.deleteById(id);
	}
	@Override
	public SupplyResponse getBySupplyId(Long supplyId) {
		Supply supply = supplyRepository.findById(supplyId).orElseThrow(() -> new SupplyNotFoundException("Supply with id: "+supplyId+" not found"));
		return supplyMapper.toResponse(supply);
	}
	@Override
	public List<SupplyResponse> getAllSupply() {
		return supplyRepository.findAll().stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<SupplyResponse> getByStorage(Storage storage) {
		return supplyRepository.findByStorage(storage).stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<SupplyResponse> getBySuppliesByGoodsName(String name) {
		return supplyRepository.findSuppliesByGoodsName(name).stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<SupplyResponse> getBySuppliesWithMinQuantity(Integer minQuantity) {
		return supplyRepository.findSuppliesWithMinQuantity(minQuantity).stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<SupplyResponse> getBySuppliesByStorageId(Long storageId) {
		return supplyRepository.findSuppliesByStorageId(storageId).stream()
				.map(supplyMapper::toResponse)
				.collect(Collectors.toList());
	}
}
