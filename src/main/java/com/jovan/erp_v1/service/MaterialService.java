package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.exception.DuplicateCodeException;
import com.jovan.erp_v1.exception.MaterialNotFoundException;
import com.jovan.erp_v1.exception.ResourceNotFoundException;
import com.jovan.erp_v1.mapper.MaterialMapper;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.specification.MaterialSpecification;
import com.jovan.erp_v1.request.MaterialRequest;
import com.jovan.erp_v1.response.MaterialResponse;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialService implements IMaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    @Transactional
    @Override
    public MaterialResponse create(MaterialRequest request) {
        if (materialRepository.findByCode(request.code()).size() > 0) {
            throw new DuplicateCodeException("Material with code '" + request.code() + "' already exists.");
        }
        Material m = materialMapper.toEntity(request);
        Material saved = materialRepository.save(m);
        return materialMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public MaterialResponse update(Long id, MaterialRequest request) {
        Material existing = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id " + id));
        List<Material> materialsWithSameCode = materialRepository.findByCode(request.code());
        boolean isDuplicateCode = materialsWithSameCode.stream()
                .anyMatch(m -> !m.getId().equals(id));
        if (isDuplicateCode) {
            throw new DuplicateCodeException("Material with code '" + request.code() + "' already exists.");
        }
        materialMapper.toUpdateEntity(existing, request);
        Material updated = materialRepository.save(existing);
        return new MaterialResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new MaterialNotFoundException("Material not found with id: " + id);
        }
        materialRepository.deleteById(id);
    }

    @Override
    public MaterialResponse findOne(Long id) {
        Material m = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNotFoundException("Material not found with id: " + id));
        return new MaterialResponse(m);
    }

    @Override
    public List<MaterialResponse> findAll() {
        return materialRepository.findAll().stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> searchMaterials(String name, String code, UnitOfMeasure unit, BigDecimal currentStock,
            Long storageId, BigDecimal reorderLevel) {
        // Pronađi materijale po uslovima
        List<Material> materials = materialRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (code != null && !code.isEmpty()) {
                predicates.add(cb.equal(root.get("code"), code));
            }
            if (unit != null) {
                predicates.add(cb.equal(root.get("unit"), unit));
            }
            if (currentStock != null) {
                predicates.add(cb.equal(root.get("currentStock"), currentStock));
            }
            if (storageId != null) {
                predicates.add(cb.equal(root.get("storage").get("id"), storageId));
            }
            if (reorderLevel != null) {
                predicates.add(cb.equal(root.get("reorderLevel"), reorderLevel));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });
        // Mapa u response DTO
        return materials.stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByStorage_Id(Long storageId) {
        return materialRepository.findByStorage_Id(storageId).stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByCode(String code) {
        return materialRepository.findByCode(code).stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByNameContainingIgnoreCase(String name) {
        return materialRepository.findByNameContainingIgnoreCase(name).stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> search(String name, String code) {
        return materialRepository.search(name, code).stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByUnit(UnitOfMeasure unit) {
        return materialRepository.findByUnit(unit).stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByStorage_Name(String storageName) {
        return materialRepository.findByStorage_Name(storageName).stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByStorage_Capacity(Double capacity) {
        return materialRepository.findByStorage_Capacity(capacity).stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByStorage_Type(StorageType type) {
        return materialRepository.findByStorage_Type(type).stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByCurrentStock(BigDecimal currentStock) {
        return materialRepository.findByCurrentStock(currentStock).stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findByReorderLevel(BigDecimal reorderLevel) {
        return materialRepository.findByReorderLevel(reorderLevel).stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

}
