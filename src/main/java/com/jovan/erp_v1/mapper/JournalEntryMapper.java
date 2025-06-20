package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.JournalEntry;
import com.jovan.erp_v1.model.JournalItem;
import com.jovan.erp_v1.request.JournalEntryRequest;
import com.jovan.erp_v1.response.JournalEntryResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JournalEntryMapper {

    private final JournalItemMapper journalItemMapper;

    public JournalEntry toEntity(JournalEntryRequest request) {
        JournalEntry j = new JournalEntry();
        j.setEntryDate(request.entryDate());
        j.setDescription(request.description());
        j.getItems().clear();
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
        return new JournalEntryResponse(entries);
    }

    public List<JournalEntryResponse> toResponseList(List<JournalEntry> entries) {
        return entries.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
