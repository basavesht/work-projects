package com.tcs.bancs.ui.filters.security.saml;

import org.apache.log4j.Logger;

public class SimpleKeyProperties {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SimpleKeyProperties.class);
	
	private String algorithm;
	private String init;
	private String key;
	private String mode = "ECB";//Default
	private String padding = "PKCS5Padding";//Default
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String getInit() {
		return init;
	}
	public void setInit(String init) {
		this.init = init;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getPadding() {
		return padding;
	}
	public void setPadding(String padding) {
		this.padding = padding;
	}

}
