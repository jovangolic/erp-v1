package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.ConfirmationDocument;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.ShiftRequest;
import com.jovan.erp_v1.response.ShiftResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class ShiftMapper extends AbstractMapper<ShiftRequest> {

    public Shift toEntity(ShiftRequest request,User shiftSupervisor) {
    	Objects.requireNonNull(request,"ShiftRequest must not be null");
    	Objects.requireNonNull(shiftSupervisor,"ShiftSupervisor must not be null");
    	validateIdForCreate(request, ShiftRequest::id);
        Shift shift = new Shift();
        shift.setEndTime(request.endTime());
        shift.setShiftSupervisor(shiftSupervisor);
        shift.setDocuments(new ArrayList<>());
        return shift;
    }

    public ShiftResponse toResponse(Shift shift) {
    	Objects.requireNonNull(shift, "Shift must not be null");
        return new ShiftResponse(shift);
    }
    
    public Shift toUpdateEntity(Shift shift, ShiftRequest request, User shiftSupervisor) {
    	Objects.requireNonNull(request,"ShiftRequest must not be null");
    	Objects.requireNonNull(shift,"ShiftRequest must not be null");
    	Objects.requireNonNull(shiftSupervisor,"ShiftSupervisor must not be null");
    	validateIdForUpdate(request, ShiftRequest::id);
    	return buildShiftFromRequest(shift, request, shiftSupervisor);
    }
    
    /*private Shift buildShiftFromRequest(Shift shift, ShiftRequest request, User shiftSupervisor) {
    	if (request.documents() != null) {
    	    List<ConfirmationDocument> docs = mapDocuments(request.documents());
    	    shift.setDocuments(docs);
    	} //treba ovu promenljivu dodati u request, ako bude bilo potrebno
    	shift.setEndTime(request.endTime());
    	shift.setShiftSupervisor(shiftSupervisor);
    	return shift;
    }*/
    
    //ako zatreba
    private Shift buildShiftFromRequest(Shift shift, ShiftRequest request, User shiftSupervisor) {
        shift.setEndTime(request.endTime());
        shift.setShiftSupervisor(shiftSupervisor);
        if (request.documents() != null) {
            List<ConfirmationDocument> docs = request.documents().stream()
                .map(docRequest -> {
                    ConfirmationDocument doc = new ConfirmationDocument();
                    doc.setFilePath(docRequest.filePath());
                    doc.setCreatedBy(shiftSupervisor);
                    doc.setShift(shift); // veoma bitno
                    return doc;
                }).collect(Collectors.toList());
            shift.setDocuments(docs);
        }
        return shift;
    }
    
    public List<ShiftResponse> toList(List<Shift> responses){
    	if(responses == null || responses.isEmpty()) {
    		return Collections.emptyList();
    	}
    	return responses.stream()
    			.map(this::toResponse)
    			.collect(Collectors.toList());
    }
}
