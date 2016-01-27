package com.imctube.cinema.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.imctube.cinema.model.Artist;
import com.imctube.cinema.model.Movie;
import com.imctube.cinema.model.MovieClip;
import com.imctube.cinema.service.MovieService;

@Path("movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    private static MovieService movieService = new MovieService();

    @GET
    public List<Movie> getMovies(@PathParam("artistId") String artistId) {
	if (artistId == null || artistId.isEmpty()) {
	    return movieService.getMovies();
	} else {
	    return movieService.getMovies(artistId);
	}
    }

    @GET
    @Path("/{movieId}")
    public Movie getMovie(@PathParam("movieId") String movieId) {
	return movieService.getMovie(movieId);
    }

    @POST
    public Movie addMovie(Movie movie) {
	return movieService.addMovie(movie);
    }

    @POST
    @Path("/{movieId}/artists/{artistId}")
    public Artist addArtist(@PathParam("movieId") String movieId, @PathParam("artistId") String artistId) {
	return movieService.addArtist(artistId, movieId);
    }

    @PUT
    @Path("/{movieId}")
    public Movie updateMovie(@PathParam("movieId") String movieId, Movie movie) {
	return movieService.updateMovie(movieId, movie);
    }

    @DELETE
    @Path("/{movieId}")
    public Movie removeMovie(@PathParam("movieId") String movieId) {
	return movieService.removeMovie(movieId);
    }

    @Path("/{movieId}/clips")
    public MovieClipResource getMovieClipResource() {
	return new MovieClipResource();
    }
}
