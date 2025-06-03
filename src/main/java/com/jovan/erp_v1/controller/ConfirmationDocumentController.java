package com.jovan.erp_v1.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jovan.erp_v1.dto.GoodsDispatchDTO;
import com.jovan.erp_v1.exception.ConfirmationDocumentNotFoundException;
import com.jovan.erp_v1.exception.NoSuchShiftErrorException;
import com.jovan.erp_v1.model.ConfirmationDocument;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.ShiftRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.ConfirmationDocumentRequest;
import com.jovan.erp_v1.response.ConfirmationDocumentResponse;
import com.jovan.erp_v1.service.IConfirmationDocumentService;
import com.jovan.erp_v1.service.PdfGeneratorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/confirmationDocuments")
@RequiredArgsConstructor
public class ConfirmationDocumentController {

    private final IConfirmationDocumentService confirmationDocumentService;
    private final PdfGeneratorService pdfGeneratorService;
    private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @PostMapping("/create/new-confirm-document")
    public ResponseEntity<ConfirmationDocumentResponse> create(
            @Valid @RequestBody ConfirmationDocumentRequest request) {
        ConfirmationDocument saved = confirmationDocumentService.saveDocument(request);
        return new ResponseEntity<>(new ConfirmationDocumentResponse(saved), HttpStatus.CREATED);
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<ConfirmationDocument> getById(@PathVariable Long id) {
        return confirmationDocumentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ConfirmationDocumentResponse>> getAll() {
        List<ConfirmationDocument> docs = confirmationDocumentService.getAllDocuments();
        List<ConfirmationDocumentResponse> response = docs.stream()
                .map(ConfirmationDocumentResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ConfirmationDocument>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(confirmationDocumentService.getDocumentsByUserId(userId));
    }

    @GetMapping("/created-after/{date}")
    public ResponseEntity<List<ConfirmationDocument>> getAfter(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(confirmationDocumentService.getDocumentsCreatedAfter(date));
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        confirmationDocumentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("shiftId") Long shiftId) {
        try {
            ConfirmationDocument saved = confirmationDocumentService.uploadDocument(file, userId, shiftId);
            return ResponseEntity.ok("Document uploaded successfully. Document ID: " + saved.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload document: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        Optional<ConfirmationDocument> optDoc = confirmationDocumentService.getDocumentById(id);
        if (optDoc.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ConfirmationDocument document = optDoc.get();
        Path filePath = Paths.get(document.getFilePath());
        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        try {
            Resource resource = new UrlResource(filePath.toUri());
            String fileName = filePath.getFileName().toString();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<Resource> generateAndSaveDocument(@RequestBody GoodsDispatchDTO dto) throws Exception {
        // 1. Generiši PDF
        byte[] pdfBytes = pdfGeneratorService.generateDispatchConfirmation(dto);
        // 2. Snimi fajl na disk
        String folderPath = "generated-docs/";
        Files.createDirectories(Paths.get(folderPath)); // Kreiraj folder ako ne postoji
        String filename = "confirmation_" + System.currentTimeMillis() + ".pdf";
        Path path = Paths.get(folderPath + filename);
        Files.write(path, pdfBytes);
        // 3. Pripremi DTO i pozovi servis
        ConfirmationDocumentRequest request = new ConfirmationDocumentRequest(
                null, // ID postavlja JPA
                path.toString(), // Putanja do fajla
                LocalDateTime.now(), // Vreme kreiranja
                dto.getEmployeeId(), // ID korisnika iz DTO
                dto.getShiftId() // ID smene iz DTO
        );
        confirmationDocumentService.saveDocument(request);
        // 4. Vrati PDF korisniku za preuzimanje
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateConfirmationDocument(@PathVariable Long id,
            @RequestBody ConfirmationDocumentRequest request) {
        ConfirmationDocument doc = confirmationDocumentService.update(id, request);
        return ResponseEntity.ok(doc);
    }

    @ExceptionHandler(ConfirmationDocumentNotFoundException.class)
    public ResponseEntity<String> handleConfirmationDocumentNotFoundException(
            ConfirmationDocumentNotFoundException err) {
        return ResponseEntity.badRequest().body(err.getMessage());
    }
}
