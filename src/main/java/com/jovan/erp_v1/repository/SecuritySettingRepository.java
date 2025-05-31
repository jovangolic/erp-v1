package com.jovan.erp_v1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.SecuritySetting;

@Repository
public interface SecuritySettingRepository extends JpaRepository<SecuritySetting, Long> {

    Optional<SecuritySetting> findBySettingName(String settingName);
}
