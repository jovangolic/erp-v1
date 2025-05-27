package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.SupplierNotFoundException;
import com.jovan.erp_v1.mapper.VendorMapper;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.repository.VendorRepository;
import com.jovan.erp_v1.request.VendorRequest;
import com.jovan.erp_v1.response.VendorResponse;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendorService implements IVendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    @Transactional
    @Override
    public VendorResponse createVendor(VendorRequest request) {
        Vendor vendor = vendorMapper.toEntity(request);
        Vendor saved = vendorRepository.save(vendor);
        return vendorMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public VendorResponse updateVendor(Long id, VendorRequest request) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Vendor not found with id: " + id));

        vendor.setName(request.name());
        vendor.setEmail(request.email());
        vendor.setPhoneNumber(request.phoneNumber());

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
        return vendorMapper.toResponseList(vendorRepository.findByName(name));
    }

    @Override
    public Optional<VendorResponse> findByEmail(String email) {
        return vendorRepository.findByEmail(email)
                .map(vendorMapper::toResponse);
    }

    @Override
    public List<VendorResponse> findByAddress(String address) {
        return vendorMapper.toResponseList(vendorRepository.findByAddress(address));
    }

    // Mape funkcije (ručno, ali može i preko MapStruct-a)
    private Vendor mapToEntity(VendorRequest request) {
        Vendor vendor = new Vendor();
        vendor.setName(request.name());
        vendor.setEmail(request.email());
        vendor.setPhoneNumber(request.phoneNumber());
        vendor.setAddress(request.address());
        return vendor;
    }

    private VendorResponse mapToResponse(Vendor vendor) {
        return new VendorResponse(vendor);
    }
}
