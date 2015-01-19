package com.ibm.gbsc.web.springmvc.view;

import java.io.File;

/**
 * @author yangyz
 *
 */
public final class CustomerResourceResolver {
	/**
	 *
	 */
	private CustomerResourceResolver() {

	}

	/**
	 *
	 */
	public static final String CUSTOMER_CODE = "CUSTOMER_CODE";

	private static final String UNIX_PATH = "/default/";

	private static final String DOS_PATH = "\\default\\";

	/**
	 * @param realPath
	 *            realPath.
	 * @param url
	 *            url.
	 * @param customer
	 *            customer.
	 * @return string.
	 */
	public static String resolveResourcePath(String realPath, String url, String customer) {
		if (customer == null) {
			return url;
		}
		String localPath = realPath;
		int idx = realPath.indexOf(UNIX_PATH);
		if (idx < 0) {
			idx = realPath.indexOf(DOS_PATH);
		}
		if (idx >= 0) {
			localPath = realPath.substring(0, idx + 1) + customer + realPath.substring(idx - 1 + UNIX_PATH.length());
			File f = new File(localPath);

			if (f.exists()) {
				idx = url.indexOf("/default/");
				url = url.substring(0, idx + 1) + customer + url.substring(idx + UNIX_PATH.length() - 1);
				return url;
			}
		}

		return url;
	}
}
