package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.TransportStatus;
import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.model.TransportOrder;
import com.jovan.erp_v1.model.Vehicle;

@Repository
public interface TransportOrderRepository extends JpaRepository<TransportOrder, Long> {

    Optional<TransportOrder> findByVehicle_Model(String model);
    Optional<TransportOrder> findByDriver_Name(String name);
    Optional<TransportOrder> findByVehicleId(Long vehicleId);
    Optional<TransportOrder> findByDriverId(Long driversId);
    List<TransportOrder> findByStatus(TransportStatus status);
    List<TransportOrder> findByOutboundDelivery_Id(Long outboundDeliveryId);
    List<TransportOrder> findByOutboundDelivery_Status(DeliveryStatus status);
    List<TransportOrder> findByScheduledDateBetween(LocalDate from, LocalDate to);
    
    //nove metode
    List<TransportOrder> findByScheduledDate(LocalDate scheduleDate);
    @Query("SELECT to FROM TransportOrder to WHERE to.status = 'PENDING'")
    List<TransportOrder> findByPending();
    @Query("SELECT to FROM TransportOrder to WHERE to.status = 'ON_THE_WAY'")
    List<TransportOrder> findByOn_The_Way();
    @Query("SELECT to FROM TransportOrder to WHERE to.status = 'COMPLETED'")
    List<TransportOrder> findByCompleted();
    @Query("SELECT to FROM TransportOrder to WHERE to.status = 'FAILED'")
    List<TransportOrder> findByFailed();
    List<TransportOrder> findByVehicle_Status(VehicleStatus status);
    @Query("SELECT to FROM TransportOrder to WHERE to.vehicle.status = 'AVAILABLE'")
    List<TransportOrder> findByVehice_Available();
    @Query("SELECT to FROM TransportOrder to WHERE to.vehicle.status = 'IN_USE'")
    List<TransportOrder> findByVehice_In_Use();
    @Query("SELECT to FROM TransportOrder to WHERE to.vehicle.status = 'UNDER_MAINTENANCE'")
    List<TransportOrder> findByVehice_Under_Maintenance();
    @Query("SELECT to FROM TransportOrder to WHERE to.vehicle.status = 'OUT_OF_SERVICE'")
    List<TransportOrder> findByVehice_Out_Of_Service();
    @Query("SELECT to FROM TransportOrder to WHERE to.vehicle.status = 'RESERVED'")
    List<TransportOrder> findByVehice_Reserved();
    @Query("SELECT to FROM TransportOrder to WHERE to.vehicle.model = :vehicleModel AND to.driver.name = :driverName")
    List<TransportOrder> findByVehicleAndDriver(@Param("vehicleModel") String vehicleModel,@Param("driverName") String driverName);
    @Query("SELECT to FROM TransportOrder to WHERE to.outboundDelivery.status  ='PENDING'")
    List<TransportOrder> findByOutboundDelivery_Pending();
    @Query("SELECT to FROM TransportOrder to WHERE to.outboundDelivery.status  ='IN_TRANSIT'")
    List<TransportOrder> findByOutboundDelivery_In_Transit();
    @Query("SELECT to FROM TransportOrder to WHERE to.outboundDelivery.status  ='DELIVERED'")
    List<TransportOrder> findByOutboundDelivery_Delivered();
    @Query("SELECT to FROM TransportOrder to WHERE to.outboundDelivery.status  ='CANCELLED'")
    List<TransportOrder> findByOutboundDelivery_Cancelled();
    List<TransportOrder> findByOutboundDelivery_DeliveryDate(LocalDate deliveryDate);
    List<TransportOrder> findByOutboundDelivery_DeliveryDateBetween(LocalDate deliveryDateStart, LocalDate deliveryDateEnd);
    @Query("SELECT to FROM TransportOrder to WHERE to.outboundDelivery.buyer.id = :buyerId ")
    List<TransportOrder> findByOutboundDelivery_Buyer_Id(@Param("buyerId") Long buyerId); 
    @Query("SELECT to FROM TransportOrder to WHERE LOWER(to.outboundDelivery.buyer.companyName) LIKE LOWER (CONCAT('%', :buyerCompanyName,'%'))")
    List<TransportOrder> findByOutboundDelivery_Buyer_CompanyNameContainingIgnoreCase(@Param("buyerCompanyName") String buyerCompanyName);
    @Query("SELECT to FROM TransportOrder to WHERE LOWER(to.outboundDelivery.buyer.phoneNumber) LIKE LOWER (CONCAT('%', :phoneNumber,'%'))")
    List<TransportOrder> findByOutboundDelivery_Buyer_PhoneNumberLikeIgnoreCase(@Param("phoneNumber") String phoneNumber);
    @Query("SELECT to FROM TransportOrder to WHERE LOWER(to.outboundDelivery.buyer.email) LIKE LOWER (CONCAT('%', :email,'%'))")
    List<TransportOrder> findByOutboundDelivery_Buyer_EmailLikeIgnoreCase(@Param("email") String email);
    @Query("SELECT to FROM TransportOrder to WHERE LOWER(to.outboundDelivery.buyer.address) LIKE LOWER (CONCAT('%', :buyerAddrres,'%'))")
    List<TransportOrder> findByOutboundDelivery_Buyer_Address(@Param("buyerAddrres") String buyerAddrres);
    @Query("SELECT to FROM TransportOrder to WHERE LOWER(to.outboundDelivery.buyer.pib) LIKE LOWER (CONCAT('%', :buyerPib,'%'))")
    List<TransportOrder> findByOutboundDelivery_Buyer_PibLikeIgnoreCase(@Param("buyerPib") String buyerPib);
    @Query("SELECT di FROM TransportOrder to " +
    	       "JOIN to.outboundDelivery od " +
    	       "JOIN od.items di " +
    	       "WHERE to.id = :transportOrderId")
    List<TransportOrder> findDeliveryItemsByTransportOrderId(@Param("transportOrderId") Long transportOrderId);
    @Query("SELECT DISTINCT to FROM TransportOrder to " +
    	       "JOIN to.outboundDelivery od " +
    	       "JOIN od.items di")
    List<TransportOrder> findAllWithDeliveryItems();
    List<TransportOrder> findByOutboundDelivery_DeliveryDateAfter(LocalDate deliveryAfter);
    List<TransportOrder> findByOutboundDelivery_DeliveryDateBefore(LocalDate deliveryBefore);
    TransportOrder findByVehicle_RegistrationNumber(String registrationNumber);
    Boolean existsByVehice_RegistrationNumber(String registrationNumber);
    @Query("SELECT to FROM TransportOrder to WHERE to.status = :transportStatus AND to.vehicle.status = :vehicleStatus")
    List<TransportOrder> findByStatus_AndVehicle_Status(@Param("transportStatus") TransportStatus transportStatus,@Param("vehicleStatus") VehicleStatus vehicleStatus);
    List<TransportOrder> findByStatusIn(List<TransportStatus> statuses);
    List<TransportOrder> findByVehicle_StatusIn(List<VehicleStatus> statuses);
    List<TransportOrder> findByVehicleAndStatusIn(Vehicle vehicle, List<TransportStatus> statuses);
    @Query("SELECT to FROM TransportOrder to WHERE to.vehicle.status <> 'AVAILABLE'")
    List<TransportOrder> findWithInactiveVehicles();
    List<TransportOrder> findByScheduledDateAfter(LocalDate scheduledDateAfter);
    List<TransportOrder> findByScheduledDateBefore(LocalDate scheduledDateBefore);
}




