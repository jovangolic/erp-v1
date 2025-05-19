package com.jovan.erp_v1.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.jovan.erp_v1.exception.UserInvalidException;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.UserRepository;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Component
@RequiredArgsConstructor
public class ValidationUtil {

	public static final Logger log = LoggerFactory.getLogger(ValidationUtil.class);
    //public static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private final UserRepository userRepository;

	public static boolean isValidEmail(String identifier) {
		try {
			new InternetAddress(identifier).validate();
			return true;
		} catch (AddressException e) {
			log.warn("Invalid email address: {}", identifier);
			return false;
		}
	}

	public static void validatePassword(String password) {
		if (password == null || password.length() < 8) {
			throw new UserInvalidException(ApiMessages.PASSWORD_TOO_SHORT_ERROR.getMessage());
		}
		if (password.length() >= 128) {
			throw new UserInvalidException(ApiMessages.PASSWORD_TOO_LONG_ERROR.getMessage());
		}
		if (password.matches(".*\\s.*")) {
			throw new UserInvalidException(ApiMessages.PASSWORD_CONTAINS_WHITESPACE_ERROR.getMessage());
		}

		StringBuilder message = new StringBuilder("Password must contain at least ");
		boolean needsComma = false;

		if (!password.matches(".*[A-Z].*")) {
			message.append("one uppercase letter");
			needsComma = true;
		}
		if (!password.matches(".*[a-z].*")) {
			if (needsComma) message.append(", ");
			message.append("one lowercase letter");
			needsComma = true;
		}
		if (!password.matches(".*[0-9].*")) {
			if (needsComma) message.append(", ");
			message.append("one digit");
			needsComma = true;
		}
		if (!password.matches(".*[^A-Za-z0-9].*")) {
			if (needsComma) message.append(", ");
			message.append("one special character");
		}

		if (message.length() > "Password must contain at least ".length()) {
			int lastCommaIndex = message.lastIndexOf(",");
			if (lastCommaIndex > -1) {
				message.replace(lastCommaIndex, lastCommaIndex + 1, " and");
			}
			throw new UserInvalidException(message.toString());
		}
	}

	public static void validateUserDetailsNotEmpty(User user) {
		if (user == null ||
			user.getFirstName() == null || user.getFirstName().isEmpty() ||
			user.getLastName() == null || user.getLastName().isEmpty() ||
			user.getEmail() == null || user.getEmail().isEmpty() ||
			user.getPassword() == null || user.getPassword().isEmpty()) {
			throw new UserInvalidException(ApiMessages.USER_DETAILS_EMPTY_ERROR.getMessage());
		}
	}

	public void validateUserDetails(User user) {
		validateUserDetailsNotEmpty(user);
		if (!isValidEmail(user.getEmail())) {
			throw new UserInvalidException(String.format(
				ApiMessages.USER_EMAIL_ADDRESS_INVALID_ERROR.getMessage(), user.getEmail()));
		}
		validatePassword(user.getPassword());
	}

	public void validateNewUser(User user) {
		validateUserDetails(user);
		if (doesEmailExist(user.getEmail())) {
			throw new UserInvalidException(ApiMessages.USER_EMAIL_ALREADY_EXISTS_ERROR.getMessage());
		}
	}

	public boolean doesEmailExist(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

}
