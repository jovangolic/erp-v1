package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.DriverErrorException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.VehicleErrorException;
import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.TransportOrder;
import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.repository.DriversRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.repository.VehicleRepository;
import com.jovan.erp_v1.request.TransportOrderRequest;
import com.jovan.erp_v1.response.TransportOrderResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransportOrderMapper {

	private final VehicleRepository vehicleRepository;
	private final DriversRepository driversRepository;
	private final OutboundDeliveryRepository outRepo;
	
	public TransportOrder toEntity(TransportOrderRequest request) {
		TransportOrder to = new TransportOrder();
		to.setId(request.id());
		to.setScheduledDate(request.scheduledDate());
		Vehicle v = vehicleRepository.findById(request.vehicleId()).orElseThrow(() -> new VehicleErrorException("Vehicle not found"));
		Driver d = driversRepository.findById(request.driversId()).orElseThrow(() -> new DriverErrorException("Drover not found "));
		OutboundDelivery delivery = outRepo.findById(request.outboundDeliveryId()).orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found"));
		to.setVehicle(v);
		to.setOutboundDelivery(delivery);
		to.setDriver(d);
		to.setStatus(request.status());
		return to;
	}
	
	public TransportOrderResponse toResponse(TransportOrder to) {
		return new TransportOrderResponse(to);
	}
	
	public List<TransportOrderResponse> toResponseList(List<TransportOrder> tos){
		return tos.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
