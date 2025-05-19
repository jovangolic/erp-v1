package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.NoSuchShiftErrorException;
import com.jovan.erp_v1.exception.NoSuchShiftReportFoundException;
import com.jovan.erp_v1.mapper.ShiftReportMapper;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.ShiftReport;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.ShiftReportRepository;
import com.jovan.erp_v1.repository.ShiftRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.ShiftReportRequest;
import com.jovan.erp_v1.response.ShiftReportResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftReportService implements IShiftReportService {

	private final ShiftReportRepository shiftReportRepository;
	private final ShiftReportMapper shiftReportMapper;
	private final UserRepository userRepository;
	private final ShiftRepository shiftRepository;
	
	@Transactional
	@Override
	public ShiftReport save(ShiftReportRequest request) {
		User creator = userRepository.findById(request.createdById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Shift shift = shiftRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchShiftErrorException("Shift not found"));
        ShiftReport report = shiftReportMapper.toEntity(request);
        report.setCreatedBy(creator);
        report.setRelatedShift(shift);
        report.setCreatedAt(LocalDateTime.now());

        return shiftReportRepository.save(report);
	}
	
	@Transactional
	@Override
	public ShiftReport update(Long id, ShiftReportRequest request) {
		ShiftReport report = shiftReportRepository.findById(id).orElseThrow(() -> new NoSuchShiftReportFoundException("Report not found with id: " + id));
		Shift shift = shiftRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchShiftErrorException("Shift not found"));
		User creator = userRepository.findById(request.createdById())
                .orElseThrow(() -> new RuntimeException("User not found"));
		report.setCreatedBy(creator);
		report.setRelatedShift(shift);
		report.setDescription(request.description());
		report.setFilePath(request.filePath());
		return shiftReportRepository.save(report);
	}
	@Override
	public List<ShiftReportResponse> getAll() {
		return shiftReportRepository.findAll().stream()
                .map(shiftReportMapper::toDto)
                .collect(Collectors.toList());
	}
	@Override
    public ShiftReportResponse getById(Long id) {
        ShiftReport report = shiftReportRepository.findById(id)
                .orElseThrow(() -> new NoSuchShiftReportFoundException("Report not found"));
        return shiftReportMapper.toDto(report);
    }

    @Override
    public List<ShiftReportResponse> getByShiftId(Long shiftId) {
        return shiftReportRepository.findByRelatedShiftId(shiftId).stream()
                .map(shiftReportMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
	@Override
	public void delete(Long id) {
		if(!shiftReportRepository.existsById(id)) {
			throw new NoSuchShiftReportFoundException("Shift-report not found");
		}
		shiftReportRepository.deleteById(id);
	}
	
	
	
}
