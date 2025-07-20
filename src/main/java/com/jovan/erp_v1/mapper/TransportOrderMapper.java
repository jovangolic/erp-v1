package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.TransportOrder;
import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.request.TransportOrderRequest;
import com.jovan.erp_v1.response.TransportOrderResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class TransportOrderMapper extends AbstractMapper<TransportOrderRequest> {

	public TransportOrder toEntity(TransportOrderRequest request,Vehicle v, Driver d, OutboundDelivery od) {
		Objects.requireNonNull(request, "TransportOrderRequest must not be null");
		Objects.requireNonNull(v, "Vehicle must not be null");
		Objects.requireNonNull(d, "Driver must not be null");
		Objects.requireNonNull(od, "OutboundDelivery must not be null");
		validateIdForCreate(request, TransportOrderRequest::id);
		TransportOrder to = new TransportOrder();
		to.setId(request.id());
		to.setScheduledDate(request.scheduledDate());
		to.setVehicle(v);
		to.setOutboundDelivery(od);
		to.setDriver(d);
		to.setStatus(request.status());
		return to;
	}
	
	public TransportOrder toEntityUpdate(TransportOrder to, TransportOrderRequest request,Vehicle v, Driver d, OutboundDelivery od) {
		Objects.requireNonNull(to, "TransportOrder must not be null");
		Objects.requireNonNull(request, "TransportOrderRequest must not be null");
		Objects.requireNonNull(v, "Vehicle must not be null");
		Objects.requireNonNull(d, "Driver must not be null");
		Objects.requireNonNull(od, "OutboundDelivery must not be null");
		validateIdForUpdate(request, TransportOrderRequest::id);
		to.setScheduledDate(request.scheduledDate());
		to.setVehicle(v);
		to.setDriver(d);
		to.setStatus(request.status());
		to.setOutboundDelivery(od);
		return to;
	}
	
	public TransportOrderResponse toResponse(TransportOrder to) {
		Objects.requireNonNull(to, "TransportOrder must not be null");
		return new TransportOrderResponse(to);
	}
	
	public List<TransportOrderResponse> toResponseList(List<TransportOrder> tos){
		if(tos == null || tos.isEmpty()) {
			return Collections.emptyList();
		}
		return tos.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
