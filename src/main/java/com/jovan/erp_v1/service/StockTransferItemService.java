package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import com.jovan.erp_v1.mapper.StockTransferItemMapper;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.StockTransfer;
import com.jovan.erp_v1.model.StockTransferItem;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.StockTransferItemRepository;
import com.jovan.erp_v1.repository.StockTransferRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.request.StockTransferItemRequest;
import com.jovan.erp_v1.response.StockTransferItemResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockTransferItemService implements IStockTransferItemService {

    private final StockTransferItemRepository stockTransferItemRepository;
    private final StockTransferItemMapper stockTransferItemMapper;
    private final ProductRepository productRepository;
    private final StockTransferRepository stockTransferRepository;
    private final SupplyRepository supplyRepository;

    @Transactional
    @Override
    public StockTransferItemResponse create(StockTransferItemRequest request) {
    	validateStockTransferItemRequestForCreate(request);
        StockTransferItem item = stockTransferItemMapper.toEntity(request);
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
        item.setProduct(product);
        item.setQuantity(request.quantity());
        item.setStockTransfer(st);
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
    	if(!stockTransferItemRepository.existsByProductId(productId)) {
    		String msg = String.format("No product found with ID equal to %d", productId);
    		throw new NoDataFoundException(msg);
    	}
    	List<StockTransferItem> items = stockTransferItemRepository.findByProductId(productId);
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
    	validateBigDecimal(quantity);
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
    	if(!stockTransferItemRepository.existsByStockTransferId(stockTransferId)) {
    		String msg = String.format("No stock transfer found with stockTransferId equal to %d", stockTransferId);
    		throw new NoDataFoundException(msg);
    	}
    	List<StockTransferItem> items = stockTransferItemRepository.findByStockTransferId(stockTransferId);
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByStockTransfer_FromStorageId(Long fromStorageId) {
    	if(!stockTransferItemRepository.existsByStockTransfer_FromStorageId(fromStorageId)) {
    		String msg = String.format("No stock transfer found with fromStorageId equal to %d", fromStorageId);
    		throw new NoDataFoundException(msg);
    	}
    	List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_FromStorageId(fromStorageId);
        return items.stream()
                .map(StockTransferItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransferItemResponse> findByStockTransfer_ToStorageId(Long toStorageId) {
    	if(!stockTransferItemRepository.existsByStockTransferId(toStorageId)) {
    		String msg = String.format("No stock transfer found with toStorageId equal to %d", toStorageId);
    		throw new NoDataFoundException(msg);
    	}
    	List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_ToStorageId(toStorageId);
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
		if(!stockTransferItemRepository.existsByProduct_Shelf_Id(shelfId)) {
			String msg = String.format("No product found with shelfId equal to %d", shelfId);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_Id(shelfId);
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_RowCount(Integer rowCount) {
		if(!stockTransferItemRepository.existsByProduct_Shelf_RowCount(rowCount)) {
			String msg = String.format("No product found on shelf with row-count equal to %d", rowCount);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_RowCount(rowCount);
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_Cols(Integer cols) {
		if(!stockTransferItemRepository.existsByProduct_Shelf_Cols(cols)) {
			String msg = String.format("No product found on shelf with cols equal to %d", cols);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_Cols(cols);
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_RowCountGreaterThanEqual(Integer rowCount) {
		if(!stockTransferItemRepository.existsByProduct_Shelf_RowCountGreaterThanEqual(rowCount)) {
			String msg = String.format("No product found on shelf with row-count greater than %d", rowCount);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_RowCountGreaterThanEqual(rowCount);
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_ColsGreaterThanEqual(Integer cols) {
		if(!stockTransferItemRepository.existsByProduct_Shelf_ColsGreaterThanEqual(cols)) {
			String msg = String.format("No product found on shelf with cols greater than %d", cols);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_ColsGreaterThanEqual(cols);
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_RowCountLessThanEqual(Integer rowCount) {
		if(!stockTransferItemRepository.existsByProduct_Shelf_RowCountLessThanEqual(rowCount)) {
			String msg = String.format("No product found on shelf with row-count less than %d", rowCount);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_RowCountLessThanEqual(rowCount);
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_ColsLessThanEqual(Integer cols) {
		if(!stockTransferItemRepository.existsByProduct_Shelf_ColsLessThanEqual(cols)) {
			String msg = String.format("No product found on shelf with cols less than %d", cols);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_ColsLessThanEqual(cols);
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_RowCountBetween(Integer minRowCount,
			Integer maxRowCount) {
		if(!stockTransferItemRepository.existsByProduct_Shelf_RowCountBetween(minRowCount, maxRowCount)) {
			String msg = String.format("No product found with shelf between %d and %d", minRowCount, maxRowCount);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_RowCountBetween(minRowCount, maxRowCount);
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByProduct_Shelf_ColsBetween(Integer minCols, Integer maxCols) {
		if(!stockTransferItemRepository.existsByProduct_Shelf_ColsBetween(minCols, maxCols)) {
			String msg = String.format("No product found with shelf between %d and %d", minCols,maxCols);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByProduct_Shelf_ColsBetween(minCols, maxCols);
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
		if(!stockTransferItemRepository.existsByStockTransfer_Status(status)) {
			String msg = String.format("No stock transfer found with status equal to %s", status);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_Status(status);
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_TransferDate(LocalDate transferDate) {
		if(!stockTransferItemRepository.existsByStockTransfer_TransferDate(transferDate)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No stock transfer found with transfer-date equal to %s", transferDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_TransferDate(transferDate);
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_TransferDateBetween(LocalDate transferDateStart,
			LocalDate transferDateEnd) {
		if(!stockTransferItemRepository.existsByStockTransfer_TransferDateBetween(transferDateStart, transferDateEnd)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No stock transfer found with date between %s and %s", 
					transferDateStart.format(formatter),transferDateEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_TransferDateBetween(transferDateStart, transferDateEnd);
		return items.stream()
		        .map(stockTransferItemMapper::toResponse)
		        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_StatusIn(List<TransferStatus> statuses) {
	    if (!stockTransferItemRepository.existsByStockTransfer_StatusIn(statuses)) {
	        String msg = String.format("No stock transfers found for statuses: %s", statuses);
	        throw new NoDataFoundException(msg);
	    }
	    List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_StatusIn(statuses);
	    return items.stream()
	        .map(stockTransferItemMapper::toResponse)
	        .collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_StatusNot(TransferStatus status) {
		if(!stockTransferItemRepository.existsByStockTransfer_Status(status)) {
			String msg = String.format("No stock transfer found with status equal to %s", status);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_StatusNot(status);
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_TransferDateAfter(LocalDate date) {
		if(!stockTransferItemRepository.existsByStockTransfer_TransferDateAfter(date)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No stock transfer found with date equal to %s", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_TransferDateAfter(date);
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_TransferDateBefore(LocalDate date) {
		if(!stockTransferItemRepository.existsByStockTransfer_TransferDateBefore(date)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No stock transfer found with date equal to %s", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_TransferDateBefore(date);
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_StatusAndQuantityGreaterThan(TransferStatus status,
			BigDecimal quantity) {
		validateBigDecimal(quantity);
		if(!stockTransferItemRepository.existsByStockTransfer_Status(status)) {
			String msg = String.format("No stock transfer found with status equal to %s", status);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_StatusAndQuantityGreaterThan(status, quantity);
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<StockTransferItemResponse> findByStockTransfer_StatusAndStockTransfer_TransferDateBetween(
			TransferStatus status, LocalDate start, LocalDate end) {
		if(!stockTransferItemRepository.existsByStockTransfer_StatusAndStockTransfer_TransferDateBetween(status, start, end)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format(
				    "No stock transfer found with status %s and date between %s and %s",
				    status, start.format(formatter), end.format(formatter)
				);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_StatusAndStockTransfer_TransferDateBetween(status, start, end);
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
		if(!stockTransferItemRepository.existsByStockTransfer_FromStorage_Type(type)) {
			String msg = String.format("No stock transfer found wuth storage-type equal to %s", type);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_FromStorage_Type(type);
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
		if(!stockTransferItemRepository.existsByStockTransfer_ToStorage_Type(type)) {
			String msg = String.format("No given storage type found %s", type);
			throw new NoDataFoundException(msg);
		}
		List<StockTransferItem> items = stockTransferItemRepository.findByStockTransfer_ToStorage_Type(type);
		return items.stream().map(stockTransferItemMapper::toResponse).collect(Collectors.toList());
	}
	
	
	
	private void validateUnitMeasure(UnitMeasure unitMeasure) {
		if(unitMeasure == null) {
			throw new NoDataFoundException("UnitMeasure unitMeasure must not be null");
		}
	}
	
	private void validateSupplierType(SupplierType supplierType) {
		if(supplierType == null) {
			throw new NoDataFoundException("SupplierType supplierType must not be null");
		}
	}
	
	private void validateGoodsType(GoodsType goodsType) {
		if(goodsType == null) {
			throw new NoDataFoundException("GoodsType goodsType must not be null");
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new NoDataFoundException("String must not be empty nor null");
		}
	}
	
	private Supply fetchSupplyId(Long supplyId) {
		if(supplyId == null) {
			throw new NoDataFoundException("Supply ID must not be null");
		}
		return supplyRepository.findById(supplyId).orElseThrow(() -> new SupplyNotFoundException("Supply not found with id "+supplyId));
	}
	
	private void validateStorageType(StorageType type) {
		if(type == null) {
			throw new NoDataFoundException("StorageType tpe must nost be null");
		}
	}
	
	private void validateStockTransferItemRequestForCreate(StockTransferItemRequest request) {
		fetchProductId(request.productId());
		fetchStockTransferId(request.stockTransferId());
		validateBigDecimal(request.quantity());
	}
    
    private Product fetchProductId(Long productId) {
    	if(productId == null) {
    		throw new NoDataFoundException("Product ID must not be null");
    	}
    	return productRepository.findById(productId).orElseThrow(() -> new NoSuchProductException("Product not found with id: "+productId));
    }

    private StockTransfer fetchStockTransferId(Long stockTransferId) {
    	if(stockTransferId == null) {
    		throw new NoDataFoundException("StockTransfer ID must not be null");
    	}
    	return stockTransferRepository.findById(stockTransferId).orElseThrow(() -> new StockTransferErrorException("StockTransfer not found with id: "+stockTransferId));
    }
    
    private void validateBigDecimal(BigDecimal num) {
    	if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
    		throw new NoDataFoundException("Number must be positive");
    	}
    }

}
