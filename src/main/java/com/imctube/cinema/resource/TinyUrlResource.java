package com.imctube.cinema.resource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.imctube.cinema.model.Artist;
import com.imctube.cinema.model.Lock;
import com.imctube.cinema.model.Movie;
import com.imctube.cinema.model.TinyUrl;
import com.imctube.cinema.model.User;
import com.imctube.cinema.service.ArtistService;
import com.imctube.cinema.service.MovieService;
import com.imctube.cinema.service.TinyUrlService;

import jersey.repackaged.com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Sets;

@Path("tinyurl")
// @Authorize
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TinyUrlResource {

	private static TinyUrlService tinyurl_service = new TinyUrlService();

	@POST
	@Path("/{tinyUrl}/redirect")
	public String getFullUrl(@Context final HttpServletRequest request, @PathParam("tinyUrl") String tinyUrl) {
		return tinyurl_service.getFullUrl(tinyUrl);
	}

	@POST
	@Path("/{fullUrl}/tiny")
	public String getTinyUrl(@Context final HttpServletRequest request, @PathParam("fullUrl") String fullUrl) {
		return tinyurl_service.getTinyUrl(fullUrl);
	}
}
