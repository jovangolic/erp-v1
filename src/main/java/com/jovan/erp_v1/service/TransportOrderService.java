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
import com.jovan.erp_v1.exception.DriverErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.TransportOrderErrorException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.exception.VehicleErrorException;
import com.jovan.erp_v1.mapper.TransportOrderMapper;
import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.TransportOrder;
import com.jovan.erp_v1.model.Vehicle;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByPending() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOn_The_Way() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByCompleted() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByFailed() {
		// TODO Auto-generated method stub
		return null;
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
	public List<TransportOrderResponse> findByVehice_Available() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByVehice_In_Use() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByVehice_Under_Maintenance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByVehice_Out_Of_Service() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByVehice_Reserved() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByVehicleAndDriver(String vehicleModel, String driverName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Pending() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_In_Transit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Delivered() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Cancelled() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_DeliveryDate(LocalDate deliveryDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_DeliveryDateBetween(LocalDate deliveryDateStart,
			LocalDate deliveryDateEnd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_Id(Long buyerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_CompanyNameContainingIgnoreCase(
			String buyerCompanyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_PhoneNumberLikeIgnoreCase(String phoneNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_EmailLikeIgnoreCase(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_Address(String buyerAddrres) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_Buyer_PibLikeIgnoreCase(String buyerPib) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findDeliveryItemsByTransportOrderId(Long transportOrderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findAllWithDeliveryItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_DeliveryDateAfter(LocalDate deliveryAfter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByOutboundDelivery_DeliveryDateBefore(LocalDate deliveryBefore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransportOrderResponse findByVehicle_RegistrationNumber(String registrationNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean existsByVehice_RegistrationNumber(String registrationNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByStatus_AndVehicle_Status(TransportStatus transportStatus,
			VehicleStatus vehicleStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByStatusIn(List<TransportStatus> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByVehicle_StatusIn(List<VehicleStatus> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByVehicleAndStatusIn(Vehicle vehicle, List<TransportStatus> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findWithInactiveVehicles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByScheduledDateAfter(LocalDate scheduledDateAfter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportOrderResponse> findByScheduledDateBefore(LocalDate scheduledDateBefore) {
		// TODO Auto-generated method stub
		return null;
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
