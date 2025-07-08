package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.LogisticsProviderErrorException;
import com.jovan.erp_v1.model.LogisticsProvider;
import com.jovan.erp_v1.repository.LogisticsProviderRepository;
import com.jovan.erp_v1.request.LogisticsProviderRequest;
import com.jovan.erp_v1.response.LogisticsProviderResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogisticsProviderService implements ILogisticsProviderService {

    private final LogisticsProviderRepository logisticsProviderRepository;

    @Transactional
    @Override
    public LogisticsProviderResponse create(LogisticsProviderRequest request) {
    	validate(request.name(),request.contactPhone(), request.email(),request.website());
        LogisticsProvider provider = new LogisticsProvider();
        provider.setName(request.name());
        provider.setContactPhone(request.contactPhone());
        provider.setEmail(request.email());
        provider.setWebsite(request.website());
        return new LogisticsProviderResponse(logisticsProviderRepository.save(provider));
    }

    @Transactional
    @Override
    public LogisticsProviderResponse update(Long id, LogisticsProviderRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        LogisticsProvider provider = logisticsProviderRepository.findById(id)
                .orElseThrow(() -> new LogisticsProviderErrorException("Logistics-provider not found " + id));
        validate(request.name(),request.contactPhone(), request.email(),request.website());
        provider.setName(request.name());
        provider.setContactPhone(request.contactPhone());
        provider.setEmail(request.email());
        provider.setWebsite(request.website());
        return new LogisticsProviderResponse(logisticsProviderRepository.save(provider));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!logisticsProviderRepository.existsById(id)) {
            throw new LogisticsProviderErrorException("LogisticsProvider not found: " + id);
        }
        logisticsProviderRepository.deleteById(id);
    }

    @Override
    public LogisticsProviderResponse findOneById(Long id) {
        LogisticsProvider provider = logisticsProviderRepository.findById(id)
                .orElseThrow(() -> new LogisticsProviderErrorException("Logistics-provider not found " + id));
        return new LogisticsProviderResponse(provider);
    }

    @Override
    public List<LogisticsProviderResponse> findAll() {
        return logisticsProviderRepository.findAll().stream()
                .map(LogisticsProviderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LogisticsProviderResponse> findByName(String name) {
    	validate(name);
        return logisticsProviderRepository.findByName(name).stream()
                .map(LogisticsProviderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LogisticsProviderResponse> findByNameContainingIgnoreCase(String fragment) {
    	validate(fragment);
        return logisticsProviderRepository.findByNameContainingIgnoreCase(fragment).stream()
                .map(LogisticsProviderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LogisticsProviderResponse> searchByNameOrWebsite(String query) {
    	validate(query);
        return logisticsProviderRepository.searchByNameOrWebsite(query).stream()
                .map(LogisticsProviderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public LogisticsProviderResponse findByContactPhone(String contactPhone) {
    	validate(contactPhone);
        LogisticsProvider provider = logisticsProviderRepository.findByContactPhone(contactPhone);
        if (provider == null) {
            throw new LogisticsProviderErrorException("Provider with phone not found");
        }
        return new LogisticsProviderResponse(provider);
    }

    @Override
    public LogisticsProviderResponse findByEmail(String email) {
    	validate(email);
        LogisticsProvider provider = logisticsProviderRepository.findByEmail(email);
        if (provider == null) {
            throw new LogisticsProviderErrorException("Provider with email not found");
        }
        return new LogisticsProviderResponse(provider);
    }

    @Override
    public LogisticsProviderResponse findByWebsite(String website) {
    	validate(website);
        LogisticsProvider provider = logisticsProviderRepository.findByWebsite(website);
        if (provider == null) {
            throw new LogisticsProviderErrorException("Provider with website not found");
        }
        return new LogisticsProviderResponse(provider);
    }
    
    
     public static void validate(String... strings) {
          for(String s : strings) {
              if (s == null || s.trim().isEmpty()) {
                  throw new IllegalArgumentException("String must not be null nor empty");
               }
          } 
    }
}
