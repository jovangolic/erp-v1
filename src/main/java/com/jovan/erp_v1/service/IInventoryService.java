package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.request.InventoryRequest;
import com.jovan.erp_v1.response.InventoryResponse;

public interface IInventoryService {

	InventoryResponse create(InventoryRequest request);
	InventoryResponse update(Long id, InventoryRequest request);
	void delete(Long id);
	List<InventoryResponse> findInventoryByStatus(InventoryStatus status);
	List<InventoryResponse> findByStorageEmployeeId(Long storageEmpolyeeId);
	List<InventoryResponse> findByStorageForemanId(Long storageForemanId);
	InventoryResponse findById(Long id); // korisno za detaljan prikaz
    
    List<InventoryResponse> findAll(); // za prikaz svih inventura
    
    List<InventoryResponse> findByDate(LocalDate date); // filtriranje po datumu

    List<InventoryResponse> findByDateRange(LocalDate startDate, LocalDate endDate); // pretraga po opsegu

    void changeStatus(Long inventoryId, InventoryStatus newStatus); // korisno za prelazak inventure iz "DRAFT" u "FINAL"

    List<InventoryResponse> findPendingInventories(); // sve koje nisu aligned ili nisu FINAL
}
