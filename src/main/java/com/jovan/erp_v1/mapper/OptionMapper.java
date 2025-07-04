package com.jovan.erp_v1.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Option;
import com.jovan.erp_v1.request.OptionRequest;
import com.jovan.erp_v1.response.OptionResponse;

@Component
public class OptionMapper {

    public Option toEntity(OptionRequest request) {
    	Objects.requireNonNull(request,"OptionRequest must not be null");
        return Option.builder()
                .id(request.id())
                .label(request.label())
                .value(request.value())
                .category(request.category())
                .active(request.active())
                .build();
    }

    public OptionResponse toResponse(Option option) {
    	Objects.requireNonNull(option,"Option must not be null");
        return new OptionResponse(
                option.getId(),
                option.getLabel(),
                option.getValue(),
                option.getCategory(),
                option.isActive());
    }
}
