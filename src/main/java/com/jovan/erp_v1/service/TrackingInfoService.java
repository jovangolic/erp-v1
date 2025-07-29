package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.LogisticsProviderErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.ShipmentNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.TrackingInfoErrorException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.TrackingInfoMapper;
import com.jovan.erp_v1.model.LogisticsProvider;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.Shipment;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.TrackingInfo;
import com.jovan.erp_v1.repository.LogisticsProviderRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.repository.ShipmentRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.TrackingInfoRepository;
import com.jovan.erp_v1.repository.specification.TrackingInfoSpecifications;
import com.jovan.erp_v1.request.TrackingInfoRequest;
import com.jovan.erp_v1.response.TrackingInfoResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackingInfoService implements ITrackingInfoService {

    private final TrackingInfoRepository trackingInfoRepository;
    private final TrackingInfoMapper infoMapper;
    private final StorageRepository storageRepository;
    private final LogisticsProviderRepository logisticsProviderRepository;
    private final OutboundDeliveryRepository outboundDeliveryRepository;
    private final ShipmentRepository shipmentRepository;

    @Transactional
    @Override
    public TrackingInfoResponse create(TrackingInfoRequest request) {
    	validateCreateTrackingInfoRequest(request);
    	Shipment ship = fetchShipmentId(request.shipmentId());
    	if (ship.getTrackingInfo() != null) {
		    throw new ValidationException("TrackingInfo already exists for this shipment.");
    	}
        TrackingInfo info = infoMapper.toEntity(request, ship);
        TrackingInfo saved = trackingInfoRepository.save(info);
        return infoMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public TrackingInfoResponse update(Long id, TrackingInfoRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        TrackingInfo info = trackingInfoRepository.findById(id)
                .orElseThrow(() -> new TrackingInfoErrorException("TrackingInfo not found " + id));
        validateUpdateTrackingInfoRequest(request, info);
        infoMapper.updateEntity(info, request);
        TrackingInfo update = trackingInfoRepository.save(info);
        return infoMapper.toResponse(update);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!trackingInfoRepository.existsById(id)) {
            throw new TrackingInfoErrorException("TrackingInbfo not found " + id);
        }
        trackingInfoRepository.deleteById(id);
    }

    @Override
    public TrackingInfoResponse findOneById(Long id) {
        TrackingInfo info = trackingInfoRepository.findById(id)
                .orElseThrow(() -> new TrackingInfoErrorException("TrackingInfo not found " + id));
        return new TrackingInfoResponse(info);
    }

    @Override
    public List<TrackingInfoResponse> findAll() {
    	List<TrackingInfo> items = trackingInfoRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("Data for tracking-info is empty");
    	}
        return items.stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public TrackingInfoResponse findByTrackingNumber(String trackingNumber) {
        TrackingInfo info = trackingInfoRepository.findByTrackingNumber(trackingNumber);
        if (info == null) {
            throw new TrackingInfoErrorException("Tracking info not found for tracking number " + trackingNumber);
        }
        return infoMapper.toResponse(info);
    }

    @Override
    public TrackingInfoResponse findByShipmentId(Long shipmentId) {
        TrackingInfo info = trackingInfoRepository.findByShipmentId(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found " + shipmentId));
        return infoMapper.toResponse(info);
    }

    @Override
    public List<TrackingInfoResponse> findByEstimatedDeliveryBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
    	List<TrackingInfo> items = trackingInfoRepository.findByEstimatedDeliveryBetween(start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("Tracking-info for estimated delivery between %s and %s is not found",
    				start.format(formatter),end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findByCurrentLocationAndCurrentStatus(String location, ShipmentStatus status) {
    	validateString(location);
    	validateShipmentStatus(status);
    	List<TrackingInfo> items = trackingInfoRepository.findByCurrentLocationAndCurrentStatus(location, status);
    	if(items.isEmpty()) {
    		String msg = String.format("Tracking-info for location %s and shipment status %s is not found",
    				location,status);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findByEstimatedDelivery(LocalDate date) {
    	if(date == null) {
    		throw new ValidationException("Date must not be null");
    	}
    	if (date.isBefore(LocalDate.now().minusDays(1))) {
    	    throw new IllegalArgumentException(date + " ne sme biti u prošlosti.");
    	}
    	List<TrackingInfo> items = trackingInfoRepository.findByEstimatedDelivery(date);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("Tracking-info for estimated delivery date %s is not found", date.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findAllByOrderByEstimatedDeliveryAsc() {
    	List<TrackingInfo> items = trackingInfoRepository.findAllByOrderByEstimatedDeliveryAsc();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("Tracking-info for estimated delivery in ascending order, is not found");
    	}
        return items.stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to) {
    	DateValidator.validateRange(from, to);
    	List<TrackingInfo> items = trackingInfoRepository.findByCreatedAtBetween(from, to);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    		String msg = String.format("Tracking-info for createdAt between %s and %s is not found",
    				from.format(formatter),to.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findByUpdatedAtBetween(LocalDateTime from, LocalDateTime to) {
    	DateValidator.validateRange(from, to);
    	List<TrackingInfo> items = trackingInfoRepository.findByUpdatedAtBetween(from, to);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    		String msg = String.format("Tracking-info for updatedAt between %s and %s is not found",
    				from.format(formatter),to.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findByUpdatedAtAfter(LocalDateTime since) {
    	DateValidator.validateNotInFuture(since, "Date after");
    	List<TrackingInfo> items = trackingInfoRepository.findByUpdatedAtAfter(since);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    		String msg = String.format("Tracking-info for updatedAt after %s is not found", since.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TrackingInfoResponse> findByStorageTypeAndStatus(StorageType type, StorageStatus status) {
    	validateStorageType(type);
        validateStorageStatus(status);
        Specification<TrackingInfo> spec = Specification.where(TrackingInfoSpecifications.hasOriginStorageType(type))
                                                        .and(TrackingInfoSpecifications.hasOriginStorageStatus(status));
        List<TrackingInfo> results = trackingInfoRepository.findAll(spec);
        return results.stream()
                      .map(TrackingInfoResponse::new)
                      .collect(Collectors.toList());
    }
    
    @Override
    public List<TrackingInfoResponse> findByTypeAndStatus(StorageType type, StorageStatus status) {
    	validateStorageType(type);
        validateStorageStatus(status);
        List<TrackingInfo> byType = trackingInfoRepository.findByShipment_OriginStorage_Type(type);
        List<TrackingInfo> byStatus = trackingInfoRepository.findByShipment_OriginStorage_Status(status);
        List<TrackingInfo> filtered = byType.stream()
                                            .filter(byStatus::contains)
                                            .collect(Collectors.toList());
        return filtered.stream()
                       .map(TrackingInfoResponse::new)
                       .collect(Collectors.toList());
    }
    
    //nove metode

	@Override
	public List<TrackingInfoResponse> findByPending() {
		List<TrackingInfo> items = trackingInfoRepository.findByPending();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Tracking-info for shipment-status 'Pending' is not found");
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipped() {
		List<TrackingInfo> items = trackingInfoRepository.findByShipped();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Tracking-info for shipment-status 'Shipped' is not found");
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByIn_Transit() {
		List<TrackingInfo> items = trackingInfoRepository.findByIn_Transit();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Tracking-info for shipment-status 'In_Transit' is not found");
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByDelivered() {
		List<TrackingInfo> items = trackingInfoRepository.findByDelivered();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Tracking-info for shipment-status 'Delivered' is not found");
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByDelayed() {
		List<TrackingInfo> items = trackingInfoRepository.findByDelayed();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Tracking-info for shipment-status 'Delayed' is not found");
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByCancelled() {
		List<TrackingInfo> items = trackingInfoRepository.findByCancelled();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Tracking-info for shipment-status 'Cancelled' is not found");
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_ShipmentDate(LocalDate shipmentDate) {
		DateValidator.validateNotInFuture(shipmentDate, "Shipment date");
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_ShipmentDate(shipmentDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("Tracking-info for shipment date %s is not found",
					shipmentDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_ShipmentDateBetween(LocalDate shipmentDateStart,
			LocalDate shipmentDateEnd) {
		DateValidator.validateRange(shipmentDateStart, shipmentDateEnd);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_ShipmentDateBetween(shipmentDateStart, shipmentDateEnd);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("Tracking-info for shipment date between %s and %s is not found",
					shipmentDateStart.format(formatter),shipmentDateEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_Provider_Id(Long providerId) {
		fetchLogisticsProviderId(providerId);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_Provider_Id(providerId);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for logistics provider %d is not found", providerId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_OutboundDelivery_Id(Long outboundDeliveryId) {
		fetchOutboundDeliveryId(outboundDeliveryId);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_OutboundDelivery_Id(outboundDeliveryId);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for outbound-delivery %d is not found", outboundDeliveryId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_OriginStorage_Id(Long originStorageId) {
		fetchStorageId(originStorageId);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_OriginStorage_Id(originStorageId);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for origin-storage %d is not found", originStorageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByTrackingNumberAndCurrentLocation(String trackingNumber,
			String currentLocation) {
		validateString(trackingNumber);
		validateString(currentLocation);
		List<TrackingInfo> items = trackingInfoRepository.findByTrackingNumberAndCurrentLocation(trackingNumber, currentLocation);
		if(items.isEmpty()) {
			String msg = String.format("Tracking-info for tracking-number %s and currebt-location %s is not found",
					trackingNumber,currentLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public Boolean existsByTrackingNumber(String trackingNumber) {
		validateString(trackingNumber);
		return trackingInfoRepository.existsByTrackingNumber(trackingNumber);
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_Provider_NameContainingIgnoreCase(String providerName) {
		validateString(providerName);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_Provider_NameContainingIgnoreCase(providerName);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for provider name %s is not found", providerName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_Provider_ContactPhoneLikeIgnoreCase(String contactPhone) {
		validateString(contactPhone);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_Provider_ContactPhoneLikeIgnoreCase(contactPhone);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for provider contact-phone %s is not found", contactPhone);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_Provider_EmailLikeIgnoreCase(String providerEmail) {
		validateString(providerEmail);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_Provider_EmailLikeIgnoreCase(providerEmail);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for provider email %s is not found", providerEmail);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_Provider_Website(String providerWebsite) {
		validateString(providerWebsite);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_Provider_Website(providerWebsite);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for provider website %s is not found", providerWebsite);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_OriginStorage_NameContainingIgnoreCase(String storageName) {
		validateString(storageName);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_OriginStorage_NameContainingIgnoreCase(storageName);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for origin-storage name %s is not found", storageName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_OriginStorage_LocationContainingIgnoreCase(
			String storageLocation) {
		validateString(storageLocation);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_OriginStorage_LocationContainingIgnoreCase(storageLocation);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for origin-storage location %s is not found", storageLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_OriginStorage_Capacity(BigDecimal storageCapacity) {
		validateBigDecimal(storageCapacity);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_OriginStorage_Capacity(storageCapacity);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for origin-storage capacity %s is not found", storageCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_OriginStorage_CapacityGreaterThan(BigDecimal storageCapacity) {
		validateBigDecimal(storageCapacity);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_OriginStorage_CapacityGreaterThan(storageCapacity);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for origin-storage with capacity greater than %s is not found", storageCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_OriginStorage_CapacityLessThan(BigDecimal storageCapacity) {
		validateBigDecimalNonNegative(storageCapacity);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_OriginStorage_CapacityLessThan(storageCapacity);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for origin-storage with capacity less than %s, is not found", storageCapacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_OriginStorage_Type(StorageType type) {
		validateStorageType(type);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_OriginStorage_Type(type);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for origin storage type %s, is not found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TrackingInfoResponse> findByShipment_OriginStorage_Status(StorageStatus status) {
		validateStorageStatus(status);
		List<TrackingInfo> items = trackingInfoRepository.findByShipment_OriginStorage_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("Shipment for origin storage status %s, is not found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(infoMapper::toResponse).collect(Collectors.toList());
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}
	
	private void validateStorageType(StorageType type) {
		if(type == null) {
			throw new ValidationException("StorageType type must not be null");
		}
	}
	
	private void validateStorageStatus(StorageStatus status) {
		if(status == null) {
			throw new ValidationException("StorageStatus status must not be null");
		}
	}
	
	private void validateBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Number must not be null nor negative");
		}
	}
	
	private Storage fetchStorageId(Long storageId) {
		if(storageId == null) {
			throw new ValidationException("Storage ID must not be null");
		}
		return storageRepository.findById(storageId).orElseThrow(() -> new StorageNotFoundException("Storage not found with id "+storageId));
	}
	
	private OutboundDelivery fetchOutboundDeliveryId(Long outId) {
		if(outId == null ) {
			throw new ValidationException("Outbound-delivery ID must not be null");
		}
		return outboundDeliveryRepository.findById(outId).orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found with id "+outId));
	}
	
	private LogisticsProvider fetchLogisticsProviderId(Long providerId) {
		if(providerId == null) {
			throw new ValidationException("LogisticsProvider ID must not be null");
		}
		return logisticsProviderRepository.findById(providerId).orElseThrow(() -> new LogisticsProviderErrorException("Logistics-provider not found with id "+providerId));
	}
	
	private void validateShipmentStatus(ShipmentStatus status) {
		if(status == null) {
			throw new ValidationException("ShipmentStatus status must not be null");
		}
	}
	
	private Shipment fetchShipmentId(Long shipId) {
		if(shipId == null) {
			throw new ValidationException("Shipment ID must not be null");
		}
		return shipmentRepository.findById(shipId).orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with id "+shipId));
	}
	
	
	private void validateUpdateTrackingInfoRequest(TrackingInfoRequest request, TrackingInfo trackingInfo) {
		if(request == null) {
			throw new ValidationException("TrackingInfoRequest must not be null");
		}
		if(request.trackingNumber() == null) {
			throw new ValidationException("Tracking number must not be null");
		}
		if (!trackingInfo.getTrackingNumber().equals(request.trackingNumber())
			    && trackingInfoRepository.existsByTrackingNumber(request.trackingNumber())) {
			    throw new ValidationException("Tracking number must be unique.");
		}
		validateString(request.currentLocation());
		if(request.estimatedDelivery() == null) {
    		throw new ValidationException("Date must not be null");
    	}
    	if (request.estimatedDelivery().isBefore(LocalDate.now().minusDays(1))) {
    	    throw new IllegalArgumentException(request.estimatedDelivery() + " must not be in past.");
    	}
    	validateShipmentStatus(request.currentStatus());
    	if(!trackingInfo.getShipment().getId().equals(request.shipmentId())) {
    		throw new ValidationException("Shipment ID cannot be changed once assigned.");
    	}
	}
	
	private void validateCreateTrackingInfoRequest(TrackingInfoRequest request) {
		if(request == null) {
			throw new ValidationException("TrackingInfoRequest must not be null");
		}
		if(request.trackingNumber() == null) {
			throw new ValidationException("Tracking number must not be null");
		}
		if (trackingInfoRepository.existsByTrackingNumber(request.trackingNumber())) {
		    throw new ValidationException("Tracking number must be unique.");
		}
		validateString(request.currentLocation());
		if(request.estimatedDelivery() == null) {
    		throw new ValidationException("Date must not be null");
    	}
    	if (request.estimatedDelivery().isBefore(LocalDate.now().minusDays(1))) {
    	    throw new IllegalArgumentException(request.estimatedDelivery() + " must not be in past.");
    	}
    	validateShipmentStatus(request.currentStatus());
	}
}
