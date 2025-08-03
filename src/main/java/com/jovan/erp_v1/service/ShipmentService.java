package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.LogisticsProviderErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.ShipmentNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.TrackingInfoErrorException;
import com.jovan.erp_v1.mapper.ShipmentMapper;
import com.jovan.erp_v1.mapper.TrackingInfoMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.EventLog;
import com.jovan.erp_v1.model.LogisticsProvider;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.Shipment;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.TrackingInfo;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.EventLogRepository;
import com.jovan.erp_v1.repository.LogisticsProviderRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.repository.ShipmentRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.TrackingInfoRepository;
import com.jovan.erp_v1.request.EventLogRequest;
import com.jovan.erp_v1.request.ShipmentRequest;
import com.jovan.erp_v1.request.TrackingInfoRequest;
import com.jovan.erp_v1.response.ShipmentResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipmentService implements IShipmentService {

        private final ShipmentRepository shipmentRepository;
        private final StorageRepository storageRepository;
        private final LogisticsProviderRepository providerRepository;
        private final OutboundDeliveryRepository deliveryRepository;
        private final ShipmentMapper shipmentMapper;
        private final BuyerRepository buyerRepository;
        private final TrackingInfoRepository trackingInfoRepository;
        private final EventLogRepository eventLogRepository;
        private final TrackingInfoMapper trackingInfoMapper;

        @Transactional
        @Override
        public ShipmentResponse create(ShipmentRequest request) {
        	DateValidator.validateNotNull(request.shipmentDate(), "Shipment date");
            validateShipmentStatus(request.status());
            Storage storage = fetchStorageId(request.originStorageId());
            LogisticsProvider provider = fetchLogisticsProviderId(request.providerId());
            OutboundDelivery out = fetchOutboundDeliveryId(request.outboundDeliveryId());
            validateEventLogRequest(request.eventLogRequest(), false);
            Shipment ship = shipmentMapper.toEntity(request, provider, out, storage);
            Shipment saved = shipmentRepository.save(ship);
            EventLog log = new EventLog();
            log.setDescription("Shipment created");
            log.setShipment(saved);
            eventLogRepository.save(log);
            return new ShipmentResponse(saved);
        }

        @Transactional
        @Override
        public ShipmentResponse update(Long id, ShipmentRequest request) {
        	if (!request.id().equals(id)) {
    			throw new IllegalArgumentException("ID in path and body do not match");
    		}
        	Shipment ship = shipmentRepository.findById(id)
                    .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found " + id));
		    DateValidator.validateNotNull(request.shipmentDate(), "Shipment date");
		    validateShipmentStatus(request.status());
		    Storage storage = ship.getOriginStorage();
		    if(request.originStorageId() != null && (storage.getId() == null || !request.originStorageId().equals(storage.getId()))) {
		    	storage = fetchStorageId(request.originStorageId());
		    }
		    LogisticsProvider provider = ship.getProvider();
		    if(request.providerId() != null && (provider.getId() == null || !request.providerId().equals(provider.getId()))) {
		    	provider = fetchLogisticsProviderId(request.providerId());
		    }
		    OutboundDelivery out = ship.getOutboundDelivery();
		    if(request.outboundDeliveryId() != null && (out.getId() == null || !request.outboundDeliveryId().equals(out.getId()))) {
		    	out = fetchOutboundDeliveryId(request.outboundDeliveryId());
		    }
		    validateEventLogRequest(request.eventLogRequest(), true);
		    TrackingInfoRequest infoReq = request.trackingInfo();
		    TrackingInfo info = ship.getTrackingInfo();
		    if (info == null) {
		            info = new TrackingInfo();
		            info.setShipment(ship);
		    }
		    trackingInfoMapper.updateEntity(info, infoReq);
		    Shipment saved = shipmentRepository.save(ship);
		    EventLog log = new EventLog();
		    log.setTimestamp(LocalDateTime.now());
		    log.setDescription("Shipment update - status changed to " + request.status());
		    log.setShipment(saved);
		    eventLogRepository.save(log);
		    return new ShipmentResponse(saved);
        }

        @Transactional
        @Override
        public void delete(Long id) {
        	if (!shipmentRepository.existsById(id)) {
                throw new ShipmentNotFoundException("Shipment not found " + id);
        	}
        	shipmentRepository.deleteById(id);
        }

        @Override
        public List<ShipmentResponse> findByStatus(ShipmentStatus status) {
        	validateShipmentStatus(status);
        	List<Shipment> shipments = shipmentRepository.findByStatus(status);
        	if(shipments.isEmpty()) {
        		String msg = String.format("No trackingInfo found for shipment status %s", status);
        		throw new NoDataFoundException(msg);
        	}
            return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> findByShipmentDateBetween(LocalDate from, LocalDate to) {
        	DateValidator.validateRange(from, to);
        	List<Shipment> shipments = shipmentRepository.findByShipmentDateBetween(from, to);
        	if(shipments.isEmpty()) {
        		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        		String msg = String.format("No shipments found for date between %s and %s.", 
        				from.format(formatter),to.format(formatter));
        		throw new NoDataFoundException(msg);
        	}
                return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> findByProviderId(Long providerId) {
        	fetchLogisticsProviderId(providerId);
        	List<Shipment> shipments = shipmentRepository.findByProviderId(providerId);
        	if(shipments.isEmpty()) {
        		String msg = String.format("Logistics provider ID not found %s", providerId);
        		throw new NoDataFoundException(msg);
        	}
                return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> findByProvider_NameContainingIgnoreCase(String name) {
        	validateString(name);
        	List<Shipment> shipments = shipmentRepository.findByProvider_NameContainingIgnoreCase(name);
        	if(shipments.isEmpty()) {
        		String msg = String.format("Logistics-provider name not found %s", name);
        		throw new NoDataFoundException(msg);
        	}
                return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public ShipmentResponse findByOutboundDeliveryId(Long outboundId) {
        	fetchOutboundDeliveryId(outboundId);
            Shipment s = shipmentRepository.findByOutboundDeliveryId(outboundId);
            return new ShipmentResponse(s);
        }

        @Override
        public List<ShipmentResponse> findByTrackingInfo_CurrentStatus(ShipmentStatus status) {
        	validateShipmentStatus(status);
        	List<Shipment> shipments = shipmentRepository.findByTrackingInfo_CurrentStatus(status);
        	if(shipments.isEmpty()) {
        		String msg = String.format("No trackingInfo found for shipment status %s", status);
        		throw new NoDataFoundException(msg);
        	}
                return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> findByTrackingInfoId(Long trackingInfoId) {
        	fetchTrackingInfoId(trackingInfoId);
        	List<Shipment> shipments = shipmentRepository.findByTrackingInfoId(trackingInfoId);
        	if(shipments.isEmpty()) {
        		String msg = String.format("TrackingInfo ID not found %s", trackingInfoId);
        		throw new NoDataFoundException(msg);
        	}
                return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> findByOriginStorageId(Long storageId) {
        	fetchStorageId(storageId);
        	List<Shipment> shipments =shipmentRepository.findByOriginStorageId(storageId);
        	if(shipments.isEmpty()) {
        		String msg = String.format("Storage ID for origin storage not found %s", storageId);
        		throw new NoDataFoundException(msg);
        	}
                return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> findByOriginStorage_Name(String name) {
        	validateString(name);
        	List<Shipment> shipments = shipmentRepository.findByOriginStorage_Name(name);
        	if(shipments.isEmpty()) {
        		String msg = String.format("Origin storage name not found %s", name);
        		throw new NoDataFoundException(msg);
        	}
                return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> findByOriginStorage_Location(String location) {
        	validateString(location);
        	List<Shipment> shipments = shipmentRepository.findByOriginStorage_Location(location);
        	if(shipments.isEmpty()) {
        		String msg = String.format("Location for origin storage not found %s", location);
        		throw new NoDataFoundException(msg);
        	}
                return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> findByOriginStorage_Type(StorageType type) {
        	validateStorageType(type);
        	List<Shipment> shipments = shipmentRepository.findByOriginStorage_Type(type);
        	if(shipments.isEmpty()) {
        		String msg = String.format("No type for origin storage found %s", type);
        		throw new NoDataFoundException(msg);
        	}
                return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> findByOriginStorageIdAndStatus(Long storageId, ShipmentStatus status) {
        	fetchStorageId(storageId);
        	List<Shipment> shipments = shipmentRepository.findByOriginStorageIdAndStatus(storageId, status);
        	if(shipments.isEmpty()) {
        		String msg = String.format("No shipments found with status %s for storage ID %d.", status, storageId);
        		throw new NoDataFoundException(msg);
        	}
            return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> searchByOriginStorageName(String name) {
        	validateString(name);
        	List<Shipment> shipments = shipmentRepository.searchByOriginStorageName(name);
        	if(shipments.isEmpty()) {
        		String msg = String.format("Given name for origin storage not found %s", name);
        		throw new NoDataFoundException(msg);
        	}
                return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> findByTrackingInfo_CurrentLocationContainingIgnoreCase(String location) {
        	validateString(location);
        	List<Shipment> shipments = shipmentRepository.findByTrackingInfo_CurrentLocationContainingIgnoreCase(location);
        	if(shipments.isEmpty()) {
        		String msg = String.format("No tracking-info found for current location %s", location);
        		throw new NoDataFoundException(msg);
        	}
            return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<ShipmentResponse> findOverdueDelayedShipments() {
        	List<Shipment> shipments = shipmentRepository.findOverdueDelayedShipments();
        	if(shipments.isEmpty()) {
        		throw new NoDataFoundException("No shipments found for overdue delayed");
        	}
            return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public ShipmentResponse findByOneId(Long id) {
                Shipment s = shipmentRepository.findById(id)
                                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found " + id));
                return new ShipmentResponse(s);
        }

        @Override
        public List<ShipmentResponse> findAll() {
        	List<Shipment> shipments = shipmentRepository.findAll();
        	if(shipments.isEmpty()) {
        		throw new NoDataFoundException("No shipments found");
        	}
                return shipments.stream()
                                .map(ShipmentResponse::new)
                                .collect(Collectors.toList());
        }

		@Override
		public List<ShipmentResponse> findByTrackingInfo_TrackingNumber(String trackingNumber) {
			validateString(trackingNumber);
			List<Shipment> shipments = shipmentRepository.findByTrackingInfo_TrackingNumber(trackingNumber);
			if(shipments.isEmpty()) {
				String msg = String.format("No shipment found with tracking number %s.", trackingNumber);
				throw new NoDataFoundException(msg);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByTrackingInfo_EstimatedDelivery(LocalDate estimatedDelivery) {
			DateValidator.validateFutureOrPresent(estimatedDelivery, "Estimated delivery date");
			List<Shipment> shipments = shipmentRepository.findByTrackingInfo_EstimatedDelivery(estimatedDelivery);
			if(shipments.isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				String msg = String.format("No tracking-info found for estimated delivery date %s", estimatedDelivery.format(formatter));
				throw new NoDataFoundException(msg);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByTrackingInfo_EstimatedDeliveryBetween(LocalDate estimatedDeliveryStart,
				LocalDate estimatedDeliveryEnd) {
			DateValidator.validateRange(estimatedDeliveryStart, estimatedDeliveryEnd);
			List<Shipment> shipments = shipmentRepository.findByTrackingInfo_EstimatedDeliveryBetween(estimatedDeliveryStart, estimatedDeliveryEnd);
			if(shipments.isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				String msg = String.format(
					    "No tracking-info found for estimated delivery between %s and %s.",
					    estimatedDeliveryStart.format(formatter), estimatedDeliveryEnd.format(formatter)
					);
				throw new NoDataFoundException(msg);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByProvider_EmailLikeIgnoreCase(String email) {
			validateString(email);
			List<Shipment> shipments = shipmentRepository.findByProvider_EmailLikeIgnoreCase(email);
			if(shipments.isEmpty()) {
				String msg = String.format("No email %s found for logistics provider: ", email);
				throw new NoDataFoundException(msg);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByProvider_WebsiteContainingIgnoreCase(String website) {
			validateString(website);
			List<Shipment> shipments = shipmentRepository.findByProvider_WebsiteContainingIgnoreCase(website);
			if(shipments.isEmpty()) {
				String msg = String.format("No given website %s found for logistics provider: ", website);
				throw new NoDataFoundException(msg);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByProvider_ContactPhoneLikeIgnoreCase(String phoneNumber) {
			validateString(phoneNumber);
			List<Shipment> shipments = shipmentRepository.findByProvider_ContactPhoneLikeIgnoreCase(phoneNumber);
			if(shipments.isEmpty()) {
				String msg = String.format("No given phone-number %s found for logistics provider", phoneNumber);
				throw new NoDataFoundException(msg);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByOutboundDelivery_DeliveryDate(LocalDate deliveryDate) {
			DateValidator.validateNotInFuture(deliveryDate, "Delivery date");
			List<Shipment> shipments = shipmentRepository.findByOutboundDelivery_DeliveryDate(deliveryDate);
			if(shipments.isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				String msg = String.format("No outbound-delivery found for deliveryDate %s: ", deliveryDate.format(formatter));
				throw new NoDataFoundException(msg);
			}
 			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByOutboundDelivery_DeliveryDateBetween(LocalDate deliveryDateStart,
				LocalDate deliveryDateEnd) {
			DateValidator.validateRange(deliveryDateStart, deliveryDateEnd);
			List<Shipment> shipments = shipmentRepository.findByOutboundDelivery_DeliveryDateBetween(deliveryDateStart, deliveryDateEnd);
			if(shipments.isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				String msg = String.format(
					    "No outboundDelivery found for  delivery between %s and %s",
					    deliveryDateStart.format(formatter),
					    deliveryDateEnd.format(formatter));
				throw new NoDataFoundException(msg);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByOutboundDelivery_Status(DeliveryStatus status) {
			validateDeliveryStatus(status);
			List<Shipment> shipments = shipmentRepository.findByOutboundDelivery_Status(status);
			if(shipments.isEmpty()) {
				String message = String.format("No outbound-delivery found for delivvery status %s", status);
				throw new NoDataFoundException(message);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByOutboundDelivery_Buyer_Id(Long buyerId) {
			fetchBuyerId(buyerId);
			List<Shipment> shipments = shipmentRepository.findByOutboundDelivery_Buyer_Id(buyerId);
			if(shipments.isEmpty()) {
				String message = String.format("No outbound-delivery found for buyerId %s", buyerId);
				throw new NoDataFoundException(message);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findLateShipments() {
			List<Shipment> shipments = shipmentRepository.findLateShipments();
			if(shipments.isEmpty()) {
				throw new NoDataFoundException("No late shipments found");
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findShipmentsDueSoon(LocalDate futureDate) {
			DateValidator.validatePastOrPresent(futureDate, "Future date");
			List<Shipment> shipments = shipmentRepository.findShipmentsDueSoon(futureDate);
			if(shipments.isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				String message = String.format("No shipments found that are due on or before %s", futureDate.format(formatter));
				throw new NoDataFoundException(message);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByTrackingInfoIsNull() {
			List<Shipment> shipments = shipmentRepository.findByTrackingInfoIsNull();
			if(shipments.isEmpty()) {
				throw new NoDataFoundException("Tracking info must not be null nor empty");
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByOutboundDelivery_StatusAndOutboundDelivery_DeliveryDateBetween(
				DeliveryStatus status, LocalDate from, LocalDate to) {
			validateDeliveryStatus(status);
			DateValidator.validateRange(from, to);
			List<Shipment> shipments = shipmentRepository.findByOutboundDelivery_StatusAndOutboundDelivery_DeliveryDateBetween(status, from, to);
			if(shipments.isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				String msg = String.format(
					    "No outboundDelivery found for status %s with delivery between %s and %s",
					    status,
					    from.format(formatter),
					    to.format(formatter)
					);
				throw new NoDataFoundException(msg);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByOriginStorageIdAndTrackingInfo_EstimatedDeliveryBetween(Long storageId,
				LocalDate from, LocalDate to) {
			fetchStorageId(storageId);
			DateValidator.validateRange(from, to);
			List<Shipment> shipments = shipmentRepository.findByOriginStorageIdAndTrackingInfo_EstimatedDeliveryBetween(storageId, from, to);
			if(shipments.isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				String message = String.format(
				    "No shipments found for origin storage ID %d with estimated delivery between %s and %s",
				    storageId, from.format(formatter), to.format(formatter));
				throw new NoDataFoundException(message);
			}
			return shipments.stream()
					.map(shipmentMapper::toResponse)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByTrackingInfo_CurrentStatusAndTrackingInfo_CurrentLocationContainingIgnoreCase(
				ShipmentStatus status, String location) {
			validateString(location);
		    List<Shipment> shipments = shipmentRepository
		        .findByTrackingInfo_CurrentStatusAndTrackingInfo_CurrentLocationContainingIgnoreCase(status, location);
		    if (shipments.isEmpty()) {
		        String message = String.format("No shipments found with status %s and location containing: %s", status, location);
		        throw new NoDataFoundException(message);
		    }
		    return shipments.stream()
		        .map(ShipmentResponse::new)
		        .collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findByBuyerCompanyNameContainingIgnoreCase(String buyerName) {
			validateString(buyerName);
			List<Shipment> items = shipmentRepository.findByBuyerCompanyNameContainingIgnoreCase(buyerName);
			if(items.isEmpty()) {
				String msg = String.format("No Shipments for buyer company name %s, found", buyerName);
				throw new NoDataFoundException(msg);
			}
			return items.stream()
					.map(ShipmentResponse::new)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findRecentlyDeliveredShipments(LocalDateTime fromDate) {
			DateValidator.validateNotNull(fromDate, "From date"); //method which checking that argument date is not null
			List<Shipment> shipments = shipmentRepository.findRecentlyDeliveredShipments(fromDate);
			if(shipments.isEmpty()) {
				String message = String.format("No delivered shipments found from the given date: %s", fromDate);
				throw new NoDataFoundException(message);
			}
			return shipments.stream()
		            .map(ShipmentResponse::new)
		            .collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findCancelledShipments() {
			List<Shipment> shipments = shipmentRepository.findCancelledShipments();
			if(shipments.isEmpty()) {
				throw new NoDataFoundException("No cancelled shipments found in the system");
			}
			return shipments.stream()
		            .map(ShipmentResponse::new)
		            .collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findCancelledShipmentsBetweenDates(LocalDate from, LocalDate to) {
			DateValidator.validateRange(from, to);
			List<Shipment> shipments = shipmentRepository.findCancelledShipmentsBetweenDates(from, to);
			if(shipments.isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				String msg = String.format("No Shipment for cancelled-shipments date between %s and %s, found", 
						from.format(formatter),to.format(formatter));
				throw new NoDataFoundException(msg);
			}
			return shipments.stream()
		            .map(ShipmentResponse::new)
		            .collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findDelayedShipments() {
			List<Shipment> shipments = shipmentRepository.findDelayedShipments();
			if(shipments.isEmpty()) {
				throw new NoDataFoundException("No delayed shipments found in the system");
			}
			return shipments.stream()
		            .map(ShipmentResponse::new)
		            .collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findInTransitShipments() {
			List<Shipment> shipments = shipmentRepository.findInTransitShipments();
			if(shipments.isEmpty()) {
				throw new NoDataFoundException("No transit shipments found in the system");
			}
			return shipments.stream()
		            .map(ShipmentResponse::new)
		            .collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findDeliveredShipments() {
			List<Shipment> shipments = shipmentRepository.findDeliveredShipments();
			if(shipments.isEmpty()) {
				throw new NoDataFoundException("No delivered shipments found in the system");
			}
			return shipments.stream()
		            .map(ShipmentResponse::new)
		            .collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findShipmentsByFixedStatus(ShipmentStatus status) {
			validateShipmentStatus(status);
			List<Shipment> items = shipmentRepository.findShipmentsByFixedStatus(status);
			if(items.isEmpty()) {
				String msg = String.format("No Shipment for fixed-status %s is found", status);
				throw new NoDataFoundException(msg);
			}
			return items.stream()
					.map(ShipmentResponse::new)
					.collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findPendingDeliveries() {
			List<Shipment> shipments = shipmentRepository.findPendingDeliveries();
			if(shipments.isEmpty()) {
				throw new NoDataFoundException("No pending deliveries found in the system");
			}
			return shipments.stream()
		            .map(ShipmentResponse::new)
		            .collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findInTransitDeliveries() {
			List<Shipment> shipments = shipmentRepository.findInTransitDeliveries();
			if(shipments.isEmpty()) {
				throw new NoDataFoundException("No transit deliveris found in the system");
			}
			return shipments.stream()
		            .map(ShipmentResponse::new)
		            .collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findDeliveredDeliveries() {
		    List<Shipment> shipments = shipmentRepository.findDeliveredDeliveries();
		    if(shipments.isEmpty()) {
		    	throw new NoDataFoundException("No delivered deliveries found in the system.");
		    }
		    return shipments.stream().map(shipmentMapper::toResponse).collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findCancelledDeliveries() {
			List<Shipment> shipments = shipmentRepository.findCancelledDeliveries();
			if(shipments.isEmpty()) {
				throw new NoDataFoundException("No cancelled deliveries found in the system");
			}
			return shipments.stream()
		            .map(ShipmentResponse::new)
		            .collect(Collectors.toList());
		}

		@Override
		public List<ShipmentResponse> findInTransitShipmentsWithInTransitDelivery() {
		    List<Shipment> shipments = shipmentRepository.findInTransitShipmentsWithInTransitDelivery();
		    if(shipments.isEmpty()) {
		    	throw new NoDataFoundException("No Shipment for in_transit  delivery is found");
		    }
		    return shipments.stream()
		            .map(ShipmentResponse::new)
		            .collect(Collectors.toList());
		}
		
		private void validateStorageType(StorageType type) {
			if(type == null) {
				throw new NoDataFoundException("StorageType type must not be null");
			}
		}
		
		private void validateDeliveryStatus(DeliveryStatus status) {
			if(status == null) {
				throw new IllegalArgumentException("DeliveryStatus status must not be null");
			}
		}
		
		private void validateString(String str) {
			if(str == null || str.trim().isEmpty()) {
				throw new NoDataFoundException("Textual characters must not be null nor empty");
			}
		}
		
		private void validateShipmentStatus(ShipmentStatus status) {
			if(status == null) {
				throw new IllegalArgumentException("ShipmentStatus status must not be null");
			}
		}
		
		private void validateEventLogRequest(List<EventLogRequest> requests, boolean requireShipmentId) {
		    if(requests == null || requests.isEmpty()) {
		        throw new IllegalArgumentException("EventLog must not be null nor empty");
		    }
		    for(EventLogRequest req : requests) {
		        if (req == null) {
		            throw new IllegalArgumentException("EventLog entry must not be null");
		        }
		        validateEventLogRequest(req, requireShipmentId); 
		    }
		}
		
		private void validateEventLogRequest(EventLogRequest request,boolean requireShipmentId) {
			if(request.timestamp() == null) {
				throw new IllegalArgumentException("Date and time must not be null");
			}
			if(request.description() == null || request.description().isEmpty()) {
				throw new IllegalArgumentException("Description must not be null nor empty");
			}
			if (requireShipmentId) {
				if (request.shipmentId() == null) {
					throw new IllegalArgumentException("Shipment ID must not be null");
				}
				fetchShipmentId(request.shipmentId());
			}
			
		}

		private OutboundDelivery fetchOutboundDeliveryId(Long outId) {
	    	if(outId == null) {
	    		throw new OutboundDeliveryErrorException("OutboundDelivery ID must not be null");
	    	}
	    	return deliveryRepository.findById(outId).orElseThrow(() -> new OutboundDeliveryErrorException("OuboundDelivery not found with id "+outId));
	    }
		
		private Shipment fetchShipmentId(Long shipmentId) {
			if(shipmentId == null) {
				throw new ShipmentNotFoundException("Shipment ID must not be null");
			}
			return shipmentRepository.findById(shipmentId).orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with id "+shipmentId));
		}
		
		private Buyer fetchBuyerId(Long buyerId) {
			if(buyerId == null) {
				throw new BuyerNotFoundException("Buyer ID must not be null");
			}
			return buyerRepository.findById(buyerId).orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: "+buyerId));
		}
	    
	    private Storage fetchStorageId(Long storageId) {
	    	if(storageId == null) {
	    		throw new StorageNotFoundException("Storage ID must not be null");
	    	}
	    	return storageRepository.findById(storageId).orElseThrow(() -> new StorageNotFoundException("Storage not found with id "+storageId));
	    }
	    
	    private LogisticsProvider fetchLogisticsProviderId(Long logisticsId) {
	    	if(logisticsId == null) {
	    		throw new LogisticsProviderErrorException("Logistics-provider ID must not be null");
	    	}
	    	return providerRepository.findById(logisticsId).orElseThrow(() -> new LogisticsProviderErrorException("Logistics-provider not found with id "+logisticsId));
	    }
	   
	  private TrackingInfo fetchTrackingInfoId(Long trackinInfoId) {
		  if(trackinInfoId == null) {
			  throw new NoDataFoundException("TrackingInfo ID must not be null");
		  }
		  return trackingInfoRepository.findById(trackinInfoId).orElseThrow(() -> new TrackingInfoErrorException("TrackingInfo not found with id: "+trackinInfoId));
	  }
}
