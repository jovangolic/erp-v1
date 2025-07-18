package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.mapper.ProductMapper;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.BarCodeRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.StorageRepository;
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

    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest request) {
    	validateProductRequest(request);
        Product product = productMapper.toEntity(request);
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
        product.setName(request.name());
        product.setCurrentQuantity(request.currentQuantity());
        product.setUnitMeasure(request.unitMeasure());
        product.setSupplierType(request.supplierType());
        product.setStorageType(request.storageType());
        product.setGoodsType(request.goodsType());
        Storage storage = storageRepository.findById(request.storageId())
                .orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + request.storageId()));
        product.setStorage(storage);
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
    	validateBigDecimal(quantity);
        List<Product> products = productRepository.findByCurrentQuantityLessThan(quantity);
        return productMapper.toProductResponseList(products);
    }

    @Override
    public List<ProductResponse> findByName(String name) {
    	validateString(name);
        return productMapper.toProductResponseList(productRepository.findByName(name));
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
        return productMapper.toProductResponseList(productRepository.findBySupplierType(supplierType));
    }

    @Override
    public List<ProductResponse> findByStorageType(StorageType storageType) {
    	validateStorageType(storageType);
        return productMapper.toProductResponseList(productRepository.findByStorageType(storageType));
    }

    @Override
    public List<ProductResponse> findByGoodsType(GoodsType goodsType) {
    	validateGoodsType(goodsType);
        return productMapper.toProductResponseList(productRepository.findByGoodsType(goodsType));
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
                // Postojeći barCode - ažuriraj polja
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
		return productRepository.findByUnitMeasure(unitMeasure).stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findByShelfRowColAndStorage(Integer row, Integer col, Long storageId) {
		fetchStorage(storageId);
		validateShelfRowCount(row);
		validateShelfCols(col);
		return productRepository.findByShelfRowColAndStorage(row, col, storageId).stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findByShelfRow(Integer row) {
		validateShelfRowCount(row);
		return productRepository.findByShelfRow(row).stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findByShelfColumn(Integer col) {
		validateShelfCols(col);
		return productRepository.findByShelfColumn(col).stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findBySupplyMinQuantity(BigDecimal quantity) {
		validateBigDecimal(quantity);
		return productRepository.findBySupplyMinQuantity(quantity).stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findBySupplyUpdateRange(LocalDateTime from, LocalDateTime to) {
		DateValidator.validateRange(from, to);
		return productRepository.findBySupplyUpdateRange(from, to).stream()
				.map(ProductResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductResponse> findBySupplyStorageId(Long storageId) {
		fetchStorage(storageId);
		return productRepository.findBySupplyStorageId(storageId).stream()
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
		fetchStorage(request.storageId());
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
}
