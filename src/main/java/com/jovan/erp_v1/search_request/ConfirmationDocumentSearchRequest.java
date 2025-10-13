package com.jovan.erp_v1.search_request;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.ConfirmationDocumentStatus;

public record ConfirmationDocumentSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		LocalDateTime createdAt,
		LocalDateTime createdAtBefore,
		LocalDateTime createdAtAfter,
		LocalDateTime createdAtStart,
		LocalDateTime createdAtEnd,
		Long createdById,
		Long createdByIdFrom,
		Long createdByIdTo,
		String firstName,
		String lastName,
		Long shiftId,
		Long shiftIdFrom,
		Long shiftIdTo,
		LocalDateTime startTime,
		LocalDateTime startTimeBefore,
		LocalDateTime startTimeAfter,
		LocalDateTime endTime,
		LocalDateTime endTimeBefore,
		LocalDateTime endTimeAfter,
		Long shiftSupervisorId,
		Long shiftSupervisorIdFrom,
		Long shiftSupervisorIdTo,
		ConfirmationDocumentStatus status,
		Boolean confirmed
		) {
}
