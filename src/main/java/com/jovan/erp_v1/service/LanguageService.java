package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.LanguageCodeType;
import com.jovan.erp_v1.enumeration.LanguageNameType;
import com.jovan.erp_v1.exception.LanguageErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.mapper.LanguageMapper;
import com.jovan.erp_v1.model.Language;
import com.jovan.erp_v1.repository.LanguageRepository;
import com.jovan.erp_v1.request.LanguageRequest;
import com.jovan.erp_v1.response.LanguageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageService implements ILanguageService {

    private final LanguageMapper languageMapper;
    private final LanguageRepository languageRepository;

    @Transactional
    @Override
    public LanguageResponse create(LanguageRequest request) {
    	validateLanguageCodeType(request.getLanguageCodeType());
        validateLanguageNameType(request.getLanguageNameType());
        Language language = languageMapper.toEntity(request);
        languageRepository.save(language);
        return languageMapper.toResponse(language);
    }

    @Override
    public List<LanguageResponse> getAll() {
    	List<Language> items = languageRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("Language list is empty");
    	}
        return items.stream()
                .map(languageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LanguageResponse findByCodeType(LanguageCodeType codeType) {
    	validateLanguageCodeType(codeType);
        Language language = languageRepository.findByLanguageCodeType(codeType)
                .orElseThrow(() -> new LanguageErrorException("Language with code " + codeType + " not found."));
        return languageMapper.toResponse(language);
    }

    @Override
    public LanguageResponse update(Long id, LanguageRequest request) {
        Language lang = languageRepository.findById(id)
                .orElseThrow(() -> new LanguageErrorException("Language not found " + id));
        validateLanguageCodeType(request.getLanguageCodeType());
        validateLanguageNameType(request.getLanguageNameType());
        lang.setLanguageCodeType(request.getLanguageCodeType());
        lang.setLanguageNameType(request.getLanguageNameType());
        Language saved = languageRepository.save(lang);
        return new LanguageResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!languageRepository.existsById(id)) {
            throw new LanguageErrorException("Language not found " + id);
        }
        languageRepository.deleteById(id);
    }

    @Override
    public LanguageResponse findOneById(Long id) {
        Language lang = languageRepository.findById(id)
                .orElseThrow(() -> new LanguageErrorException("Language not found " + id));
        return new LanguageResponse(lang);
    }

    @Override
    public LanguageResponse findByNameType(LanguageNameType nameType) {
    	validateLanguageNameType(nameType);
        Language name = languageRepository.findByLanguageNameType(nameType)
                .orElseThrow(() -> new LanguageErrorException("Language name not found"));
        return new LanguageResponse(name);
    }
    
    @Override
    @Transactional
	public LanguageResponse saveLanguage(LanguageRequest request) {
		Language l = Language.builder()
				.languageCodeType(request.getLanguageCodeType())
				.languageNameType(request.getLanguageNameType())
				.build();
		Language saved = languageRepository.save(l);
		return new LanguageResponse(saved.getId(), saved.getLanguageCodeType(), saved.getLanguageNameType());
	}
    
    private void validateLanguageNameType(LanguageNameType nameType) {
    	if(nameType == null) {
    		throw new LanguageErrorException("LanguageNameType nameType must not be null");
    	}
    }
    
    private void validateLanguageCodeType(LanguageCodeType codeType) {
    	if(codeType == null) {
    		throw new LanguageErrorException("LanguageCodeType codeType must not be null");
    	}
    }

}
