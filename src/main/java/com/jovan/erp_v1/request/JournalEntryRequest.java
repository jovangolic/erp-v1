package com.jovan.erp_v1.request;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JournalEntryRequest(
        Long id,
        @NotNull LocalDateTime entryDate,
        @NotNull @Size(min = 1, max = 500) String description,
        @NotEmpty @Valid List<JournalItemRequest> itemRequests) {
}
