package com.orhazal.bankingkata.service.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

	public static boolean areEqual(BigDecimal number1, BigDecimal number2) {
		if (number1 == null || number2 == null) {
			return false;
		}
		return number1.compareTo(number2) == 0;
	}

	public static boolean isNegative(BigDecimal number) {
		return number.signum() == -1;
	}

	public static boolean isZero(BigDecimal number) {
		return areEqual(BigDecimal.ZERO, number);
	}
}
