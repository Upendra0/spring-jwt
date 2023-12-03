package com.upendra.utils;

public class DayToMillisConverter {

	public static Long convert(Long days) {
		return days*24*60*60*1000;
	}
}
