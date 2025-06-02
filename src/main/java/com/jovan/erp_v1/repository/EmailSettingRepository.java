package com.jovan.erp_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.jovan.erp_v1.model.EmailSetting;

@Repository
public interface EmailSettingRepository extends JpaRepository<EmailSetting, Long> {

	Optional<EmailSetting> findTopByOrderByIdDesc();
}
