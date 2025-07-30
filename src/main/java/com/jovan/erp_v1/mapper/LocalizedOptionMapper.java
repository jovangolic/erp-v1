package com.jovan.erp_v1.mapper;

import java.util.Objects;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Language;
import com.jovan.erp_v1.model.LocalizedOption;
import com.jovan.erp_v1.model.Option;
import com.jovan.erp_v1.request.LocalizedOptionRequest;
import com.jovan.erp_v1.response.LocalizedOptionResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class LocalizedOptionMapper extends AbstractMapper<LocalizedOptionRequest> {

    public LocalizedOption toEntity(LocalizedOptionRequest request, Option opt, Language language) {
    	Objects.requireNonNull(request,"LocalizedOptionRequest must not be null");
    	Objects.requireNonNull(opt,"Option must not be null");
    	Objects.requireNonNull(language,"Language must not be null");
        LocalizedOption entity = new LocalizedOption();
        entity.setOption(opt);
        entity.setLanguage(language);
        entity.setLocalizedLabel(request.getLocalizedLabel());
        return entity;
    }

    public LocalizedOptionResponse toResponse(LocalizedOption entity) {
    	Objects.requireNonNull(entity,"LocalizedOption must not be null");
        return new LocalizedOptionResponse(entity);
    }
}
