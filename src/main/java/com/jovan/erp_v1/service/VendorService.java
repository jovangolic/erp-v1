package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.enumeration.MaterialTransactionType;
import com.jovan.erp_v1.exception.MaterialNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.SupplierNotFoundException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.VendorMapper;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialTransaction;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.MaterialTransactionRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.VendorRepository;
import com.jovan.erp_v1.request.MaterialTransactionRequest;
import com.jovan.erp_v1.request.VendorRequest;
import com.jovan.erp_v1.response.VendorResponse;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendorService implements IVendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;
    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;
    private final MaterialTransactionRepository materialTransactionRepository;

    @Transactional
    @Override
    public VendorResponse createVendor(VendorRequest request) {
    	validateString(request.name());
    	validateString(request.email());
    	validateString(request.phoneNumber());
    	validateString(request.address());
    	validateMaterialTransactionRequestCreate(request.requests());
    	Vendor vendor = new Vendor();
    	vendor.setName(request.name());
    	vendor.setEmail(request.email());
    	vendor.setPhoneNumber(request.phoneNumber());
    	vendor.setAddress(request.address());
    	List<MaterialTransaction> transactions = request.requests().stream()
    	    .map(txReq -> {
    	        Material material = fetchMaterialId(txReq.materialId());
    	        User user = fetchUserId(txReq.createdByUserId());
    	        MaterialTransaction tx = new MaterialTransaction();
    	        tx.setQuantity(txReq.quantity());
    	        tx.setType(txReq.type());
    	        tx.setTransactionDate(txReq.transactionDate());
    	        tx.setDocumentReference(txReq.documentReference());
    	        tx.setNotes(txReq.notes());
    	        tx.setStatus(txReq.status());
    	        tx.setVendor(vendor);
    	        tx.setCreatedByUser(user);
    	        tx.setMaterial(material);
    	        return tx;
    	    })
    	    .collect(Collectors.toList());
    	vendor.setMaterialTransactions(transactions);
    	Vendor saved = vendorRepository.save(vendor);
    	return vendorMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public VendorResponse updateVendor(Long id, VendorRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Vendor not found with id: " + id));
        validateString(request.name());
    	validateString(request.email());
    	validateString(request.phoneNumber());
    	validateString(request.address());
    	validateMaterialTransactionRequestUpdate(request.requests());
        vendor.setName(request.name());
        vendor.setEmail(request.email());
        vendor.setPhoneNumber(request.phoneNumber());
        vendor.setAddress(request.address());
        Map<Long, MaterialTransaction> existingTransactionsById = vendor.getMaterialTransactions()
                .stream()
                .filter(tx -> tx.getId() != null)
                .collect(Collectors.toMap(MaterialTransaction::getId, Function.identity()));
        List<MaterialTransaction> updatedTransactions = new ArrayList<>();    
            if (request.requests() != null && !request.requests().isEmpty()) {
                for (MaterialTransactionRequest txReq : request.requests()) {
                    MaterialTransaction tx;
                    Material material = fetchMaterialId(txReq.materialId());
                    User user = fetchUserId(txReq.createdByUserId());         
                    if (txReq.id() != null && existingTransactionsById.containsKey(txReq.id())) {
                        tx = existingTransactionsById.get(txReq.id());
                        tx.setQuantity(txReq.quantity());
                        tx.setType(txReq.type());
                        tx.setTransactionDate(txReq.transactionDate());
                        tx.setDocumentReference(txReq.documentReference());
                        tx.setNotes(txReq.notes());
                        tx.setStatus(txReq.status());
                        tx.setMaterial(material);
                        tx.setCreatedByUser(user);
                        tx.setVendor(vendor);
                        existingTransactionsById.remove(txReq.id());
                    } else {
                        tx = new MaterialTransaction();
                        tx.setQuantity(txReq.quantity());
                        tx.setType(txReq.type());
                        tx.setTransactionDate(txReq.transactionDate());
                        tx.setDocumentReference(txReq.documentReference());
                        tx.setNotes(txReq.notes());
                        tx.setStatus(txReq.status());
                        tx.setVendor(vendor);
                        tx.setCreatedByUser(user);
                        tx.setMaterial(material);
                    }
                    updatedTransactions.add(tx);
                }
            }
            for (MaterialTransaction toRemove : existingTransactionsById.values()) {
                vendor.getMaterialTransactions().remove(toRemove);
                materialTransactionRepository.delete(toRemove);
            }
            vendor.setMaterialTransactions(updatedTransactions);
        Vendor updated = vendorRepository.save(vendor);
        return vendorMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void deleteVendor(Long id) {
        if (!vendorRepository.existsById(id)) {
            throw new SupplierNotFoundException("Vendor not found with id: " + id);
        }
        vendorRepository.deleteById(id);
    }

    @Override
    public List<VendorResponse> findByName(String name) {
        validateString(name);
        List<Vendor> items = vendorRepository.findByNameContainingIgnoreCase(name);
        if(items.isEmpty()) {
        	String msg = String.format("", name);
        	throw new NoDataFoundException(msg);
        }
        return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());	
    }

    @Override
    public VendorResponse findByEmail(String email) {
        validateString(email);
        Vendor items = vendorRepository.findByEmail(email).orElseThrow(() -> new ValidationException("Vendor not found with email "+email));
    	return new VendorResponse(items);
    }

    @Override
    public List<VendorResponse> findByAddress(String address) {
        validateString(address);
        List<Vendor> items = vendorRepository.findByAddress(address);
        if(items.isEmpty()) {
        	String msg = String.format("Vendor not found with address %s", address);
        	throw new NoDataFoundException(msg);
        }
    	return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());	
    }


    @Override
    public VendorResponse getById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Vendor not found with id:" + id));
        return new VendorResponse(vendor);
    }

    @Override
    public List<VendorResponse> getAllVendors() {
        List<Vendor> items = vendorRepository.findAll();
        if(items.isEmpty()) {
        	throw new NoDataFoundException("Vendor lists is empty");
        }
        return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());	
    }
    
    //nove metode

	@Override
	public List<VendorResponse> findByPhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		List<Vendor> items = vendorRepository.findByPhoneNumberLikeIgnoreCase(phoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("Vendor with phone-number%s is not found", phoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());	
	}

	@Override
	public List<VendorResponse> searchByName(String nameFragment) {
		validateString(nameFragment);
		List<Vendor> items = vendorRepository.searchByName(nameFragment);
		if(items.isEmpty()) {
			String msg = String.format("Vendor with name-fragment %s is not found", nameFragment);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());	
	}

	@Override
	public List<VendorResponse> findByNameIgnoreCaseContainingAndAddressIgnoreCaseContaining(String name, String address) {
		validateString(name);
		validateString(address);
		List<Vendor> items = vendorRepository.findByNameIgnoreCaseContainingAndAddressIgnoreCaseContaining(name, address);
		if(items.isEmpty()) {
			String msg = String.format("Vendor with name %s and address %s is not found", name,address);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());	
	}

	@Override
	public List<VendorResponse> findByIdBetween(Long startId, Long endId) {
	    if (startId == null || endId == null) {
	        throw new IllegalArgumentException("StartId and endId for vendor must not be null");
	    }
	    List<Vendor> items = vendorRepository.findByIdBetween(startId, endId);
	    if (items.isEmpty()) {
	        String msg = String.format("No vendor id between %s and %s are found", startId, endId);
	        throw new NoDataFoundException(msg);
	    }
	    return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<VendorResponse> findByEmailContainingIgnoreCase(String emailFragment) {
		validateString(emailFragment);
		List<Vendor> items = vendorRepository.findByEmailContainingIgnoreCase(emailFragment);
		if(items.isEmpty()) {
			String msg = String.format("Vendor with email fragment %s is not found", emailFragment);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<VendorResponse> findByPhoneNumberContaining(String phoneNumberFragment) {
		validateString(phoneNumberFragment);
		List<Vendor> items = vendorRepository.findByPhoneNumberContaining(phoneNumberFragment);
		if(items.isEmpty()) {
			String msg = String.format("Vendor with phone number containing phone-number-fragment %s is not found", phoneNumberFragment);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<VendorResponse> findVendorsByMaterialTransactionStatus(MaterialTransactionStatus status) {
		validateMaterialTransactionStatus(status);
		List<Vendor> items = vendorRepository.findVendorsByMaterialTransactionStatus(status);
		if(items.isEmpty()) {
			String msg = String.format("Vendors with material transaction status %s is not found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public Long countByAddressContainingIgnoreCase(String addressFragment) {
		validateString(addressFragment);
		Long count = vendorRepository.countByAddressContainingIgnoreCase(addressFragment);
		return count;
	}

	@Override
	public Long countByNameContainingIgnoreCase(String nameFragment) {
		validateString(nameFragment);
		Long count = vendorRepository.countByNameContainingIgnoreCase(nameFragment);
		return count;
	}

	@Override
	public Boolean existsByEmail(String email) {
		validateString(email);
		return vendorRepository.existsByEmail(email);
	}

	@Override
	public List<VendorResponse> findAllByOrderByNameAsc() {
		List<Vendor> items = vendorRepository.findAllByOrderByNameAsc();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Vendor with name in ascending order are not found");
		}
		return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<VendorResponse> findAllByOrderByNameDesc() {
		List<Vendor> items = vendorRepository.findAllByOrderByNameDesc();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Vendors with name in descending order are not found");
		}
		return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<VendorResponse> findVendorsByTransactionStatuses(List<MaterialTransactionStatus> statuses) {
		validateEnumList(statuses, "Material transaction status");
		List<Vendor> items = vendorRepository.findVendorsByTransactionStatuses(statuses);
		if(items.isEmpty()) {
			String msg = String.format("No vendors with transtaction-statuses equal to statuses%s are not found", statuses);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(vendorMapper::toResponse).collect(Collectors.toList());
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}
	
	private void validateMaterialTransactionRequestCreate(List<MaterialTransactionRequest> requests) {
		if(requests == null || requests.isEmpty()) {
			throw new ValidationException("MaterialTransactionRequest requests must not be null nor empty");
		}
		for(MaterialTransactionRequest mt : requests) {
			if(mt.id() == null) {
				throw new ValidationException("MaterialTransactionRequest must not be null");
			}
			fetchMaterialId(mt.materialId());
			validateBigDecimal(mt.quantity());
			validateTransactionType(mt.type());
			DateValidator.validateNotNull(mt.transactionDate(), "Transaction date");
			validateString(mt.documentReference());
			validateString(mt.notes());
			validateMaterialTransactionStatus(mt.status());
			fetchUserId(mt.createdByUserId());
		}
	}
	
	private void validateMaterialTransactionRequestUpdate(List<MaterialTransactionRequest> requests) {
		if(requests == null || requests.isEmpty()) {
			throw new ValidationException("MaterialTransactionRequest requests must not be null nor empty");
		}
		for(MaterialTransactionRequest mt : requests) {
			if(mt.id() == null) {
				throw new ValidationException("MaterialTransactionRequest must not be null");
			}
			fetchMaterialId(mt.materialId());
			validateBigDecimal(mt.quantity());
			validateTransactionType(mt.type());
			DateValidator.validateNotNull(mt.transactionDate(), "Transaction date");
			fetchVendorId(mt.vendorId());
			validateString(mt.documentReference());
			validateString(mt.notes());
			validateMaterialTransactionStatus(mt.status());
			fetchUserId(mt.createdByUserId());
		}
	}
	
	private Material fetchMaterialId(Long materialId) {
		if(materialId == null) {
			throw new ValidationException("Material ID must not be null");
		}
		return materialRepository.findById(materialId).orElseThrow(() -> new MaterialNotFoundException("Material not found with id "+materialId));
	}
	
	private User fetchUserId(Long userId) {
		if(userId == null) {
			throw new ValidationException("User ID must not be null");
		}
		return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id "+userId));
	}
	
	private void validateTransactionType(MaterialTransactionType type) {
		Optional.ofNullable(type)
			.orElseThrow(() -> new ValidationException("TransactionType type must not be null"));
	}
	
	private void validateMaterialTransactionStatus(MaterialTransactionStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("MaterialTransactionStatus status must not be null"));
	}
	
	private void validateBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Quantity must not be null nor negative");
		}
	}
	
	private Vendor fetchVendorId(Long vendorId) {
		if (vendorId == null) {
			throw new ValidationException("Vendor ID must not be null");
		}
		return vendorRepository.findById(vendorId).orElseThrow(() ->
			new SupplierNotFoundException("Vendor not found with id " + vendorId));
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
}
