package com.jovan.erp_v1.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.SystemSetting;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {

    Optional<SystemSetting> findByKey(String key);

    Optional<SystemSetting> findByValue(String value);

    boolean existsByKey(String key);

    List<SystemSetting> findAllByCategory(String category);
}
