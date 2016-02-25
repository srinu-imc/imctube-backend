package com.imctube.cinema.resource;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.imctube.cinema.db.utils.Authorize;
import com.imctube.cinema.service.ArtistService;

@Path("tools")
@Authorize
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ToolResource {

    private static ArtistService artistService = new ArtistService();

    @PUT
    @Path("/artists/{artistId}/movies/{movieId}")
    public void addMovieToArtist(@Context final HttpServletRequest request, @PathParam("movieId") String movieId,
            @PathParam("artistId") String artistId) {
        Set<String> movieIds = artistService.getArtist(artistId).getMovieIdSet();
        if (!movieIds.contains(movieId)) {
            artistService.addMovie(artistId, movieId);
        }
    }
}
