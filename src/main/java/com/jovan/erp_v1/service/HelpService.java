package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.HelpCategory;
import com.jovan.erp_v1.exception.HelpErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.model.Help;
import com.jovan.erp_v1.repository.HelpRepository;
import com.jovan.erp_v1.request.HelpRequest;
import com.jovan.erp_v1.response.HelpResponse;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelpService implements IHelpService {

    private HelpRepository helpRepository;

    @Transactional
    @Override
    public HelpResponse create(HelpRequest request) {
    	validateString(request.title());
    	validateString(request.content());
    	validateHelpCategory(request.category());
    	if (helpRepository.existsByTitle(request.title())) {
            throw new ValidationException(
                String.format("Help with title '%s' already exists", request.title())
            );
        }
        Help help = new Help();
        help.setTitle(request.title());
        help.setContent(request.content());
        help.setCategory(request.category());
        help.setVisible(request.isVisible());
        helpRepository.save(help);
        return new HelpResponse(help);
    }

    @Transactional
    @Override
    public HelpResponse update(Long id, HelpRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        Help help = helpRepository.findById(id)
                .orElseThrow(() -> new HelpErrorException("Help not found"));
        validateString(request.title());
    	validateString(request.content());
    	validateHelpCategory(request.category());
        help.setTitle(request.title());
        help.setContent(request.content());
        help.setCategory(request.category());
        help.setVisible(request.isVisible());
        helpRepository.save(help);
        return new HelpResponse(help);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!helpRepository.existsById(id)) {
            throw new HelpErrorException("Help error: " + id);
        }
        helpRepository.deleteById(id);
    }

    @Override
    public HelpResponse getById(Long id) {
        Help help = helpRepository.findById(id)
                .orElseThrow(() -> new HelpErrorException("Help not found"));
        return new HelpResponse(help);
    }

    @Transactional(readOnly = true)
    @Override
    public List<HelpResponse> getAll() {
        return helpRepository.findAll()
                .stream()
                .map(HelpResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<HelpResponse> getVisible() {
        return helpRepository.findByIsVisibleTrue().stream()
                .map(HelpResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<HelpResponse> getByCategory(HelpCategory category) {
    	validateHelpCategory(category);
        return helpRepository.findByCategory(category)
                .stream()
                .map(HelpResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
	public List<HelpResponse> findAllCategories() {
		List<Help> items = helpRepository.findAllCategories();
		if(items.isEmpty()) {
			throw new NoDataFoundException("List of all help-categories, is empty");
		}
		return items.stream().map(HelpResponse::new).collect(Collectors.toList());
	}
    
    @Override
	public List<HelpResponse> findByTitleContainingIgnoreCase(String title) {
    	validateString(title);
		List<Help> items = helpRepository.findByTitleContainingIgnoreCase(title);
		if(items.isEmpty()) {
			String msg = String.format("No Help for given title %s, is found", title);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(HelpResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<HelpResponse> findByContentContainingIgnoreCase(String content) {
		validateString(content);
		List<Help> items = helpRepository.findByContentContainingIgnoreCase(content);
		if(items.isEmpty()) {
			String msg = String.format("No Help for content %s, is found", content);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(HelpResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<HelpResponse> findByTitleContainingIgnoreCaseAndContentContainingIgnoreCase(String title,
			String content) {
		validateString(title);
		validateString(content);
		List<Help> items = helpRepository.findByTitleContainingIgnoreCaseAndContentContainingIgnoreCase(title, content);
		if(items.isEmpty()) {
			String msg = String.format("No Help for given title %s and content %s, is found", title,content);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(HelpResponse::new).collect(Collectors.toList());
	}
	
	@Override
	public Boolean existsByTitle(String title) {
		validateString(title);
		return helpRepository.existsByTitle(title);
	}
    
    private void validateHelpCategory(HelpCategory category) {
    	Optional.ofNullable(category)
    		.orElseThrow(() -> new ValidationException("HelpCategory category must not be null"));
    }
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new ValidationException("Textual characters must not be null nor empty");
    	}
    }
    
    private String titleExists(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new ValidationException("Title must not be null nor empty");
    	}
    	if(!helpRepository.existsByTitle(str)) {
    		throw new ValidationException("Given title doesn't exist");
    	}
    	return str;
    }
}
