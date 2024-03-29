package com.imctube.cinema.resource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.imctube.cinema.db.utils.AuthUtils;
import com.imctube.cinema.db.utils.Authorize;
import com.imctube.cinema.model.Lock;
import com.imctube.cinema.model.Movie;
import com.imctube.cinema.model.MovieClip;
import com.imctube.cinema.model.User;
import com.imctube.cinema.service.ArtistService;
import com.imctube.cinema.service.LockService;
import com.imctube.cinema.service.MovieClipService;
import com.imctube.cinema.service.MovieService;
import com.imctube.cinema.service.UserService;

import jersey.repackaged.com.google.common.collect.ImmutableList;
import jersey.repackaged.com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Sets;

@Path("clipify")
@Authorize
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClipifyResource {

    private static MovieService movieService = new MovieService();
    private static MovieClipService movieClipService = new MovieClipService();
    private static ArtistService artistService = new ArtistService();
    private static LockService lockService = new LockService();
    private static UserService userService = new UserService();

    @GET
    public List<Movie> getMovies(@Context final HttpServletRequest request) {
        Optional<Lock> lock = lockService.getMovieLockByUserId(getUser(request).getId());

        if (lock.isPresent()) {
            return ImmutableList.of(movieService.getMovie(lock.get().getMovieId()));
        } else {
            List<Movie> movies = Lists.newArrayList();
            Set<String> lockedMovieIds = lockService.getLockedMovieIds();

            // Skip locked movies here
            for (Movie movie : movieService.getMoviesToClipfy()) {
                if (!lockedMovieIds.contains(movie.getId())) {
                    movies.add(movie);
                }
            }
            return movies;
        }
    }

    private User getUser(HttpServletRequest request) {
        return userService.getUser(request.getHeader(AuthUtils.AUTH_HEADER_KEY)).get();
    }

    @POST
    @Path("/{movieId}")
    public MovieClip getMovie(@Context final HttpServletRequest request, @PathParam("movieId") String movieId) {
        User user = getUser(request);

        Optional<Lock> lock = lockService.getMovieLockByUserId(user.getId());
        if (lock.isPresent()) {
            if (lock.get().getMovieId().equals(movieId)) {
                lockService.updateMovieLock(movieId, user.getId());
            } else {
                // Throw unauthorzed here (Movie shouldn't be assigned to
                // multiple users
                return null;
            }
        } else {
            lockService.lockMovie(movieId, user.getId());
        }

        // return last clip here
        return movieClipService.getMovieLastAddedClip(movieId);
    }

    @POST
    @Path("/chooseAnother")
    public void chooseAnotherMovie(@Context final HttpServletRequest request) {
        User user = getUser(request);
        Optional<Lock> lock = lockService.getMovieLockByUserId(user.getId());
        if (lock.isPresent()) {
            lockService.removeMovieLockByUserId(lock.get().getUserId());
        }
    }

    @POST
    @Path("/{movieId}/doneClipify")
    public void doneClipify(@Context final HttpServletRequest request, @PathParam("movieId") String movieId) {
        User user = getUser(request);
        Optional<Lock> lock = lockService.getMovieLockByUserId(user.getId());
        if (lock.isPresent()) {
            lockService.removeMovieLockByUserId(lock.get().getUserId());

        }
        updateMovieAsClipified(movieId);
    }

    @POST
    @Path("/{movieId}/clips")
    public MovieClip addMovieClip(@Context final HttpServletRequest request, @PathParam("movieId") String movieId,
            @QueryParam("isLastClip") boolean isLastClip, MovieClip clip) {
        User user = getUser(request);
        clip.setClipifiedBy(user.getId());

        for (String artistId : clip.getArtistIds()) {
            artistService.addMovie(artistId, movieId);
        }
        updateMovieArtists(movieId, clip.getArtistIds());

        // Refresh lock
        if (isLastClip) {
            lockService.removeMovieLockByUserId(user.getId());
            updateMovieAsClipified(movieId);
        } else {
            lockService.updateMovieLock(movieId, user.getId());
        }

        return movieClipService.addMovieClip(movieId, clip);
    }

    private void updateMovieArtists(String movieId, Set<String> artistIds) {
        Movie movie = movieService.getMovie(movieId);
        Set<String> existingArtists = movie.getArtistIdSet();
        movie.setArtistIds(Sets.union(artistIds, existingArtists));
        movieService.updateMovie(movieId, movie);
    }

    private void updateMovieAsClipified(String movieId) {
        Movie movie = movieService.getMovie(movieId);
        movie.setClipified(true);
        movieService.updateMovie(movieId, movie);
    }
}
