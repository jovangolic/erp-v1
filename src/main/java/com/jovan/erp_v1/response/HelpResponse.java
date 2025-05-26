package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.HelpCategory;
import com.jovan.erp_v1.model.Help;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpResponse {

    private Long id;
    private String title;
    private String content;
    private HelpCategory category;
    private boolean isVisible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HelpResponse(Help help) {
        this.id = help.getId();
        this.title = help.getTitle();
        this.content = help.getContent();
        this.category = help.getCategory();
        this.isVisible = help.isVisible();
        this.createdAt = help.getCreatedAt();
        this.updatedAt = help.getUpdatedAt();
    }
}
