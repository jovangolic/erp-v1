package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.FileAction;
import com.jovan.erp_v1.enumeration.FileExtension;
import com.jovan.erp_v1.exception.FileOptErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.FileOptMapper;
import com.jovan.erp_v1.model.FileOpt;
import com.jovan.erp_v1.repository.FileOptRepository;
import com.jovan.erp_v1.request.FileOptRequest;
import com.jovan.erp_v1.response.FileOptResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileOptService implements IFileOptService {

    private final FileOptRepository fileOptRepository;
    private final FileOptMapper fileOptMapper;

    @Transactional
    @Override
    public FileOptResponse create(FileOptRequest request) {
    	validateFileOptRequest(request);
        FileOpt fileOpt = fileOptMapper.toEntity(request);
        fileOptRepository.save(fileOpt);
        return fileOptMapper.toResponse(fileOpt);
    }

    @Transactional
    @Override
    public FileOptResponse update(Long id, FileOptRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        FileOpt fileOpt = fileOptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FileOpt not found"));
        validateFileOptRequest(request);
        fileOpt.setExtension(request.extension());
        fileOpt.setMimeType(request.mimeType());
        fileOpt.setMaxSizeInBytes(request.maxSizeInBytes());
        fileOpt.setUploadEnabled(request.uploadEnabled());
        fileOpt.setPreviewEnabled(request.previewEnabled());
        fileOpt.setAvailableActions(request.availableActions());
        fileOptRepository.save(fileOpt);
        return fileOptMapper.toResponse(fileOpt);
    }

    @Transactional
    @Override
    public void delete(Long id) {
    	if(!fileOptRepository.existsById(id)) {
    		throw new NoDataFoundException("FileOPT is not found "+id);
    	}
        fileOptRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public FileOptResponse getById(Long id) {
        FileOpt fileOpt = fileOptRepository.findById(id)
                .orElseThrow(() -> new FileOptErrorException("FileOpt not found"));
        return fileOptMapper.toResponse(fileOpt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileOptResponse> getAll() {
    	List<FileOpt> items = fileOptRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("List of FileOPT is empty");
    	}
        return items
                .stream()
                .map(fileOptMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileOptResponse> getByExtension(FileExtension extension) {
    	validateFileExtension(extension);
    	List<FileOpt> items = fileOptRepository.findByExtension(extension);
    	if(items.isEmpty()) {
    		String msg = String.format("No FileOPT for file extension %s is found", extension);
    		throw new NoDataFoundException(msg);
    	}
        return items
                .stream()
                .map(fileOptMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileOptResponse> getByAction(FileAction action) {
    	validateFileAction(action);
    	List<FileOpt> items = fileOptRepository.findByAvailableActions(action);
    	if(items.isEmpty()) {
    		String msg = String.format("No FileOpt for file action %s is found", action);
    		throw new NoDataFoundException(msg);
    	}
        return items
                .stream().map(fileOptMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    private void validateFileExtension(FileExtension extension) {
    	Optional.ofNullable(extension)
    		.orElseThrow(() -> new ValidationException("FileExtension extension must not be null"));
    }
    
    private void validateFileAction(FileAction action) {
    	Optional.ofNullable(action)
    		.orElseThrow(() -> new ValidationException("FileAction action must not be null"));
    }
    
    private void validateFileOptRequest(FileOptRequest request) {
    	if(request == null) {
    		throw new ValidationException("FileOptRequest request must not be null");
    	}
    	validateFileExtension(request.extension());
    	validateString(request.mimeType());
    	validateLong(request.maxSizeInBytes());
    	validateBoolean(request.uploadEnabled());
    	validateBoolean(request.previewEnabled());
    	validateFileAction(request.availableActions());
    }
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new ValidationException("String must not be null nor empty");
    	}
    }
    
    private void validateFileAction(Set<FileAction> availableActions) {
    	if(availableActions == null || availableActions.isEmpty()) {
    		throw new ValidationException("FileAction must not be empty nor null");
    	}
    	for(FileAction fa : availableActions) {
    		if(fa == null) {
    			throw new ValidationException("FileAction must not be null");
    		}
    		validateFileActionRequest(fa);
    	}
    }
    
    private void validateFileActionRequest(FileAction act) {
    	if(act == null) {
    		throw new ValidationException("FileAction must not be null");
    	}
    	validateFileAction(act);
    }
    
    private void validateLong(Long num) {
    	if(num == null || num <= 0) {
    		throw new ValidationException("Number must not be null nor negative");
    	}
    }
    
    private void validateBoolean(Boolean bool) {
    	if(bool == null) {
    		throw new ValidationException("Boolean value must not be null");
    	}
    }
}
