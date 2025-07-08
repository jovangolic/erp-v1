package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.RawMaterialNotFoundException;
import com.jovan.erp_v1.exception.ShelfNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.SupplyNotFoundException;
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
		return rawMaterialMapper.toResponseList(rawMaterialRepository.findAll());
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
		RawMaterial material = rawMaterialMapper.toEntity(request);
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
		// Ažuriranje osnovnih podataka
		existing.setName(request.name());
		existing.setUnitMeasure(request.unitMeasure());
		existing.setSupplierType(request.supplierType());
		existing.setStorageType(request.storageType());
		existing.setGoodsType(request.goodsType());
		existing.setCurrentQuantity(request.currentQuantity());
		// Ažuriranje skladišta
		Storage storage = fetchStorage(request.storageId());
		existing.setStorage(storage);
		// Skladište
		existing.setStorage(fetchStorage(request.storageId()));
		// Supply
		existing.setSupply(
				request.supplyId() != null ? fetchSupply(request.supplyId()) : null);
		// Proizvod
		existing.setProduct(
				request.productId() != null ? fetchProduct(request.productId()) : null);
		// Ažuriranje bar kodova
		existing.getBarCodes().clear(); // zbog `orphanRemoval = true`
		List<BarCode> barCodes = request.barCodes().stream()
				.map((Function<BarCodeRequest, BarCode>) barCodeReq -> {
					User scannedBy = null;
					if (barCodeReq.scannedById() != null) {
						scannedBy = userRepository.findById(barCodeReq.scannedById())
								.orElseThrow(() -> new IllegalArgumentException(
										"Korisnik sa ID " + barCodeReq.scannedById() + " nije pronađen."));
					}
					return BarCode.builder()
							.code(barCodeReq.code())
							.scannedAt(barCodeReq.scannedAt() != null ? barCodeReq.scannedAt() : LocalDateTime.now())
							.scannedBy(scannedBy)
							.goods(existing)
							.build();
				})
				.collect(Collectors.toList());// koristi collect da bude jasnije
		existing.getBarCodes().addAll(barCodes);
		RawMaterial updated = rawMaterialRepository.save(existing);
		return rawMaterialMapper.toResponse(updated);
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
		return rawMaterialMapper.toResponseList(
				rawMaterialRepository.findByNameContainingIgnoreCase(name));
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
		return rawMaterialRepository.findByCurrentQuantity(currentQuantity).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByCurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		return rawMaterialRepository.findByCurrentQuantityLessThan(currentQuantity).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByCurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		return rawMaterialRepository.findByCurrentQuantityGreaterThan(currentQuantity).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		return rawMaterialRepository.findByProduct_CurrentQuantity(currentQuantity).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		return rawMaterialRepository.findByProduct_CurrentQuantityGreaterThan(currentQuantity).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		return rawMaterialRepository.findByProduct_CurrentQuantityLessThan(currentQuantity).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByShelf_Id(Long shelfId) {
		fetchShelfId(shelfId);
		return rawMaterialRepository.findByShelf_Id(shelfId).stream()
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
		return rawMaterialRepository.findByShelfAndQuantityGreaterThan(shelfId, quantity).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByShelfAndQuantityLessThan(Long shelfId, BigDecimal quantity) {
		fetchShelfId(shelfId);
		validateBigDecimal(quantity);
		return rawMaterialRepository.findByShelfAndQuantityLessThan(shelfId, quantity).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByShelfAndExactQuantity(Long shelfId, BigDecimal quantity) {
		fetchShelfId(shelfId);
		validateBigDecimal(quantity);
		return rawMaterialRepository.findByShelfAndExactQuantity(shelfId, quantity).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByShelf_IdAndCurrentQuantityGreaterThan(Long shelfId, BigDecimal quantity) {
		fetchShelfId(shelfId);
		validateBigDecimal(quantity);
		return rawMaterialRepository.findByShelf_IdAndCurrentQuantityGreaterThan(shelfId, quantity).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByShelf_IdAndCurrentQuantityLessThan(Long shelfId, BigDecimal quantity) {
		fetchShelfId(shelfId);
		validateBigDecimal(quantity);
		return rawMaterialRepository.findByShelf_IdAndCurrentQuantityLessThan(shelfId, quantity).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findBySupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		return rawMaterialRepository.findBysupplierType(supplierType).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByStorageType(StorageType storageType) {
		validateStorageType(storageType);
		return rawMaterialRepository.findByStorageType(storageType).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByGoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		return rawMaterialRepository.findByGoodsType(goodsType).stream()
				.map(RawMaterialResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<RawMaterialResponse> findByUnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		return rawMaterialRepository.findByUnitMeasure(unitMeasure).stream()
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
		if (request.storageId() == null) {
			throw new StorageNotFoundException("Storage ID must not be null");
		}
		if (request.productId() == null) {
			throw new ProductNotFoundException("Product ID must not be null");
		}
		if (request.supplyId() == null) {
			throw new SupplyNotFoundException("Supply ID must not be null");
		}
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
		if (request.storageId() == null) {
			throw new StorageNotFoundException("Storage ID must not be null");
		}
		if (request.productId() == null) {
			throw new ProductNotFoundException("Product ID must not be null");
		}
		if (request.supplyId() == null) {
			throw new SupplyNotFoundException("Supply ID must not be null");
		}
	}

	private void validateShelfRowCount(Integer rowCount) {
		if (rowCount == null || rowCount < 0) {
			throw new IllegalArgumentException("Row-count must not be null or negative");
		}
		if (!rawMaterialRepository.existsByShelf_RowCount(rowCount)) {
			throw new IllegalArgumentException("Row-count doesn't exists");
		}
	}

	private void validateShelfCols(Integer cols) {
		if (cols == null || cols < 0) {
			throw new IllegalArgumentException("Cols must not be null or negative");
		}
		if (!rawMaterialRepository.existsByShelf_Cols(cols)) {
			throw new IllegalArgumentException("Cols doesn't exists");
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
			throw new IllegalArgumentException("UnitMeasure unitMeasure must not be null");
		}
	}

	private void validateSupplierType(SupplierType supplierType) {
		if (supplierType == null) {
			throw new IllegalArgumentException("SupplierType supplierType must not be null");
		}
	}

	private void validateStorageType(StorageType storageType) {
		if (storageType == null) {
			throw new IllegalArgumentException("StorageType storageType must not be null");
		}
	}

	private void validateGoodsType(GoodsType goodsType) {
		if (goodsType == null) {
			throw new IllegalArgumentException("GoodsType goodsType must not be null");
		}
	}

	private void validateString(String str) {
		if (str == null || str.trim().isEmpty()) {
			throw new IllegalArgumentException("String must not be null nor empty");
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
			throw new IllegalArgumentException("Current Quantity must not be null nor negative");
		}
	}

	private void validateBarCodes(List<BarCodeRequest> barCodes) {
		if (barCodes == null || barCodes.isEmpty()) {
			throw new IllegalArgumentException("BarCodes must not be null nor empty");
		}
		for (BarCodeRequest bc : barCodes) {
			validateBarCode(bc);
		}
	}

	private void validateBarCode(BarCodeRequest bc) {
		if (bc.code() == null || bc.code().isBlank()) {
			throw new IllegalArgumentException("Code must not be null or blank");
		}
		if (barCodeRepository.existsByCode(bc.code())) {
			throw new IllegalArgumentException("Bar-code with code '" + bc.code() + "' already exists");
		}
		if (bc.goodsId() == null) {
			throw new IllegalArgumentException("Goods Id must not be null");
		}
		if (!goodsRepository.existsById(bc.goodsId())) {
			throw new IllegalArgumentException("Goods with ID " + bc.goodsId() + " doesn't exist");
		}
		if (bc.scannedById() == null) {
			throw new IllegalArgumentException("BarCode must have a scanner (user ID)");
		}
		if (!userRepository.existsById(bc.scannedById())) {
			throw new IllegalArgumentException("User with ID " + bc.scannedById() + " doesn't exist");
		}
	}

}
