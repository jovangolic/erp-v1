package com.jovan.erp_v1.mapper;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.EditOpt;
import com.jovan.erp_v1.request.EditOptRequest;
import com.jovan.erp_v1.response.EditOptResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EditOptMapper {

    public EditOpt toEntity(EditOptRequest request) {
        return EditOpt.builder()
                .id(request.id())
                .name(request.name())
                .value(request.value())
                .type(request.type())
                .editable(request.editable())
                .visible(request.visible())
                .build();
    }

    public EditOptResponse toResponse(EditOpt editOpt) {
        return new EditOptResponse(
                editOpt.getId(),
                editOpt.getName(),
                editOpt.getValue(),
                editOpt.getType(),
                editOpt.isEditable(),
                editOpt.isVisible());
    }
}
