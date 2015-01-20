package com.ibm.gbsc.auth.encode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * 定制的SHA编码器，能处理未编码和已编码的情况，用于实现模拟单点登录.
 * 
 * @author Johnny
 */
public class CustomizedShaPasswordEncoder extends ShaPasswordEncoder {
	/**
	 * encoded password prefix.
	 */
	public static final String ENCODED_PREFIX = "ENC|";
	/**
	 * delimeter of encoded password and method.
	 */
	public static final String ENCODED_DELIMETER = ":";

	/**
	 * logger.
	 */
	Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * default constructor.
	 */
	public CustomizedShaPasswordEncoder() {
		super();
	}

	/**
	 * @param strength
	 *            encode strength
	 */
	public CustomizedShaPasswordEncoder(int strength) {
		super(strength);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder#isPasswordValid(java.lang.String,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		if (rawPass.startsWith(ENCODED_PREFIX)) {
			int sepPos = rawPass.indexOf(ENCODED_DELIMETER);
			String encMethod = rawPass.substring(ENCODED_PREFIX.length(), sepPos);
			if (!this.getAlgorithm().equals(encMethod)) {
				throw new RuntimeException("Encode method " + encMethod + " is not supported. We only support '" + this.getAlgorithm() + "'");
			}
			String encPassIn = rawPass.substring(sepPos + 1);
			return encPassIn.equals(encPass);
		}
		return super.isPasswordValid(encPass, rawPass, salt);
	}

}
