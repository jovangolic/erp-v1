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
	InventoryResponse findById(Long id);
    List<InventoryResponse> findAll(); 
    List<InventoryResponse> findByDate(LocalDate date);
    List<InventoryResponse> findByDateRange(LocalDate startDate, LocalDate endDate); 
    void changeStatus(Long inventoryId, InventoryStatus newStatus); 
    List<InventoryResponse> findPendingInventories();
    
    //nove metode
    List<InventoryResponse> findByDateAfter(LocalDate date);
    List<InventoryResponse> findByDateBefore(LocalDate date);
    List<InventoryResponse> findByStorageEmployee_FullNameContainingIgnoreCase(String firstName, String lastName);
    List<InventoryResponse> findBystorageForeman_FullNameContainingIgnoreCase( String firstName,String lastName);
    List<InventoryResponse> findByStorageEmployee_EmailILikegnoreCase( String email);
    List<InventoryResponse> findByStorageEmployee_Address(String address);
    List<InventoryResponse> findByStorageEmployee_PhoneNumberLikeIgnoreCase( String phoneNumber);
    List<InventoryResponse> findByStorageForeman_Address( String address);
    List<InventoryResponse> findByStorageForeman_PhoneNumberLikeIgnoreCase( String phoneNumber);
    List<InventoryResponse> findByStorageForeman_EmailLikeIgnoreCase( String email);
    List<InventoryResponse> findByStatusAndStorageEmployeeFullNameContainingIgnoreCase( InventoryStatus status, String firstName,String lastName);
    List<InventoryResponse> findByStatusAndStorageForemanFullNameContainingIgnoreCase( InventoryStatus status, String firstName, String lastName);
    List<InventoryResponse> findInventoryByStorageForemanIdAndDateRange( Long foremanId,LocalDate startDate, LocalDate endDate);
    List<InventoryResponse> findByStorageEmployeeIdAndStatus( Long employeeId,InventoryStatus status);
	List<InventoryResponse> findByStorageForemanIdAndStatus(Long foremanId, InventoryStatus status);
	List<InventoryResponse> findByStorageEmployeeIdAndDateBetween( Long employeeId, LocalDate startDate, LocalDate endDate);
	List<InventoryResponse> findByStorageForemanIdAndDateBetween( Long foremanId,LocalDate startDate, LocalDate endDate);
    Long countByStorageForemanId( Long foremanId);
    Boolean existsByStatus(InventoryStatus status);
}
