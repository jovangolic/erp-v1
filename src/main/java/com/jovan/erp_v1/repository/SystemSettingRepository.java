package com.jovan.erp_v1.repository;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.SettingDataType;
import com.jovan.erp_v1.enumeration.SystemSettingCategory;
import com.jovan.erp_v1.model.SystemSetting;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {

    Optional<SystemSetting> findBySettingKey(String settingKey);
    Optional<SystemSetting> findByValue(String value);
    boolean existsBySettingKey(String settingKey);
    
    //nove metode
    List<SystemSetting> findByDataType(SettingDataType dataType);
    List<SystemSetting> findByValueAndCategory(String value, SystemSettingCategory category);
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'STRING' ")
    List<SystemSetting> findString();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'INTEGER' ")
    List<SystemSetting> findInteger();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'BOOLEAN' ")
    List<SystemSetting> findBoolean();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'DOUBLE' ")
    List<SystemSetting> findDouble();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'DATE'")
    List<SystemSetting> findDate();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'TIME' ")
    List<SystemSetting> findTime();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'DATETIME' ")
    List<SystemSetting> findDateTime();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'GENERAL' ")
    List<SystemSetting> findGeneral();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'SECURITY' ")
    List<SystemSetting> findSecurity();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'NOTIFICATIONS' ")
    List<SystemSetting> findNotifications();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'UI' ")
    List<SystemSetting> findUi();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'PERFORMANCE' ")
    List<SystemSetting> findPerformance();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'EMAIL' ")
    List<SystemSetting> findEmail();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'INTEGRATIONS' ")
    List<SystemSetting> findIntegrations();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'FEATURE_FLAGS' ")
    List<SystemSetting> findFeatureFlags();
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = 'USER_MANAGEMENT' ")
    List<SystemSetting> findUserManagement();
    List<SystemSetting> findByDataTypeAndValue(SettingDataType dateType, String value);
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.category = :category AND ss.isVisible IS TRUE")
    List<SystemSetting> findByCategoryAndIsVisibleTrue(@Param("category") SystemSettingCategory category);
    List<SystemSetting> findByEditableTrueAndIsVisibleTrue();
    List<SystemSetting> findBySettingKeyContainingIgnoreCase(String keyword);
    List<SystemSetting> findBySettingKeyStartingWith(String prefix);
    List<SystemSetting> findBySettingKeyEndingWith(String suffix);
    List<SystemSetting> findBySettingKeyContaining(String substring);
    List<SystemSetting> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<SystemSetting> findByUpdatedAtAfter(LocalDateTime time);
    List<SystemSetting> findByUpdatedAtBefore(LocalDateTime time);
    List<SystemSetting> findAllByCategoryOrderBySettingKeyAsc(SystemSettingCategory category);
    List<SystemSetting> findAllByDataTypeOrderByUpdatedAtDesc(SettingDataType dataType);
    List<SystemSetting> findByDataTypeIn(List<SettingDataType> types);
    @Query("SELECT ss FROM SystemSetting ss WHERE ss.dataType = :type")
    List<SystemSetting> findByDataTypeCustom(@Param("type") SettingDataType type);
    List<SystemSetting> findAllByCategory(SystemSettingCategory category);
    Boolean existsByCategory(SystemSettingCategory category);
    @Query("SELECT COUNT(ss) FROM SystemSetting ss WHERE ss.category = :category")
    Long countByCategory(@Param("category") SystemSettingCategory category);
    List<SystemSetting> findByCategoryAndDataType(SystemSettingCategory category, SettingDataType dataType);
    Boolean existsByCategoryAndDataType(SystemSettingCategory category, SettingDataType dataType);
    @Query("SELECT DISTINCT ss.category FROM SystemSetting ss")
    List<SystemSettingCategory> findDistinctCategories();
    List<SystemSetting> findByCategoryAndEditable(SystemSettingCategory category, Boolean editable);
    List<SystemSetting> findByCategoryAndIsVisible(SystemSettingCategory category, Boolean isVisible);
    List<SystemSetting> findByCategoryAndValue(SystemSettingCategory category, String value);
    List<SystemSetting> findByCategoryIn(List<SystemSettingCategory> categories);
    List<SystemSetting> findByCategoryAndCreatedAtAfter(SystemSettingCategory category, LocalDateTime fromDate);
    List<SystemSetting> findByCategoryAndUpdatedAtBetween(SystemSettingCategory category, LocalDateTime start, LocalDateTime end);
    Long countByCategoryAndEditable(SystemSettingCategory category, Boolean editable);
    
    //boolean
    boolean existsBySettingKeyAndIsVisibleTrue(String settingKey);
    boolean existsByCategory(String category);
    long countByEditableTrue();
}
