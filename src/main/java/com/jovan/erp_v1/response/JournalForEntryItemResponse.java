package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.model.JournalEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalForEntryItemResponse {

	private Long id;
    private LocalDateTime entryDate;
    private String description;
    
    public JournalForEntryItemResponse(JournalEntry j) {
    	this.id = j.getId();
        this.entryDate = j.getEntryDate();
        this.description = j.getDescription();
    }
}
