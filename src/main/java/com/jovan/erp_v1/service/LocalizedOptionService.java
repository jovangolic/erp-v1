package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.LocalizedOptionErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.OptionErrorException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.LocalizedOptionMapper;
import com.jovan.erp_v1.model.Language;
import com.jovan.erp_v1.model.LocalizedOption;
import com.jovan.erp_v1.model.Option;
import com.jovan.erp_v1.repository.LanguageRepository;
import com.jovan.erp_v1.repository.LocalizedOptionRepository;
import com.jovan.erp_v1.repository.OptionRepository;
import com.jovan.erp_v1.request.LocalizedOptionRequest;
import com.jovan.erp_v1.response.LocalizedOptionResponse;
import com.jovan.erp_v1.enumeration.LanguageCodeType;
import com.jovan.erp_v1.enumeration.LanguageNameType;
import com.jovan.erp_v1.enumeration.OptionCategory;
import com.jovan.erp_v1.exception.LanguageErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocalizedOptionService implements ILocalizedOptionService {

    private final LocalizedOptionRepository localizedOptionRepository;
    private final LocalizedOptionMapper localizedOptionMapper;
    private final LanguageRepository languageRepository;
    private final OptionRepository optionRepository;

    @Transactional
    @Override
    public LocalizedOptionResponse create(LocalizedOptionRequest request) {
    	Option opt = fetchOption(request.getOptionId());
    	Language lang = fetchLanguage(request.getLanguageId());
    	validateString(request.getLocalizedLabel());
        LocalizedOption entity = localizedOptionMapper.toEntity(request,opt,lang);
        localizedOptionRepository.save(entity);
        return localizedOptionMapper.toResponse(entity);
    }

    @Override
    public List<LocalizedOptionResponse> getAll() {
    	List<LocalizedOption> items = localizedOptionRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("LocalizedOption list is empty");
    	}
        return items.stream()
                .map(localizedOptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocalizedOptionResponse> getTranslationsForOption(Long optionId) {
    	fetchOption(optionId);
    	List<LocalizedOption> items = localizedOptionRepository.findByOptionId(optionId);
    	if(items.isEmpty()) {
    		String msg = String.format("No LocalizedOption for option-id %d is found", optionId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(localizedOptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LocalizedOptionResponse addTranslationForOption(Long optionId, LocalizedOptionRequest request) {
        request.setOptionId(optionId);
        return create(request);
    }

    @Override
    public void delete(Long id) {
        if (!localizedOptionRepository.existsById(id)) {
            throw new LocalizedOptionErrorException("LocalizedOption not found " + id);
        }
        localizedOptionRepository.deleteById(id);
    }

    @Override
    public LocalizedOptionResponse update(Long id, LocalizedOptionRequest request) {
        LocalizedOption local = localizedOptionRepository.findById(id)
                .orElseThrow(() -> new LocalizedOptionErrorException("LocalizedOption not found " + id));
        Option opt = fetchOption(request.getOptionId());
        Language lang = fetchLanguage(request.getLanguageId());
        validateString(request.getLocalizedLabel());
        local.setLocalizedLabel(request.getLocalizedLabel());
        local.setLanguage(lang);
        local.setOption(opt);
        LocalizedOption saved = localizedOptionRepository.save(local);
        return new LocalizedOptionResponse(saved);
    }

    @Override
    public void deleteAllByOptionId(Long optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new OptionErrorException("Option not found " + optionId);
        }
        localizedOptionRepository.deleteByOptionId(optionId);
    }

    @Override
    public LocalizedOptionResponse getTranslation(Long optionId, Long languageId) {
    	validateIds(optionId, languageId);
        return localizedOptionRepository.findByOptionIdAndLanguageId(optionId, languageId)
                .map(localizedOptionMapper::toResponse)
                .orElseThrow(() -> new OptionErrorException("Prevod nije pronađen"));
    }

    @Override
    public List<LocalizedOptionResponse> getAllByLanguage(Long languageId) {
    	if(!localizedOptionRepository.existsByLanguage_Id(languageId)) {
			throw new LanguageErrorException("Language ID not found");
		}
        List<LocalizedOption> lista = localizedOptionRepository.findByLanguageId(languageId);
        if(lista.isEmpty()) {
        	String msg = String.format("No LocalizedOption for language-id %d is found", languageId);
        	throw new NoDataFoundException(msg);
        }
        return lista.stream().map(localizedOptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LocalizedOptionResponse findOne(Long id) {
        LocalizedOption opt = localizedOptionRepository.findById(id)
                .orElseThrow(() -> new LocalizedOptionErrorException("LocalizedOption not found " + id));
        return new LocalizedOptionResponse(opt);
    }

	@Override
	public List<LocalizedOptionResponse> findByOption_Label(String label) {
		validateString(label);
		List<LocalizedOption> items = localizedOptionRepository.findByOption_Label(label);
		if(items.isEmpty()) {
			String msg = String.format("No LocalizedOption for option label %s is found", label);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(LocalizedOptionResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<LocalizedOptionResponse> findByOption_Value(String value) {
		validateString(value);
		List<LocalizedOption> items = localizedOptionRepository.findByOption_Value(value);
		if(items.isEmpty()) {
			String msg = String.format("No LocalizedOption for option value %s is found", value);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(LocalizedOptionResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<LocalizedOptionResponse> findByOption_Category(OptionCategory category) {
		validateOptionCategory(category);
		List<LocalizedOption> items = localizedOptionRepository.findByOption_Category(category);
		if(items.isEmpty()) {
			String msg = String.format("No LocalizedOption for option category %s is found", category);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(LocalizedOptionResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<LocalizedOptionResponse> findByLanguage_Id(Long languageId) {
		fetchLanguage(languageId);
		List<LocalizedOption> items = localizedOptionRepository.findByLanguage_Id(languageId);
		if(items.isEmpty()) {
			String msg = String.format("No LocalizedOption for language-id %d is found", languageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(LocalizedOptionResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<LocalizedOptionResponse> findByLanguage_LanguageCodeType(LanguageCodeType languageCodeType) {
		validateLanguageCodeType(languageCodeType);
		List<LocalizedOption> items = localizedOptionRepository.findByLanguage_LanguageCodeType(languageCodeType);
		if(items.isEmpty()) {
			String msg = String.format("No LocalizedOption for language-code-type %s is found", languageCodeType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(LocalizedOptionResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<LocalizedOptionResponse> findByLanguage_LanguageNameType(LanguageNameType languageNameType) {
		validateLanguageNameType(languageNameType);
		List<LocalizedOption> items = localizedOptionRepository.findByLanguage_LanguageNameType(languageNameType);
		if(items.isEmpty()) {
			String msg = String.format("No LocalizedOption for language-name-type %s is found", languageNameType);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(LocalizedOptionResponse::new)
				.collect(Collectors.toList());
	}
	
	@Transactional
	@Override
	public LocalizedOptionResponse saveLozalizedOptions(LocalizedOptionRequest request) {
		LocalizedOption lo = LocalizedOption.builder()
				.id(request.getId())
				.option(fetchOption(request.getOptionId()))
				.language(fetchLanguage(request.getLanguageId()))
				.localizedLabel(request.getLocalizedLabel())
				.build();
		LocalizedOption saved = localizedOptionRepository.save(lo);
		return new LocalizedOptionResponse(saved);
	}
	
	@Transactional
	@Override
	public LocalizedOptionResponse saveAs(Long sourceId, String newLabel) {
		if(sourceId == null) {
			throw new ValidationException("SourceId must not be null");
		}
		validateString(newLabel);
		//1. pronalazenje postojeceg localized option
		LocalizedOption lo = localizedOptionRepository.findById(sourceId).orElseThrow(() -> new ValidationException("LocalizedOption not found with id "+sourceId));
		//2. pravljenje kopije (id se NE prenosi, jer se pravi novi red u bazi)
		LocalizedOption copyLo = LocalizedOption.builder()
				.option(lo.getOption())
	            .language(lo.getLanguage())
	            .localizedLabel(newLabel != null ? newLabel : lo.getLocalizedLabel())
	            .build();
		//3. snimanje nove instance
		LocalizedOption saved = localizedOptionRepository.save(copyLo);
		return new LocalizedOptionResponse(saved);
	}

	@Transactional
	@Override
	public List<LocalizedOptionResponse> saveAll(List<LocalizedOptionRequest> requests) {
		if(requests == null || requests.isEmpty()) {
			throw new ValidationException("LocalizedOption list must not be empty");
		}
		// Mapiranje request -> entity
	    List<LocalizedOption> items = requests.stream()
	    		.map(item -> LocalizedOption.builder()
	    				.id(item.getId())
	    				.option(fetchOption(item.getOptionId()))
	    				.language(fetchLanguage(item.getLanguageId()))
	    				.localizedLabel(item.getLocalizedLabel())
	    				.build())
	    		.toList();
	    // Snimanje u bazi
	    List<LocalizedOption> saved = localizedOptionRepository.saveAll(items);
	    //Mapiranje u entitet
		return saved.stream().map(localizedOptionMapper::toResponse).collect(Collectors.toList());
	}
	
	private void validateOptionCategory(OptionCategory category) {
		if(category == null) {
			throw new IllegalArgumentException("OptionCategory category must not be null");
		}
	}
	
	private void validateLanguageCodeType(LanguageCodeType languageCodeType) {
		if(languageCodeType == null) {
			throw new IllegalArgumentException("LanguageCodeType languageCodeType must not be null");
		}
	}
	
	private void validateLanguageNameType(LanguageNameType languageNameType) {
		if(languageNameType == null) {
			throw new IllegalArgumentException("LanguageNameType languageNameType must not be null");
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new IllegalArgumentException("String musrt not be null nor empty");
		}
	}
	
	private Option fetchOption(Long optionId) {
		if(optionId == null) {
			throw new OptionErrorException("Option ID must not be null nor empty");
		}
		return optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionErrorException("Option not found"));
	}
	
	private Language fetchLanguage(Long languageId) {
		if(languageId == null) {
			throw new LanguageErrorException("Language ID must not be null nor empty");
		}
		return languageRepository.findById(languageId)
                .orElseThrow(() -> new LanguageErrorException("Language not found"));
	}
	
	private void validateIds(Long optionId, Long languageId) {
		fetchLanguage(languageId);
		fetchOption(optionId);
	}

}
