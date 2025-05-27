package com.jovan.erp_v1.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jovan.erp_v1.exception.NoSuchShiftErrorException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.model.ConfirmationDocument;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.ConfirmationDocumentRepository;
import com.jovan.erp_v1.repository.ShiftRepository;
import com.jovan.erp_v1.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfirmationDocumentService implements IConfirmationDocumentService {

	private final ConfirmationDocumentRepository confirmationDocumentRepository;
	private final UserRepository userRepository;
	private final ShiftRepository shiftRepository;
	private final String FILE_DIRECTORY = "C:/Users/Admin/erp";

	@Transactional
	public ConfirmationDocument saveDocument(ConfirmationDocument document) {
		document.setCreatedAt(LocalDateTime.now());
		return confirmationDocumentRepository.save(document);
	}

	public Optional<ConfirmationDocument> getDocumentById(Long id) {
		return confirmationDocumentRepository.findById(id);
	}

	public List<ConfirmationDocument> getAllDocuments() {
		return confirmationDocumentRepository.findAll();
	}

	public List<ConfirmationDocument> getDocumentsByUserId(Long userId) {
		return confirmationDocumentRepository.findByCreatedById(userId);
	}

	public List<ConfirmationDocument> getDocumentsCreatedAfter(LocalDateTime date) {
		return confirmationDocumentRepository.findByCreatedAtAfter(date);
	}

	@Transactional
	public void deleteDocument(Long id) {
		confirmationDocumentRepository.deleteById(id);
	}

	@Override
	public ConfirmationDocument uploadDocument(MultipartFile file, Long userId, Long shiftId) throws IOException {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
		Shift shift = shiftRepository.findById(shiftId)
				.orElseThrow(() -> new NoSuchShiftErrorException("Shift not found"));
		// cuvanje fajla na disku
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(FILE_DIRECTORY, fileName);
		Files.createDirectories(filePath.getParent());
		Files.write(filePath, file.getBytes());
		// kreiranje i cuvanje dokumenta
		ConfirmationDocument document = new ConfirmationDocument();
		document.setFilePath(fileName);
		document.setCreatedAt(LocalDateTime.now());
		document.setCreatedBy(user);
		document.setShift(shift);
		return confirmationDocumentRepository.save(document);
	}
}
