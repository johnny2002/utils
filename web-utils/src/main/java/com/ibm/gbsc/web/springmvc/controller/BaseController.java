package com.ibm.gbsc.web.springmvc.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Controller父类.
 *
 * @author Johnny
 *
 */
public abstract class BaseController {

	@Autowired
	MessageSource messageSource;

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	private final Map<String, Validator> validatorMap = new HashMap<String, Validator>();

	/**
	 * @param binder
	 *            binder.
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		for (String formCommandName : validatorMap.keySet()) {
			if (formCommandName.equals(binder.getObjectName())) {
				binder.setValidator(validatorMap.get(formCommandName));
			}
		}
	}

	/**
	 * @param formCommand
	 *            formCommand.
	 * @param validator
	 *            validator.
	 */
	protected void setValidator(String formCommand, Validator validator) {
		validatorMap.put(formCommand, validator);
	}

	/**
	 * @param validatorMap
	 *            validatorMap.
	 */
	protected void setValidator(Map<String, Validator> validatorMap) {
		validatorMap.putAll(validatorMap);
	}

	/**
	 * @param msgId
	 *            msgId.
	 * @param params
	 *            params.
	 * @param locale
	 *            locale.
	 * @return string.
	 */
	public String getMsg(String msgId, Object[] params, Locale locale) {
		return messageSource.getMessage(msgId, params, locale);
	}

	/**
	 * @param msgId
	 *            msgId.
	 * @param params
	 *            params.
	 * @return string.
	 */
	public String getMsg(String msgId, Object[] params) {
		return messageSource.getMessage(msgId, params, null);
	}

	/**
	 * @param msgId
	 *            msgId.
	 * @return string.
	 */
	public String getMsg(String msgId) {
		return messageSource.getMessage(msgId, null, null);

	}

}
