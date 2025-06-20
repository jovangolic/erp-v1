package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.AccountNotFoundErrorException;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.JournalItem;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.request.JournalItemRequest;
import com.jovan.erp_v1.response.JournalItemResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JournalItemMapper {

    private final AccountRepository accountRepository;

    public JournalItem toEntity(JournalItemRequest request) {
        JournalItem item = new JournalItem();
        item.setAccount(fetchAccount(request.accountId()));
        item.setCredit(request.credit());
        item.setDebit(request.debit());
        return item;
    }

    public void toUpdateEntity(JournalItem item, JournalItemRequest request) {
        item.setAccount(fetchAccount(request.accountId()));
        item.setCredit(request.credit());
        item.setDebit(request.debit());
    }

    public JournalItemResponse toResponse(JournalItem items) {
        return new JournalItemResponse(items);
    }

    public List<JournalItemResponse> toResponseList(List<JournalItem> items) {
        return items.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Account fetchAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundErrorException("Account not found"));
    }
}
