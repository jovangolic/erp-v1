package com.jovan.erp_v1.mapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.LedgerEntry;
import com.jovan.erp_v1.request.LedgerEntryRequest;
import com.jovan.erp_v1.response.LedgerEntryResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class LedgerEntryMapper extends AbstractMapper<LedgerEntryRequest> {

    public LedgerEntry toEntity(LedgerEntryRequest request, Account account) {
        Objects.requireNonNull(request,"LedgerEntryRequest must not be null");
        Objects.requireNonNull(account,"Account must not be null");
        validateIdForCreate(request, LedgerEntryRequest::id);
        LedgerEntry l = new LedgerEntry();
        l.setId(request.id());
        l.setAmount(request.amount());
        l.setDescription(request.description());
        l.setAccount(account);
        l.setType(request.type());
        return l;
    }

    public LedgerEntry toUpdateEntity(LedgerEntry entry, LedgerEntryRequest request, Account acc) {
    	Objects.requireNonNull(request,"LedgerEntryRequest must not be null");
    	Objects.requireNonNull(entry,"LedgerEntry must not be null");
    	validateIdForUpdate(request, LedgerEntryRequest::id);
    	return buildLedgerEntryFromRequest(entry, request, acc);
    }
    
    private LedgerEntry buildLedgerEntryFromRequest(LedgerEntry entry, LedgerEntryRequest request, Account acc) {
    	if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive and not null");
        }
    	if (!request.entryDate().equals(entry.getEntryDate())) {
    	    throw new UnsupportedOperationException("entryDate cannot be changed once set");
    	}
    	entry.setDescription(request.description());
        entry.setAccount(acc);
        entry.setAccount(acc);
        entry.setType(request.type());
        return entry;
    }

    public LedgerEntryResponse toResponse(LedgerEntry entry) {
    	Objects.requireNonNull(entry,"LedgerEntry must not be null");
        return new LedgerEntryResponse(entry);
    }

    public List<LedgerEntryResponse> toResponseList(List<LedgerEntry> entries) {
    	if(entries == null || entries.isEmpty()) {
    		return Collections.emptyList();
    	}
        return entries.stream().map(this::toResponse).collect(Collectors.toList());
    }

}
