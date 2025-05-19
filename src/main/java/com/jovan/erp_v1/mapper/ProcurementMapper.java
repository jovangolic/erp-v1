package com.jovan.erp_v1.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.SupplyItem;
import com.jovan.erp_v1.repository.ItemSalesRepository;
import com.jovan.erp_v1.repository.SupplyItemRepository;
import com.jovan.erp_v1.request.ProcurementRequest;
import com.jovan.erp_v1.response.ProcurementResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProcurementMapper {

	private final ItemSalesRepository itemSalesRepository;
	private final SupplyItemRepository supplyItemRepository;
	
	public Procurement toEntity(ProcurementRequest request) {
		Procurement procurement = new Procurement();
		procurement.setId(request.id());
		procurement.setDate(request.date());
		procurement.setTotalCost(request.totalCost());
		if (request.itemSalesIds() != null) {
	        List<ItemSales> itemSalesList = itemSalesRepository.findAllById(request.itemSalesIds());
	         // Dodatno setovanje relacije ka procurement-u:
	         itemSalesList.forEach(item -> item.setProcurement(procurement));
	         procurement.setItemSales(itemSalesList);
	     }
	     if (request.supplyItemIds() != null) {
	         List<SupplyItem> supplyItemList = supplyItemRepository.findAllById(request.supplyItemIds());
	         supplyItemList.forEach(supply -> supply.setProcurement(procurement));
	         procurement.setSupplies(supplyItemList);
	     }
		return procurement;
	}
	
	public ProcurementResponse toResponse(Procurement procurement) {
	    return new ProcurementResponse(procurement);
	}

    public List<ProcurementResponse> toResponseList(List<Procurement> procurementList) {
        return procurementList.stream()
                .map(this::toResponse)
                .toList();
    }
}
