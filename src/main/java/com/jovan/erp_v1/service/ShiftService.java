package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.NoSuchShiftErrorException;
import com.jovan.erp_v1.exception.SupervisorNotFoundException;
import com.jovan.erp_v1.mapper.ShiftMapper;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.ShiftRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.ShiftRequest;
import com.jovan.erp_v1.response.ShiftResponse;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftService implements IShiftService {

	private final ShiftRepository shiftRepository;
	private final ShiftMapper shiftMapper;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public Shift save(ShiftRequest request) {
		User supervisor = userRepository.findById(request.shiftSupervisorId())
				.orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));
		Shift shift = shiftMapper.toEntity(request);
		shift.setShiftSupervisor(supervisor);
		return shiftRepository.save(shift);
	}

	@Transactional
	@Override
	public Shift update(Long id, ShiftRequest request) {
		Shift shift = shiftRepository.findById(id)
				.orElseThrow(() -> new NoSuchShiftErrorException("Shift not found with id: " + id));
		User supervisor = userRepository.findById(request.shiftSupervisorId())
				.orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));
		shift.setStartTime(request.startTime());
		shift.setEndTime(request.endTime());
		shift.setShiftSupervisor(supervisor);
		return shiftRepository.save(shift);
	}

	@Override
	public List<ShiftResponse> getAll() {
		return shiftRepository.findAll().stream()
				.map(shiftMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public ShiftResponse getById(Long id) {
		Shift shift = shiftRepository.findById(id)
				.orElseThrow(() -> new NoSuchShiftErrorException("Shift not found"));
		return shiftMapper.toDto(shift);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if (!shiftRepository.existsById(id)) {
			throw new NoSuchShiftErrorException("Shift not found");
		}
		shiftRepository.deleteById(id);
	}
}
