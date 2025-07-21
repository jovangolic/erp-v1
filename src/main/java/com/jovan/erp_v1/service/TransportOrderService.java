package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.TransportStatus;
import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.DriverErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.TransportOrderErrorException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.exception.VehicleErrorException;
import com.jovan.erp_v1.mapper.TransportOrderMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.TransportOrder;
import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.DriversRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.repository.TransportOrderRepository;
import com.jovan.erp_v1.repository.VehicleRepository;
import com.jovan.erp_v1.request.TransportOrderRequest;
import com.jovan.erp_v1.response.TransportOrderResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransportOrderService implements ITransportOrderService {

    private final TransportOrderRepository transportOrderRepository;
    private final TransportOrderMapper transportOrderMapper;
    private final VehicleRepository vehicleRepository;
    private final DriversRepository driversRepository;
    private final OutboundDeliveryRepository outRepo;
    private final BuyerRepository buyerRepository;

    @Transactional
    @Override
    public TransportOrderResponse create(TransportOrderRequest request) {
    	DateValidator.validateNotInPast(request.scheduledDate(), "Schedule date");
        Vehicle v = fetchVehicleId(request.vehicleId());
        Driver d = fetchDriverId(request.driversId());
        validateTransportStatus(request.status());
        OutboundDelivery delivery = fetchOutboundDeliveryId(request.outboundDeliveryId());
        TransportOrder to = transportOrderMapper.toEntity(request, v, d, delivery);
        TransportOrder saved = transportOrderRepository.save(to);
        return transportOrderMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public TransportOrderResponse update(Long id, TransportOrderRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        TransportOrder to = transportOrderRepository.findById(id)
                .orElseThrow(() -> new TransportOrderErrorException("TransportOrder not found " + id));
        DateValidator.validateNotInPast(request.scheduledDate(), "Schedule date");
        Vehicle v = fetchVehicleId(request.vehicleId());
        Driver d = fetchDriverId(request.driversId());
        validateTransportStatus(request.status());
        OutboundDelivery delivery = fetchOutboundDeliveryId(request.outboundDeliveryId());
        transportOrderMapper.toEntityUpdate(to, request, v, d, delivery);
        TransportOrder updated = transportOrderRepository.save(to);
        return transportOrderMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!transportOrderRepository.existsById(id)) {
            throw new TransportOrderErrorException("TransportOrder not found " + id);
        }
        transportOrderRepository.deleteById(id);
    }

    @Override
    public TransportOrderResponse findOne(Long id) {
        TransportOrder to = transportOrderRepository.findById(id)
                .orElseThrow(() -> new TransportOrderErrorException("TransportOrder not found " + id));
        return new TransportOrderResponse(to);
    }

    @Override
    public List<TransportOrderResponse> findAll() {
    	List<TransportOrder> items = transportOrderRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("List of transport-order items is empty");
    	}
        return items.stream()
                .map(TransportOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public TransportOrderResponse findByVehicle_Model(String model) {
    	validateString(model);
        TransportOrder to = transportOrderRepository.findByVehicle_Model(model)
                .orElseThrow(() -> new VehicleErrorException("Vehicle model not found"));
        return new TransportOrderResponse(to);
    }

    @Override
    public TransportOrderResponse findByDriver_Name(String name) {
    	validateString(name);
        TransportOrder to = transportOrderRepository.findByDriver_Name(name)
                .orElseThrow(() -> new DriverErrorException("Driver's name not found"));
        return new TransportOrderResponse(to);
    }

    @Override
    public TransportOrderResponse findByVehicleId(Long vehicleId) {
    	if(vehicleId == null) {
    		throw new ValidationException("ID for vehicle, must not be null");
    	}
        TransportOrder to = transportOrderRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleErrorException("Vehicle not found"));
        return new TransportOrderResponse(to);

    }

    @Override
    public TransportOrderResponse findByDriverId(Long driversId) {
    	if(driversId == null) {
    		throw new ValidationException("ID for driver must not be null");
    	}
        TransportOrder to = transportOrderRepository.findById(driversId)
                .orElseThrow(() -> new DriverErrorException("Driver not found"));
        return new TransportOrderResponse(to);
    }

    @Override
    public List<TransportOrderResponse> findByStatus(TransportStatus status) {
    	validateTransportStatus(status);
    	List<TransportOrder> items = transportOrderRepository.findByStatus(status);
    	if(items.isEmpty()) {
    		String msg = String.format("TransportStatus for status %s is not found", status);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(TransportOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransportOrderResponse> findByOutboundDelivery_Id(Long outboundDeliveryId) {
    	fetchOutboundDeliveryId(outboundDeliveryId);
    	List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_Id(outboundDeliveryId);
    	if(items.isEmpty()) {
    		String msg = String.format("Transport-order for outboundDelivery %d is not found", outboundDeliveryId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(TransportOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransportOrderResponse> findByOutboundDelivery_Status(DeliveryStatus status) {
    	validateDeliveryStatus(status);
    	List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_Status(status);
    	if(items.isEmpty()) {
    		String msg = String.format("Transport-order for delivery status %s is not found", status);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(TransportOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransportOrderResponse> findByScheduledDateBetween(LocalDate from, LocalDate to) {
    	DateValidator.validateRange(from, to);
    	List<TransportOrder> items = transportOrderRepository.findByScheduledDateBetween(from, to);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("Transport-order for schedule date between %s and %s is not found", 
    				from.format(formatter),to.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(TransportOrderResponse::new)
                .collect(Collectors.toList());
    }
    
    //nove metode

	@Override
	public List<TransportOrderResponse> findByScheduledDate(LocalDate scheduleDate) {
		DateValidator.validateNotInPast(scheduleDate, "Schedule date");
		List<TransportOrder> items = transportOrderRepository.findByScheduledDate(scheduleDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("TransportOrder for schedule date %s is not found", scheduleDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByPending() {
		List<TransportOrder> items = transportOrderRepository.findByPending();
		if(items.isEmpty()) {
			throw new NoDataFoundException("TransportStatus equal to 'PENDING' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOn_The_Way() {
		List<TransportOrder> items = transportOrderRepository.findByOn_The_Way();
		if(items.isEmpty()) {
			throw new NoDataFoundException("TransportStatus equal to 'ON_THE_WAY' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByCompleted() {
		List<TransportOrder> items = transportOrderRepository.findByCompleted();
		if(items.isEmpty()) {
			throw new NoDataFoundException("TransportStatus equal to 'COMPLETED' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByFailed() {
		List<TransportOrder> items = transportOrderRepository.findByFailed();
		if(items.isEmpty()) {
			throw new NoDataFoundException("TransportStatus equal to 'FAIL' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByVehicle_Status(VehicleStatus status) {
		validateVehicleStatus(status);
		List<TransportOrder> items = transportOrderRepository.findByVehicle_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("Transport-order for vehicle status %s is not found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByVehicle_Available() {
		List<TransportOrder> items = transportOrderRepository.findByVehicle_Available();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Vehicle status equal to 'AVAILABLE' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByVehicle_In_Use() {
		List<TransportOrder> items = transportOrderRepository.findByVehicle_In_Use();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Vehicle status equal to 'IN_USE' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByVehicle_Under_Maintenance() {
		List<TransportOrder> items = transportOrderRepository.findByVehicle_Under_Maintenance();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Vehicle status equal to 'UNDER_MAINTENANCE' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByVehicle_Out_Of_Service() {
		List<TransportOrder> items = transportOrderRepository.findByVehicle_Out_Of_Service();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Vehicle status equal to 'OUT_OF_SERVICE' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByVehicle_Reserved() {
		List<TransportOrder> items = transportOrderRepository.findByVehicle_Reserved();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Vehicle status equal to 'RESERVED' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByVehicleAndDriver(String vehicleModel, String driverName) {
		validateString(vehicleModel);
		validateString(driverName);
		List<TransportOrder> items = transportOrderRepository.findByVehicleAndDriver(vehicleModel, driverName);
		if(items.isEmpty()) {
			String msg = String.format("TransportOrder for vehicle model %s and driver name %s is not found",
					vehicleModel,driverName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Pending() {
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_Pending();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Delivery status equal to 'PENDING' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_In_Transit() {
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_In_Transit();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Delivery status equal to 'IN_TRANSIT' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Delivered() {
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_Delivered();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Delivery status equal to 'DELIVERED' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Cancelled() {
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_Cancelled();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Delivery status equat to 'CANCELLED' is not found for TransportOrder");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_DeliveryDate(LocalDate deliveryDate) {
		DateValidator.validateNotInPast(deliveryDate, "Delivery data");
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_DeliveryDate(deliveryDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("TransportOrder for outbound-delivery date %s is not found", deliveryDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_DeliveryDateBetween(LocalDate deliveryDateStart,
			LocalDate deliveryDateEnd) {
		DateValidator.validateRange(deliveryDateStart, deliveryDateEnd);
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_DeliveryDateBetween(deliveryDateStart, deliveryDateEnd);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("TransportOrder for outbound delivery date between %s and %s is not found",
					deliveryDateStart.format(formatter),deliveryDateEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_Id(Long buyerId) {
		fetchBuyerId(buyerId);
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_Buyer_Id(buyerId);
		if(items.isEmpty()) {
			String msg = String.format("TransportOrder for outbound-delivery equal to %s is not found", buyerId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_CompanyNameContainingIgnoreCase(
			String buyerCompanyName) {
		validateString(buyerCompanyName);
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_Buyer_CompanyNameContainingIgnoreCase(buyerCompanyName);
		if(items.isEmpty()) {
			String msg = String.format("TransportOrder for buyer company name %s is not found", buyerCompanyName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_PhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_Buyer_PhoneNumberLikeIgnoreCase(phoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("TransportOrder for buyer phone-number %s is not found", phoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_EmailLikeIgnoreCase(String email) {
		validateString(email);
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_Buyer_EmailLikeIgnoreCase(email);
		if(items.isEmpty()) {
			String msg = String.format("TransportOrder for buyer email %s is not found", email);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_Address(String buyerAddrres) {
		validateString(buyerAddrres);
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_Buyer_Address(buyerAddrres);
		if(items.isEmpty()) {
			String msg = String.format("TransportOrder for buyer address %s is not found", buyerAddrres);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_PibLikeIgnoreCase(String buyerPib) {
		validateString(buyerPib);
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_Buyer_PibLikeIgnoreCase(buyerPib);
		if(items.isEmpty()) {
			String msg = String.format("TransportOrder for buyer-pib %s is not found", buyerPib);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findDeliveryItemsByTransportOrderId(Long transportOrderId) {
		fetchTransportOrderId(transportOrderId);
		List<TransportOrder> items = transportOrderRepository.findDeliveryItemsByTransportOrderId(transportOrderId);
		if(items.isEmpty()) {
			String msg = String.format("Delivery items not found for TransportOrder %d", transportOrderId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findAllWithDeliveryItems() {
		List<TransportOrder> items = transportOrderRepository.findAllWithDeliveryItems();
		if(items.isEmpty()) {
			throw new NoDataFoundException("TransportOrder for delivery items list is empty");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_DeliveryDateAfter(LocalDate deliveryAfter) {
		DateValidator.validateNotInPast(deliveryAfter, "Delivery date after");
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_DeliveryDateAfter(deliveryAfter);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("TransportOrder for outbound delivery date after %s is not found", deliveryAfter.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_DeliveryDateBefore(LocalDate deliveryBefore) {
		DateValidator.validateNotInFuture(deliveryBefore, "Delivery date before");
		List<TransportOrder> items = transportOrderRepository.findByOutboundDelivery_DeliveryDateBefore(deliveryBefore);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("TransportOrder for outbound delivery date before %s is not found", deliveryBefore.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public TransportOrderResponse findByVehicle_RegistrationNumber(String registrationNumber) {
		validateString(registrationNumber);
		TransportOrder items = transportOrderRepository.findByVehicle_RegistrationNumber(registrationNumber);
		if(items == null) {
			String msg = String.format("TransportOrder for vehicle registration-number %s is not found", registrationNumber);
			throw new NoDataFoundException(msg);
		}
		return new TransportOrderResponse(items);
	}

	@Override
	public Boolean existsByVehicle_RegistrationNumber(String registrationNumber) {
		validateString(registrationNumber);
		return transportOrderRepository.existsByVehicle_RegistrationNumber(registrationNumber);
	}

	@Override
	public List<TransportOrderResponse> findByStatus_AndVehicle_Status(TransportStatus transportStatus,
			VehicleStatus vehicleStatus) {
		validateTransportStatus(transportStatus);
		validateVehicleStatus(vehicleStatus);
		List<TransportOrder> items = transportOrderRepository.findByStatus_AndVehicle_Status(transportStatus, vehicleStatus);
		if(items.isEmpty()) {
			String msg = String.format("Transport order for transport status %s and vehicle status %s is not found",
					transportStatus,vehicleStatus);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByStatusIn(List<TransportStatus> statuses) {
		validateEnumList(statuses, "Transport Status");
		List<TransportOrder> items = transportOrderRepository.findByStatusIn(statuses);
		if(items.isEmpty()) {
			throw new NoDataFoundException("TransportStatus list is empty");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByVehicle_StatusIn(List<VehicleStatus> statuses) {
		validateEnumList(statuses, "Vehicle status");
		List<TransportOrder> items = transportOrderRepository.findByVehicle_StatusIn(statuses);
		if(items.isEmpty()) {
			throw new NoDataFoundException("VehicleStatus list is empty");
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByVehicleAndStatusIn(Vehicle vehicle, List<TransportStatus> statuses) {
		if(vehicle == null) {
			throw new ValidationException("Vehicle must not be null");
		}
		validateEnumList(statuses, "Transport Status");
		List<TransportOrder> items = transportOrderRepository.findByVehicleAndStatusIn(vehicle, statuses);
		if(items.isEmpty()) {
			String msg = String.format("Transport-order for vehicle %s and statuses %s is not found", vehicle,statuses);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findWithInactiveVehicles() {
		List<TransportOrder> items = transportOrderRepository.findWithInactiveVehicles();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Transport-order for inactive vehicles are not found"); 
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByScheduledDateAfter(LocalDate scheduledDateAfter) {
		DateValidator.validateNotInPast(scheduledDateAfter, "Schedule date after");
		List<TransportOrder> items = transportOrderRepository.findByScheduledDateAfter(scheduledDateAfter);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("Transport-order for schedule date after %s is not found", scheduledDateAfter.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransportOrderResponse> findByScheduledDateBefore(LocalDate scheduledDateBefore) {
		DateValidator.validateNotInFuture(scheduledDateBefore, "Schedule date before");
		List<TransportOrder> items = transportOrderRepository.findByScheduledDateBefore(scheduledDateBefore);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("Transport-order for schedule date before %s is not found", scheduledDateBefore.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transportOrderMapper::toResponse).collect(Collectors.toList());
	}
	
	private TransportOrder fetchTransportOrderId(Long transportOrderid) {
		if(transportOrderid == null) {
			throw new ValidationException("TransportOrder ID must not be null");
		}
		return transportOrderRepository.findById(transportOrderid).orElseThrow(() -> new TransportOrderErrorException("TransportOrder not found with id "+transportOrderid));
	}
	
	private <T extends Enum<T>> void validateEnumList(List<T> statuses, String enumName) {
		if (statuses == null || statuses.isEmpty()) {
	        throw new ValidationException(enumName + " list must not be null nor empty");
	    }
		for(T status : statuses) {
			if(status == null) {
				throw new ValidationException(enumName+" value must not be null");
			}
		}
	}
	
	private Buyer fetchBuyerId(Long buyerId) {
		if(buyerId == null) {
			throw new ValidationException("Buyer ID must not be null");
		}
		return buyerRepository.findById(buyerId).orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id "+buyerId));
	}
	
	private Vehicle fetchVehicleId(Long vehicleId) {
		if(vehicleId == null) {
			throw new ValidationException("Vehicle ID must not be null");
		}
		return vehicleRepository.findById(vehicleId).orElseThrow(() -> new VehicleErrorException("Vehicle not found with id "+vehicleId));
	}
	
	private Driver fetchDriverId(Long driverId) {
		if(driverId == null) {
			throw new ValidationException("Drive ID must not be null");
		}
		return driversRepository.findById(driverId).orElseThrow(() -> new DriverErrorException("Driver not found with id "+driverId));
	}
	
	private OutboundDelivery fetchOutboundDeliveryId(Long outboundDeliveryId) {
		if(outboundDeliveryId == null) {
			throw new ValidationException("OutboundDelivery ID must not be null");
		}
		return outRepo.findById(outboundDeliveryId).orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found with id "+outboundDeliveryId));
	}
	
	private void validateTransportStatus(TransportStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("TransportStatus must not be null"));
	}
	
	private void validateDeliveryStatus(DeliveryStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("DeliveryStatus status must not be null"));
	}
	
	private void validateVehicleStatus(VehicleStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("VehicleStatus status must not br null"));
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not ne null nor empty");
		}
	}

}
