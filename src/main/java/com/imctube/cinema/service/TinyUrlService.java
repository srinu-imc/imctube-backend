package com.imctube.cinema.service;

import com.imctube.cinema.db.TinyUrlMapDb;

public class TinyUrlService {
	public TinyUrlService() {
		// TODO Auto-generated constructor stub
	}

	public String getFullUrl(String hashUrl) {
		return TinyUrlMapDb.getFullUrl(hashUrl);
	}

	public String getTinyUrl(String fullUrl) {
		return TinyUrlMapDb.getTinyUrl(fullUrl);
	}
}
