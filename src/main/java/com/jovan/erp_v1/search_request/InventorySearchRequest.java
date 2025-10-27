package com.jovan.erp_v1.search_request;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.InventoryTypeStatus;

public record InventorySearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		Long storageEmployeeId,
		Long storageEmployeeIdFrom,
		Long storageEmployeeIdTo,
		String employeeFirstName,
		String employeeLastName,
		String employeeEmail,
		String employeePhoneNumber,
		Long storageForemanId,
		Long storageForemanIdFrom,
		Long storageForemanIdTo,
		String foremanFirstName,
		String foremanLastName,
		String foremanEmail,
		String foremanPhoneNumber,
		LocalDate date,
		LocalDate dateBefore,
		LocalDate dateAfter,
		Boolean aligned,
		InventoryStatus status,
		Boolean confirmed,
		InventoryTypeStatus typeStatus
		) {
}
