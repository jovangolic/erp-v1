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
import com.jovan.erp_v1.enumeration.ConfirmationDocumentStatus;
import com.jovan.erp_v1.model.ConfirmationDocument;
import com.jovan.erp_v1.request.ConfirmationDocumentRequest;
import com.jovan.erp_v1.response.ConfirmationDocumentResponse;
import com.jovan.erp_v1.save_as.ConfirmationDocumentSaveAsRequest;
import com.jovan.erp_v1.search_request.ConfirmationDocumentSearchRequest;
import com.jovan.erp_v1.service.IConfirmationDocumentService;
import com.jovan.erp_v1.service.PdfGeneratorService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/confirmationDocuments")
@RequiredArgsConstructor
@PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_ACCESS)
public class ConfirmationDocumentController {

    private final IConfirmationDocumentService confirmationDocumentService;
    private final PdfGeneratorService pdfGeneratorService;

    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @PostMapping("/create/new-confirm-document")
    public ResponseEntity<ConfirmationDocumentResponse> create(
            @Valid @RequestBody ConfirmationDocumentRequest request) {
        ConfirmationDocument saved = confirmationDocumentService.saveDocument(request);
        return new ResponseEntity<>(new ConfirmationDocumentResponse(saved), HttpStatus.CREATED);
    }

    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_ACCESS)
    @GetMapping("/get-one/{id}")
    public ResponseEntity<ConfirmationDocument> getById(@PathVariable Long id) {
        return confirmationDocumentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_ACCESS)
    @GetMapping("/get-all")
    public ResponseEntity<List<ConfirmationDocumentResponse>> getAll() {
        List<ConfirmationDocument> docs = confirmationDocumentService.getAllDocuments();
        List<ConfirmationDocumentResponse> response = docs.stream()
                .map(ConfirmationDocumentResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_ACCESS)
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ConfirmationDocument>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(confirmationDocumentService.getDocumentsByUserId(userId));
    }

    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_ACCESS)
    @GetMapping("/created-after/{date}")
    public ResponseEntity<List<ConfirmationDocument>> getAfter(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(confirmationDocumentService.getDocumentsCreatedAfter(date));
    }

    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        confirmationDocumentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
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

    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_ACCESS)
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

    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @PostMapping("/generate")
    public ResponseEntity<Resource> generateAndSaveDocument(@RequestBody GoodsDispatchDTO dto) throws Exception {
        // 1. Generisi PDF
        byte[] pdfBytes = pdfGeneratorService.generateDispatchConfirmation(dto);
        // 2. Snimi fajl na disk
        String folderPath = "generated-docs/";
        Files.createDirectories(Paths.get(folderPath)); 
        String filename = "confirmation_" + System.currentTimeMillis() + ".pdf";
        Path path = Paths.get(folderPath + filename);
        Files.write(path, pdfBytes);
        // 3. Pripremi DTO i pozovi servis
        ConfirmationDocumentRequest request = new ConfirmationDocumentRequest(
                null, // ID postavlja JPA
                path.toString(), // Putanja do fajla
                LocalDateTime.now(), // Vreme kreiranja
                dto.getEmployeeId(), // ID korisnika iz DTO
                dto.getShiftId(), // ID smene iz DTO
                null,
                null
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

    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateConfirmationDocument(@PathVariable Long id,
            @RequestBody ConfirmationDocumentRequest request) {
        ConfirmationDocument doc = confirmationDocumentService.update(id, request);
        return ResponseEntity.ok(doc);
    }
    
    //nove metode
    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_ACCESS)
    @GetMapping("/track-confirmation-doc/{id}")
    public ResponseEntity<ConfirmationDocumentResponse> trackConfirmationDoc(@PathVariable Long id){
    	ConfirmationDocumentResponse items = confirmationDocumentService.trackConfirmationDoc(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @PostMapping("/{id}/confirm")
    public ResponseEntity<ConfirmationDocumentResponse> confirmConfDoc(@PathVariable Long id){
    	ConfirmationDocumentResponse items = confirmationDocumentService.confirmConfDoc(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ConfirmationDocumentResponse> cancelConfirmationDoc(@PathVariable Long id){
    	ConfirmationDocumentResponse items = confirmationDocumentService.cancelConfirmationDoc(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @PostMapping("/{id}/close")
    public ResponseEntity<ConfirmationDocumentResponse> closeConfirmationDoc(@PathVariable Long id){
    	ConfirmationDocumentResponse items = confirmationDocumentService.closeConfirmationDoc(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @PostMapping("/{id}/status/{status}")
    public ResponseEntity<ConfirmationDocumentResponse> changeStatus(@PathVariable Long id,@PathVariable ConfirmationDocumentStatus status){
    	ConfirmationDocumentResponse items = confirmationDocumentService.changeStatus(id, status);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @PostMapping("/save")
    public ResponseEntity<ConfirmationDocumentResponse> saveConfirmationDoc(@Valid @RequestBody ConfirmationDocumentRequest request){
    	ConfirmationDocumentResponse items = confirmationDocumentService.saveConfirmationDoc(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @PostMapping("/save-as")
    public ResponseEntity<ConfirmationDocumentResponse> saveAs(@Valid @RequestBody ConfirmationDocumentSaveAsRequest request){
    	ConfirmationDocumentResponse items = confirmationDocumentService.saveAs(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @PostMapping("/save-all")
    public ResponseEntity<List<ConfirmationDocumentResponse>> saveAll(@Valid @RequestBody List<ConfirmationDocumentRequest> requests){
    	List<ConfirmationDocumentResponse> items = confirmationDocumentService.saveAll(requests);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CONFIRMATION_DOCUMENT_FULL_ACCESS)
    @PostMapping("/general-search")
    public ResponseEntity<List<ConfirmationDocumentResponse>> generalSearch(@RequestBody ConfirmationDocumentSearchRequest request){
    	List<ConfirmationDocumentResponse> items = confirmationDocumentService.generalSearch(request);
    	return ResponseEntity.ok(items);
    }
}
