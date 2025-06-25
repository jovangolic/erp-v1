package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.MovementType;
import com.jovan.erp_v1.model.MaterialMovement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialMovementResponse {

    private Long id;
    private MaterialResponse material;
    private LocalDate movementDate;
    private MovementType type;
    private BigDecimal quantity;
    private StorageBasicResponse fromStorage;
    private StorageBasicResponse toStorage;

    public MaterialMovementResponse(MaterialMovement mat) {
        this.id = mat.getId();
        this.material = new MaterialResponse(mat.getMaterial());
        this.movementDate = mat.getMovementDate();
        this.type = mat.getType();
        this.quantity = mat.getQuantity();
        this.fromStorage = mat.getFromStorage() != null ? new StorageBasicResponse(mat.getFromStorage()) : null;
        this.toStorage = mat.getToStorage() != null ? new StorageBasicResponse(mat.getToStorage()) : null;
    }
}
