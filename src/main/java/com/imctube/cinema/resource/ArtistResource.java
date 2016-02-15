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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.imctube.cinema.db.utils.Authorize;
import com.imctube.cinema.model.Artist;
import com.imctube.cinema.service.ArtistService;

@Path("artists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArtistResource {

    private static ArtistService artistService = new ArtistService();

    @GET
    public List<Artist> getArtists(@QueryParam("onlyHaveMovies") boolean onlyHaveMovies) {
        return artistService.getArtists(onlyHaveMovies);
    }

    @GET
    @Path("/{artistId}")
    public Artist getArtist(@PathParam("artistId") String artistId) {
        return artistService.getArtist(artistId);
    }

    @POST
    @Authorize
    public Artist addArtist(Artist artist) {
        return artistService.addArtist(artist);
    }

    @PUT
    @Authorize
    @Path("/{artistId}")
    public Artist updateArtist(@PathParam("artistId") String artistId, Artist artist) {
        return artistService.updateArtist(artist.getId(), artist);
    }

    @DELETE
    @Authorize
    @Path("/{artistId}")
    public Artist removeArtist(@PathParam("artistId") String artistId) {
        return artistService.removeArtist(artistId);
    }

    @Path("/{artistId}/movies")
    public MovieResource getMovieResource() {
        return new MovieResource();
    }

    @Path("{artistId}/clips")
    public MovieClipResource getMovieClipResource() {
        return new MovieClipResource();
    }
}
