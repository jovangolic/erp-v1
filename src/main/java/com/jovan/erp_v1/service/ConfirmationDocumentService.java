package com.jovan.erp_v1.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jovan.erp_v1.enumeration.ConfirmationDocumentStatus;
import com.jovan.erp_v1.exception.ConfirmationDocumentNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.NoSuchShiftErrorException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.ConfirmationDocumentMapper;
import com.jovan.erp_v1.model.ConfirmationDocument;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.ConfirmationDocumentRepository;
import com.jovan.erp_v1.repository.ShiftRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.specification.ConfirmationDocumentSpecification;
import com.jovan.erp_v1.request.ConfirmationDocumentRequest;
import com.jovan.erp_v1.response.ConfirmationDocumentResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.ConfirmationDocumentSaveAsRequest;
import com.jovan.erp_v1.search_request.ConfirmationDocumentSearchRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfirmationDocumentService implements IConfirmationDocumentService {

	private final ConfirmationDocumentRepository confirmationDocumentRepository;
	private final UserRepository userRepository;
	private final ShiftRepository shiftRepository;
	private final ConfirmationDocumentMapper mapper;
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

	private Path resolveFilePath(String fileName) {
		return Paths.get(FILE_DIRECTORY, fileName);
	}

	@Transactional
	public ConfirmationDocument saveDocument(ConfirmationDocumentRequest request) {
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		Shift shift = shiftRepository.findById(request.shiftId())
				.orElseThrow(() -> new NoSuchShiftErrorException("Shift not found"));
		ConfirmationDocument document = new ConfirmationDocument();
		document.setFilePath(request.filePath()); // Ako dolazi putanja iz requesta (npr. ako je klijent već uploadovao)
		document.setCreatedAt(request.createdAt() != null ? request.createdAt() : LocalDateTime.now());
		document.setCreatedBy(user);
		document.setShift(shift);
		return confirmationDocumentRepository.save(document);
	}

	@Transactional
	@Override
	public ConfirmationDocument update(Long id, ConfirmationDocumentRequest request) {
		ConfirmationDocument doc = confirmationDocumentRepository.findById(id)
				.orElseThrow(() -> new ConfirmationDocumentNotFoundException("Document not found" + id));
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		Shift shift = shiftRepository.findById(request.shiftId())
				.orElseThrow(() -> new NoSuchShiftErrorException("Shift not found"));
		doc.setFilePath(request.filePath());
		doc.setCreatedAt(request.createdAt());
		doc.setCreatedBy(user);
		doc.setShift(shift);
		return confirmationDocumentRepository.save(doc);
	}	
	
	@Transactional(readOnly = true)
	@Override
	public ConfirmationDocumentResponse trackConfirmationDoc(Long id) {
		ConfirmationDocument conf = confirmationDocumentRepository.findById(id).orElseThrow(()-> new ValidationException("ConfirmationDocument not found with id "+id));
		return new ConfirmationDocumentResponse(conf);
	}

	@Transactional
	@Override
	public ConfirmationDocumentResponse confirmConfDoc(Long id) {
		ConfirmationDocument conf = confirmationDocumentRepository.findById(id).orElseThrow(()-> new ValidationException("ConfirmationDocument not found with id "+id));
		conf.setConfirmed(true);
		conf.setStatus(ConfirmationDocumentStatus.CONFIRMED);
		return new ConfirmationDocumentResponse(confirmationDocumentRepository.save(conf));
	}

	@Transactional
	@Override
	public ConfirmationDocumentResponse cancelConfirmationDoc(Long id) {
		ConfirmationDocument conf = confirmationDocumentRepository.findById(id).orElseThrow(()-> new ValidationException("ConfirmationDocument not found with id "+id));
		if(conf.getStatus() != ConfirmationDocumentStatus.CONFIRMED && conf.getStatus() != ConfirmationDocumentStatus.NEW) {
			throw new ValidationException("Only NEW or CONFIRMED confirmation-documents can be cancelled");
		}
		conf.setStatus(ConfirmationDocumentStatus.CANCELLED);
		return new ConfirmationDocumentResponse(confirmationDocumentRepository.save(conf));
	}

	@Transactional
	@Override
	public ConfirmationDocumentResponse closeConfirmationDoc(Long id) {
		ConfirmationDocument conf = confirmationDocumentRepository.findById(id).orElseThrow(()-> new ValidationException("ConfirmationDocument not found with id "+id));
		if(conf.getStatus() != ConfirmationDocumentStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED confirmation-documents can be closed");
		}
		conf.setStatus(ConfirmationDocumentStatus.CLOSED);
		return new ConfirmationDocumentResponse(confirmationDocumentRepository.save(conf));
	}

	@Transactional
	@Override
	public ConfirmationDocumentResponse changeStatus(Long id, ConfirmationDocumentStatus status) {
		ConfirmationDocument conf = confirmationDocumentRepository.findById(id).orElseThrow(()-> new ValidationException("ConfirmationDocument not found with id "+id));
		validateConfirmationDocumentStatus(status);
		if(conf.getStatus() == ConfirmationDocumentStatus.CLOSED) {
			throw new ValidationException("Closed confirmation-documents cannot change status");
		}
		if(status == ConfirmationDocumentStatus.CONFIRMED) {
			if(conf.getStatus() != ConfirmationDocumentStatus.NEW) {
				throw new ValidationException("Only NEW confirmation-documents can be confirmed");
			}
			conf.setConfirmed(true);	
		}
		conf.setStatus(status);
		return new ConfirmationDocumentResponse(confirmationDocumentRepository.save(conf));
	}

	@Transactional
	@Override
	public ConfirmationDocumentResponse saveConfirmationDoc(ConfirmationDocumentRequest request) {
		ConfirmationDocument conf = ConfirmationDocument.builder()
				.id(request.id())
				.filePath(request.filePath())
				.createdBy(validateUserId(request.userId()))
				.shift(validateShiftId(request.shiftId()))
				.confirmed(request.confirmed())
				.status(request.status())
				.build();
		ConfirmationDocument saved = confirmationDocumentRepository.save(conf);
		return new ConfirmationDocumentResponse(saved);
	}
	
	private final AbstractSaveAllService<ConfirmationDocument, ConfirmationDocumentResponse> saveAllHelper = new AbstractSaveAllService<ConfirmationDocument, ConfirmationDocumentResponse>() {
		
		@Override
		protected Function<ConfirmationDocument, ConfirmationDocumentResponse> toResponse() {
			return ConfirmationDocumentResponse::new;
		}
		
		@Override
		protected JpaRepository<ConfirmationDocument, Long> getRepository() {
			return confirmationDocumentRepository;
		}
	};
	
	private final AbstractSaveAsService<ConfirmationDocument, ConfirmationDocumentResponse> saveAsHelper = new AbstractSaveAsService<ConfirmationDocument, ConfirmationDocumentResponse>() {
		
		@Override
		protected ConfirmationDocumentResponse toResponse(ConfirmationDocument entity) {
			return new ConfirmationDocumentResponse(entity);
		}
		
		@Override
		protected JpaRepository<ConfirmationDocument, Long> getRepository() {
			return confirmationDocumentRepository;
		}
		
		@Override
		protected ConfirmationDocument copyAndOverride(ConfirmationDocument source, Map<String, Object> overrides) {
			return ConfirmationDocument.builder()
					.filePath((String) overrides.getOrDefault("File-path", source.getFilePath()))
					.createdBy(validateUserId(source.getCreatedBy().getId()))
					.shift(validateShiftId(source.getShift().getId()))
					.confirmed(source.getConfirmed())
					.status(source.getStatus())
					.build();
		}
	};
	
	@Transactional
	@Override
	public ConfirmationDocumentResponse saveAs(ConfirmationDocumentSaveAsRequest request) {
		Map<String, Object> overrides = new HashMap<>();
		if(request.filePath() != null) overrides.put("File-path", request.filePath());
		if(request.userId() != null) overrides.put("User-ID", request.userId());
		if(request.shiftId() != null) overrides.put("Shift-ID", request.shiftId());
		if(request.confirmed() != null) overrides.put("Confirmed", request.confirmed());
		if(request.status() != null) overrides.put("Status", request.status());
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}

	@Transactional
	@Override
	public List<ConfirmationDocumentResponse> saveAll(List<ConfirmationDocumentRequest> requests) {
		List<ConfirmationDocument> items = requests.stream()
				.map(req -> ConfirmationDocument.builder()
						.id(req.id())
						.filePath(req.filePath())
						.createdBy(validateUserId(req.userId()))
						.shift(validateShiftId(req.shiftId()))
						.confirmed(req.confirmed())
						.status(req.status())
						.build())
				.collect(Collectors.toList());
		return saveAllHelper.saveAll(items);
	}

	@Override
	public List<ConfirmationDocumentResponse> generalSearch(ConfirmationDocumentSearchRequest request) {
		Specification<ConfirmationDocument> spec = ConfirmationDocumentSpecification.fromRequest(request);
		List<ConfirmationDocument> items = confirmationDocumentRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No ConfirmationDocuments found for given criteria");
		}
		return items.stream().map(mapper::toResponse).collect(Collectors.toList());
	}
	
	private void validateConfirmationDocumentStatus(ConfirmationDocumentStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("ConfirmationDocumentStatus status must not be null"));
	}
	
	private User validateUserId(Long id) {
		if(id == null) {
			throw new ValidationException("User ID must not be null");
		}
		return userRepository.findById(id).orElseThrow(() -> new ValidationException("User not found with id"+id));
	}
	
	private Shift validateShiftId(Long id) {
		if(id == null) {
			throw new ValidationException("Shift ID must not be null");
		}
		return shiftRepository.findById(id).orElseThrow(() -> new ValidationException("Shift not found with id "+id));
	}

}
