package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.jovan.erp_v1.enumeration.SettingDataType;
import com.jovan.erp_v1.enumeration.SystemSettingCategory;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.SystemSettingErrorNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.SystemSettingMapper;
import com.jovan.erp_v1.model.SystemSetting;
import com.jovan.erp_v1.repository.SystemSettingRepository;
import com.jovan.erp_v1.request.SystemSettingCreateRequest;
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
    	List<SystemSetting> items = settingRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("No items found");
    	}
        return items
                .stream()
                .map(SystemSettingResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public SystemSettingResponse getByKey(String key) {
    	validateString(key);
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
        validateSystemSettingCreateRequest(request);
        SystemSetting setting = settingMapper.toEntity(request);
        SystemSetting saved = settingRepository.save(setting);
        return new SystemSettingResponse(saved);
    }

    @Transactional
    @Override
    public SystemSettingResponse update(SystemSettingUpdateRequest request) {
        SystemSetting setting = settingRepository.findById(request.id())
                .orElseThrow(() -> new SystemSettingErrorNotFoundException("Setting not found"));
        validateSystemSettingUpdateRequest(request);
        settingMapper.toEntityUpdate(setting, request);
        SystemSetting updated = settingRepository.save(setting);
        return new SystemSettingResponse(updated);
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
		validateSettingDataType(dataType);
		List<SystemSetting> items = settingRepository.findByDataType(dataType);
		if(items.isEmpty()) {
			String msg = String.format("No system-setting for data-type is found %s", dataType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByValueAndCategory(String value, SystemSettingCategory category) {
		validateString(value);
		validateSystemSettingCategory(category);
		List<SystemSetting> items = settingRepository.findByValueAndCategory(value, category);
		if(items.isEmpty()) {
			String msg = String.format("No system-setting for value %s and category %s is found",
					value,category);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findString() {
		List<SystemSetting> items = settingRepository.findString();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for date-types 'String' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findInteger() {
		List<SystemSetting> items = settingRepository.findInteger();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for date-types 'Integer' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findBoolean() {
		List<SystemSetting> items = settingRepository.findBoolean();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for date-types 'Boolean' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findDouble() {
		List<SystemSetting> items = settingRepository.findDouble();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for date-types 'Double' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findDate() {
		List<SystemSetting> items  = settingRepository.findDate();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for data-types 'Date' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findTime() {
		List<SystemSetting> items = settingRepository.findTime();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for data-types 'Time' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findDateTime() {
		List<SystemSetting> items = settingRepository.findDate();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for date-types 'DateTime' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByDataTypeAndValue(SettingDataType dateType, String value) {
		validateSettingDataType(dateType);
		validateString(value);
		List<SystemSetting> items = settingRepository.findByDataTypeAndValue(dateType, value);
		if(items.isEmpty()) {
			String msg = String.format("No system-setting for date-type %s and value %s is found",
					dateType,value);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}


	@Override
	public List<SystemSettingResponse> findByCategoryAndIsVisibleTrue(SystemSettingCategory category) {
		List<SystemSetting> items = settingRepository.findByCategoryAndIsVisibleTrue(category);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No system-setting for category and isVisible= 'true' is found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByEditableTrueAndIsVisibleTrue() {
		List<SystemSetting> items = settingRepository.findByEditableTrueAndIsVisibleTrue();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No attributes for editable= 'true' and isVisible= 'true' found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findBySettingKeyContainingIgnoreCase(String keyword) {
		validateString(keyword);
		List<SystemSetting> items = settingRepository.findBySettingKeyContainingIgnoreCase(keyword);
		if(items.isEmpty()) {
			String msg = String.format("No setting key for keyword %s is found", keyword);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findBySettingKeyStartingWith(String prefix) {
		validateString(prefix);
		List<SystemSetting> items = settingRepository.findBySettingKeyStartingWith(prefix);
		if(items.isEmpty()) {
			String msg = String.format("System-setting for setting key starting with prefix %s is not found", prefix);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findBySettingKeyEndingWith(String suffix) {
		validateString(suffix);
		List<SystemSetting> items = settingRepository.findBySettingKeyEndingWith(suffix);
		if(items.isEmpty()) {
			String msg = String.format("System-setting for setting key ending with suffix %s is not found", suffix);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findBySettingKeyContaining(String substring) {
		validateString(substring);
		List<SystemSetting> items = settingRepository.findBySettingKeyContaining(substring);
		if(items.isEmpty()) {
			String msg = String.format("System-setting for setting key containing substring %s is not found", substring);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<SystemSetting> items = settingRepository.findByCreatedAtBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("System-setting for createdAt between %s and %s is not found",
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByUpdatedAtAfter(LocalDateTime time) {
		DateValidator.validateNotInPast(time, "Date-time");
		List<SystemSetting> items = settingRepository.findByUpdatedAtAfter(time);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("System-setting for updateAt after %s is not found", time.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByUpdatedAtBefore(LocalDateTime time) {
		DateValidator.validateNotInFuture(time, "Date-time");
		List<SystemSetting> items = settingRepository.findByUpdatedAtBefore(time);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("System-setting for updateAt before %s is not found", time.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findAllByCategoryOrderBySettingKeyAsc(SystemSettingCategory category) {
		validateSystemSettingCategory(category);
		List<SystemSetting> items = settingRepository.findAllByCategoryOrderBySettingKeyAsc(category);
		if(items.isEmpty()) {
			String msg = String.format("System-setting for category %s order by setting keys in ascending order, is not found", category);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findAllByDataTypeOrderByUpdatedAtDesc(SettingDataType dataType) {
		validateSettingDataType(dataType);
		List<SystemSetting> items= settingRepository.findAllByDataTypeOrderByUpdatedAtDesc(dataType);
		if(items.isEmpty()) {
			String msg = String.format("System-setting for dataType %s order by descending is not found ", dataType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByDataTypeIn(List<SettingDataType> types) {
		validateSettingDataType(types);
		List<SystemSetting> items = settingRepository.findByDataTypeIn(types);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No system-settings found for provided types");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findByDataTypeCustom(SettingDataType type) {
		validateSettingDataType(type);
		List<SystemSetting> items = settingRepository.findByDataTypeCustom(type);
		if(items.isEmpty()) {
			String msg = String.format("System-setting for data-type is not found %s", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findGeneral() {
		List<SystemSetting> items = settingRepository.findGeneral();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for category 'General' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findSecurity() {
		List<SystemSetting> items = settingRepository.findSecurity();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for category 'Security' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findNotifications() {
		List<SystemSetting> items = settingRepository.findNotifications();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for category 'Notifications' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findUi() {
		List<SystemSetting> items = settingRepository.findUi();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for category 'UI' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findPerformance() {
		List<SystemSetting> items = settingRepository.findPerformance();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for category 'Performance' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findEmail() {
		List<SystemSetting> items = settingRepository.findEmail();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for category 'Email' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findIntegrations() {
		List<SystemSetting> items = settingRepository.findIntegrations();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for category 'Integrations' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findFeatureFlags() {
		List<SystemSetting> items = settingRepository.findFeatureFlags();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for category 'feature_flags' is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SystemSettingResponse> findUserManagement() {
		List<SystemSetting> items = settingRepository.findUserManagement();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-setting for category 'user_maintenance is not found");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
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
		validateSystemSettingCategories(categories);
		List<SystemSetting> items = settingRepository.findByCategoryIn(categories);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No system-settings found for provided categories");
		}
		return items.stream()
				.map(settingMapper::toResponse)
				.collect(Collectors.toList());
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
	
	private void validateSystemSettingUpdateRequest(SystemSettingUpdateRequest request) {
		if(request == null) {
			throw new ValidationException("System setting update request must not be null");
		}
		validateString(request.value());
		if (request.description() != null) {
			validateString(request.description());
		}
		validateSystemSettingCategory(request.category());
		validateSettingDataType(request.dataType());
		validateBoolean(request.editable());
		validateBoolean(request.isVisible());
		if (request.defaultValue() != null) {
			validateString(request.defaultValue());
		}
	}
	
	private void validateSystemSettingCreateRequest(SystemSettingCreateRequest request) {
		if(request == null) {
			throw new ValidationException("System-setting-create-request must not be null");
		}
		validateString(request.settingKey());
		validateString(request.value());
		if (request.description() != null) {
			validateString(request.description());
		}
		validateSystemSettingCategory(request.category());
		validateSettingDataType(request.dataType());
		validateBoolean(request.editable());
		validateBoolean(request.isVisible());
		if (request.defaultValue() != null) {
			validateString(request.defaultValue());
		}
	}
	
	private void validateSettingDataType(List<SettingDataType> types) {
		if(types == null || types.isEmpty()) {
			throw new ValidationException("List of system-setting data-type must not be null or empty");
		}
		if(types.stream().anyMatch(Objects::isNull)) {
			throw new ValidationException("System-setting type list must not contain null elements");
		}
	}
	
	private void validateSystemSettingCategories(List<SystemSettingCategory> categories) {
		if (categories == null || categories.isEmpty()) {
			throw new ValidationException("List of system-setting categories must not be null or empty");
		}
		if (categories.stream().anyMatch(Objects::isNull)) {
			throw new ValidationException("System-setting category list must not contain null elements");
		}
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
