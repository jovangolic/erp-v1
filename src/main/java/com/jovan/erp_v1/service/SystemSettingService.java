package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.SettingDataType;
import com.jovan.erp_v1.enumeration.SystemSettingCategory;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ResourceNotFoundException;
import com.jovan.erp_v1.exception.SystemSettingErrorNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.SystemSettingMapper;
import com.jovan.erp_v1.model.SystemSetting;
import com.jovan.erp_v1.repository.SystemSettingRepository;
import com.jovan.erp_v1.request.SystemSettingCreateRequest;
import com.jovan.erp_v1.request.SystemSettingRequest;
import com.jovan.erp_v1.request.SystemSettingUpdateRequest;
import com.jovan.erp_v1.response.SystemSettingResponse;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemSettingService implements ISystemSetting {

    private final SystemSettingRepository settingRepository;
    private final SystemSettingMapper settingMapper;

    @Override
    public List<SystemSettingResponse> getAll() {
        return settingRepository.findAll()
                .stream()
                .map(SystemSettingResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemSettingResponse> getByCategory(String category) {
        return settingRepository.findAllByCategory(category)
                .stream()
                .map(SystemSettingResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public SystemSettingResponse getByKey(String key) {
        SystemSetting setting = settingRepository.findBySettingKey(key)
                .orElseThrow(() -> new SystemSettingErrorNotFoundException("Setting not found for key: " + key));
        return new SystemSettingResponse(setting);
    }

    @Transactional
    @Override
    public SystemSettingResponse create(SystemSettingCreateRequest request) {
        if (settingRepository.existsBySettingKey(request.settingKey())) {
            throw new IllegalArgumentException("Setting with this key already exists.");
        }
        SystemSetting setting = new SystemSetting();
        setting.setSettingKey(request.settingKey());
        setting.setValue(request.value());
        setting.setDescription(request.description());
        setting.setCategory(request.category());
        setting.setDataType(request.dataType());
        setting.setEditable(request.editable() != null ? request.editable() : true);
        setting.setIsVisible(request.isVisible() != null ? request.isVisible() : true);
        setting.setDefaultValue(request.defaultValue());

        return new SystemSettingResponse(settingRepository.save(setting));
    }

    @Transactional
    @Override
    public SystemSettingResponse update(SystemSettingUpdateRequest request) {
        SystemSetting setting = settingRepository.findById(request.id())
                .orElseThrow(() -> new SystemSettingErrorNotFoundException("Setting not found"));

        setting.setValue(request.value());
        setting.setDescription(request.description());
        setting.setCategory(request.category());
        setting.setDataType(request.dataType());
        setting.setEditable(request.editable());
        setting.setIsVisible(request.isVisible());
        setting.setDefaultValue(request.defaultValue());

        return new SystemSettingResponse(settingRepository.save(setting));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!settingRepository.existsById(id)) {
            throw new SystemSettingErrorNotFoundException("Setting not found with id: " + id);
        }
        settingRepository.deleteById(id);
    }

    @Override
    public SystemSettingResponse getOneById(Long id) {
        SystemSetting sys = settingRepository.findById(id)
                .orElseThrow(() -> new SystemSettingErrorNotFoundException("SystemSetting not found " + id));
        return new SystemSettingResponse(sys);
    }
    
    //nove metode

	@Override
	public List<SystemSettingResponse> findByDataType(SettingDataType dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findByValueAndCategory(String value, String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findInteger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findBoolean() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findDouble() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findDateTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findByDateTypeAndValue(SettingDataType dateType, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findByCategoryAndDataType(String category, SettingDataType dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findByCategoryAndIsVisibleTrue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findByEditableTrueAndIsVisibleTrue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findBySettingKeyContainingIgnoreCase(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findBySettingKeyStartingWith(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findBySettingKeyEndingWith(String suffix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findBySettingKeyContaining(String substring) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findByUpdatedAtAfter(LocalDateTime time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findByUpdatedAtBefore(LocalDateTime time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findAllByCategoryOrderBySettingKeyAsc(String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findAllByDataTypeOrderByUpdatedAtDesc(SettingDataType dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findByDataTypeIn(List<SettingDataType> types) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findByDataTypeCustom(SettingDataType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findGeneral() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findSecurity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findNotifications() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findUi() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findPerformance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findEmail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findIntegrations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findFeatureFlags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findUserManagement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findAllByCategory(SystemSettingCategory category) {
		validateSystemSettingCategory(category);
		List<SystemSetting> items = settingRepository.findAllByCategory(category);
		if(items.isEmpty()) {
			String msg = String.format("No system-setting for type category %s found", category);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public Boolean existsByCategory(SystemSettingCategory category) {
		validateSystemSettingCategory(category);
		Boolean exists = settingRepository.existsByCategory(category);
		return exists != null ? exists : false;
	}

	@Override
	public Long countByCategory(SystemSettingCategory category) {
		validateSystemSettingCategory(category);
		Long items = settingRepository.countByCategory(category);
		if(items == null) {
			items = 0L;
		}
		return items;
	}

	@Override
	public List<SystemSettingResponse> findByCategoryAndDataType(SystemSettingCategory category,
			SettingDataType dataType) {
		validateSystemSettingCategory(category);
		validateSettingDataType(dataType);
		List<SystemSetting> items = settingRepository.findByCategoryAndDataType(category, dataType);
		if(items.isEmpty()) {
			String msg = String.format("No system-setting for category %s and dateType %s found",
					category,dataType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public Boolean existsByCategoryAndDataType(SystemSettingCategory category, SettingDataType dataType) {
		validateSystemSettingCategory(category);
		validateSettingDataType(dataType);
		Boolean bool = settingRepository.existsByCategoryAndDataType(category, dataType);
		return bool != null ? bool : false;
	}

	@Override
	public List<SystemSettingCategory> findDistinctCategories() {
		List<SystemSettingCategory> items = settingRepository.findDistinctCategories();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Distinct system-setting-categories not found");
		}
		return items;
	}

	@Override
	public List<SystemSettingResponse> findByCategoryAndEditable(SystemSettingCategory category, Boolean editable) {
		validateSystemSettingCategory(category);
		validateBoolean(editable);
		List<SystemSetting> items = settingRepository.findByCategoryAndEditable(category, editable);
		if(items.isEmpty()) {
			String msg = String.format("No system-setting for category %s and ediateble= %s found",
					category, editable);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByCategoryAndIsVisible(SystemSettingCategory category, Boolean isVisible) {
		validateSystemSettingCategory(category);
		validateBoolean(isVisible);
		List<SystemSetting> items = settingRepository.findByCategoryAndIsVisible(category, isVisible);
		if(items.isEmpty()) {
			String msg = String.format("No category %s and isVisible %s found for system-setting",
					category, isVisible);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByCategoryAndValue(SystemSettingCategory category, String value) {
		validateSystemSettingCategory(category);
		validateString(value);
		List<SystemSetting> items = settingRepository.findByCategoryAndValue(category, value);
		if(items.isEmpty()) {
			String msg = String.format("No category %s and value % found for system-setting",
					category,value);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByCategoryIn(List<SystemSettingCategory> categories) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SystemSettingResponse> findByCategoryAndCreatedAtAfter(SystemSettingCategory category,
			LocalDateTime fromDate) {
		validateSystemSettingCategory(category);
		DateValidator.validatePastOrPresent(fromDate, "Date from");
		List<SystemSetting> items = settingRepository.findByCategoryAndCreatedAtAfter(category, fromDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No category %s and created-at after %s found for system-setting",
					category, fromDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByCategoryAndUpdatedAtBetween(SystemSettingCategory category,
			LocalDateTime start, LocalDateTime end) {
		validateSystemSettingCategory(category);
		DateValidator.validateRange(start, end);
		List<SystemSetting> items = settingRepository.findByCategoryAndUpdatedAtBetween(category, start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No category %s and updated dates between %s and %s found for system-setting",
					category,start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public Long countByCategoryAndEditable(SystemSettingCategory category, Boolean editable) {
		validateSystemSettingCategory(category);
		validateBoolean(editable);
		Long items = settingRepository.countByCategoryAndEditable(category, editable);
		if(items == null) {
			throw new NoDataFoundException("Count for category and editable, is not found");
		}
		return items;
	}
	
	private void validateSystemSettingCategory(SystemSettingCategory category) {
		if(category == null) {
			throw new ValidationException("SystemSettingCategory category must not be null");
		}
	}
	
	private void validateSettingDataType(SettingDataType dataType) {
		if(dataType == null) {
			throw new ValidationException("SettingDataType dataType must not be null");
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be empty nor null");
		}
	}
	
	private void validateBoolean(Boolean bool) {
		if(bool == null) {
			throw new ValidationException("Boolean attribute value must not be null");
		}
	}
}
