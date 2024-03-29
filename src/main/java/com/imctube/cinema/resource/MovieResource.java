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

import com.imctube.cinema.db.utils.Authorize;
import com.imctube.cinema.model.Artist;
import com.imctube.cinema.model.Movie;
import com.imctube.cinema.service.MovieService;

@Path("movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    private static MovieService movieService = new MovieService();

    @GET
    public List<Movie> getMovies(@PathParam("artistId") String artistId) {
        if (artistId == null || artistId.isEmpty()) {
            return movieService.getMoviesClipified();
        } else {
            return movieService.getMovies(artistId);
        }
    }

    @GET
    @Authorize
    @Path("/all")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GET
    @Path("/{movieId}")
    public Movie getMovie(@PathParam("movieId") String movieId) {
        return movieService.getMovie(movieId);
    }

    @GET
    @Path("/{movieId}/artists")
    public List<Artist> getArtists(@PathParam("movieId") String movieId) {
        return movieService.getArtists(movieId);
    }

    @POST
    @Authorize
    public Movie addMovie(Movie movie) {
        return movieService.addMovie(movie);
    }

    @PUT
    @Authorize
    @Path("/{movieId}")
    public Movie updateMovie(@PathParam("movieId") String movieId, Movie movie) {
        return movieService.updateMovie(movieId, movie);
    }

    @DELETE
    @Authorize
    @Path("/{movieId}")
    public Movie removeMovie(@PathParam("movieId") String movieId) {
        return movieService.removeMovie(movieId);
    }

    @Path("/{movieId}/clips")
    public MovieClipResource getMovieClipResource() {
        return new MovieClipResource();
    }
}
