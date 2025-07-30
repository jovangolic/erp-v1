package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.JournalEntry;
import com.jovan.erp_v1.model.JournalItem;
import com.jovan.erp_v1.request.JournalItemRequest;
import com.jovan.erp_v1.response.JournalItemResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class JournalItemMapper extends AbstractMapper<JournalItemRequest> {

    public JournalItem toEntity(JournalItemRequest request, Account account, JournalEntry entry) {
    	Objects.requireNonNull(request,"JournalItemRequest must not be null");
    	Objects.requireNonNull(account,"Account must not be null");
    	Objects.requireNonNull(entry,"JournalEntry must not be null");
    	validateIdForCreate(request, JournalItemRequest::id);
        JournalItem item = new JournalItem();
        item.setAccount(account);
        item.setCredit(request.credit());
        item.setDebit(request.debit());
        item.setJournalEntry(entry);
        return item;
    }

    public JournalItem toUpdateEntity(JournalItem item, JournalItemRequest request, Account account, JournalEntry entry) {
    	Objects.requireNonNull(item,"JournalItem must not be null");
    	Objects.requireNonNull(request,"JournalItemRequest must not be null");
    	Objects.requireNonNull(account,"Account must not be null");
    	Objects.requireNonNull(entry,"JournalEntry must not be null");
        item.setAccount(account);
        item.setCredit(request.credit());
        item.setDebit(request.debit());
        item.setJournalEntry(entry);
        return item;
    }

    public JournalItemResponse toResponse(JournalItem items) {
    	Objects.requireNonNull(items,"JournalItem must not be null");
        return new JournalItemResponse(items);
    }

    public List<JournalItemResponse> toResponseList(List<JournalItem> items) {
    	if(items == null || items.isEmpty()) {
    		return Collections.emptyList();
    	}
        return items.stream().map(this::toResponse).collect(Collectors.toList());
    }

}
