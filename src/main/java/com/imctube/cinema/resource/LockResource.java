package com.imctube.cinema.resource;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.imctube.cinema.db.utils.Authorize;
import com.imctube.cinema.model.Lock;
import com.imctube.cinema.service.LockService;

@Path("locks")
@Authorize
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LockResource {

    private static LockService lockService = new LockService();

    @GET
    public List<Lock> getLocks() {
        return lockService.getMovieLocks();
    }

    @GET
    @Path("movie/{movieId}")
    public Lock getLockByMovieId(@PathParam("movieId") String movieId) {
        Optional<Lock> lock = lockService.getLockByMovieId(movieId);
        if (lock.isPresent()) {
            return lock.get();
        } else {
            return null;
        }
    }

    @GET
    @Path("user/{userId}/movie")
    public Lock getLockByUserId(@PathParam("userId") String userId) {
        Optional<Lock> lock = lockService.getMovieLockByUserId(userId);
        if (lock.isPresent()) {
            return lock.get();
        } else {
            return null;
        }
    }

    @GET
    @Path("user/{userId}/clip")
    public Lock getMovieClipLockByUserId(@PathParam("userId") String userId) {
        Optional<Lock> lock = lockService.getMovieClipLockByUserId(userId);
        if (lock.isPresent()) {
            return lock.get();
        } else {
            return null;
        }
    }

    @POST
    @Path("movie/{movieId}/user/{userId}")
    public Lock addLock(@PathParam("movieId") String movieId, @PathParam("userId") String userId) {
        return lockService.lockMovie(movieId, userId);
    }

    @PUT
    @Path("movie/{movieId}/user/{userId}")
    public Lock updateLock(@PathParam("movieId") String movieId, @PathParam("userId") String userId) {
        return lockService.updateMovieLock(movieId, userId);
    }
}
