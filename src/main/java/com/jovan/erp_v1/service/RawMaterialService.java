package com.jovan.erp_v1.service;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.RawMaterialNotFoundException;
import com.jovan.erp_v1.exception.ShelfNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.SupplyNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.RawMaterialMapper;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.RawMaterial;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.BarCodeRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.RawMaterialRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.specification.RawMaterialSpecification;
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.request.RawMaterialRequest;
import com.jovan.erp_v1.response.RawMaterialResponse;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RawMaterialService implements IRawMaterialService {

	private final RawMaterialRepository rawMaterialRepository;
	private final RawMaterialMapper rawMaterialMapper;
	private final StorageRepository storageRepository;
	private final SupplyRepository supplyRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final BarCodeRepository barCodeRepository;
	private final GoodsRepository goodsRepository;
	private final ShelfRepository shelfRepository;

	@Override
	public List<RawMaterialResponse> findAll() {
		List<RawMaterial> items = rawMaterialRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("RawMaterial list is empty");
		}
		return items.stream().map(rawMaterialMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public RawMaterialResponse findById(Long id) {
		RawMaterial material = rawMaterialRepository.findById(id)
				.orElseThrow(() -> new RawMaterialNotFoundException("RawMaterial not found"));
		return rawMaterialMapper.toResponse(material);
	}

	@Transactional
	@Override
	public RawMaterialResponse save(RawMaterialRequest request) {
		validateRawMaterialCreateRequest(request);
		Product product = fetchProduct(request.productId());
		Storage storage = fetchStorage(request.storageId());
		Supply supply = fetchSupply(request.supplyId());
		Shelf shelf = null;
    	if (isShelfRequired(request.goodsType())) {
    	    if (request.shelfId() == null) throw new ValidationException("Shelf is required for this goods type.");
    	    shelf = fetchShelfId(request.shelfId());
    	} else if (request.shelfId() != null) {
    	    throw new ValidationException("Shelf should not be set for this goods type.");
    	}
		RawMaterial material = rawMaterialMapper.toEntity(request,product,storage,supply,shelf);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<BarCode> barCodes = request.barCodes().stream()
        		.map(barCodeRequest -> {
        	        BarCode barCode = new BarCode();
        	        barCode.setCode(barCodeRequest.code());
        	        barCode.setGoods(product);
        	        barCode.setScannedBy(currentUser);
        	        return barCode;
        	    })
        	    .toList();
         material.setBarCodes(barCodes);
		return rawMaterialMapper.toResponse(rawMaterialRepository.save(material));
	}

	@Transactional
	@Override
	public RawMaterialResponse update(Long id, RawMaterialRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		RawMaterial existing = rawMaterialRepository.findById(id)
				.orElseThrow(() -> new RawMaterialNotFoundException("RawMaterial not found with id: " + id));
		validateRawMaterialUpdateRequest(request);
		Product product = fetchProduct(request.productId());
		if(request.productId() != null && (product.getId() == null || !request.productId().equals(product.getId()))) {
			product = fetchProduct(request.productId());
		}
		Storage storage = existing.getStorage();
        if(request.storageId() != null && (storage.getId() == null || !request.storageId().equals(storage.getId()))) {
        	storage = fetchStorage(request.storageId());
        }
        Supply supply = existing.getSupply();
        if(request.supplyId() != null && (supply.getId() == null || !request.supplyId().equals(supply.getId()))) {
        	supply = fetchSupply(request.supplyId());
        }
        Shelf shelf = null;
    	if (isShelfRequired(request.goodsType())) {
    	    if (request.shelfId() == null) throw new ValidationException("Shelf is required for this goods type.");
    	    shelf = fetchShelfId(request.shelfId());
    	} else if (request.shelfId() != null) {
    	    throw new ValidationException("Shelf should not be set for this goods type.");
    	}
		rawMaterialMapper.toEntityUpdate(existing, request, product, storage, supply, shelf);
		updateBarCodes(product, request.barCodes(), userRepository);
		RawMaterial updated = rawMaterialRepository.save(existing);
		return rawMaterialMapper.toResponse(updated);
	}
	
	private void updateBarCodes(Product product, List<BarCodeRequest> barCodeRequests, UserRepository userRepository) {
        Map<Long, BarCode> existingBarCodesById = product.getBarCodes().stream()
                .filter(bc -> bc.getId() != null)
                .collect(Collectors.toMap(BarCode::getId, Function.identity()));
        List<BarCode> updatedBarCodes = new ArrayList<>();
        for (BarCodeRequest bcRequest : barCodeRequests) {
            User scannedBy = null;
            if (bcRequest.scannedById() != null) {
                scannedBy = userRepository.findById(bcRequest.scannedById())
                        .orElseThrow(() -> new IllegalArgumentException("Korisnik sa ID " + bcRequest.scannedById() + " nije pronađen."));
            }
            if (bcRequest.id() != null && existingBarCodesById.containsKey(bcRequest.id())) {
                BarCode existing = existingBarCodesById.get(bcRequest.id());
                existing.setCode(bcRequest.code());
                existing.setScannedAt(bcRequest.scannedAt());
                existing.setScannedBy(scannedBy);
                updatedBarCodes.add(existing);
                existingBarCodesById.remove(bcRequest.id());
            } else {
                // Novi barCode
                BarCode newBarCode = BarCode.builder()
                        .code(bcRequest.code())
                        .scannedAt(bcRequest.scannedAt())
                        .scannedBy(scannedBy)
                        .goods(product)
                        .build();
                updatedBarCodes.add(newBarCode);
            }
        }
        product.getBarCodes().clear();
        product.getBarCodes().addAll(updatedBarCodes);
    }

	@Transactional
	@Override
	public void delete(Long id) {
		if (!rawMaterialRepository.existsById(id)) {
			throw new RawMaterialNotFoundException("RawMaterial not found");
		}
		rawMaterialRepository.deleteById(id);
	}

	@Override
	public List<RawMaterialResponse> findByName(String name) {
		validateString(name);
		List<RawMaterial> items = rawMaterialRepository.findByName(name);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial name %s is found", name);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterial> filterRawMaterial(Long shelfId, BigDecimal minQty, BigDecimal maxQty, Long productId) {
		Specification<RawMaterial> spec = Specification
				.where(RawMaterialSpecification.hasShelfId(shelfId))
				.and(RawMaterialSpecification.quantityGreaterThan(minQty))
				.and(RawMaterialSpecification.quantityLessThan(maxQty))
				.and(RawMaterialSpecification.productIdEquals(productId));
		return rawMaterialRepository.findAll(spec);
	}

	@Override
	public List<RawMaterialResponse> findByCurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<RawMaterial> items = rawMaterialRepository.findByCurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for current-quantity %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByCurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<RawMaterial> items = rawMaterialRepository.findByCurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for current-qunatity less than %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByCurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<RawMaterial> items = rawMaterialRepository.findByCurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for current-quantity greater than %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<RawMaterial> items = rawMaterialRepository.findByProduct_CurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for product current-quantity %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<RawMaterial> items = rawMaterialRepository.findByProduct_CurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for product current-quantity greater than %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<RawMaterial> items = rawMaterialRepository.findByProduct_CurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg =  String.format("No RawMaterial for product current-quantity less than %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByShelf_Id(Long shelfId) {
		fetchShelfId(shelfId);
		List<RawMaterial> items = rawMaterialRepository.findByShelf_Id(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for shelf-id %d is found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public Long countByShelf_RowCount(Integer rowCount) {
		validateShelfRowCount(rowCount);
		return rawMaterialRepository.countByShelf_Cols(rowCount);
	}

	@Override
	public Long countByShelf_Cols(Integer cols) {
		validateShelfCols(cols);
		return rawMaterialRepository.countByShelf_Cols(cols);
	}

	@Override
	public List<RawMaterialResponse> findByShelfAndQuantityGreaterThan(Long shelfId, BigDecimal quantity) {
		fetchShelfId(shelfId);
		validateBigDecimal(quantity);
		List<RawMaterial> items = rawMaterialRepository.findByShelfAndQuantityGreaterThan(shelfId, quantity);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for shelf-id %d and quantity greater than %s is found", shelfId,quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByShelfAndQuantityLessThan(Long shelfId, BigDecimal quantity) {
		fetchShelfId(shelfId);
		validateBigDecimalNonNegative(quantity);
		List<RawMaterial> items = rawMaterialRepository.findByShelfAndQuantityLessThan(shelfId, quantity);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for shelf-id %d and quantity less than %s is found", shelfId,quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByShelfAndExactQuantity(Long shelfId, BigDecimal quantity) {
		fetchShelfId(shelfId);
		validateBigDecimal(quantity);
		List<RawMaterial> items = rawMaterialRepository.findByShelfAndExactQuantity(shelfId, quantity);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for shelf-id %d and exact-quantity %s is found", shelfId,quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByShelf_IdAndCurrentQuantityGreaterThan(Long shelfId, BigDecimal quantity) {
		fetchShelfId(shelfId);
		validateBigDecimal(quantity);
		List<RawMaterial> items = rawMaterialRepository.findByShelf_IdAndCurrentQuantityGreaterThan(shelfId, quantity);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for shelf-id %d and quantity greater than %s is found", shelfId,quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByShelf_IdAndCurrentQuantityLessThan(Long shelfId, BigDecimal quantity) {
		fetchShelfId(shelfId);
		validateBigDecimalNonNegative(quantity);
		List<RawMaterial> items = rawMaterialRepository.findByShelf_IdAndCurrentQuantityLessThan(shelfId, quantity);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for shelf-id %d and quantity less than %s is found",
					shelfId,quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findBySupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<RawMaterial> items = rawMaterialRepository.findBysupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for supplier-type %s is found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByStorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<RawMaterial> items = rawMaterialRepository.findByStorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for storage-type %s is found", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByGoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<RawMaterial> items = rawMaterialRepository.findByGoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for goods-type %s is found", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByUnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<RawMaterial> items = rawMaterialRepository.findByUnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No RawMaterial for unit-measure %s is found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	private void validateRawMaterialCreateRequest(RawMaterialRequest request) {
		if (request == null) {
			throw new RawMaterialNotFoundException("RawMaterialRequest must not be null");
		}
		validateString(request.name());
		validateUnitMeasure(request.unitMeasure());
		validateSupplierType(request.supplierType());
		validateStorageType(request.storageType());
		validateGoodsType(request.goodsType());
		validateBigDecimal(request.currentQuantity());
		validateBarCodes(request.barCodes());
	}

	private void validateRawMaterialUpdateRequest(RawMaterialRequest request) {
		if (request == null) {
			throw new RawMaterialNotFoundException("RawMaterialRequest must not ne bull");
		}
		validateString(request.name());
		validateUnitMeasure(request.unitMeasure());
		validateSupplierType(request.supplierType());
		validateStorageType(request.storageType());
		validateGoodsType(request.goodsType());
		validateBigDecimal(request.currentQuantity());
		validateBarCodes(request.barCodes());
	}

	private void validateShelfRowCount(Integer rowCount) {
		if (rowCount == null || rowCount < 0) {
			throw new ValidationException("Row-count must not be null or negative");
		}
		if (!rawMaterialRepository.existsByShelf_RowCount(rowCount)) {
			throw new ValidationException("Row-count doesn't exists");
		}
	}

	private void validateShelfCols(Integer cols) {
		if (cols == null || cols < 0) {
			throw new ValidationException("Cols must not be null or negative");
		}
		if (!rawMaterialRepository.existsByShelf_Cols(cols)) {
			throw new ValidationException("Cols doesn't exists");
		}
	}

	private Shelf fetchShelfId(Long shelfId) {
		if (shelfId == null) {
			throw new ShelfNotFoundException("Shelf ID must not be null");
		}
		return shelfRepository.findById(shelfId)
				.orElseThrow(() -> new ShelfNotFoundException("Shelf not found with id " + shelfId));
	}

	private void validateUnitMeasure(UnitMeasure unitMeasure) {
		if (unitMeasure == null) {
			throw new ValidationException("UnitMeasure unitMeasure must not be null");
		}
	}

	private void validateSupplierType(SupplierType supplierType) {
		if (supplierType == null) {
			throw new ValidationException("SupplierType supplierType must not be null");
		}
	}

	private void validateStorageType(StorageType storageType) {
		if (storageType == null) {
			throw new ValidationException("StorageType storageType must not be null");
		}
	}

	private void validateGoodsType(GoodsType goodsType) {
		if (goodsType == null) {
			throw new ValidationException("GoodsType goodsType must not be null");
		}
	}

	private void validateString(String str) {
		if (str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}

	private Storage fetchStorage(Long storageId) {
		if (storageId == null) {
			throw new StorageNotFoundException("Storage ID must not be null");
		}
		return storageRepository.findById(storageId)
				.orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + storageId));
	}

	private Supply fetchSupply(Long supplyId) {
		if (supplyId == null) {
			throw new SupplyNotFoundException("Supply ID must not be null");
		}
		return supplyRepository.findById(supplyId)
				.orElseThrow(() -> new SupplyNotFoundException("Supply not found with id " + supplyId));
	}

	private Product fetchProduct(Long productId) {
		if (productId == null) {
			throw new ProductNotFoundException("Product ID must not be null");
		}
		return productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with id " + productId));
	}

	private void validateBigDecimal(BigDecimal currentQuantity) {
		if (currentQuantity == null || currentQuantity.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Current Quantity must not be null nor negative");
		}
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}

	private void validateBarCodes(List<BarCodeRequest> barCodes) {
		if (barCodes == null || barCodes.isEmpty()) {
			throw new ValidationException("BarCodes must not be null nor empty");
		}
		for (BarCodeRequest bc : barCodes) {
			if(bc.id() == null) {
				throw new ValidationException("BarCodeRequest must not be null");
			}
			validateBarCode(bc);
		}
	}

	private void validateBarCode(BarCodeRequest bc) {
		if (bc.code() == null || bc.code().isBlank()) {
			throw new ValidationException("Code must not be null or blank");
		}
		if (barCodeRepository.existsByCode(bc.code())) {
			throw new ValidationException("Bar-code with code '" + bc.code() + "' already exists");
		}
		if (bc.goodsId() == null) {
			throw new ValidationException("Goods Id must not be null");
		}
		if (!goodsRepository.existsById(bc.goodsId())) {
			throw new ValidationException("Goods with ID " + bc.goodsId() + " doesn't exist");
		}
		if (bc.scannedById() == null) {
			throw new ValidationException("BarCode must have a scanner (user ID)");
		}
		if (!userRepository.existsById(bc.scannedById())) {
			throw new ValidationException("User with ID " + bc.scannedById() + " doesn't exist");
		}
	}
	
	private boolean isShelfRequired(GoodsType type) {
	    return switch (type) {
	        case FINISHED_PRODUCT, PALLETIZED_GOODS, SEMI_FINISHED_PRODUCT -> true;
	        case BULK_GOODS, CONSTRUCTION_MATERIAL, RAW_MATERIAL, WRITE_OFS -> false;
	    };
	}
}
