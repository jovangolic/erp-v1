package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.ProductMapper;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.BarCodeRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.request.ProductRequest;
import com.jovan.erp_v1.response.ProductResponse;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final StorageRepository storageRepository;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private final BarCodeRepository barCodeRepository;
    private final GoodsRepository goodsRepository;
    private final SupplyRepository supplyRepository;
    private final ShelfRepository shelfRepository;

    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest request) {
    	Storage storage = fetchStorage(request.storageId());
    	Supply supply = fetchSupplyId(request.supplyId());
    	Shelf shelf = null;
    	if (isShelfRequired(request.goodsType())) {
    	    if (request.shelfId() == null) throw new ValidationException("Shelf is required for this goods type.");
    	    shelf = fetchShelfId(request.shelfId());
    	} else if (request.shelfId() != null) {
    	    throw new ValidationException("Shelf should not be set for this goods type.");
    	}
    	validateProductRequest(request);
        Product product = productMapper.toEntity(request,storage,supply,shelf);
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
        product.setBarCodes(barCodes);
        Product saved = productRepository.save(product);
        return productMapper.toProductResponse(saved);
    }

    @Transactional
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        validateProductRequest(request);
        Storage storage = product.getStorage();
        if(request.storageId() != null && (storage.getId() == null || !request.storageId().equals(storage.getId()))) {
        	storage = fetchStorage(request.storageId());
        }
        Supply supply = product.getSupply();
        if(request.supplyId() != null && (supply.getId() == null || !request.supplyId().equals(supply.getId()))) {
        	supply = fetchSupplyId(request.supplyId());
        }
        Shelf shelf = null;
    	if (isShelfRequired(request.goodsType())) {
    	    if (request.shelfId() == null) throw new ValidationException("Shelf is required for this goods type.");
    	    shelf = fetchShelfId(request.shelfId());
    	} else if (request.shelfId() != null) {
    	    throw new ValidationException("Shelf should not be set for this goods type.");
    	}
        productMapper.toEntityUpdate(product, request, storage, supply, shelf);
        updateBarCodes(product, request.barCodes(), userRepository);
        Product updated = productRepository.save(product);
        return productMapper.toProductResponse(updated);
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = fetchProduct(id);
        return productMapper.toProductResponse(product);
    }

    @Override
    public List<ProductResponse> findAll() {
        return productMapper.toProductResponseList(productRepository.findAll());
    }

    @Override
    public ProductResponse findByBarCode(String code) {
        Product product = productRepository.findByBarCodes(code)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with barCode: " + code));
        if (product == null) {
            throw new ProductNotFoundException("Product not found with bar code: " + code);
        }
        return productMapper.toProductResponse(product);
    }

    @Override
    public List<ProductResponse> findByCurrentQuantityLessThan(BigDecimal quantity) {
    	validateBigDecimalNonNegative(quantity);
        List<Product> products = productRepository.findByCurrentQuantityLessThan(quantity);
        if(products.isEmpty()) {
        	String msg = String.format("No Product for quantity less than %s is found", quantity);
        	throw new NoDataFoundException(msg);
        }
        return productMapper.toProductResponseList(products);
    }

    @Override
    public List<ProductResponse> findByName(String name) {
    	validateString(name);
    	List<Product> items = productRepository.findByName(name);
    	if(items.isEmpty()) {
    		String msg = String.format("No Product for given name %s is found", name);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream().map(productMapper::toProductResponse).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> findByStorageId(Long storageId) {
        Storage storage = fetchStorage(storageId);
        List<Product> products = productRepository.findByStorage(storage);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found for storage id: " + storageId);
        }
        return productMapper.toProductResponseList(products);
    }

    @Override
    public List<ProductResponse> findBySupplierType(SupplierType supplierType) {
    	validateSupplierType(supplierType);
    	List<Product> items = productRepository.findBySupplierType(supplierType);
    	if(items.isEmpty()) {
    		String msg = String.format("No Product for supplier-type %s is found", supplierType);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> findByStorageType(StorageType storageType) {
    	validateStorageType(storageType);
    	List<Product> items = productRepository.findByStorageType(storageType);
    	if(items.isEmpty()) {
    		String msg = String.format("No Product for storage-type %s is found", storageType);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> findByGoodsType(GoodsType goodsType) {
    	validateGoodsType(goodsType);
    	List<Product> items = productRepository.findByGoodsType(goodsType);
    	if(items.isEmpty()) {
    		String msg = String.format("No Product for goods-type %s is found", goodsType);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
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

	@Override
	public List<ProductResponse> findByUnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<Product> items = productRepository.findByUnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No Product for unit measure %s is found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findByShelfRowColAndStorage(Integer row, Integer col, Long storageId) {
		fetchStorage(storageId);
		validateShelfRowCount(row);
		validateShelfCols(col);
		List<Product> items = productRepository.findByShelfRowColAndStorage(row, col, storageId);
		if(items.isEmpty()) {
			String msg = String.format("No Product for shelf row %d, shelf col %d and storage-id %d is found", 
					row,col,storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findByShelfRow(Integer row) {
		validateShelfRowCount(row);
		List<Product> items = productRepository.findByShelfRow(row);
		if(items.isEmpty()) {
			String msg = String.format("No Product for shelf row %d is found", row);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findByShelfColumn(Integer col) {
		validateShelfCols(col);
		List<Product> items = productRepository.findByShelfColumn(col);
		if(items.isEmpty()) {
			String msg = String.format("No Product for shelg column %d is found", col);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findBySupplyMinQuantity(BigDecimal quantity) {
		validateBigDecimalNonNegative(quantity);
		List<Product> items = productRepository.findBySupplyMinQuantity(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No Product for supply min-quantity %s is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findBySupplyUpdateRange(LocalDateTime from, LocalDateTime to) {
		DateValidator.validateRange(from, to);
		List<Product> items = productRepository.findBySupplyUpdateRange(from, to);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Product for supply date between %s and %s is found", 
					from.format(formatter), to.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findBySupplyStorageId(Long storageId) {
		fetchStorage(storageId);
		List<Product> items = productRepository.findBySupplyStorageId(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No Product for supply storage-id %d is found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public Long countByShelfRowCount(Integer rowCount) {
		validateShelfRowCount(rowCount);
		return productRepository.countByShelfRowCount(rowCount);
	}

	@Override
	public Long countByShelfCols(Integer cols) {
		validateShelfCols(cols);
		return productRepository.countByShelfCols(cols);
	}
	
	private void validateProductRequest(ProductRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("ProductRequest must not be null");
		}
		validateString(request.name());
		validateUnitMeasure(request.unitMeasure());
		validateSupplierType(request.supplierType());
		validateStorageType(request.storageType());
		validateGoodsType(request.goodsType());
		validateBigDecimal(request.currentQuantity());
		validateBarCodes(request.barCodes());
	}
	
	private void validateUnitMeasure(UnitMeasure unitMeasure) {
		if(unitMeasure == null) {
			throw new IllegalArgumentException("UnitMeasure unitMeasure must not be null");
		}
	}
	
	private void validateSupplierType(SupplierType supplierType) {
		if(supplierType == null) {
			throw new IllegalArgumentException("SupplierType supplierType must not be null");
		}
	}
	
	private void validateStorageType(StorageType storageType) {
		if(storageType == null) {
			throw new IllegalArgumentException("StorageType storageType must not be null");
		}
	}
	
	private void validateGoodsType(GoodsType goodsType) {
		if(goodsType == null) {
			throw new IllegalArgumentException("GoodsType goodsType must not be null");
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new IllegalArgumentException("String must not be null nor empty");
		}
	}
	
	private Storage fetchStorage(Long storageId) {
		if(storageId == null) {
			throw new StorageNotFoundException("Storage ID must not be null");
		}
		return storageRepository.findById(storageId).orElseThrow(() -> new StorageNotFoundException("Storage not found with id: "+storageId));
	}
	
	private Product fetchProduct(Long productId) {
		if(productId == null) {
			throw new ProductNotFoundException("Product ID must not be null");
		}
		return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with id "+productId));
	}
	
	private void validateBigDecimal(BigDecimal currentQuantity) {
		if(currentQuantity == null || currentQuantity.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Current Quantity must not be null nor negative");
		}
	}
	
	private void validateShelfRowCount(Integer rowCount) {
		if(rowCount == null || rowCount < 0) {
			throw new IllegalArgumentException("Row-count must not be null or negative");
		}
		if(!productRepository.existsByShelfRowCount(rowCount)) {
			throw new IllegalArgumentException("Row-count doesn't exists");
		}
	}
	
	private void validateShelfCols(Integer cols) {
		if(cols == null || cols < 0) {
			throw new IllegalArgumentException("Cols must not be null or negative");
		}
		if(!productRepository.existsByShelfCols(cols)) {
			throw new IllegalArgumentException("Cols doesn't exists");
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
	
	private boolean isShelfRequired(GoodsType type) {
	    return switch (type) {
	        case FINISHED_PRODUCT, PALLETIZED_GOODS, SEMI_FINISHED_PRODUCT -> true;
	        case BULK_GOODS, CONSTRUCTION_MATERIAL, RAW_MATERIAL, WRITE_OFS -> false;
	    };
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
	
	private Supply fetchSupplyId(Long supplyId) {
		if(supplyId == null) {
			throw new ValidationException("Supply ID must not be null");
		}
		return supplyRepository.findById(supplyId).orElseThrow(() -> new ValidationException("Supply not found with id "+supplyId));
	}
	
	private Shelf fetchShelfId(Long shelfId) {
		if(shelfId == null) {
			throw new ValidationException("Shelf ID must not be null");
		}
		return shelfRepository.findById(shelfId).orElseThrow(() -> new ValidationException("Shelf not found with id "+shelfId));
	}
}
