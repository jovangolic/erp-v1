package com.jovan.erp_v1.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.OptionCategory;
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

    @Override
    public OptionResponse create(OptionRequest request) {
        Option option = optionMapper.toEntity(request);
        return optionMapper.toResponse(optionRepository.save(option));
    }

    @Override
    public OptionResponse update(Long id, OptionRequest request) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new OptionErrorException("Option not found"));
        option.setLabel(request.label());
        option.setValue(request.value());
        option.setCategory(request.category());
        option.setActive(request.active());
        return optionMapper.toResponse(optionRepository.save(option));
    }

    @Override
    public void delete(Long id) {
        optionRepository.deleteById(id);
    }

    @Override
    public List<OptionResponse> getAll() {
        return optionRepository.findAll().stream()
                .map(optionMapper::toResponse)
                .toList();
    }

    @Override
    public List<OptionResponse> getByCategory(OptionCategory category) {
        return optionRepository.findByCategory(category).stream()
                .map(optionMapper::toResponse)
                .toList();
    }

    @Override
    public OptionResponse getOne(Long id) {
        Option opt = optionRepository.findById(id)
                .orElseThrow(() -> new OptionErrorException("Option not found " + id));
        return new OptionResponse(opt.getId(), opt.getLabel(), opt.getValue(), opt.getCategory(), opt.isActive());
    }
}
