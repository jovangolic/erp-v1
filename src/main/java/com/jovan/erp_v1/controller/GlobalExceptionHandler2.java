package com.jovan.erp_v1.controller;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import com.jovan.erp_v1.exception.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jovan.erp_v1.response.ErrorResponse;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler2 {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), LocalDateTime.now(), status.value());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            StorageNotFoundException.class,
            GoodsNotFoundException.class,
            ProductNotFoundException.class,
            UserNotFoundException.class,
            NoSuchShiftErrorException.class,
            NoSuchShiftReportFoundException.class,
            ProcurementNotFoundException.class,
            BuyerNotFoundException.class,
            ConfirmationDocumentNotFoundException.class,
            InvoiceNotFoundException.class,
            SalesOrderNotFoundException.class,
            PaymentNotFoundException.class,
            SalesNotFoundException.class,
            ProductNotFoundException.class,
            RawMaterialNotFoundException.class,
            ItemSalesNotFoundException.class,
            SupplierNotFoundException.class,
            RoleAlreadyExistException.class,
            UserInvalidException.class,
            SupplyNotFoundException.class,
            SupplyItemNotFoundException.class,
            UnauthorizedSalesException.class,
            SupervisorNotFoundException.class,
            DuplicateBarCodeException.class,
            DuplicatePIBException.class,
            InsufficientRawMaterialException.class,
            InsufficientStockException.class,
            InternalServerException.class,
            InvalidCredentialsException.class,
            InvalidTokenException.class,
            InvalidProcurementDataException.class,
            ItemSalesNotFoundException.class,
            PhotoRetrievalException.class,
            ResourceNotFoundException.class,
            UserAlreadyExistsException.class,
            UserInvalidException.class,
            TokenNotFoundException.class,
            StorageEmployeeNotFoundException.class,
            StorageForemanNotFoundException.class,
            InventoryNotFoundException.class,
            InventoryItemsNotFoundException.class,
            BarCodeNotUniqueException.class,
            BarCodeNotFoundException.class,
            RoleErrorNotFoundException.class,
            EmailAlreadyExistsException.class,
            ShelfNotFoundException.class,
            SystemSettingErrorNotFoundException.class,
            HelpErrorException.class

    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(RuntimeException err) {
        log.error("Bad request error: ", err);
        return buildErrorResponse(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException err) {
        log.error("JSON processing error: ", err);
        return buildErrorResponse(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException err) {
        log.error("REST client error: ", err);
        return buildErrorResponse(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException err) {
        log.error("Unauthorized error: ", err);
        return buildErrorResponse(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception err, WebRequest request) {
        log.error("Unhandled exception: ", err);
        return buildErrorResponse(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation: ", ex);

        String errorMessages = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));

        ErrorResponse error = new ErrorResponse(errorMessages, LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation error: ", ex);

        String errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponse error = new ErrorResponse(errorMessages, LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
