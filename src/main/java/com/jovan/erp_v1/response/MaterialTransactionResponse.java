package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.enumeration.MaterialTransactionType;
import com.jovan.erp_v1.model.MaterialTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialTransactionResponse {

    private Long id;
    private MaterialResponse materialResponse;
    private BigDecimal quantity;
    private MaterialTransactionType type;
    private LocalDate transactionDate;
    private VendorResponse vendorResponse;
    private String documentReference;
    private String notes;
    private MaterialTransactionStatus status;
    private UserResponse userResponse;

    public MaterialTransactionResponse(MaterialTransaction mt) {
        this.id = mt.getId();
        this.materialResponse = mt.getMaterial() != null ? new MaterialResponse(mt.getMaterial()) : null;
        this.quantity = mt.getQuantity();
        this.type = mt.getType();
        this.transactionDate = mt.getTransactionDate();
        this.vendorResponse = mt.getVendor() != null ? new VendorResponse(mt.getVendor()) : null;
        this.documentReference = mt.getDocumentReference();
        this.notes = mt.getNotes();
        this.status = mt.getStatus();
        this.userResponse = mt.getCreatedByUser() != null ? new UserResponse(mt.getCreatedByUser()) : null;
    }
}
