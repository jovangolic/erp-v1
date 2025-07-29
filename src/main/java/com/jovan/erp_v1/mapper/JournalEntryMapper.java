package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.JournalEntry;
import com.jovan.erp_v1.model.JournalItem;
import com.jovan.erp_v1.request.JournalEntryRequest;
import com.jovan.erp_v1.response.JournalEntryResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JournalEntryMapper extends AbstractMapper<JournalEntryRequest> {

    private final JournalItemMapper journalItemMapper;

    public JournalEntry toEntity(JournalEntryRequest request) {
    	Objects.requireNonNull(request, "JournalEntryRequest must not be null");
    	validateIdForCreate(request, JournalEntryRequest::id);
        JournalEntry j = new JournalEntry();
        j.setEntryDate(request.entryDate());
        j.setDescription(request.description());
        List<JournalItem> items = request.itemRequests().stream()
                .map(itemReq -> {
                    JournalItem item = journalItemMapper.toEntity(itemReq);
                    item.setJournalEntry(j);
                    return item;
                })
                .collect(Collectors.toList());
        j.setItems(items);
        return j;
    }

    public void toUpdateEntity(JournalEntry entry, JournalEntryRequest request) {
    	Objects.requireNonNull(entry, "JournalEntry must not be null");
    	Objects.requireNonNull(request, "JournalEntryRequest must not be null");
    	validateIdForUpdate(request, JournalEntryRequest::id);
        entry.setEntryDate(request.entryDate());
        entry.setDescription(request.description());
        entry.getItems().clear();
        List<JournalItem> entries = request.itemRequests().stream()
                .map(itemsReq -> {
                    JournalItem e = journalItemMapper.toEntity(itemsReq);
                    e.setJournalEntry(entry);
                    return e;
                })
                .collect(Collectors.toList());
        entry.setItems(entries);
    }

    public JournalEntryResponse toResponse(JournalEntry entries) {
    	Objects.requireNonNull(entries,"JournalEntry must not be null");
        return new JournalEntryResponse(entries);
    }

    public List<JournalEntryResponse> toResponseList(List<JournalEntry> entries) {
    	if(entries == null || entries.isEmpty()) {
    		return Collections.emptyList();
    	}
        return entries.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
