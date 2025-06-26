package com.jovan.erp_v1.response;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.model.ProductionOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionOrderResponse {

    private Long id;
    private String orderNumber;
    private ProductResponse productResponse;
    private Integer quantityPlanned;
    private Integer quantityProduced;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProductionOrderStatus status;
    private WorkCenterResponse workCenterResponse;

    public ProductionOrderResponse(ProductionOrder order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.productResponse = new ProductResponse(order.getProduct());
        this.quantityPlanned = order.getQuantityPlanned();
        this.quantityProduced = order.getQuantityProduced();
        this.startDate = order.getStartDate();
        this.endDate = order.getEndDate();
        this.status = order.getStatus();
        this.workCenterResponse = new WorkCenterResponse(order.getWorkCenter());
    }
}
