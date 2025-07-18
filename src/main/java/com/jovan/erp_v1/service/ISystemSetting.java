package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.enumeration.SettingDataType;
import com.jovan.erp_v1.enumeration.SystemSettingCategory;
import com.jovan.erp_v1.request.SystemSettingCreateRequest;
import com.jovan.erp_v1.request.SystemSettingUpdateRequest;
import com.jovan.erp_v1.response.SystemSettingResponse;

public interface ISystemSetting {

    List<SystemSettingResponse> getAll();
    List<SystemSettingResponse> getByCategory(String category);
    SystemSettingResponse getByKey(String key);
    SystemSettingResponse create(SystemSettingCreateRequest request);
    SystemSettingResponse update(SystemSettingUpdateRequest request);
    void delete(Long id);
    SystemSettingResponse getOneById(Long id);
    
    //nove metode
    List<SystemSettingResponse> findByDataType(SettingDataType dataType);
    List<SystemSettingResponse> findByValueAndCategory(String value, String category);
    List<SystemSettingResponse> findString();
    List<SystemSettingResponse> findInteger();
    List<SystemSettingResponse> findBoolean();
    List<SystemSettingResponse> findDouble();
    List<SystemSettingResponse> findDate();
    List<SystemSettingResponse> findTime();
    List<SystemSettingResponse> findDateTime();
    List<SystemSettingResponse> findByDateTypeAndValue(SettingDataType dateType, String value);
    List<SystemSettingResponse> findByCategoryAndDataType(String category, SettingDataType dataType);
    List<SystemSettingResponse> findByCategoryAndIsVisibleTrue();
    List<SystemSettingResponse> findByEditableTrueAndIsVisibleTrue();
    List<SystemSettingResponse> findBySettingKeyContainingIgnoreCase(String keyword);
    List<SystemSettingResponse> findBySettingKeyStartingWith(String prefix);
    List<SystemSettingResponse> findBySettingKeyEndingWith(String suffix);
    List<SystemSettingResponse> findBySettingKeyContaining(String substring);
    List<SystemSettingResponse> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<SystemSettingResponse> findByUpdatedAtAfter(LocalDateTime time);
    List<SystemSettingResponse> findByUpdatedAtBefore(LocalDateTime time);
    List<SystemSettingResponse> findAllByCategoryOrderBySettingKeyAsc(String category);
    List<SystemSettingResponse> findAllByDataTypeOrderByUpdatedAtDesc(SettingDataType dataType);
    List<SystemSettingResponse> findByDataTypeIn(List<SettingDataType> types);
    List<SystemSettingResponse> findByDataTypeCustom( SettingDataType type);
    List<SystemSettingResponse> findGeneral();
    List<SystemSettingResponse> findSecurity();
    List<SystemSettingResponse> findNotifications();
    List<SystemSettingResponse> findUi();
    List<SystemSettingResponse> findPerformance();
    List<SystemSettingResponse> findEmail();
    List<SystemSettingResponse> findIntegrations();
    List<SystemSettingResponse> findFeatureFlags();
    List<SystemSettingResponse> findUserManagement();
    List<SystemSettingResponse> findAllByCategory(SystemSettingCategory category);
    Boolean existsByCategory(SystemSettingCategory category);
    Long countByCategory( SystemSettingCategory category);
    List<SystemSettingResponse> findByCategoryAndDataType(SystemSettingCategory category, SettingDataType dataType);
    Boolean existsByCategoryAndDataType(SystemSettingCategory category, SettingDataType dataType);
    List<SystemSettingCategory> findDistinctCategories();
    List<SystemSettingResponse> findByCategoryAndEditable(SystemSettingCategory category, Boolean editable);
    List<SystemSettingResponse> findByCategoryAndIsVisible(SystemSettingCategory category, Boolean isVisible);
    List<SystemSettingResponse> findByCategoryAndValue(SystemSettingCategory category, String value);
    List<SystemSettingResponse> findByCategoryIn(List<SystemSettingCategory> categories);
    List<SystemSettingResponse> findByCategoryAndCreatedAtAfter(SystemSettingCategory category, LocalDateTime fromDate);
    List<SystemSettingResponse> findByCategoryAndUpdatedAtBetween(SystemSettingCategory category, LocalDateTime start, LocalDateTime end);
    Long countByCategoryAndEditable(SystemSettingCategory category, Boolean editable);
}
