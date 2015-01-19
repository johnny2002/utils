package com.ibm.gbsc.web.springmvc.converter;

import java.text.ParseException;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * @author yangyz
 *
 */
public class StringDateConverter extends DateConverterBase implements Converter<String, Date> {

	/** {@inheritDoc} */
	@Override
	public Date convert(String source) {
		if (source == null) {
			return null;
		}
		String trim = source.trim();
		if (trim.length() == 0) {
			return null;
		}
		try {
			return source.contains(":") ? getDateTimeFormat().parse(trim) : getDateFormat().parse(trim);
		} catch (ParseException e) {
			throw new RuntimeException("无效的日期格式：" + trim);
		}
	}

}
