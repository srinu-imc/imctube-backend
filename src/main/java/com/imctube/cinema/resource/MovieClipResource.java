package com.imctube.cinema.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.imctube.cinema.db.utils.Authorize;
import com.imctube.cinema.model.Artist;
import com.imctube.cinema.model.MovieClip;
import com.imctube.cinema.service.ClipViewCountService;
import com.imctube.cinema.service.ClipViewLast2MinService;
import com.imctube.cinema.service.ClipViewLogService;
import com.imctube.cinema.service.MovieClipService;

@Path("clips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieClipResource {
    private static MovieClipService movieClipService = new MovieClipService();
    private static ClipViewLast2MinService clipViewLast2MinService = new ClipViewLast2MinService();
    private static ClipViewLogService clipViewLogService = new ClipViewLogService();
    private static ClipViewCountService clipViewCountService = new ClipViewCountService();

    @GET
    public List<MovieClip> getMovieClips(@PathParam("artistId") String artistId, @PathParam("movieId") String movieId,
            @QueryParam("page") int page, @QueryParam("allClips") boolean allClips) {
        if (artistId == null && movieId == null) {
            if(allClips) {
                return movieClipService.getMovieClips();
            } else {
                return movieClipService.getMovieClips(page);
            }
        } else if (artistId != null && movieId != null) {
            return movieClipService.getMovieClips(artistId, movieId);
        } else if (movieId != null) {
            return movieClipService.getMovieClips(movieId);
        } else {
            return movieClipService.getArtistMovieClips(artistId, page);
        }
    }

    @GET
    @Path("/{clipId}")
    public MovieClip getMovieClip(@Context final HttpServletRequest request, @PathParam("movieId") String movieId,
            @PathParam("clipId") String clipId) {
        if (movieId != null && clipId.equals("lastClip")) {
            return movieClipService.getMovieLastAddedClip(movieId);
        } else {
            return getMovieClip(clipId, request.getHeader("X-Real-IP"));
        }
    }

    private MovieClip getMovieClip(String clipId, String host) {
        // Add userId if it is available
        if (!clipViewLast2MinService.isViewedInLast2Min(clipId, host)) {
            clipViewLast2MinService.insertClipView(clipId, host);
            clipViewCountService.incrClipViewCount(clipId);
            clipViewLogService.addClipViewLog(clipId, host, null);
        }

        return movieClipService.getMovieClip(clipId);
    }

    @GET
    @Authorize
    @Path("/{clipId}/artists")
    public List<Artist> getMovieClipArtists(@PathParam("clipId") String clipId) {
        return movieClipService.getArtists(clipId);
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
