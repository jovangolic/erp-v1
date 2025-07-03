package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.LanguageCodeType;
import com.jovan.erp_v1.enumeration.LanguageNameType;
import com.jovan.erp_v1.enumeration.OptionCategory;
import com.jovan.erp_v1.model.LocalizedOption;

@Repository
public interface LocalizedOptionRepository extends JpaRepository<LocalizedOption, Long> {

    List<LocalizedOption> findByOptionId(Long optionId);

    List<LocalizedOption> findByLanguageId(Long languageId);

    Optional<LocalizedOption> findByOptionIdAndLanguageId(Long optionId, Long languageId);

    Optional<LocalizedOption> findByLocalizedLabel(String localizedLabel);

    void deleteByOptionId(Long optionId);
    //nove metode
    List<LocalizedOption> findByOption_Label(String label);
    List<LocalizedOption> findByOption_Value(String value);
    List<LocalizedOption> findByOption_Category(OptionCategory category);
    List<LocalizedOption> findByLanguage_Id(Long languageId);
    List<LocalizedOption> findByLanguage_LanguageCodeType(LanguageCodeType languageCodeType);
    List<LocalizedOption> findByLanguage_LanguageNameType(LanguageNameType languageNameType);
    boolean existsByLanguage_Id(Long languageId);
}
