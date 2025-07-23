package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.BuyerMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.request.BuyerRequest;
import com.jovan.erp_v1.response.BuyerResponse;

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
	public List<BuyerResponse> searchBuyers(String keyword) {
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
}
