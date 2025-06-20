package com.jovan.erp_v1.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.model.JournalEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryResponse {

    private Long id;
    private LocalDateTime entryDate;
    private String description;
    private List<JournalItemResponse> responses;

    public JournalEntryResponse(JournalEntry j) {
        this.id = j.getId();
        this.entryDate = j.getEntryDate();
        this.description = j.getDescription();
        this.responses = j.getItems()
                .stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }
}
