package com.ibm.gbsc.web.springmvc.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * @author yangyz
 *
 */
public class DateStringConverter extends DateConverterBase implements Converter<Date, String> {

	/** {@inheritDoc} */
	@Override
	public String convert(Date source) {
		if (source == null) {
			return "";
		}
		if (source instanceof java.sql.Date) {
			return getDateFormat().format(source);
		}
		if (source instanceof java.sql.Time) {
			return getTimeFormat().format(source);
		}
		String dtString = getDateTimeFormat().format(source);
		int indexOfTime = dtString.indexOf(" 00:00:00");
		if (indexOfTime > 0) {
			return dtString.substring(0, indexOfTime);
		}
		return dtString;
	}

}
