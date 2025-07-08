package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.mapstruct.control.MappingControl.Use;
import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.ShiftRequest;
import com.jovan.erp_v1.response.ShiftResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShiftMapper extends AbstractMapper<ShiftRequest> {

	private final UserRepository userRepository;

    public Shift toEntity(ShiftRequest request) {
        Shift shift = new Shift();
        shift.setStartTime(request.startTime());
        shift.setEndTime(request.endTime());
        if (request.shiftSupervisorId() != null) {
            userRepository.findById(request.shiftSupervisorId())
                .ifPresent(shift::setShiftSupervisor);
        }
        return shift;
    }

    public ShiftResponse toResponse(Shift shift) {
    	Objects.requireNonNull(shift, "Shift must not be null");
        return new ShiftResponse(shift);
    }
    
    public Shift toUpdateEntity(Shift shift, ShiftRequest request) {
    	Objects.requireNonNull(request,"ShiftRequest must not be null");
    	Objects.requireNonNull(shift,"ShiftRequest must not be null");
    	validateIdForUpdate(request, ShiftRequest::id);
    	return buildShiftFromRequest(shift, request);
    }
    
    private Shift buildShiftFromRequest(Shift shift, ShiftRequest request) {
    	/*if (request.documents() != null) {
    	    List<ConfirmationDocument> docs = mapDocuments(request.documents());
    	    shift.setDocuments(docs);
    	}*/ //treba ovu promenljivu dodati u request, ako bude bilo potrebno
    	shift.setStartTime(request.startTime());
    	shift.setEndTime(request.endTime());
    	shift.setShiftSupervisor(fetchUser(request.shiftSupervisorId()));
    	return shift;
    }
    
    //ako zatreba
    /*private Shift buildShiftFromRequest(Shift shift, ShiftRequest request) {
        shift.setStartTime(request.startTime());
        shift.setEndTime(request.endTime());
        shift.setShiftSupervisor(fetchUser(request.shiftSupervisorId()));
        
        if (request.documents() != null) {
            List<ConfirmationDocument> docs = request.documents().stream()
                .map(docRequest -> {
                    ConfirmationDocument doc = new ConfirmationDocument();
                    doc.setFilePath(docRequest.filePath());
                    doc.setCreatedAt(LocalDateTime.now());
                    doc.setCreatedBy(fetchUser(docRequest.createdById()));
                    doc.setShift(shift); // veoma bitno
                    return doc;
                }).collect(Collectors.toList());
            shift.setDocuments(docs);
        }

        return shift;
    }*/
    
    public List<ShiftResponse> toList(List<Shift> responses){
    	if(responses == null || responses.isEmpty()) {
    		return Collections.emptyList();
    	}
    	return responses.stream()
    			.map(this::toResponse)
    			.collect(Collectors.toList());
    }
    
    private User fetchUser(Long userId) {
    	if(userId == null) {
    		throw new UserNotFoundException("User ID must not be null");
    	}
    	return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: "+userId));
    }
	
}
