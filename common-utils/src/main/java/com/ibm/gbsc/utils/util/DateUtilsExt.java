package com.ibm.gbsc.utils.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;

/**
 * @author yangyz
 *
 */
public class DateUtilsExt extends DateUtils {
	/**
	 * @param cal cal.
	 */
	public static void goMonthEnd(Calendar cal) {
		int oldMonth = cal.get(Calendar.MONTH);
		do {
			cal.add(Calendar.DATE, 1);
		} while (oldMonth == cal.get(Calendar.MONTH));

		cal.add(Calendar.DATE, -1);
	}

	/**
	 * @param cal cal.
	 */
	public static void goNextMonthBegin(Calendar cal) {
		int oldMonth = cal.get(Calendar.MONTH);
		do {
			cal.add(Calendar.DATE, 1);
		} while (oldMonth == cal.get(Calendar.MONTH));

		// cal.add(Calendar.DATE, -1);
	}

	/**
	 * @param cal cal.
	 */
	public static void goLastMonthEnd(Calendar cal) {
		int mnth = cal.get(Calendar.MONTH);
		cal.add(Calendar.DATE, 1);
		if (mnth == cal.get(Calendar.MONTH)) {
			// 给定日期不是月末
			while (mnth == cal.get(Calendar.MONTH)) {
				cal.add(Calendar.DATE, -1);
			}
			// cal.add(Calendar.DATE, -1);
		} else {
			// 给定日期是月末
			cal.add(Calendar.MONTH, -1);
			cal.add(Calendar.DATE, -1);

		}
	}

	/**
	 * @param date date.
	 * @return string.
	 */
	public static final String toGMT(Date date) {
		Locale aLocale = Locale.US;
		DateFormat fmt = new SimpleDateFormat("EEE,d MMM yyyy hh:mm:ss z", new DateFormatSymbols(aLocale));
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		return fmt.format(date);
	}

	/**
	 * @param reportDate reportDate.
	 * @return date.
	 */
	public static Date toReportDate(String reportDate) {
		try {
			DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
			if (reportDate.length() == 6) {
				reportDate += "01";
				Date dt = fmt.parse(reportDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(dt);
				goMonthEnd(cal);
				return cal.getTime();
			}
			return fmt.parse(reportDate);
		} catch (ParseException e) {
			throw new RuntimeException(reportDate + " is not well formed. Sample form: '20130331'", e);
		}
	}

	/**
	 * @param cal cal.
	 * @return boolean.
	 */
	public static boolean isMonthEnd(Calendar cal) {
		boolean flag = false;
		int mnth = cal.get(Calendar.MONTH);
		cal.add(Calendar.DATE, 1);
		if (mnth != cal.get(Calendar.MONTH)) {
			flag = true;
		}
		cal.add(Calendar.DATE, -1);
		return flag;
	}

	// 生成页面的年月日

	/**
	 * @param year year.
	 * @return map.
	 */
	public static Map<Integer, Integer> getReportYears(int year) {

		Map<Integer, Integer> items = new LinkedHashMap<Integer, Integer>();
		for (int start = 2000; start <= year; start++) {
			items.put(start, start);
		}
		return items;
	}

	/**
	 * @return map.
	 */
	public static Map<Integer, Integer> getReportMonth() {

		Map<Integer, Integer> items = new LinkedHashMap<Integer, Integer>();
		for (int month = 1; month < 13; month++) {
			items.put(month, month);
		}
		return items;
	}

	/**
	 * @return map.
	 */
	public static Map<Integer, Integer> getReportDate() {

		Map<Integer, Integer> items = new LinkedHashMap<Integer, Integer>();
		for (int date = 1; date <= 31; date++) {
			items.put(date, date);
		}
		return items;
	}
}
