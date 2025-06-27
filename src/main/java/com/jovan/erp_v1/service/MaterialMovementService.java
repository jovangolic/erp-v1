package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.MovementType;
import com.jovan.erp_v1.exception.MaterialMovementNotFoundException;
import com.jovan.erp_v1.mapper.MaterialMovementMapper;
import com.jovan.erp_v1.model.MaterialMovement;
import com.jovan.erp_v1.repository.MaterialMovementRepository;
import com.jovan.erp_v1.request.MaterialMovementRequest;
import com.jovan.erp_v1.response.MaterialMovementResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialMovementService implements IMaterialMovementService {

    private final MaterialMovementRepository materialMovementRepository;
    private final MaterialMovementMapper materialMovementMapper;

    @Transactional
    @Override
    public MaterialMovementResponse create(MaterialMovementRequest request) {
        validateIDs(request);
        DateValidator.validateNotNull(request.movementDate(), "Datum kretanja");
        DateValidator.validateNotInFuture(request.movementDate(), "Datum kretanja");
        validateMovementType(request.type());
        if (request.quantity() == null || request.quantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Količina mora biti pozitivan broj i ne sme biti null.");
        }
        log.info("Kreiranje materijalnog kretanja: materialId={}, datum={}", request.materialId(),
                request.movementDate());
        MaterialMovement m = materialMovementMapper.toEntity(request);
        MaterialMovement saved = materialMovementRepository.save(m);
        return materialMovementMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public MaterialMovementResponse update(Long id, MaterialMovementRequest request) {
        MaterialMovement m = materialMovementRepository.findById(id).orElseThrow(
                () -> new MaterialMovementNotFoundException("Material sa datim ID-om nije pronadjen" + id));
        DateValidator.validateNotNull(request.movementDate(), "Datum kretanja");
        DateValidator.validateNotInFuture(request.movementDate(), "Datum kretanja");
        validateMovementType(request.type());
        if (request.quantity() == null || request.quantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Količina mora biti pozitivan broj i ne sme biti null.");
        }
        log.info("Kreiranje materijalnog kretanja: materialId={}, datum={}", request.materialId(),
                request.movementDate());
        materialMovementMapper.toUpdateEntity(m, request);
        return materialMovementMapper.toResponse(materialMovementRepository.save(m));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!materialMovementRepository.existsById(id)) {
            throw new MaterialMovementNotFoundException("Material sa datim ID-om nije pronadjen" + id);
        }
        materialMovementRepository.deleteById(id);
    }

    @Override
    public MaterialMovementResponse findOne(Long id) {
        MaterialMovement m = materialMovementRepository.findById(id).orElseThrow(
                () -> new MaterialMovementNotFoundException("Material sa datim ID-om nije pronadjen" + id));
        return new MaterialMovementResponse(m);
    }

    @Override
    public List<MaterialMovementResponse> findAll() {
        List<MaterialMovement> lista = materialMovementRepository.findAll();
        if (lista.isEmpty()) {
            log.warn("Nema pronađenih kretanja materijala.");
        }
        return lista.stream().map(MaterialMovementResponse::new).toList();
    }

    @Override
    public List<MaterialMovementResponse> findByType(MovementType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByType'");
    }

    @Override
    public List<MaterialMovementResponse> findByQuantity(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("quantity mora biti pozitivan broj i ne sme biti null.");
        }
        return materialMovementRepository.findByQuantity(quantity).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByQuantityGreaterThan(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("quantity mora biti pozitivan broj i ne sme biti null.");
        }
        return materialMovementRepository.findByQuantityGreaterThan(quantity).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByQuantityLessThan(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("quantity mora biti pozitivan broj i ne sme biti null.");
        }
        return materialMovementRepository.findByQuantityLessThan(quantity).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByFromStorage_Id(Long fromStorageId) {
        if (fromStorageId == null) {
            throw new IllegalArgumentException("fromStorageId ID ne sme biti null");
        }
        return materialMovementRepository.findByFromStorage_Id(fromStorageId).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByToStorage_Id(Long toStorageId) {
        if (toStorageId == null) {
            throw new IllegalArgumentException("toStorage ID ne sme biti null");
        }
        return materialMovementRepository.findByToStorage_Id(toStorageId).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByFromStorage_NameContainingIgnoreCase(String fromStorageName) {
        if (fromStorageName == null || fromStorageName.trim() == "") {
            throw new IllegalArgumentException("fromStorageName ne sme biti null ili prazan string");
        }
        return materialMovementRepository.findByToStorage_LocationContainingIgnoreCase(fromStorageName).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByToStorage_NameContainingIgnoreCase(String toStorageName) {
        if (toStorageName == null || toStorageName.trim() == "") {
            throw new IllegalArgumentException("toStorageName ne sme biti null ili prazan string");
        }
        return materialMovementRepository.findByToStorage_NameContainingIgnoreCase(toStorageName).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByFromStorage_LocationContainingIgnoreCase(String fromStorageLocation) {
        if (fromStorageLocation == null || fromStorageLocation.trim() == "") {
            throw new IllegalArgumentException("fromStorageLocation ne sme biti null ili prazan string");
        }
        return materialMovementRepository.findByToStorage_LocationContainingIgnoreCase(fromStorageLocation).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByToStorage_LocationContainingIgnoreCase(String toStorageLocation) {
        if (toStorageLocation == null || toStorageLocation.trim() == "") {
            throw new IllegalArgumentException("toStorageLocation ne sme biti null ili prazan string");
        }
        return materialMovementRepository.findByToStorage_LocationContainingIgnoreCase(toStorageLocation).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByFromStorage_Capacity(BigDecimal capacity) {
        if (capacity == null || capacity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Kapacitet mora biti pozitivan broj i ne sme biti null.");
        }
        return materialMovementRepository.findByToStorage_Capacity(capacity).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByToStorage_Capacity(BigDecimal capacity) {
        if (capacity == null || capacity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Kapacitet mora biti pozitivan broj i ne sme biti null.");
        }
        return materialMovementRepository.findByToStorage_Capacity(capacity).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByMovementDate(LocalDate movementDate) {
        DateValidator.validateNotInFuture(movementDate, "Datumn kretanja");
        DateValidator.validateNotNull(movementDate, "Datum kretanja");
        return materialMovementRepository.findByMovementDate(movementDate).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByMovementDateBetween(LocalDate start, LocalDate end) {
        DateValidator.validateRange(start, end);
        return materialMovementRepository.findByMovementDateBetween(start, end).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByMovementDateGreaterThanEqual(LocalDate date) {
        DateValidator.validateNotInFuture(date, "Datumn kretanja");
        DateValidator.validateNotNull(date, "Datum kretanja");
        return materialMovementRepository.findByMovementDateGreaterThanEqual(date).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialMovementResponse> findByMovementDateAfterOrEqual(LocalDate movementDate) {
        DateValidator.validateNotInFuture(movementDate, "Datumn kretanja");
        DateValidator.validateNotNull(movementDate, "Datum kretanja");
        return materialMovementRepository.findByMovementDateAfterOrEqual(movementDate).stream()
                .map(MaterialMovementResponse::new)
                .collect(Collectors.toList());
    }

    private void validateIDs(MaterialMovementRequest request) {
        if (request.materialId() == null) {
            throw new IllegalArgumentException("ID za materijal ne sme biti null.");
        }
        if (request.fromStorageId() == null) {
            throw new IllegalArgumentException("ID za početno skladište (fromStorage) ne sme biti null.");
        }
        if (request.toStorageId() == null) {
            throw new IllegalArgumentException("ID za ciljno skladište (toStorage) ne sme biti null.");
        }
    }

    private void validateMovementType(MovementType type) {
        if (type == null) {
            throw new IllegalArgumentException("Tip kretanja (MovementType) ne sme biti null.");
        }
    }

}
