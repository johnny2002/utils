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
		return getDateFormat().format(source);
	}

}
