package com.jovan.erp_v1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jovan.erp_v1.enumeration.BuyerStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Buyer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String companyName;
	
	@Column(nullable = false, unique = true)
	private String pib;
	
	@NotEmpty
	@Column(nullable = false)
	private String address;
	
	@Column
	private String contactPerson;
	
	@NotEmpty
	@Email
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String phoneNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private BuyerStatus status = BuyerStatus.NEW;
	
	@Column(nullable = false)
	@Builder.Default
	private Boolean confirmed = false;
	
	@OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
	@Builder.Default
	private List<SalesOrder> salesOrders = new ArrayList<>();
	
	public static String generateRandomPib() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for(var i = 0; i < 10; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}
}
