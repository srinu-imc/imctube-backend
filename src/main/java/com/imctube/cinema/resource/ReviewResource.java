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

import com.imctube.cinema.db.utils.AuthUtils;
import com.imctube.cinema.db.utils.Authorize;
import com.imctube.cinema.model.ClipReviewRequest;
import com.imctube.cinema.model.Lock;
import com.imctube.cinema.model.Movie;
import com.imctube.cinema.model.MovieClip;
import com.imctube.cinema.model.User;
import com.imctube.cinema.service.LockService;
import com.imctube.cinema.service.MovieClipService;
import com.imctube.cinema.service.MovieService;
import com.imctube.cinema.service.UserService;

import jersey.repackaged.com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Sets;

@Path("review")
@Authorize
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewResource {

    private static MovieService movieService = new MovieService();
    private static MovieClipService movieClipService = new MovieClipService();
    private static LockService lockService = new LockService();
    private static UserService userService = new UserService();

    @GET
    public List<Movie> getMovies(@Context final HttpServletRequest request, @QueryParam("reviewed") boolean reviewed) {
        if (reviewed) {
            return movieService.getMoviesReviewed();
        } else {
            return movieService.getMoviesToReview();
        }
    }

    @GET
    @Path("/{movieId}/clips")
    public List<MovieClip> getMovieClips(@Context final HttpServletRequest request,
            @PathParam("movieId") String movieId) {
        User user = getUser(request);
        Optional<Lock> optionalLock = lockService.getMovieClipLockByUserId(user.getId());

        Set<String> lockedClipIds = lockService.getLockedMovieClipIds();
        List<MovieClip> rClips = Lists.newArrayList();
        for (MovieClip clip : movieClipService.getMovieClipsToReview(movieId)) {
            if (!lockedClipIds.contains(clip.getClipId())) {
                rClips.add(clip);
            }
            if (optionalLock.isPresent() && optionalLock.get().getClipId().equals(clip.getClipId())) {
                rClips.add(clip);
            }
        }

        return rClips;
    }

    @POST
    @Path("/clips/{clipId}")
    public MovieClip getMovieClipForReview(@Context final HttpServletRequest request,
            @PathParam("clipId") String clipId) {
        User user = getUser(request);

        Optional<Lock> optionalLock = lockService.getMovieClipLockByUserId(user.getId());
        if (optionalLock.isPresent()) {
            if (optionalLock.get().getClipId().equals(clipId)) {
                // This is to handle the refresh case
                // Soft locks needs to be refreshed ignoring for now
                // (Otherwise extra soft locks will be created)
                lockService.updateMovieClipLock(clipId, user.getId());
            } else {
                // Remove previously got hard lock and soft locks
                Lock lock = lockService.removeMovieClipLockByUserId(user.getId());
                removeAdjacentClipLocks(lock.getClipId());

                // Add soft locks to previous and next clips
                lockService.lockMovieClip(clipId, user.getId());
                lockAdjacentClips(clipId);
            }
        } else {
            lockService.lockMovieClip(clipId, user.getId());
            lockAdjacentClips(clipId);
        }
        return movieClipService.getMovieClip(clipId);
    }

    @PUT
    @Path("/clips/{clipid}")
    public void reviewClip(ClipReviewRequest request, @Context final HttpServletRequest servletRequest) {
        String option = request.getOption();
        MovieClip originalClip = request.getOriginalClip();
        MovieClip newClip = request.getNewClip();
        User user = getUser(servletRequest);

        if (option.equals("newClip")) {
            partialReviewAddNewClip(originalClip, newClip);
        } else if (option.equals("mergeWithEarlierClip")) {
            if (newClip != null) {
                partialReviewMergeWithEarlierClip(originalClip, newClip);
            } else {
                fullyReviewedMergeWithEarlier(originalClip, user.getId());
            }
        } else if (option.equals("mergeWithNextClip")) {
            fullyReviewedMergeWithNext(originalClip, user.getId());
        } else if (option.equals("markAsReviewed")) {
            fullyReviewedMarkAsReviewed(originalClip, user.getId());
        }
    }

    private void partialReviewAddNewClip(MovieClip originalClip, MovieClip newClip) {
        Optional<MovieClip> prevClip = movieClipService.getPrevMovieClip(originalClip.getClipId());
        newClip = movieClipService.addMovieClip(newClip.getMovieId(), newClip);
        movieClipService.updateMovieClip(originalClip.getClipId(), originalClip);

        // Remove soft lock from prev clip
        // Get soft lock on the new clip
        // now we will have locks only on adjacent clips
        lockService.addSoftLockToMovieClip(newClip.getClipId());
        if (prevClip.isPresent()) {
            lockService.removeSoftLockFromMovieClip(prevClip.get().getClipId());
        }
    }

    private void partialReviewMergeWithEarlierClip(MovieClip originalClip, MovieClip newClip) {
        Optional<MovieClip> optionalPrevClip = movieClipService.getPrevMovieClip(originalClip.getClipId());
        if (optionalPrevClip.isPresent()) {
            MovieClip prevClip = optionalPrevClip.get();
            prevClip.setEndTime(newClip.getEndTime());
            prevClip.setArtistIds(Sets.union(prevClip.getArtistIds(), newClip.getArtistIds()));
            movieClipService.updateMovieClip(prevClip.getClipId(), prevClip);
        } else {
            newClip = movieClipService.addMovieClip(newClip.getMovieId(), newClip);
            lockService.addSoftLockToMovieClip(newClip.getClipId());
        }
        movieClipService.updateMovieClip(originalClip.getClipId(), originalClip);
    }

    private void fullyReviewedMarkAsReviewed(MovieClip clip, String userId) {
        // Mark as reviewed if there are no changes to clip
        clip.setReviewed(true);
        movieClipService.updateMovieClip(clip.getClipId(), clip);

        removeAdjacentClipLocks(clip.getClipId());
        lockService.removeMovieClipLockByUserId(userId);
    }

    private void fullyReviewedMergeWithEarlier(MovieClip clip, String userId) {
        Optional<MovieClip> optionalPrev = movieClipService.getPrevMovieClip(clip.getClipId());
        Optional<MovieClip> optionalNext = movieClipService.getNextMovieClip(clip.getClipId());

        if (optionalPrev.isPresent()) {
            MovieClip prevClip = optionalPrev.get();
            prevClip.setEndTime(clip.getEndTime());
            prevClip.setArtistIds(Sets.union(prevClip.getArtistIds(), clip.getArtistIds()));
            movieClipService.updateMovieClip(prevClip.getClipId(), prevClip);
            movieClipService.removeMovieClip(clip.getClipId());
            lockService.removeSoftLockFromMovieClip(prevClip.getClipId());
        } else {
            fullyReviewedMarkAsReviewed(clip, userId);
            return;
        }

        if (optionalNext.isPresent()) {
            lockService.removeSoftLockFromMovieClip(optionalNext.get().getClipId());
        }
        lockService.removeMovieClipLockByUserId(userId);
    }

    private void fullyReviewedMergeWithNext(MovieClip clip, String userId) {
        Optional<MovieClip> optionalPrev = movieClipService.getPrevMovieClip(clip.getClipId());
        Optional<MovieClip> optionalNext = movieClipService.getNextMovieClip(clip.getClipId());

        if (optionalNext.isPresent()) {
            MovieClip nextClip = optionalNext.get();
            nextClip.setStartTime(clip.getStartTime());
            nextClip.setArtistIds(Sets.union(nextClip.getArtistIds(), clip.getArtistIds()));
            movieClipService.updateMovieClip(nextClip.getClipId(), nextClip);
            movieClipService.removeMovieClip(clip.getClipId());
            lockService.removeSoftLockFromMovieClip(nextClip.getClipId());
        } else {
            fullyReviewedMarkAsReviewed(clip, userId);
            return;
        }
        lockService.removeMovieClipLockByUserId(userId);
        if (optionalPrev.isPresent()) {
            lockService.removeSoftLockFromMovieClip(optionalPrev.get().getClipId());
        }
    }

    private User getUser(HttpServletRequest request) {
        return userService.getUser(request.getHeader(AuthUtils.AUTH_HEADER_KEY)).get();
    }

    private void removeAdjacentClipLocks(String clipId) {
        Optional<MovieClip> optionalPrev = movieClipService.getPrevMovieClip(clipId);
        Optional<MovieClip> optionalNext = movieClipService.getNextMovieClip(clipId);

        if (optionalPrev.isPresent()) {
            lockService.removeSoftLockFromMovieClip(optionalPrev.get().getClipId());
        }
        if (optionalNext.isPresent()) {
            lockService.removeSoftLockFromMovieClip(optionalNext.get().getClipId());
        }
    }

    private void lockAdjacentClips(String clipId) {
        Optional<MovieClip> optionalPrev = movieClipService.getPrevMovieClip(clipId);
        Optional<MovieClip> optionalNext = movieClipService.getNextMovieClip(clipId);

        if (optionalPrev.isPresent()) {
            lockService.addSoftLockToMovieClip(optionalPrev.get().getClipId());
        }

        if (optionalNext.isPresent()) {
            lockService.addSoftLockToMovieClip(optionalNext.get().getClipId());
        }
    }
}
