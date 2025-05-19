package com.jovan.erp_v1.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jovan.erp_v1.model.User;

public class ERPUserDetails implements UserDetails {
	
	private final User user;
	
	 public ERPUserDetails(User user) {
	        this.user = user;
	    }

	    public Long getId() {
	        return user.getId();
	    }

	    public String getEmail() {
	        return user.getEmail();
	    }

	    public String getFullName() {
	        return user.getFirstName() + " " + user.getLastName();
	    }

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return user.getRoles()
	                .stream()
	                .map(role -> new SimpleGrantedAuthority(role.getName()))
	                .collect(Collectors.toList());
	    }

	    @Override
	    public String getPassword() {
	        return user.getPassword();
	    }

	    @Override
	    public String getUsername() {
	        return user.getUsername(); // koristiš username za logovanje
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true; // eventualno dodaj flag "enabled" u entitet ako želiš bolju kontrolu
	    }

}
