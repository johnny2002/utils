/**
 *
 */
package com.ibm.gbsc.web.springmvc.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Johnny
 *
 */
public class DateConverterBase {
	private final String datePattern = "yyyy-MM-dd";
	private final String timePattern = "HH:mm:ss";
	private DateFormat dateFormat = new SimpleDateFormat(datePattern);
	private DateFormat dateTimeFormat = new SimpleDateFormat(datePattern + " " + timePattern);

	/**
	 * @return dateformate.
	 */
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat
	 *            is to set value.
	 */
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @return the dateTimeFormat
	 */
	public DateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}

	/**
	 * @param dateTimeFormat
	 *            the dateTimeFormat to set
	 */
	public void setDateTimeFormat(DateFormat dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

}
