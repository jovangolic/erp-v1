package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.BillOfMaterialsErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.BillOfMaterialsMapper;
import com.jovan.erp_v1.model.BillOfMaterials;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.BillOfMaterialsRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.repository.specification.BillOfMaterialsSpecification;
import com.jovan.erp_v1.request.BillOfMaterialsRequest;
import com.jovan.erp_v1.response.BillOfMaterialsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillOfMaterialsService implements IBillOfMaterialsService {

    private final BillOfMaterialsRepository billOfMaterialsRepository;
    private final BillOfMaterialsMapper billOfMaterialsMapper;
    private final StorageRepository storageRepository;
    private final SupplyRepository supplyRepository;
    private final ShelfRepository shelfRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public BillOfMaterialsResponse create(BillOfMaterialsRequest request) {
        validateRequest(request);
        Product parentProduct = validateParentProductId(request.parentProductId());
        Product component = validateComponentId(request.componentId());
        if (request.parentProductId().equals(request.componentId())) {
            throw new ValidationException("Parent product and component must be different");
        }
        BillOfMaterials bom = billOfMaterialsMapper.toEntity(request, parentProduct,component);
        BillOfMaterials saved = billOfMaterialsRepository.save(bom);
        return billOfMaterialsMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public BillOfMaterialsResponse update(Long id, BillOfMaterialsRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        BillOfMaterials bom = billOfMaterialsRepository.findById(id)
                .orElseThrow(() -> new BillOfMaterialsErrorException("BillOfMaterials not found with id: " + id));
        validateBigDecimal(request.quantity());
        Product parentProduct = bom.getParentProduct();
        Product component = bom.getComponent();
        if(request.parentProductId() != null && (bom.getParentProduct() == null || !request.parentProductId().equals(bom.getParentProduct().getId()))) {
        	parentProduct = validateParentProductId(request.parentProductId());
        }
        if(request.componentId() != null && (bom.getComponent() == null || !request.componentId().equals(bom.getComponent().getId()))) {
        	component = validateComponentId(request.componentId());
        }
        if (request.parentProductId().equals(request.componentId())) {
            throw new ValidationException("Parent product and component must be different");
        }
        billOfMaterialsMapper.toUpdateEntity(bom, request, parentProduct, component);
        return billOfMaterialsMapper.toResponse(billOfMaterialsRepository.save(bom));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!billOfMaterialsRepository.existsById(id)) {
            throw new BillOfMaterialsErrorException("BillOfMaterials not found with id: " + id);
        }
        billOfMaterialsRepository.deleteById(id);
    }

    @Override
    public BillOfMaterialsResponse findOne(Long id) {
        BillOfMaterials bom = billOfMaterialsRepository.findById(id)
                .orElseThrow(() -> new BillOfMaterialsErrorException("BillOfMaterials not found with id: " + id));
        return new BillOfMaterialsResponse(bom);
    }

    @Override
    public List<BillOfMaterialsResponse> findAll() {
    	List<BillOfMaterials> items = billOfMaterialsRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("List of bill-of-materials is empty");
    	}
        return billOfMaterialsRepository.findAll().stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillOfMaterialsResponse> findByParentProductId(Long parentProductId) {
    	validateParentProductId(parentProductId);
    	List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProductId(parentProductId);
    	if(items.isEmpty()) {
    		String msg = String.format("Bill of materials with parent product id %d is not found", parentProductId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillOfMaterialsResponse> findByComponentId(Long componentId) {
    	validateComponentId(componentId);
    	List<BillOfMaterials> items = billOfMaterialsRepository.findByComponentId(componentId);
    	if(items.isEmpty()) {
    		String msg = String.format("Bill of materials with component-id %d is not found", componentId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillOfMaterialsResponse> findByQuantityGreaterThan(BigDecimal quantity) {
    	validateBigDecimal(quantity);
    	List<BillOfMaterials> items = billOfMaterialsRepository.findByQuantityGreaterThan(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format(" Bill of materials with quantity greater than %s is not found", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillOfMaterialsResponse> findByQuantity(BigDecimal quantity) {
    	validateBigDecimal(quantity);
    	List<BillOfMaterials> items = billOfMaterialsRepository.findByQuantity(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format(" Bill of materials with quantity %s is not found", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillOfMaterialsResponse> findByQuantityLessThan(BigDecimal quantity) {
    	validateBigDecimalNonNegative(quantity);
    	List<BillOfMaterials> items = billOfMaterialsRepository.findByQuantityLessThan(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format(" Bill of materials with quantity less than %s is not found", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BillOfMaterialsResponse> filterBOMs(Long parentProductId, Long componentId,
            BigDecimal minQuantity, BigDecimal maxQuantity){
    	Specification<BillOfMaterials> spec = Specification.where(null);
        spec = spec.and(BillOfMaterialsSpecification.hasParentProductId(parentProductId));
        spec = spec.and(BillOfMaterialsSpecification.hasComponentId(componentId));
        spec = spec.and(BillOfMaterialsSpecification.quantityGreaterThanOrEqual(minQuantity));
        spec = spec.and(BillOfMaterialsSpecification.quantityLessThanOrEqual(maxQuantity));
        List<BillOfMaterials> items = billOfMaterialsRepository.findAll(spec);
        return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
    }
    
    //nove metode
    
    @Override
	public List<BillOfMaterialsResponse> findByParentProduct_CurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_CurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component with current quantity  %s found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProduct_CurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_CurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent product with current quantity less than %s found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_CurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent product with current quantity greater than %s found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponent_CurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponent_CurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component with current quantity %s found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponent_CurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponent_CurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component with current quantity less than %s found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponent_CurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponent_CurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component with current quantity greater than %s found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProductIdAndQuantityGreaterThan(Long parentProductId,
			BigDecimal quantity) {
		validateParentProductId(parentProductId);
		validateBigDecimal(quantity);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProductIdAndQuantityGreaterThan(parentProductId, quantity);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent-product-id %d and quantity greater than %s found",
					parentProductId, quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProductIdAndQuantityLessThan(Long parentProductId,
			BigDecimal quantity) {
		validateParentProductId(parentProductId);
		validateBigDecimalNonNegative(quantity);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProductIdAndQuantityLessThan(parentProductId, quantity);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent-product-id %d and quantity less than %s found",
					parentProductId, quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public BillOfMaterialsResponse findByParentProductIdAndComponentId(Long parentProductId, Long componentId) {
		validateParentProductId(parentProductId);
		validateComponentId(componentId);
		BillOfMaterials items = billOfMaterialsRepository.findByParentProductIdAndComponentId(parentProductId, componentId)
				.orElseThrow(() -> new ValidationException("No parentProductId and componentId found"));
		return new BillOfMaterialsResponse(items);
	}

	@Override
	public Boolean existsByParentProductIdAndComponentId(Long parentProductId, Long componentId) {
		validateParentProductId(parentProductId);
		validateComponentId(componentId);
		return billOfMaterialsRepository.existsByParentProductIdAndComponentId(parentProductId, componentId);
	}

	@Override
	public void deleteByParentProductId(Long parentProductId) {
		validateParentProductId(parentProductId);
		billOfMaterialsRepository.deleteByParentProductId(parentProductId);
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProductIdOrderByQuantityDesc(Long parentProductId) {
		validateParentProductId(parentProductId);;
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProductIdOrderByQuantityDesc(parentProductId);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent product id %d with quantity order by descending, not found", parentProductId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProductIdOrderByQuantityAsc(Long parentProductId) {
		validateParentProductId(parentProductId);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProductIdOrderByQuantityAsc(parentProductId);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent product id %d with quantity order by ascending, not found", parentProductId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByQuantityBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByQuantityBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for quantity between %s and %s found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findComponentsByProductIdAndComponentNameContaining(Long productId,
			String name) {
		validateParentProductId(productId);
		validateString(name);
		List<BillOfMaterials> items = billOfMaterialsRepository.findComponentsByProductIdAndComponentNameContaining(productId, name);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component containing product-id %d and component name %s found",productId, name);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponent_NameContainingIgnoreCase(String name) {
		validateString(name);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponent_NameContainingIgnoreCase(name);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component with name %s found", name);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProduct_NameContainingIgnoreCase(String name) {
		validateString(name);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_NameContainingIgnoreCase(name);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent product with name %s found", name);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponent_GoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponent_GoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component with goods-type %s found", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponent_SupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponent_SupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No BillOdMaterial for component with supplier-type %s found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProduct_UnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_UnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent product with unit-measeure %s found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponent_Shelf_Id(Long shelfId) {
		validateShelfId(shelfId);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponent_Shelf_Id(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component with shelf-id %d found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProduct_Storage_Id(Long storageId) {
		validateStorageId(storageId);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_Storage_Id(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent product with storage-id %d found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProduct_Shelf_Id(Long shelfId) {
		validateShelfId(shelfId);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_Shelf_Id(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent product with shelf-id %d found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponent_Storage_Id(Long storageId) {
		validateStorageId(storageId);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponent_Storage_Id(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component with storage id %d found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProduct_GoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_GoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent product with goods type %s found", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProduct_SupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_SupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent product supplier-type %s found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponent_UnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponent_UnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component unit-measure %s is found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProduct_Supply_Id(Long supplyId) {
		validateSupplyId(supplyId);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_Supply_Id(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for parent product supply-id %d is found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponent_Supply_Id(Long supplyId) {
		validateSupplyId(supplyId);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponent_Supply_Id(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component supply-id %d is found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponent_StorageType(StorageType type) {
		validateStorageType(type);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponent_StorageType(type);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component storage type %s is found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProduct_StorageType(StorageType type) {
		validateStorageType(type);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_StorageType(type);
		if(items.isEmpty()) {
			String msg = String.format("No BilOfMaterials for parent product storage type %s is found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByParentProduct_Storage_IdAndComponent_GoodsType(Long storageId,
			GoodsType goodsType) {
		validateStorageId(storageId);
		validateGoodsType(goodsType);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByParentProduct_Storage_IdAndComponent_GoodsType(storageId, goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials with parent product storage %d and component goods type %s found", 
					storageId, goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findWhereParentAndComponentShareSameStorage() {
		List<BillOfMaterials> items = billOfMaterialsRepository.findWhereParentAndComponentShareSameStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No BillOfMaterials where parent and componetn share same storage found.");
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByMinQuantityAndComponentGoodsType(BigDecimal minQuantity,
			GoodsType goodsType) {
		validateBigDecimalNonNegative(minQuantity);
		validateGoodsType(goodsType);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByMinQuantityAndComponentGoodsType(minQuantity, goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials with min-quantity %s and component goods type %s found",
					minQuantity,goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findByComponentStorageAndUnitMeasure(Long storageId, UnitMeasure unitMeasure) {
		validateStorageId(storageId);
		validateUnitMeasure(unitMeasure);
		List<BillOfMaterials> items = billOfMaterialsRepository.findByComponentStorageAndUnitMeasure(storageId, unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No BillOfMaterials for component storage %d and unit measure %s found", 
					storageId, unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findAllOrderByQuantityDesc() {
		List<BillOfMaterials> items = billOfMaterialsRepository.findAllOrderByQuantityDesc();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No BillOfMaterials found with quantity in descending order");
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BillOfMaterialsResponse> findAllOrderByQuantityAsc() {
		List<BillOfMaterials> items = billOfMaterialsRepository.findAllOrderByQuantityAsc();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No BillOfMaterials found with quantity, in ascending order");
		}
		return items.stream().map(billOfMaterialsMapper::toResponse).collect(Collectors.toList());
	}

    private void validateRequest(BillOfMaterialsRequest request) {
        if(request == null) {
        	throw new ValidationException("BillOfMaterials request must not be null");
        }
        validateParentProductId(request.parentProductId());
        validateComponentId(request.componentId());
        if (request.quantity() == null || request.quantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive number");
        }
    }
    
    private Product validateComponentId(Long componentId) {
    	if (componentId == null) {
            throw new NoSuchProductException("Component must not be null");
        }
    	return productRepository.findById(componentId).orElseThrow(() -> new ValidationException("Component not found with id "+componentId));
    }
    
    private Product validateParentProductId(Long parentProductId) {
    	if (parentProductId == null) {
            throw new NoSuchProductException("ParentProduct must not be null");
        }
    	return productRepository.findById(parentProductId).orElseThrow(() -> new ValidationException("Parent-product not found with id "+parentProductId));
    }
    
    private void validateBigDecimal(BigDecimal quantity) {
    	if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive number");
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
    
    private void validateUnitMeasure(UnitMeasure unitMeasure) {
    	Optional.ofNullable(unitMeasure)
    		.orElseThrow(() -> new ValidationException("UnitMeasure unitMeasure must not be null"));
    }

    private void validateStorageType(StorageType type) {
    	Optional.ofNullable(type)
    		.orElseThrow(() -> new ValidationException("StorageType type must not be null"));
    }
    
    private void validateSupplierType(SupplierType supplierType) {
    	Optional.ofNullable(supplierType)
    		.orElseThrow(() -> new ValidationException("SupplierType supplierType must not be null"));
    }
    
    private void validateGoodsType(GoodsType goodsType) {
    	Optional.ofNullable(goodsType)
    		.orElseThrow(() -> new ValidationException("GoodsType goodsType must not be null"));
    }
    
    private Storage validateStorageId(Long storageId) {
    	if(storageId == null) {
    		throw new ValidationException("Storage ID must not be null");
    	}
    	return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("Storage not found with id "+storageId));
    }
    
    private Supply validateSupplyId(Long supplyId) {
    	if(supplyId == null) {
    		throw new ValidationException("Supply ID must not be null");
    	}
    	return supplyRepository.findById(supplyId).orElseThrow(() -> new ValidationException("Supply not found with id "+supplyId));
    }
    
    private Shelf validateShelfId(Long shelfId) {
    	if(shelfId == null) {
    		throw new ValidationException("Shelf ID must not be null");
    	}
    	return shelfRepository.findById(shelfId).orElseThrow(() -> new ValidationException("Shelf not found with id "+shelfId));
    }
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new ValidationException("Textual characters must not be null nor empty");
    	}
    }
    
    private void validateMinAndMax(BigDecimal min, BigDecimal max) {
        if (min == null || min.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Minimalna vrednost mora biti veća od nule.");
        }
        if (max == null || max.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Maksimalna vrednost mora biti veća od nule.");
        }
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Minimalna vrednost ne sme biti veća od maksimalne.");
        }
    }
}
