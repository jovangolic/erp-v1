package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.GoToCategory;
import com.jovan.erp_v1.enumeration.GoToType;
import com.jovan.erp_v1.model.GoTo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoToResponse {

	private Long id;
	private String label;
	private String description;
	private GoToCategory category;
	private GoToType type;
	private String path;
	private String icon;
	@Builder.Default
    private Boolean active = true;
	private String roles;
	
	public GoToResponse(GoTo gt) {
		this.id = gt.getId();
		this.label = gt.getLabel();
		this.description = gt.getDescription();
		this.category = gt.getCategory();
		this.type = gt.getType();
		this.path = gt.getPath();
		this.icon = gt.getIcon();
		this.active = gt.getActive();
		this.roles = gt.getRoles();
	}
}
