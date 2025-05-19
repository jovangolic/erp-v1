package com.jovan.erp_v1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.ConfirmationDocumentNotFoundException;
import com.jovan.erp_v1.exception.DuplicateBarCodeException;
import com.jovan.erp_v1.exception.DuplicatePIBException;
import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.InsufficientRawMaterialException;
import com.jovan.erp_v1.exception.InsufficientStockException;
import com.jovan.erp_v1.exception.InvalidCredentialsException;
import com.jovan.erp_v1.exception.InvalidProcurementDataException;
import com.jovan.erp_v1.exception.InvalidTokenException;
import com.jovan.erp_v1.exception.InvoiceNotFoundException;
import com.jovan.erp_v1.exception.ItemSalesNotFoundException;
import com.jovan.erp_v1.exception.NoSuchElementException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.NoSuchShiftErrorException;
import com.jovan.erp_v1.exception.NoSuchShiftReportFoundException;
import com.jovan.erp_v1.exception.PaymentNotFoundException;
import com.jovan.erp_v1.exception.PhotoRetrievalException;
import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.RawMaterialNotFoundException;
import com.jovan.erp_v1.exception.ResourceNotFoundException;
import com.jovan.erp_v1.exception.RoleAlreadyExistException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.exception.StorageCapacityExceededException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.SupervisorNotFoundException;
import com.jovan.erp_v1.exception.SupplierNotFoundException;
import com.jovan.erp_v1.exception.SupplyItemNotFoundException;
import com.jovan.erp_v1.exception.SupplyNotFoundException;
import com.jovan.erp_v1.exception.UnauthorizedException;
import com.jovan.erp_v1.exception.UnauthorizedSalesException;
import com.jovan.erp_v1.exception.UserInvalidException;
import com.jovan.erp_v1.exception.UserNotFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(
            IllegalArgumentException err) {
        return ResponseEntity.badRequest().body(err.getMessage());
    }
	
	@ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException err) {
        return ResponseEntity.internalServerError().body(err.getMessage());
    }
	
	@ExceptionHandler(RestClientException.class)
    public ResponseEntity<String> handleRestClientException(RestClientException err) {
        return ResponseEntity.internalServerError().body(err.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException err) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());
    }

    @ExceptionHandler(UserInvalidException.class)
    public ResponseEntity<String> handleUserInvalidException(UserInvalidException err) {
        return ResponseEntity.badRequest().body(err.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception err, WebRequest request) {
        return ResponseEntity.internalServerError()
                .body("{\"message\": \"An unexpected error occurred: " + err.getMessage() + "\"}");
    }
    
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotSuchElementException(NoSuchElementException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(PhotoRetrievalException.class)
    public ResponseEntity<String> handePhotoRetievalException(PhotoRetrievalException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
	
    @ExceptionHandler(RoleAlreadyExistException.class)
    public ResponseEntity<String> handleRoleAlreadyExistException(RoleAlreadyExistException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(UnauthorizedSalesException.class)
    public ResponseEntity<String> handleUnauthorizedSalesException(UnauthorizedSalesException err){
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());
    }
    
    @ExceptionHandler(RawMaterialNotFoundException.class)
    public ResponseEntity<String> handleRawMaterialNotFoundException(RawMaterialNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(NoSuchProductException.class)
    public ResponseEntity<String> handleNoSuchProductException(NoSuchProductException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(InvalidCredentialsException.class)
    	public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException err){
    		return ResponseEntity.badRequest().body(err.getMessage());
    	}
    
    @ExceptionHandler(BuyerNotFoundException.class)
    public ResponseEntity<String> handleBuyerNotFoundException(BuyerNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<String> handleSupplierNotFoundException(SupplierNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(DuplicatePIBException.class)
    public ResponseEntity<String> handleDuplicatePIBException(DuplicatePIBException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(StorageNotFoundException.class)
    public ResponseEntity<String> handleStorageNotFoundException(StorageNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(StorageCapacityExceededException.class)
    public ResponseEntity<String> handleStorageCapacityExceededException(StorageCapacityExceededException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(GoodsNotFoundException.class)
    public ResponseEntity<String> handleGoodsNotFoundException(GoodsNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(DuplicateBarCodeException.class)
    public ResponseEntity<String> handleDuplicateBarCodeException(DuplicateBarCodeException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(InsufficientRawMaterialException.class)
    public ResponseEntity<String> handleInsufficientRawMaterialException(InsufficientRawMaterialException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(SupplyNotFoundException.class)
    public ResponseEntity<String> handleSupplyNotFoundException(SupplyNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(ProcurementNotFoundException.class)
    public ResponseEntity<String> handleProcurementNotFoundException(ProcurementNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(InvalidProcurementDataException.class)
    public ResponseEntity<String> handleInvalidProcurementDataException(InvalidProcurementDataException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    
    @ExceptionHandler(SalesNotFoundException.class)
    public ResponseEntity<String> handleSalesNotFoundException(SalesNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(ItemSalesNotFoundException.class)
    public ResponseEntity<String> handleItemSalesNotFoundException(ItemSalesNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> handleInsufficientStockException(InsufficientStockException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<String> handlePaymentNotFoundException(PaymentNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(SalesOrderNotFoundException.class)
    public ResponseEntity<String> handleSalesOrderNotFoundException(SalesOrderNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<String> handleInvoiceNotFoundException(InvoiceNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(SupplyItemNotFoundException.class)
    public ResponseEntity<String> handleSupplyItemNotFoundException(SupplyItemNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(SupervisorNotFoundException.class)
    public ResponseEntity<String> handleSupervisorNotFoundException(SupervisorNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(NoSuchShiftErrorException.class)
    public ResponseEntity<String> handleNoSushShiftErrorException(NoSuchShiftErrorException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(NoSuchShiftReportFoundException.class)
    public ResponseEntity<String> handleNoSushShiftReportFoundException(NoSuchShiftReportFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
    @ExceptionHandler(ConfirmationDocumentNotFoundException.class)
    public ResponseEntity<String> handleConfirmationDocumentNotFoundException(ConfirmationDocumentNotFoundException err){
    	return ResponseEntity.badRequest().body(err.getMessage());
    }
    
}
