package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.HelpCategory;
import com.jovan.erp_v1.exception.HelpErrorException;
import com.jovan.erp_v1.model.Help;
import com.jovan.erp_v1.repository.HelpRepository;
import com.jovan.erp_v1.request.HelpRequest;
import com.jovan.erp_v1.response.HelpResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelpService implements IHelpService {

    private HelpRepository helpRepository;

    @Transactional
    @Override
    public HelpResponse create(HelpRequest request) {
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
        Help help = helpRepository.findById(id)
                .orElseThrow(() -> new HelpErrorException("Help not found"));
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
        return helpRepository.findByCategory(category)
                .stream()
                .map(HelpResponse::new)
                .collect(Collectors.toList());
    }

}
