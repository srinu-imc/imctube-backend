package com.imctube.cinema.resource;

import java.util.List;
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
import com.imctube.cinema.model.Movie;
import com.imctube.cinema.model.MovieClip;
import com.imctube.cinema.service.ArtistService;
import com.imctube.cinema.service.ClipViewCountService;
import com.imctube.cinema.service.MovieClipService;
import com.imctube.cinema.service.MovieService;

import jersey.repackaged.com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Sets;

@Path("tools")
// @Authorize
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ToolResource {

    private static ArtistService artistService = new ArtistService();
    private static MovieService movieService = new MovieService();
    private static MovieClipService movieClipService = new MovieClipService();
    private static ClipViewCountService clipViewCountService = new ClipViewCountService();

    @PUT
    @Path("/artists/{artistId}/movies/{movieId}")
    public void addMovieToArtist(@Context final HttpServletRequest request, @PathParam("movieId") String movieId,
            @PathParam("artistId") String artistId) {
        Set<String> movieIds = artistService.getArtist(artistId).getMovieIdSet();
        if (!movieIds.contains(movieId)) {
            artistService.addMovie(artistId, movieId);
        }
    }

    @GET
    @Path("/movies/dups/videoids")
    public List<Movie> getDuplicateVideoIds() {
        Set<String> videoIds = Sets.newHashSet();
        List<Movie> dups = Lists.newArrayList();
        for (Movie movie : movieService.getAllMovies()) {
            if (videoIds.contains(movie.getVideoId())) {
                Movie dupMovie = new Movie();
                dupMovie.setVideoId(movie.getVideoId());
                dups.add(dupMovie);
            } else {
                videoIds.add(movie.getVideoId());
            }
        }
        return dups;
    }

    @GET
    @Path("/artists/dups")
    public List<Artist> getDuplicateArtists() {
        Set<String> artistNames = Sets.newHashSet();
        List<Artist> dups = Lists.newArrayList();
        for (Artist artist : artistService.getArtists(false)) {
            if (artistNames.contains(artist.getIndustryName().toLowerCase())) {
                Artist dupArtist = new Artist();
                dupArtist.setIndustryName(artist.getIndustryName());
                dups.add(dupArtist);
            } else {
                artistNames.add(artist.getIndustryName().toLowerCase());
            }
        }
        return dups;
    }

    @GET
    @Path("/movies/dups/movies")
    public List<Movie> getDuplicateMovieNames() {
        Set<String> movieNames = Sets.newHashSet();
        List<Movie> dups = Lists.newArrayList();
        for (Movie movie : movieService.getAllMovies()) {
            if (movieNames.contains(movie.getName().toLowerCase())) {
                Movie dupMovie = new Movie();
                dupMovie.setName(movie.getName());
                dups.add(dupMovie);
            } else {
                movieNames.add(movie.getName().toLowerCase());
            }
        }
        return dups;
    }

    @GET
    @Path("/movies/nothumbnails")
    public List<Movie> getNoThumbnails() {
        List<Movie> movieIds = Lists.newArrayList();
        for (Movie movie : movieService.getAllMovies()) {
            if (movie.getThumbnailCount() == 0) {
                Movie dupMovie = new Movie();
                dupMovie.setVideoId(movie.getVideoId());
                movieIds.add(dupMovie);
            }
        }
        return movieIds;
    }

    @PUT
    @Path("/movies/{videoId}")
    public void updateThumbnailCount(@Context final HttpServletRequest request, @PathParam("videoId") String videoId,
            @QueryParam("count") int count) {

        Movie movie = movieService.getMovieByVideoId(videoId);
        movie.setThumbnailCount(count);
        movieService.updateMovie(movie.getId(), movie);
    }

    @POST
    @Path("/changeUrl")
    public void updateUrl() {
        List<Artist> artists = artistService.getArtists(false);
        for (Artist artist : artists) {
            if (artist.getThumbnail() != null) {
                artist.setThumbnail(artist.getThumbnail().replace("resources", "https://s3.amazonaws.com/imctube"));
                artistService.updateArtist(artist.getId(), artist);
            }
        }

        // TODO: Change to get all clips
        List<MovieClip> movieClips = movieClipService.getMovieClips(0);
        for (MovieClip movieClip : movieClips) {
            Set<String> thumbnails = movieClip.getThumbnails();
            Set<String> newThumbnails = Sets.newHashSet();
            for (String thumbnail : thumbnails) {
                newThumbnails.add(thumbnail.replace("resources", "https://s3.amazonaws.com/imctube"));
            }
            movieClip.setThumbnails(newThumbnails);
            movieClipService.updateMovieClip(movieClip.getClipId(), movieClip);
        }
    }

    @POST
    @Path("/initViewCount")
    public void initViewCount() {
        List<MovieClip> clips = movieClipService.getMovieClips();
        for (MovieClip clip : clips) {
            clipViewCountService.incrClipViewCount(clip.getClipId());
        }
    }
}
