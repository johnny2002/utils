/**
 *
 */
package com.ibm.gbsc.web.springmvc.view;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.view.JstlView;

/**
 * @author Johnny
 *
 */
public class CustomerJstlView extends JstlView {
	Logger log = LoggerFactory.getLogger(getClass());
	private String customer;

	/**
	 *
	 */
	public CustomerJstlView() {
		super();
		init();
	}

	/**
	 *
	 */
	private void init() {
		customer = System.getenv(CustomerResourceResolver.CUSTOMER_CODE);
		if (customer == null) {
			customer = System.getProperty(CustomerResourceResolver.CUSTOMER_CODE);
		}
		if (StringUtils.isBlank(customer)) {
			customer = null;
		}
	}

	/**
	 * @param url
	 *            url.
	 * @param messageSource
	 *            messageSource.
	 */
	public CustomerJstlView(String url, MessageSource messageSource) {
		super(url, messageSource);
		init();
	}

	/**
	 * @param url
	 *            url.
	 */
	public CustomerJstlView(String url) {
		super(url);
		init();
	}

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		if (customer != null) {
			String resolvedUrl = CustomerResourceResolver
			        .resolveResourcePath(getServletContext().getRealPath(getUrl()), getUrl(), customer);
			if (log.isDebugEnabled()) {
				log.debug("The resolved url:" + resolvedUrl);
			}
			setUrl(resolvedUrl);
		}
		return super.checkResource(locale);
	}

}
