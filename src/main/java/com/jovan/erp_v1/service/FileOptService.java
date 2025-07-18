package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.FileAction;
import com.jovan.erp_v1.enumeration.FileExtension;
import com.jovan.erp_v1.exception.FileOptErrorException;
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
        return fileOptRepository.findAll()
                .stream()
                .map(fileOptMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileOptResponse> getByExtension(FileExtension extension) {
        return fileOptRepository.findByExtension(extension)
                .stream()
                .map(fileOptMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileOptResponse> getByAction(FileAction action) {
        return fileOptRepository.findByAvailableActions(action)
                .stream().map(fileOptMapper::toResponse)
                .collect(Collectors.toList());
    }
}
