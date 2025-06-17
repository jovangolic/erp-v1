package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.LocalizedOption;

@Repository
public interface LocalizedOptionRepository extends JpaRepository<LocalizedOption, Long> {

    List<LocalizedOption> findByOptionId(Long optionId);

    List<LocalizedOption> findByLanguageId(Long languageId);

    Optional<LocalizedOption> findByOptionIdAndLanguageId(Long optionId, Long languageId);

    Optional<LocalizedOption> findByLocalizedLabel(String localizedLabel);

    void deleteByOptionId(Long optionId);
}
