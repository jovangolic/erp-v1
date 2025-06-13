package com.jovan.erp_v1.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.UUID;

@Component
public class CredentialGenerator {

    private static final String EMAIL_DOMAIN = "firma.rs";
    private static final SecureRandom random = new SecureRandom();
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%&*";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

    // Generiši email: marko.markovic@firma.rs
    public String generateEmail(String firstName, String lastName) {
        return (firstName + "." + lastName + "@" + EMAIL_DOMAIN).toLowerCase();
    }

    // Generiši korisničko ime: mmarkovic
    public String generateUsername(String firstName, String lastName) {
        return (firstName.charAt(0) + lastName).toLowerCase();
    }

    // Generiši jaku lozinku: velika, mala slova, broj i spec. karakter
    public String generateRandomPassword() {
        StringBuilder sb = new StringBuilder();
        sb.append(randomChar(UPPER));
        sb.append(randomChar(LOWER));
        sb.append(randomChar(DIGITS));
        sb.append(randomChar(SPECIAL));
        for (int i = 0; i < 4; i++) {
            sb.append(randomChar(ALL));
        }
        return shuffleString(sb.toString());
    }

    private char randomChar(String chars) {
        return chars.charAt(random.nextInt(chars.length()));
    }

    private String shuffleString(String input) {
        char[] a = input.toCharArray();
        for (int i = a.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
        return new String(a);
    }
}
