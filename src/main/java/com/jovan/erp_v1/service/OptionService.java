package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.OptionCategory;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.OptionErrorException;
import com.jovan.erp_v1.mapper.OptionMapper;
import com.jovan.erp_v1.model.Option;
import com.jovan.erp_v1.repository.OptionRepository;
import com.jovan.erp_v1.request.OptionRequest;
import com.jovan.erp_v1.response.OptionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OptionService implements IOptionService {

    private final OptionRepository optionRepository;
    private final OptionMapper optionMapper;

    @Transactional
    @Override
    public OptionResponse create(OptionRequest request) {
    	validateCreateOption(request);
        Option option = optionMapper.toEntity(request);
        return optionMapper.toResponse(optionRepository.save(option));
    }

    @Transactional
    @Override
    public OptionResponse update(Long id, OptionRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new OptionErrorException("Option not found"));
        validateUpdateOption(id, request);
        option.setLabel(request.label());
        option.setValue(request.value());
        option.setCategory(request.category());
        option.setActive(request.active());
        return optionMapper.toResponse(optionRepository.save(option));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        optionRepository.deleteById(id);
    }

    @Override
    public List<OptionResponse> getAll() {
    	List<Option> items = optionRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("Option list is empty");
    	}
        return items.stream()
                .map(optionMapper::toResponse)
                .toList();
    }

    @Override
    public List<OptionResponse> getByCategory(OptionCategory category) {
    	validateOptionCategory(category);
    	List<Option> items = optionRepository.findByCategory(category);
    	if(items.isEmpty()) {
    		String msg = String.format("No Option for category %s is found", category);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(optionMapper::toResponse)
                .toList();
    }

    @Override
    public OptionResponse getOne(Long id) {
        Option opt = optionRepository.findById(id)
                .orElseThrow(() -> new OptionErrorException("Option not found " + id));
        return new OptionResponse(opt.getId(), opt.getLabel(), opt.getValue(), opt.getCategory(), opt.getActive());
    }

	@Override
	public List<OptionResponse> findByCategoryAndActiveTrue(OptionCategory category) {
		validateOptionCategory(category);
		List<Option> items = optionRepository.findByCategoryAndActiveTrue(category);
		if(items.isEmpty()) {
			String msg = String.format("No Option for option-category %s is found", category);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(optionMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	private void validateOptionCategory(OptionCategory category) {
		if(category == null) {
			throw new IllegalArgumentException("OptionCategory category must not be null ");
		}
	}
	
	private void validateNonEmpty(String value, String fieldName) {
	    if (value == null || value.trim().isEmpty()) {
	        throw new IllegalArgumentException(fieldName + " must not be null or empty.");
	    }
	}
	
	private void validateBoolean(Boolean active) {
		if(active == null) {
			throw new IllegalArgumentException("Option must be active");
		}
	}
	
	private void validateCreateOption(OptionRequest request) {
	    validateNonEmpty(request.label(), "Label");
	    validateNonEmpty(request.value(), "Value");
	    validateOptionCategory(request.category());
	    validateBoolean(request.active());
	    if (optionRepository.existsByLabel(request.label())) {
	        throw new IllegalArgumentException("Label '" + request.label() + "' already exists.");
	    }
	    if (optionRepository.existsByValue(request.value())) {
	        throw new IllegalArgumentException("Value '" + request.value() + "' already exists.");
	    }
	}
	
	private void validateUpdateOption(Long id, OptionRequest request) {
	    validateNonEmpty(request.label(), "Label");
	    validateNonEmpty(request.value(), "Value");
	    validateOptionCategory(request.category());
	    validateBoolean(request.active());
	    Option labelMatch = optionRepository.findByLabel(request.label());
	    if (labelMatch != null && !labelMatch.getId().equals(id)) {
	        throw new IllegalArgumentException("Label '" + request.label() + "' already used by another option.");
	    }
	    Option valueMatch = optionRepository.findByValue(request.value());
	    if (valueMatch != null && !valueMatch.getId().equals(id)) {
	        throw new IllegalArgumentException("Value '" + request.value() + "' already used by another option.");
	    }
	}

}
