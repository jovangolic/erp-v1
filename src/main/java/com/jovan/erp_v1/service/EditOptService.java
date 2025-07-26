package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.EditOptType;
import com.jovan.erp_v1.exception.EditOptErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
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
    	validateEditOptRequest(request);
        return editOptMapper.toResponse(
                editOptRepository.save(editOptMapper.toEntity(request)));
    }

    @Override
    public EditOptResponse update(Long id, EditOptRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        EditOpt opt = editOptRepository.findById(id)
                .orElseThrow(() -> new EditOptErrorException("EditOpt not found"));
        validateEditOptRequest(request);
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
    	List<EditOpt> items = editOptRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("EditOPT list is empty");
    	}
        return items.stream()
                .map(editOptMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<EditOptResponse> getByType(EditOptType type) {
    	validateEditOptType(type);
    	List<EditOpt> items = editOptRepository.findByType(type);
    	if(items.isEmpty()) {
    		String msg = String.format("No EditOPT with type equal to %s is found", type);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(editOptMapper::toResponse)
                .toList();
    }
    
    @Override
	public EditOptResponse findByName(String name) {
		validateString(name);
		EditOpt items = editOptRepository.findByName(name);
		return editOptMapper.toResponse(items);
	}

	@Override
	public EditOptResponse findByValue(String value) {
		validateString(value);
		EditOpt items = editOptRepository.findByValue(value);
		return editOptMapper.toResponse(items);
	}
    
    private void validateEditOptType(EditOptType type) {
    	Optional.ofNullable(type)
    		.orElseThrow(()-> new ValidationException("EditOptType type must not be null"));
    }
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new ValidationException("String must not be null nor empty");
    	}
    }
    
    private void validateBoolean(Boolean bool) {
    	if(bool == null) {
    		throw new ValidationException("Boolean value must not be null");
    	}
    }
    
    private void validateEditOptRequest(EditOptRequest request) {
    	if(request == null) {
    		throw new ValidationException("EditOptRequest request must not be null");
    	}
    	validateString(request.name());
    	validateString(request.value());
    	validateEditOptType(request.type());
    	validateBoolean(request.editable());
    	validateBoolean(request.visible());
    }
}
