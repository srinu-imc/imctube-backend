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
import com.imctube.cinema.model.MovieClip;
import com.imctube.cinema.service.MovieClipService;

@Path("clips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieClipResource {
    private static MovieClipService movieClipService = new MovieClipService();

    @GET
    public List<MovieClip> getMovieClips(@PathParam("artistId") String artistId, @PathParam("movieId") String movieId) {
        if (artistId == null && movieId == null) {
            return movieClipService.getMovieClips();
        } else if (artistId != null && movieId != null) {
            return movieClipService.getMovieClips(artistId, movieId);
        } else if (movieId != null) {
            return movieClipService.getMovieClips(movieId);
        } else {
            return movieClipService.getArtistMovieClips(artistId);
        }
    }

    @GET
    @Path("/{clipId}")
    public MovieClip getMovieClip(@PathParam("movieId") String movieId, @PathParam("clipId") String clipId) {
        if (movieId != null && clipId.equals("lastClip")) {
            return movieClipService.getMovieLastAddedClip(movieId);
        } else {
            return movieClipService.getMovieClip(clipId);
        }
    }

    @POST
    @Authorize
    @Path("/{clipId}/artists/{artistId}")
    public MovieClip tagArtistToMovieClip(@PathParam("clipId") String clipId, @PathParam("artistId") String artistId) {
        return movieClipService.tagArtistToMovieClip(clipId, artistId);
    }

    @PUT
    @Authorize
    @Path("/{clipId}")
    public MovieClip updateMovie(@PathParam("clipId") String clipId, MovieClip clip) {
        return movieClipService.updateMovieClip(clipId, clip);
    }

    @DELETE
    @Authorize
    @Path("/{clipId}")
    public MovieClip removeMovieClip(@PathParam("clipId") String clipId) {
        return movieClipService.removeMovieClip(clipId);
    }
}
