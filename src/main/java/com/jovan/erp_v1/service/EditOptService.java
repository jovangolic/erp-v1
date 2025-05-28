package com.jovan.erp_v1.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.EditOptType;
import com.jovan.erp_v1.exception.EditOptErrorException;
import com.jovan.erp_v1.mapper.EditOptMapper;
import com.jovan.erp_v1.model.EditOpt;
import com.jovan.erp_v1.repository.EditOptRepository;
import com.jovan.erp_v1.request.EditOptRequest;
import com.jovan.erp_v1.response.EditOptResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EditOptService implements IEditOptService {

    private final EditOptRepository editOptRepository;
    private final EditOptMapper editOptMapper;

    @Override
    public EditOptResponse create(EditOptRequest request) {
        return editOptMapper.toResponse(
                editOptRepository.save(editOptMapper.toEntity(request)));
    }

    @Override
    public EditOptResponse update(Long id, EditOptRequest request) {
        EditOpt opt = editOptRepository.findById(id)
                .orElseThrow(() -> new EditOptErrorException("EditOpt not found"));
        opt.setId(id);
        opt.setName(request.name());
        opt.setValue(request.value());
        opt.setType(request.type());
        opt.setEditable(request.editable());
        opt.setVisible(request.visible());
        return editOptMapper.toResponse(editOptRepository.save(opt));
    }

    @Override
    public void delete(Long id) {
        editOptRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public EditOptResponse getById(Long id) {
        return editOptRepository.findById(id)
                .map(editOptMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("EditOpt not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EditOptResponse> getAll() {
        return editOptRepository.findAll().stream()
                .map(editOptMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<EditOptResponse> getByType(EditOptType type) {
        return editOptRepository.findByType(type).stream()
                .map(editOptMapper::toResponse)
                .toList();
    }
}
