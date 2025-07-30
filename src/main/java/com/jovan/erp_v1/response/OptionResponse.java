package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.OptionCategory;
import com.jovan.erp_v1.model.Option;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionResponse {

	private Long id;
	private String label;
	private String value;
	private OptionCategory category;
	private Boolean active;
	
	public OptionResponse(Option opt) {
		this.id = opt.getId();
		this.label = opt.getLabel();
		this.value = opt.getValue();
		this.category = opt.getCategory();
		this.active = opt.getActive();
	}
}
