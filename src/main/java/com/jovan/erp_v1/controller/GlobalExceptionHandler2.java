package com.jovan.erp_v1.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.jovan.erp_v1.exception.*;
import org.springframework.core.convert.ConversionFailedException;
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
            HelpErrorException.class,
            OptionErrorException.class,
            FileOptErrorException.class,
            EditOptErrorException.class,
            PermissionErrorException.class,
            SystemStateErrorException.class,
            SecuritySettingErrorException.class,
            EmailSettingNotFoundException.class,
            ReportErrorException.class,
            InboundDeliveryErrorException.class,
            OutboundDeliveryErrorException.class,
            DeliveryItemErrorException.class,
            VehicleErrorException.class,
            LogisticsProviderErrorException.class,
            RouteNotFoundException.class,
            TrackingInfoErrorException.class,
            ShipmentNotFoundException.class,
            TransportOrderErrorException.class,
            StockTransferErrorException.class,
            StockTransferItemErrorException.class,
            CompanyEmailErrorException.class,
            LocalizedOptionErrorException.class,
            LanguageErrorException.class,
            AccountNotFoundErrorException.class,
            FiscalYearErrorException.class,
            FiscalQuarterErrorException.class,
            BalanceSheetErrorException.class,
            TaxRateErrorException.class,
            LedgerEntryErrorException.class,
            IncomeStatementErrorException.class,
            JournalEntryErrorException.class,
            JournalItemErrorException.class,
            WorkCenterErrorException.class,
            MaterialNotFoundException.class,
            DuplicateCodeException.class,
            ProductionOrderErrorException.class,
            CapacityPlanningErrorException.class,
            ShiftPlanningErrorException.class,
            BillOfMaterialsErrorException.class,
            MaterialMovementNotFoundException.class,
            MaterialRequestObjectErrorException.class,
            MaterialRequirementErrorException.class,
            MaterialTransactionErrorException.class,
            DuplicateOrderNumberException.class,
            PaymentReferenceNotFoundException.class,
            NoDataFoundException.class

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

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Object> handleConversionFailedException(ConversionFailedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Bad Request");
        body.put("message", "Neispravan tip vrednosti: " + ex.getValue()); // izvuče problematičnu vrednost
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleEnumMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body("Neispravan parametar: " + ex.getValue());
    }
}
