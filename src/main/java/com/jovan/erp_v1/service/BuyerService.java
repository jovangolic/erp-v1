package com.jovan.erp_v1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.BuyerStatus;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.BuyerMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.specification.BuyerSpecification;
import com.jovan.erp_v1.request.BuyerRequest;
import com.jovan.erp_v1.response.BuyerResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.BuyerSaveAsRequest;
import com.jovan.erp_v1.search_request.BuyerSearchRequest;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuyerService implements IBuyerService {

	private final BuyerRepository buyerRepository;
	private final BuyerMapper buyerMapper;

	@Transactional
	@Override
	public BuyerResponse createBuyer(BuyerRequest request) {
		validateBuyerRequestForCreate(request);
		Buyer buyer = buyerMapper.toEntity(request);
		if(buyer.getPib() == null || buyer.getPib().isBlank()) {
			String uniquePib = Buyer.generateRandomPib();
			buyer.setPib(uniquePib);
		}
		Buyer saved = buyerRepository.save(buyer);
		return new BuyerResponse(saved);
	}

	@Transactional
	@Override
	public BuyerResponse updateBuyer(String pib, BuyerRequest request) {
		Buyer buyer = buyerRepository.findByPib(pib)
				.orElseThrow(() -> new BuyerNotFoundException("Buyer with PIB " + pib + " not found."));
		validateBuyerRequestForUpdate(request);
		buyerMapper.updateBuyer(request, buyer);
		Buyer saved = buyerRepository.save(buyer);
		return new BuyerResponse(saved);
	}

	private String generateUniquePib() {
		int attempts = 0;
		String pib;
		do {
			pib = generateRandomPib();
			attempts++;
			if (attempts > 10) {
				throw new RuntimeException("Unable to generate unique PIB.");
			}
		} while (buyerRepository.existsByPib(pib));
		return pib;
	}

	private String generateRandomPib() {
		Random random = new Random();
		StringBuilder pibBuilder = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			pibBuilder.append(random.nextInt(10));
		}
		return pibBuilder.toString();
	}

	@Override
	public List<BuyerResponse> getAllBuyers() {
		List<Buyer> items = buyerRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("List of buyer items is empty");
		}
		return items.stream()
				.map(BuyerResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public BuyerResponse getBuyerById(Long id) {
		Buyer buyer = buyerRepository.findById(id).orElseThrow(() -> new BuyerNotFoundException("Buyer not found"));
		return new BuyerResponse(buyer);
	}

	@Override
	@Transactional
	public void deleteBuyer(Long id) {
		if (!buyerRepository.existsById(id)) {
			throw new BuyerNotFoundException("Buyer not found.");
		}
		buyerRepository.deleteById(id);
	}

	@Override
	public List<BuyerResponse> searchBuyer(String keyword) {
		validateString(keyword);
		List<Buyer> items = buyerRepository.findByCompanyNameContainingIgnoreCase(keyword);
		if(items.isEmpty()) {
			String msg = String.format("", keyword);
			throw new NoDataFoundException(msg);
		}
		return items
				.stream().map(BuyerResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public BuyerResponse getBuyerByPid(String pib) {
		Buyer buyer = buyerRepository.findByPib(pib)
				.orElseThrow(() -> new BuyerNotFoundException("Buyer not found" + pib));
		return new BuyerResponse(buyer);
	}
	
	@Override
	public List<BuyerResponse> findByAddressContainingIgnoreCase(String address) {
		validateString(address);
		List<Buyer> items = buyerRepository.findByAddressContainingIgnoreCase(address);
		if(items.isEmpty()) {
			String msg = String.format("No Buyer with address %s , is found", address);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(buyerMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BuyerResponse> findByContactPerson(String contactPerson) {
		validateString(contactPerson);
		List<Buyer> items = buyerRepository.findByContactPerson(contactPerson);
		if(items.isEmpty()) {
			String msg = String.format("No Buyer with contact-person %s, is found", contactPerson);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(buyerMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BuyerResponse> findByContactPersonContainingIgnoreCase(String contactPersonFragment) {
		validateString(contactPersonFragment);
		List<Buyer> items = buyerRepository.findByContactPersonContainingIgnoreCase(contactPersonFragment);
		if(items.isEmpty()) {
			String msg = String.format("No Buyer with contact-person %s containing fragment, is found", contactPersonFragment);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(buyerMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BuyerResponse> findByPhoneNumberContaining(String phoneFragment) {
		validateString(phoneFragment);
		List<Buyer> items = buyerRepository.findByPhoneNumberContaining(phoneFragment);
		if(items.isEmpty()) {
			String msg = String.format("No Buyer with phonr-number %s is found", phoneFragment);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(buyerMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BuyerResponse> findByCompanyNameContainingIgnoreCaseAndAddressContainingIgnoreCase(String companyName,
			String address) {
		validateString(companyName);
		validateString(address);
		List<Buyer> items = buyerRepository.findByCompanyNameContainingIgnoreCaseAndAddressContainingIgnoreCase(companyName, address);
		if(items.isEmpty()) {
			String msg = String.format("No Buyer with compnay name %s and address %s found", 
					companyName,address);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(buyerMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BuyerResponse> findBuyersWithSalesOrders() {
		List<Buyer> items = buyerRepository.findBuyersWithSalesOrders();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Buyers with sales orders are not found");
		}
		return items.stream().map(buyerMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BuyerResponse> findBuyersWithoutSalesOrders() {
		List<Buyer> items = buyerRepository.findBuyersWithoutSalesOrders();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Buyers without sales ordreds are not found");
		}
		return items.stream().map(buyerMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public Boolean existsByEmail(String email) {
		validateString(email);
		return buyerRepository.existsByEmail(email);
	}

	@Override
	public List<BuyerResponse> searchBuyers(String companyName, String email) {
		validateString(companyName);
		validateString(email);
		List<Buyer> items = buyerRepository.searchBuyers(companyName, email);
		if(items.isEmpty()) {
			String msg = String.format("", companyName,email);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(buyerMapper::toResponse).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	@Override
	public BuyerResponse trackBuyer(Long id) {
		Buyer b = buyerRepository.findById(id).orElseThrow(() -> new ValidationException("Buyer not found with id "+id));
		return new BuyerResponse(b);
	}

	@Transactional(readOnly = true)
	@Override
	public BuyerResponse confirmBuyer(Long id) {
		Buyer b = buyerRepository.findById(id).orElseThrow(() -> new ValidationException("Buyer not found with id "+id));
		b.setConfirmed(true);
		b.setStatus(BuyerStatus.CONFIRMED);
		return new BuyerResponse(buyerRepository.save(b));
	}

	@Transactional
	@Override
	public BuyerResponse closeBuyer(Long id) {
		Buyer b = buyerRepository.findById(id).orElseThrow(() -> new ValidationException("Buyer not found with id "+id));
		if(b.getStatus() != BuyerStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED buyers can be closed");
		}
		b.setStatus(BuyerStatus.CLOSED);
		return new BuyerResponse(buyerRepository.save(b));
	}

	@Transactional
	@Override
	public BuyerResponse cancelBuyer(Long id) {
		Buyer b = buyerRepository.findById(id).orElseThrow(() -> new ValidationException("Buyer not found with id "+id));
		if(b.getStatus() != BuyerStatus.NEW && b.getStatus() != BuyerStatus.CONFIRMED) {
			throw new ValidationException("Only NEW or CONFIRMED buyers can be cancelled");
		}
		b.setStatus(BuyerStatus.CANCELLED);
		return new BuyerResponse(buyerRepository.save(b));
	}

	@Transactional
	@Override
	public BuyerResponse changeStatus(Long id, BuyerStatus status) {
		Buyer b = buyerRepository.findById(id).orElseThrow(() -> new ValidationException("Buyer not found with id "+id));
		validateBuyerStatus(status);
		if(b.getStatus() == BuyerStatus.CLOSED) {
			throw new ValidationException("Closed buyers cannot change status");
		}
		if(status == BuyerStatus.CONFIRMED) {
			if(b.getStatus() != BuyerStatus.NEW) {
				throw new ValidationException("Only NEW buyers can be confirmed");
			}
			b.setConfirmed(true);
		}
		b.setStatus(status);
		return new BuyerResponse(buyerRepository.save(b));
	}

	@Transactional
	@Override
	public BuyerResponse saveBuyer(BuyerRequest request) {
		Buyer b = Buyer.builder()
				.id(request.id())
				.companyName(request.companyName())
				.pib(request.pib())
				.address(request.address())
				.contactPerson(request.contactPerson())
				.email(request.email())
				.phoneNumber(request.phoneNumber())
				.confirmed(request.confirmed())
				.status(request.status())
				.build();
		Buyer saved = buyerRepository.save(b);
		return new BuyerResponse(saved);
	}
	
	private final AbstractSaveAsService<Buyer, BuyerResponse> saveAsHelper = new AbstractSaveAsService<Buyer, BuyerResponse>() {
		
		@Override
		protected BuyerResponse toResponse(Buyer entity) {
			return new BuyerResponse(entity);
		}
		
		@Override
		protected JpaRepository<Buyer, Long> getRepository() {
			return buyerRepository;
		}
		
		@Override
		protected Buyer copyAndOverride(Buyer source, Map<String, Object> overrides) {
			return Buyer.builder()
					.companyName((String) overrides.getOrDefault("Company-name", source.getCompanyName()))
					.pib((String) overrides.getOrDefault("PIB", source.getPib()))
					.address((String) overrides.getOrDefault("Address", source.getAddress()))
					.contactPerson((String) overrides.getOrDefault("Contact-Person", source.getContactPerson()))
					.email((String) overrides.getOrDefault("Email", source.getEmail()))
					.phoneNumber((String) overrides.getOrDefault("Phone-number", source.getPhoneNumber()))
					.status(source.getStatus())
					.confirmed(source.getConfirmed())
					.build();
		}
	};
	
	private final AbstractSaveAllService<Buyer, BuyerResponse> saveAllHelper = new AbstractSaveAllService<Buyer, BuyerResponse>() {
		
		@Override
		protected Function<Buyer, BuyerResponse> toResponse() {
			return BuyerResponse::new;
		}
		
		@Override
		protected JpaRepository<Buyer, Long> getRepository() {
			return buyerRepository;
		}
	};

	@Transactional
	@Override
	public BuyerResponse saveAs(BuyerSaveAsRequest request) {
		Map<String, Object> overrides = new HashMap<String, Object>();
		if(request.companyName() != null) overrides.put("Company-name", request.companyName());
		if(request.pib() != null) overrides.put("PIB", request.pib());
		if(request.address() != null) overrides.put("Address", request.address());
		if(request.contactPerson() != null) overrides.put("Contact-Person", request.contactPerson());
		if(request.email() != null) overrides.put("Email", request.email());
		if(request.phoneNumber() != null) overrides.put("Phone-Number", request.phoneNumber());
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}

	@Transactional
	@Override
	public List<BuyerResponse> saveAll(List<BuyerRequest> requests) {
		List<Buyer> items = requests.stream()
				.map(req -> Buyer.builder()
						.id(req.id())
						.companyName(req.companyName())
						.pib(req.pib())
						.address(req.address())
						.contactPerson(req.contactPerson())
						.email(req.email())
						.phoneNumber(req.phoneNumber())
						.status(req.status())
						.confirmed(req.confirmed())
						.build())
				.collect(Collectors.toList());
		return saveAllHelper.saveAll(items);
	}

	@Override
	public List<BuyerResponse> generalSearch(BuyerSearchRequest request) {
		Specification<Buyer> spec = BuyerSpecification.fromRequest(request);
		List<Buyer> items = buyerRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Buyer's found for given criteria");
		}
		return items.stream().map(buyerMapper::toResponse).collect(Collectors.toList());
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("Trexual characters must not empty nor null");
		}
	}

	private void validateBuyerRequestForCreate(BuyerRequest request) {
		if(request == null) {
			throw new ValidationException("BuyerRequest request must not be null");
		}
		validateString(request.companyName());
		validateString(request.pib());
		validateString(request.address());
		validateString(request.contactPerson());
		validateString(request.email());
		validateString(request.phoneNumber());
	}
	
	private void validateBuyerRequestForUpdate(BuyerRequest request) {
		if(request == null) {
			throw new ValidationException("BuyerRequest request must not be null");
		}
		validateString(request.companyName());
		validateString(request.address());
		validateString(request.contactPerson());
		validateString(request.email());
		validateString(request.phoneNumber());
	}

	private void validateBuyerStatus(BuyerStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("BuyerStatus status must not be null"));
	}
}
