package com.jovan.erp_v1.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.TransportStatus;
import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.exception.VehicleErrorException;
import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.repository.VehicleRepository;
import com.jovan.erp_v1.request.TransportOrderRequest;
import com.jovan.erp_v1.response.TransportOrderResponse;
import com.jovan.erp_v1.service.ITransportOrderService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transportOrders")
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
public class TransportOrderController {

    private final ITransportOrderService transportOrderService;
    private final VehicleRepository vehicleRepository;

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_FULL_ACCESS)
    @PostMapping("/create/new-transportOrder")
    public ResponseEntity<TransportOrderResponse> create(@Valid @RequestBody TransportOrderRequest request) {
        TransportOrderResponse response = transportOrderService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<TransportOrderResponse> update(@PathVariable Long id,
            @Valid @RequestBody TransportOrderRequest request) {
        TransportOrderResponse response = transportOrderService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transportOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<TransportOrderResponse> findOne(@PathVariable Long id) {
        TransportOrderResponse response = transportOrderService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<TransportOrderResponse>> findAll() {
        List<TransportOrderResponse> responses = transportOrderService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/vehicle-model")
    public ResponseEntity<TransportOrderResponse> findByVehicle_Model(@RequestParam("model") String model) {
        TransportOrderResponse response = transportOrderService.findByVehicle_Model(model);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/driver-name")
    public ResponseEntity<TransportOrderResponse> findByDriver_FirstNameContainingIgnoreCaseAndDriver_LastNameContainingIgnoreCase(@RequestParam("firstName") String firstName,
    		@RequestParam("lastName") String lastName) {
        TransportOrderResponse response = transportOrderService.findByDriver_FirstNameContainingIgnoreCaseAndDriver_LastNameContainingIgnoreCase(firstName, lastName);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<TransportOrderResponse> findByVehicleId(@PathVariable Long vehicleId) {
        TransportOrderResponse response = transportOrderService.findByVehicleId(vehicleId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/driver/{driversId}")
    public ResponseEntity<TransportOrderResponse> findByDriverId(@PathVariable Long driversId) {
        TransportOrderResponse response = transportOrderService.findByDriverId(driversId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/transport-status")
    public ResponseEntity<List<TransportOrderResponse>> findByStatus(@RequestParam("status") TransportStatus status) {
        List<TransportOrderResponse> responses = transportOrderService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outboundDelivery/{outboundDeliveryId}")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Id(
            @PathVariable Long outboundDeliveryId) {
        List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Id(outboundDeliveryId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/delivery-status")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Status(
            @RequestParam("status") DeliveryStatus status) {
        List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Status(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/date-range")
    public ResponseEntity<List<TransportOrderResponse>> findByScheduledDateBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<TransportOrderResponse> responses = transportOrderService.findByScheduledDateBetween(from, to);
        return ResponseEntity.ok(responses);
    }
    
    //nove metode
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/by-schedule-date")
    public ResponseEntity<List<TransportOrderResponse>> findByScheduledDate(@RequestParam("scheduleDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate scheduleDate){
    	List<TransportOrderResponse> responses = transportOrderService.findByScheduledDate(scheduleDate);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/transport-status-pending")
    public ResponseEntity<List<TransportOrderResponse>> findByPending(){
    	List<TransportOrderResponse> responses = transportOrderService.findByPending();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/transport-status-on-the-way")
    public ResponseEntity<List<TransportOrderResponse>> findByOn_The_Way(){
    	List<TransportOrderResponse> responses = transportOrderService.findByOn_The_Way();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/transport-status-completed")
    public ResponseEntity<List<TransportOrderResponse>> findByCompleted(){
    	List<TransportOrderResponse> responses = transportOrderService.findByCompleted();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/transport-status-failed")
    public ResponseEntity<List<TransportOrderResponse>> findByFailed(){
    	List<TransportOrderResponse> responses = transportOrderService.findByFailed();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/vehicle-status")
    public ResponseEntity<List<TransportOrderResponse>> findByVehicle_Status(@RequestParam("status") VehicleStatus status){
    	List<TransportOrderResponse> responses = transportOrderService.findByVehicle_Status(status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/vehicle-status/by-available")
    public ResponseEntity<List<TransportOrderResponse>> findByVehicle_Available(){
    	List<TransportOrderResponse> responses = transportOrderService.findByVehicle_Available();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/vehicle-status/by-in-use")
    public ResponseEntity<List<TransportOrderResponse>> findByVehicle_In_Use(){
    	List<TransportOrderResponse> responses = transportOrderService.findByVehicle_In_Use();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/vehicle-status/by-under-maintenance")
    public ResponseEntity<List<TransportOrderResponse>> findByVehicle_Under_Maintenance(){
    	List<TransportOrderResponse> responses = transportOrderService.findByVehicle_Under_Maintenance();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/vehicle-status/by-out-of-service")
    public ResponseEntity<List<TransportOrderResponse>> findByVehicle_Out_Of_Service(){
    	List<TransportOrderResponse> responses = transportOrderService.findByVehicle_Out_Of_Service();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/vehicle-status/bty-reserved")
    public ResponseEntity<List<TransportOrderResponse>> findByVehicle_Reserved(){
    	List<TransportOrderResponse> responses = transportOrderService.findByVehicle_Reserved();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/vehicle-model-and-driver-name")
    public ResponseEntity<List<TransportOrderResponse>> findByVehicleAndDriver(@RequestParam("vehicleModel") String vehicleModel,
    		@RequestParam("driverFirstName") String driverFirstName,@RequestParam("driverLastName") String driverLastName){
    	List<TransportOrderResponse> responses = transportOrderService.findByVehicleAndDriver(vehicleModel,driverFirstName,  driverLastName);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/delivery-status-pending")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Pending(){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Pending();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/delivery-status-in-transit")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_In_Transit(){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_In_Transit();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/delivery-status-delivered")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Delivered(){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Delivered();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/delivery-status--cancelled")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Cancelled(){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Cancelled();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/delivery-date")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_DeliveryDate(@RequestParam("deliveryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_DeliveryDate(deliveryDate);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/delivery-date-range")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_DeliveryDateBetween(@RequestParam("deliveryDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDateStart,
    		@RequestParam("deliveryDateEnd")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDateEnd){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_DeliveryDateBetween(deliveryDateStart, deliveryDateEnd);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/buyer/{buyerId}")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Buyer_Id(@PathVariable Long buyerId){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Buyer_Id(buyerId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/buyer-company-name")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Buyer_CompanyNameContainingIgnoreCase(@RequestParam("buyerCompanyName") String buyerCompanyName){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Buyer_CompanyNameContainingIgnoreCase(buyerCompanyName);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/phone-number")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Buyer_PhoneNumberLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Buyer_PhoneNumberLikeIgnoreCase(phoneNumber);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/buyer-email")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Buyer_EmailLikeIgnoreCase(@RequestParam("email") String email){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Buyer_EmailLikeIgnoreCase(email);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/buyer-address")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Buyer_Address(@RequestParam("buyerAddrres") String buyerAddrres){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Buyer_Address(buyerAddrres);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/buyer-pib")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Buyer_PibLikeIgnoreCase(@RequestParam("buyerPib") String buyerPib){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Buyer_PibLikeIgnoreCase(buyerPib);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/delivery-items/{transportOrderId}")
    public ResponseEntity<List<TransportOrderResponse>> findDeliveryItemsByTransportOrderId(@PathVariable Long transportOrderId){
    	List<TransportOrderResponse> responses = transportOrderService.findDeliveryItemsByTransportOrderId(transportOrderId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/all-delivery-items")
    public ResponseEntity<List<TransportOrderResponse>> findAllWithDeliveryItems(){
    	List<TransportOrderResponse> responses = transportOrderService.findAllWithDeliveryItems();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/delivery-after")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_DeliveryDateAfter(@RequestParam("deliveryAfter") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryAfter){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_DeliveryDateAfter(deliveryAfter);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/outbound-delivery/delivery-before")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_DeliveryDateBefore(@RequestParam("deliveryBefore") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryBefore){
    	List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_DeliveryDateBefore(deliveryBefore);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/vehicle-registration-number")
    public ResponseEntity<TransportOrderResponse> findByVehicle_RegistrationNumber(@RequestParam("registrationNumber") String registrationNumber){
    	TransportOrderResponse responses = transportOrderService.findByVehicle_RegistrationNumber(registrationNumber);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/exists-by-registration-number")
    public ResponseEntity<Boolean> existsByVehicle_RegistrationNumber(@RequestParam("registrationNumber") String registrationNumber){
    	Boolean responses = transportOrderService.existsByVehicle_RegistrationNumber(registrationNumber);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/transport-status-and-vehicle-status")
    public ResponseEntity<List<TransportOrderResponse>> findByStatus_AndVehicle_Status(@RequestParam("transportStatus") TransportStatus transportStatus,
    		@RequestParam("vehicleStatus") VehicleStatus vehicleStatus){
    	List<TransportOrderResponse> responses = transportOrderService.findByStatus_AndVehicle_Status(transportStatus, vehicleStatus);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/status-in-statuses")
    public ResponseEntity<List<TransportOrderResponse>> findByStatusIn(@RequestParam("statuses") List<TransportStatus> statuses){
    	List<TransportOrderResponse> responses = transportOrderService.findByStatusIn(statuses);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/vehicle-status-in-statuses")
    public ResponseEntity<List<TransportOrderResponse>> findByVehicle_StatusIn(@RequestParam("statuses") List<VehicleStatus> statuses){
    	List<TransportOrderResponse> responses = transportOrderService.findByVehicle_StatusIn(statuses);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/by-vehicle-and-statuses")
    public ResponseEntity<List<TransportOrderResponse>> findByVehicleAndStatusIn(@RequestParam("vehicleId") Long vehicleId,@RequestParam("statuses") List<TransportStatus> statuses){
    	Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new VehicleErrorException("Vehicle with id not found "+vehicleId));
    	List<TransportOrderResponse> responses = transportOrderService.findByVehicleAndStatusIn(vehicle, statuses);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/search/inactive-vehicles")
    public ResponseEntity<List<TransportOrderResponse>> findWithInactiveVehicles(){
    	List<TransportOrderResponse> responses = transportOrderService.findWithInactiveVehicles();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/scheduled-date-after")
    public ResponseEntity<List<TransportOrderResponse>> findByScheduledDateAfter(@RequestParam("scheduledDateAfter") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate scheduledDateAfter){
    	List<TransportOrderResponse> responses = transportOrderService.findByScheduledDateAfter(scheduledDateAfter);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRANSPORT_ORDER_READ_ACCESS)
    @GetMapping("/scheduled-date-before")
    public ResponseEntity<List<TransportOrderResponse>> findByScheduledDateBefore(@RequestParam("scheduledDateBefore") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate scheduledDateBefore){
    	List<TransportOrderResponse> responses = transportOrderService.findByScheduledDateBefore(scheduledDateBefore);
    	return ResponseEntity.ok(responses);
    }
    
}
