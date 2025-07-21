package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialTransaction;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.request.MaterialTransactionRequest;
import com.jovan.erp_v1.request.VendorRequest;
import com.jovan.erp_v1.response.VendorResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class VendorMapper extends AbstractMapper<VendorRequest> {

	public Vendor toEntity(VendorRequest request, User user, Material material) {
		Objects.requireNonNull(request, "VendorRequest must not be null");
		Objects.requireNonNull(material, "Material must not be null");
		Objects.requireNonNull(user, "User must not be null");
		validateIdForCreate(request, VendorRequest::id);
		Vendor v = new Vendor();
		v.setId(request.id());
		v.setName(request.name());
		v.setEmail(request.email());
		v.setPhoneNumber(request.phoneNumber());
		v.setAddress(request.address());
		if(request.requests() != null && !request.requests().isEmpty()) {
			List<MaterialTransaction> transactions = request.requests().stream()
					.map(txReq -> {
						MaterialTransaction tx = new MaterialTransaction();
						tx.setId(txReq.id());
	                    tx.setQuantity(txReq.quantity());
	                    tx.setType(txReq.type());
	                    tx.setTransactionDate(txReq.transactionDate());
	                    tx.setDocumentReference(txReq.documentReference());
	                    tx.setNotes(txReq.notes());
	                    tx.setStatus(txReq.status());
	                    tx.setVendor(v);
	                    tx.setCreatedByUser(user);
	                    tx.setMaterial(material);
	                    return tx;
					})
					.collect(Collectors.toList());
		v.setMaterialTransactions(transactions);
		}
		return v;
	}
	
	public Vendor toEntityUpdate(Vendor vendor, VendorRequest request, User user, Material material) {
	    Objects.requireNonNull(request, "VendorRequest must not be null");
	    Objects.requireNonNull(material, "Material must not be null");
	    Objects.requireNonNull(user, "User must not be null");
	    Objects.requireNonNull(vendor, "Vendor must not be null");
	    validateIdForUpdate(request, VendorRequest::id);
	    vendor.setName(request.name());
	    vendor.setEmail(request.email());
	    vendor.setPhoneNumber(request.phoneNumber());
	    vendor.setAddress(request.address());
	    // Mapiraj postojece transakcije po ID za brzu pretragu
	    Map<Long, MaterialTransaction> existingTransactionsById = vendor.getMaterialTransactions()
	        .stream()
	        .filter(tx -> tx.getId() != null)
	        .collect(Collectors.toMap(MaterialTransaction::getId, Function.identity()));
	    List<MaterialTransaction> updatedTransactions = new ArrayList<>();
	    if (request.requests() != null && !request.requests().isEmpty()) {
	        for (MaterialTransactionRequest txReq : request.requests()) {
	            MaterialTransaction tx;
	            if (txReq.id() != null && existingTransactionsById.containsKey(txReq.id())) {
	                // Update postojece transakcije
	                tx = existingTransactionsById.get(txReq.id());
	                // Azuriranje polja
	                tx.setQuantity(txReq.quantity());
	                tx.setType(txReq.type());
	                tx.setTransactionDate(txReq.transactionDate());
	                tx.setDocumentReference(txReq.documentReference());
	                tx.setNotes(txReq.notes());
	                tx.setStatus(txReq.status());
	                // Veze ostaju iste
	            } else {
	                // Novi objekat - kreira novu transakciju
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
	    vendor.setMaterialTransactions(updatedTransactions);
	    return vendor;
	}
	
	
	public VendorResponse toResponse(Vendor vendor) {
		Objects.requireNonNull(vendor, "Vendor must not be null");
		return new VendorResponse(vendor);
	}
	
	public List<VendorResponse> toResponseList(List<Vendor> v){
		if(v == null || v.isEmpty()) {
			return Collections.emptyList();
		}
		return v.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
