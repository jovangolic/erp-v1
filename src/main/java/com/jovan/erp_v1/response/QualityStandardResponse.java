package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.QualityStandard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityStandardResponse {

	private Long id;
	
	
	public QualityStandardResponse(QualityStandard qs) {
		this.id = qs.getId();
	}
}
