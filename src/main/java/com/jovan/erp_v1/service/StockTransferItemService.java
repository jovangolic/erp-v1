package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.TransferStatus;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.StockTransferErrorException;
import com.jovan.erp_v1.exception.StockTransferItemErrorException;
import com.jovan.erp_v1.exception.SupplyNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.StockTransferItemMapper;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.StockTransfer;
import com.jovan.erp_v1.model.StockTransferItem;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StockTransferItemRepository;
import com.jovan.erp_v1.repository.StockTransferRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.request.StockTransferItemRequest;
import com.jovan.erp_v1.response.StockTransferItemResponse;
import com.jovan.erp_v1.util.DateValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockTransferItemService implements IStockTransferItemService {

    private final StockTransferItemRepository stockTransferItemRepository;
    private final StockTransferItemMapper stockTransferItemMapper;
    private final ProductRepository productRepository;
    private final StockTransferRepository stockTransferRepository;
    private final SupplyRepository supplyRepository;
    private final StorageRepository storageRepository;
    private final ShelfRepository shelfRepository;

    @Transactional
    @Override
    public StockTransferItemResponse create(StockTransferItemRequest request) {
    	Product product = fetchProductId(request.productId());
    	StockTransfer stock = fetchStockTransferId(request.stockTransferId());
    	validateStockTransferItemRequestForCreate(request);
        StockTransferItem item = stockTransferItemMapper.toEntity(request,product,stock);
        StockTransferItem saved = stockTransferItemRepository.save(item);
        return stockTransferItemMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public StockTransferItemResponse update(Long id, StockTransferItemRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        StockTransferItem item = stockTransferItemRepository.findById(id)
                .orElseThrow(() -> new StockTransferItemErrorException("StockTransferItem not found " + id));
        Product product = fetchProductId(request.productId());
        StockTransfer st = fetchStockTransferId(request.stockTransferId());
        if (request.quantity() == null || request.quantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NoDataFoundException("Quantity must be greater than zero.");
        }
        if (!item.getProduct().getId().equals(request.productId())) {
            throw new NoDataFoundException("Changing product is not allowed once item is created.");
        }
        if(!item.getStockTransfer().getId().equals(request.stockTransferId())) {
        	throw new NoDataFoundException("Changing stock-transfer is not allowed once item is created");
        }
        stockTransferItemMapper.toEntityUpdate(item, request, product, st);
        StockTransferItem saved = stockTransferItemRepository.save(item);
        return stockTransferItemMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!stockTransferItemRepository.existsById(id)) {
            throw new StockTransferItemErrorException("StockTransferItem not found " + id);
        }
        stockTransferItemRepository.deleteById(id);
    }

    @Override
    public StockTransferItemResponse findOne(Long id) {
        StockTransferItem item = stockTransferItemRepository.findById(id)
                .orElseThrow(() -> new StockTransferItemErrorException("StockTransferItem not found " + id));
        return new StockTransferItemResponse(item);
    }

    @Override
    public List<StockTransferItemResponse> findAll() {
    	List<StockTransferItem> items = stockTransferItemRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("Items list is empty");
    	}
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());

    }

    @Override
    public List<StockTransferItemResponse> findByProductId(Long productId) {
    	fetchProductId(productId);
    	List<StockTransferItem> items = stockTransferItemRepository.findByProductId(productId);
    	if(items.isEmpty()) {
    		String msg = String.format("No product found with ID equal to %d", productId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByProduct_Name(String name) {
    	validateString(name);
    	List<StockTransferItem> items = stockTransferItemRepository.findByProduct_NameContainingIgnoreCase(name);
    	if(items.isEmpty()) {
    		String msg = String.format("No product found with name equal to %s", name);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity) {
    	validateBigDecimal(currentQuantity);
    	List<StockTransferItem> items = stockTransferItemRepository.findByProduct_CurrentQuantity(currentQuantity);
    	if(items.isEmpty()) {
    		DecimalFormat df = new DecimalFormat("#.##");
    		String msg = String.format("No product found with current-quantity equal to %s", df.format(currentQuantity));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByQuantity(BigDecimal quantity) {
    	validateBigDecimal(quantity);
    	List<StockTransferItem> items = stockTransferItemRepository.findByQuantity(quantity);
    	if(items.isEmpty()) {
    		DecimalFormat df = new DecimalFormat("#.##");
    		String msg = String.format("No quantity found equal to %s", df.format(quantity));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByQuantityLessThan(BigDecimal quantity) {
    	validateBigDecimalNonNegative(quantity);
    	List<StockTransferItem> items = stockTransferItemRepository.findByQuantityLessThan(quantity);
    	if(items.isEmpty()) {
    		DecimalFormat df = new DecimalFormat("#.##");
    		String msg = String.format("No quantity found less than %s", df.format(quantity));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByQuantityGreaterThan(BigDecimal quantity) {
    	validateBigDecimal(quantity);
    	List<StockTransferItem> items = stockTransferItemRepository.findByQuantityGreaterThan(quantity);
    	if(items.isEmpty()) {
    		DecimalFormat df = new DecimalFormat("#.##");
    		String msg = String.format("No quantity found greater than %s", df.format(quantity));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByStockTransferId(Long stockTransferId) {
    	fetchStockTransferId(stockTransferId);
    	List<StockTransferItem> items = stockTransferItemRepository.findByStockTransferId(stockTransferId);
    	if(items.isEmpty()) {
    		String msg = String.format("No stock transfer found with stockTransferId equal to %d", stockTransferId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByStockTransfer_FromStorageId(Long fromStorageId) {
    	fetchFromStorageId(fromStorageId);
    	List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_FromStorageId(fromStorageId);
    	if(items.isEmpty()) {
    		String msg = String.format("No stock transfer found with fromStorageId equal to %d", fromStorageId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByStockTransfer_ToStorageId(Long toStorageId) {
    	fetchToStorageId(toStorageId);
    	List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_ToStorageId(toStorageId);
    	if(items.isEmpty()) {
    		String msg = String.format("No stock transfer found with toStorageId equal to %d", toStorageId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }
    
    //nove metode
    
    @Override
	public List<StockTransferItemResponse> findByProduct_UnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_UnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No product found with unit-measure equal to %s", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_UnitMeasureAndProduct_StorageType(UnitMeasure unitMeasure,
			StorageType storageType) {
		validateUnitMeasure(unitMeasure);
		validateStorageType(storageType);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_UnitMeasureAndProduct_StorageType(unitMeasure, storageType);
		if(items.isEmpty()) {
			String msg = String.format("No product found with unit-measure %s and storage-type %s"
					, unitMeasure,storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_SupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_SupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No product found with supplier-type equal to %s", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_GoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_GoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No product found with goods-type equal to %s", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_StorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_StorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No product found wuth storage-type equal to %s", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_Id(Long shelfId) {
		fetchShelfId(shelfId);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_Id(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No product found with shelfId equal to %d", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_RowCount(Integer rowCount) {
		validateInteger(rowCount);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_RowCount(rowCount);
		if(items.isEmpty()) {
			String msg = String.format("No product found on shelf with row-count equal to %d", rowCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_Cols(Integer cols) {
		validateInteger(cols);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_Cols(cols);
		if(items.isEmpty()) {
			String msg = String.format("No product found on shelf with cols equal to %d", cols);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_RowCountGreaterThanEqual(Integer rowCount) {
		validateInteger(rowCount);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_RowCountGreaterThanEqual(rowCount);
		if(items.isEmpty()) {
			String msg = String.format("No product found on shelf with row-count greater than %d", rowCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_ColsGreaterThanEqual(Integer cols) {
		validateInteger(cols);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_ColsGreaterThanEqual(cols);
		if(items.isEmpty()) {
			String msg = String.format("No product found on shelf with cols greater than %d", cols);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_RowCountLessThanEqual(Integer rowCount) {
		validateInteger(rowCount);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_RowCountLessThanEqual(rowCount);
		if(items.isEmpty()) {
			String msg = String.format("No product found on shelf with row-count less than %d", rowCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_ColsLessThanEqual(Integer cols) {
		validateInteger(cols);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_ColsLessThanEqual(cols);
		if(items.isEmpty()) {
			String msg = String.format("No product found on shelf with cols less than %d", cols);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_RowCountBetween(Integer minRowCount,
			Integer maxRowCount) {
		validateShelfRowsBetween(minRowCount, maxRowCount);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_RowCountBetween(minRowCount, maxRowCount);
		if(items.isEmpty()) {
			String msg = String.format("No product found with shelf between %d and %d", minRowCount, maxRowCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_ColsBetween(Integer minCols, Integer maxCols) {
		validateShelfColsBetween(minCols, maxCols);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_ColsBetween(minCols, maxCols);
		if(items.isEmpty()) {
			String msg = String.format("No product found with shelf between %d and %d", minCols,maxCols);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Supply_Id(Long supplyId) {
		fetchSupplyId(supplyId);
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Supply_Id(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No product found with supplyId equal to %s", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_Status(TransferStatus status) {
		validateTransferStatus(status);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfer found with status equal to %s", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_TransferDate(LocalDate transferDate) {
		DateValidator.validateNotNull(transferDate, "Transfer-date");
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_TransferDate(transferDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No stock transfer found with transfer-date equal to %s", transferDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_TransferDateBetween(LocalDate transferDateStart,
			LocalDate transferDateEnd) {
		DateValidator.validateRange(transferDateStart, transferDateEnd);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_TransferDateBetween(transferDateStart, transferDateEnd);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No stock transfer found with date between %s and %s", 
					transferDateStart.format(formatter),transferDateEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_StatusIn(List<TransferStatus> statuses) {
	    validateTransferStatusList(statuses);
	    List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_StatusIn(statuses);
	    if (items.isEmpty()) {
	        String msg = String.format("No stock transfers found for statuses: %s", statuses);
	        throw new NoDataFoundException(msg);
	    }
	    return items.stream()
	        .map(stockTransferItemMapper::toResponse)
	        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_StatusNot(TransferStatus status) {
		validateTransferStatus(status);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_StatusNot(status);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfer found with status equal to %s", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_TransferDateAfter(LocalDate date) {
		DateValidator.validateNotInPast(date, "Dat");
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_TransferDateAfter(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No stock transfer found with date equal to %s", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_TransferDateBefore(LocalDate date) {
		DateValidator.validateNotInFuture(date, "Date");
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_TransferDateBefore(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No stock transfer found with date equal to %s", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_StatusAndQuantityGreaterThan(TransferStatus status,
			BigDecimal quantity) {
		validateBigDecimal(quantity);
		validateBigDecimal(quantity);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_StatusAndQuantityGreaterThan(status, quantity);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfer found with status equal to %s", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_StatusAndStockTransfer_TransferDateBetween(
			TransferStatus status, LocalDate start, LocalDate end) {
		validateTransferStatus(status);
		DateValidator.validateRange(start, end);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_StatusAndStockTransfer_TransferDateBetween(status, start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format(
				    "No stock transfer found with status %s and date between %s and %s",
				    status, start.format(formatter), end.format(formatter)
				);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_FromStorage_NameContainingIgnoreCase(String name) {
		validateString(name);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_FromStorage_NameContainingIgnoreCase(name);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfer found with name equal to %s", name);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_FromStorage_LocationContainingIgnoreCase(
			String location) {
		validateString(location);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_FromStorage_LocationContainingIgnoreCase(location);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfer found with location equal to %s", location);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_FromStorage_Capacity(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_FromStorage_Capacity(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfers found with capacity equal to %s", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_FromStorage_CapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_FromStorage_CapacityGreaterThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfers found with capacity greater than %s", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_FromStorage_CapacityLessThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_FromStorage_CapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfers found with capacity less than %s", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_FromStorage_Type(StorageType type) {
		validateStorageType(type);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_FromStorage_Type(type);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfer found wuth storage-type equal to %s", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_ToStorage_NameContainingIgnoreCase(String name) {
		validateString(name);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_ToStorage_NameContainingIgnoreCase(name);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfer found with name equal to %s", name);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_ToStorage_LocationContainingIgnoreCase(String location) {
		validateString(location);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_ToStorage_LocationContainingIgnoreCase(location);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfer found with location equal to %s", location);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_ToStorage_Capacity(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_ToStorage_Capacity(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfers found with capacity equal to %s", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_ToStorage_CapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_ToStorage_CapacityGreaterThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No given capacity greater than found in stock-transfer %s", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_ToStorage_CapacityLessThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_ToStorage_CapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No stock transfers found with capacity less than %s", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_ToStorage_Type(StorageType type) {
		validateStorageType(type);
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_ToStorage_Type(type);
		if(items.isEmpty()) {
			String msg = String.format("No given storage type found %s", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}
	
	private void validateUnitMeasure(UnitMeasure unitMeasure) {
		if(unitMeasure == null) {
			throw new ValidationException("UnitMeasure unitMeasure must not be null");
		}
	}
	
	private void validateSupplierType(SupplierType supplierType) {
		if(supplierType == null) {
			throw new ValidationException("SupplierType supplierType must not be null");
		}
	}
	
	private void validateGoodsType(GoodsType goodsType) {
		if(goodsType == null) {
			throw new ValidationException("GoodsType goodsType must not be null");
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be empty nor null");
		}
	}
	
	private Supply fetchSupplyId(Long supplyId) {
		if(supplyId == null) {
			throw new ValidationException("Supply ID must not be null");
		}
		return supplyRepository.findById(supplyId).orElseThrow(() -> new SupplyNotFoundException("Supply not found with id "+supplyId));
	}
	
	private void validateStorageType(StorageType type) {
		if(type == null) {
			throw new NoDataFoundException("StorageType tpe must nost be null");
		}
	}
	
	private void validateStockTransferItemRequestForCreate(StockTransferItemRequest request) {
		validateBigDecimal(request.quantity());
	}
    
    private Product fetchProductId(Long productId) {
    	if(productId == null) {
    		throw new ValidationException("Product ID must not be null");
    	}
    	return productRepository.findById(productId).orElseThrow(() -> new NoSuchProductException("Product not found with id: "+productId));
    }

    private StockTransfer fetchStockTransferId(Long stockTransferId) {
    	if(stockTransferId == null) {
    		throw new ValidationException("StockTransfer ID must not be null");
    	}
    	return stockTransferRepository.findById(stockTransferId).orElseThrow(() -> new StockTransferErrorException("StockTransfer not found with id: "+stockTransferId));
    }
    
    private void validateBigDecimal(BigDecimal num) {
    	if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
    		throw new ValidationException("Number must be positive");
    	}
    }

    private void validateTransferStatus(TransferStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(()-> new ValidationException("TransferStatus status must nor be null"));
    }
    
    private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
    
    private Storage fetchFromStorageId(Long storageId) {
    	if(storageId == null) {
    		throw new ValidationException("FromStorage ID must not be null");
    	}
    	return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("FromStorage not found with id "+storageId));
    }
    
    private Storage fetchToStorageId(Long storageId) {
    	if(storageId == null) {
    		throw new ValidationException("ToStorage ID must not be null");
    	}
    	return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("ToStorage not found with id "+storageId));
    }
    
    private Shelf fetchShelfId(Long id) {
    	if(id == null) {
    		throw new ValidationException("Shelf ID must not be null");
    	}
    	return shelfRepository.findById(id).orElseThrow(() -> new ValidationException("Shelf not found with id "+id));
    }
    
    private void validateInteger(Integer num) {
		if(num == null || num <= 0) {
			throw new ValidationException("Number must be positive");
		}
	}
    
    private void validateTransferStatusList(List<TransferStatus> statuses) {
		if(statuses == null || statuses.isEmpty()) {
			throw new ValidationException("TransferStatus list must not be empty nor null");
		}
		for(TransferStatus status: statuses) {
			if(status == null) {
				throw new ValidationException("Each TransferStatus must not be null");
			}
		}
	}
    
    private void validateShelfColsBetween(Integer min, Integer max) {
        if (min == null || max == null) {
            throw new ValidationException("Both 'min' and 'max' must be provided (not null).");
        }
        if (min < 0) {
            throw new ValidationException("'min' must be zero or a positive number.");
        }
        if (max <= 0) {
            throw new ValidationException("'max' must be a positive number (greater than zero).");
        }
    }
    
    private void validateShelfRowsBetween(Integer min, Integer max) {
        if (min == null || max == null) {
            throw new ValidationException("Both 'min' and 'max' must be provided (not null).");
        }
        if (min < 0) {
            throw new ValidationException("'min' must be zero or a positive number.");
        }
        if (max <= 0) {
            throw new ValidationException("'max' must be a positive number (greater than zero).");
        }
    }
       
       
}
