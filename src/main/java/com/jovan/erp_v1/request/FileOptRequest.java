package com.jovan.erp_v1.request;

import java.util.Set;

import com.jovan.erp_v1.enumeration.FileAction;
import com.jovan.erp_v1.enumeration.FileExtension;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FileOptRequest(
				Long id,
                @NotNull(message = "Ekstenzija fajla je obavezna") FileExtension extension,
                @NotBlank(message = "MIME tip je obavezan") String mimeType,
                @NotNull(message = "Maksimalna veličina fajla je obavezna") @Positive(message = "Maksimalna veličina fajla mora biti pozitivna vrednost") Long maxSizeInBytes,
                boolean uploadEnabled,
                boolean previewEnabled,
                @NotEmpty(message = "Mora postojati barem jedna dostupna akcija") Set<FileAction> availableActions) {
}
