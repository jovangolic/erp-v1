package com.jovan.erp_v1.service;


import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.request.BuyerRequest;
import com.jovan.erp_v1.response.BuyerResponse;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuyerService implements IBuyerService {
	
	
	private final BuyerRepository buyerRepository;
	
	@Transactional
	@Override
	public BuyerResponse createBuyer(BuyerRequest request) {
		String uniquePib = generateUniquePib();
		Buyer buyer = new Buyer();
		buyer.setCompanyName(request.companyName());
		buyer.setAddress(request.address());
		buyer.setContactPerson(request.contactPerson());
		buyer.setEmail(request.email());
		buyer.setPhoneNumber(request.phoneNumber());
		buyer.setPib(uniquePib);
		Buyer savedBuyer = buyerRepository.save(buyer);
		return new BuyerResponse(savedBuyer);
	}

	@Transactional
	@Override
	public BuyerResponse updateBuyer(String pib, BuyerRequest request) {
		Buyer buyer = buyerRepository.findByPib(pib).orElseThrow(() -> new BuyerNotFoundException("Buyer with PIB " + pib + " not found."));
		buyer.setCompanyName(request.companyName());
		buyer.setAddress(request.address());
		buyer.setContactPerson(request.contactPerson());
		buyer.setEmail(request.email());
		buyer.setPhoneNumber(request.phoneNumber());
		buyerRepository.save(buyer);
		return new BuyerResponse(buyer);
	}
	
	 private String generateUniquePib() {
	        String pib;
	        do {
	            pib = generateRandomPib();
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
        return buyerRepository.findAll().stream()
                .map(BuyerResponse::new)
                .collect(Collectors.toList());
    }

	@Override
	public BuyerResponse getBuyerById(Long id) {
		Buyer buyer = buyerRepository.findById(id).orElseThrow(() ->  new BuyerNotFoundException("Buyer not found"));
		return new BuyerResponse(buyer);
	}

	@Override
	@Transactional
	public void deleteBuyer(Long id) {
		if(!buyerRepository.existsById(id)) {
			throw new BuyerNotFoundException("Buyer not found.");
		}
		buyerRepository.deleteById(id);
	}

}
