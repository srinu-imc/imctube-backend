package com.imctube.cinema.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TinyUrl {

	private String tinyUrl;

	private String fullUrl;

	public TinyUrl() {
	}

	public static String SYSTEM_USER_ID = "SYSTEM_USER";

	public TinyUrl(String tinyUrl, String fullUrl) {
		this.tinyUrl = tinyUrl;
		this.fullUrl = fullUrl;
	}

	public String getTinyUrl() {
		return tinyUrl;
	}

	public String getFullUrl() {
		return fullUrl;
	}
}
