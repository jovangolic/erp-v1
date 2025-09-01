package com.jovan.erp_v1.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *Pomocna klasa za centralizaciju svih finansijskih operacija i validacija. Ova klasa se ne moze instancirati
 */
public final class MoneyUtils {

	private static final int SCALE = 2; // 2 decimale - standard za ERP
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    private MoneyUtils() {
        // utility klasa - nema instanciranja
    }

    /** Normalizacija iznosa na standardnu skalu */
    public static BigDecimal normalize(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);
        }
        return value.setScale(SCALE, ROUNDING_MODE);
    }

    /** Provera da li je amount pozitivan */
    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

    /** Provera da li je amount umnozak odredjenog broja (npr. 100) */
    public static boolean isMultipleOf(BigDecimal value, int divisor) {
        if (value == null) return false;
        return value.remainder(BigDecimal.valueOf(divisor))
                    .compareTo(BigDecimal.ZERO) == 0;
    }

    /** Sigurno deljenje (sa scale i rounding mode) */
    public static BigDecimal safeDivide(BigDecimal dividend, BigDecimal divisor) {
        if (dividend == null || divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Invalid division: null or zero divisor");
        }
        return dividend.divide(divisor, SCALE, ROUNDING_MODE);
    }

    /** Dodavanje iznosa */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return normalize(a).add(normalize(b));
    }

    /** Oduzimanje iznosa */
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return normalize(a).subtract(normalize(b));
    }
}
