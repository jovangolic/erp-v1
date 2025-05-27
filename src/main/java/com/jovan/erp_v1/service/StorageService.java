package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.mapper.StorageMapper;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.StorageRequest;
import com.jovan.erp_v1.response.StorageResponse;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageService implements IStorageService {

	private final StorageRepository storageRepository;
	private final StorageMapper storageMapper;

	@Transactional
	@Override
	public StorageResponse createStorage(StorageRequest request) {
		Storage storage = storageMapper.toEntity(request);
		Storage saved = storageRepository.save(storage);
		return storageMapper.toResponse(saved);
	}

	@Transactional
	@Override
	public StorageResponse updateStorage(Long id, StorageRequest request) {
		Storage storage = storageRepository.findById(id)
				.orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + id));
		storage.setName(request.name());
		storage.setLocation(request.location());
		storage.setCapacity(request.capacity());
		storage.setType(request.type());
		Storage update = storageRepository.save(storage);
		return storageMapper.toResponse(update);
	}

	@Transactional
	@Override
	public void deleteStorage(Long id) {
		if (!storageRepository.existsById(id)) {
			throw new StorageNotFoundException("Storage not found with id: " + id);
		}
		storageRepository.deleteById(id);
	}

	@Override
	public List<StorageResponse> getByStorageType(StorageType type) {
		return storageRepository.findByType(type).stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public StorageResponse getByStorageId(Long id) {
		Storage storage = storageRepository.findById(id)
				.orElseThrow(() -> new StorageNotFoundException("Storage not found wtij id: " + id));
		return storageMapper.toResponse(storage);
	}

	@Override
	public List<StorageResponse> getByName(String name) {
		return storageRepository.findByName(name).stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> getByLocation(String location) {
		return storageRepository.findByLocation(location).stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> getByCapacity(Double capacity) {
		return storageRepository.findByCapacity(capacity).stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> getByNameAndLocation(String name, String location) {
		return storageRepository.findByNameAndLocation(name, location).stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> getByTypeAndCapacityGreaterThan(StorageType type, Double capacity) {
		return storageRepository.findByTypeAndCapacityGreaterThan(type, capacity).stream()
				.map(storageMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<StorageResponse> getStoragesWithMinGoods(int minCount) {
		List<Storage> all = storageRepository.findAll();
		List<Storage> filtered = all.stream()
				.filter(storage -> storage.getGoods().size() >= minCount)
				.toList();
		return storageMapper.toResponseList(filtered);
	}

	@Override
	public List<StorageResponse> getByNameContainingIgnoreCase(String name) {
		List<Storage> storages = storageRepository.findByNameContainingIgnoreCase(name);
		return storageMapper.toResponseList(storages);
	}

}
