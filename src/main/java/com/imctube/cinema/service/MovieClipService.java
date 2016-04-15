package com.imctube.cinema.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.imctube.cinema.db.ArtistDb;
import com.imctube.cinema.db.MovieClipDb;
import com.imctube.cinema.model.Artist;
import com.imctube.cinema.model.ClipViewCount;
import com.imctube.cinema.model.MovieClip;

import jersey.repackaged.com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Maps;

//TODO: use single ton for this 
public class MovieClipService {
    ConcurrentHashMap<String, List<ClipViewCount>> actorToClipViewCount = new ConcurrentHashMap<>();
    private static ClipViewCountService clipViewCountService = new ClipViewCountService();
    static long TWO_MIN_IN_MILLIS = 1 * 60 * 1000;
    static int PAGE_SIZE = 30;
    static Timer timer = new Timer(true);

    public MovieClipService() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (String key : actorToClipViewCount.keySet()) {
                    if (key.equals("all")) {
                        actorToClipViewCount.put(key, clipViewCountService.getClipViewCounts());
                    } else {
                        List<MovieClip> artistClips = MovieClipDb.getArtistMovieClips(key);
                        actorToClipViewCount.put(key,
                                clipViewCountService.getClipViewCounts(getMovieClipIds(artistClips)));
                    }
                }
            }
        }, TWO_MIN_IN_MILLIS, TWO_MIN_IN_MILLIS);
    }

    public List<MovieClip> getMovieClips() {
        return MovieClipDb.getMovieClips();
    }

    public List<MovieClip> getMovieClips(int page) {
        String artistId = "all";

        if (!actorToClipViewCount.containsKey(artistId)) {
            actorToClipViewCount.put("all", clipViewCountService.getClipViewCounts());
        }
        return getMovieClips(page, artistId);
    }

    private List<MovieClip> getMovieClips(int page, String artistId) {
        Map<String, Long> clipIdToViewCount = getPageClipIdToViewCount(page, artistId);
        List<MovieClip> clips = MovieClipDb.getMovieClips(Lists.newArrayList(clipIdToViewCount.keySet()));

        for (int i = 0; i < clips.size(); i++) {
            clips.get(i).setViewCount(clipIdToViewCount.get(clips.get(i).getClipId()));
        }
        clips.sort(new Comparator<MovieClip>() {
            @Override
            public int compare(MovieClip c1, MovieClip c2) {
                return (int) (c2.getViewCount() - c1.getViewCount());
            }
        });
        System.out.println("Page clip count" + clipIdToViewCount.size());
        System.out.println("Returned clips count" + clips.size());
        System.out.println(clipIdToViewCount);
        return clips;
    }

    private Map<String, Long> getPageClipIdToViewCount(int page, String actor) {
        List<ClipViewCount> clipViewCounts = actorToClipViewCount.get(actor);
        Map<String, Long> clipIdToViewCount = Maps.newHashMap();
        if ((page * PAGE_SIZE) > clipViewCounts.size()) {
            return clipIdToViewCount;
        }
        List<ClipViewCount> pageClipViews = clipViewCounts.subList(page * PAGE_SIZE,
                Math.min((page + 1) * PAGE_SIZE, clipViewCounts.size()));
        for (ClipViewCount vc : pageClipViews) {
            clipIdToViewCount.put(vc.getClipId(), vc.getCount());
        }
        return clipIdToViewCount;
    }

    private List<String> getMovieClipIds(List<MovieClip> movieClips) {
        List<String> clipIds = Lists.newArrayList();
        for (MovieClip clip : movieClips) {
            clipIds.add(clip.getClipId());
        }
        return clipIds;
    }

    public List<MovieClip> getArtistMovieClips(String artistId, int page) {

        if (!actorToClipViewCount.containsKey(artistId)) {
            List<MovieClip> artistClips = MovieClipDb.getArtistMovieClips(artistId);
            actorToClipViewCount.put(artistId, clipViewCountService.getClipViewCounts(getMovieClipIds(artistClips)));
        }
        return getMovieClips(page, artistId);
    }

    public List<MovieClip> getMovieClips(String movieId) {
        return MovieClipDb.getMovieClips(movieId);
    }

    public List<MovieClip> getMovieClipsToReview(String movieId) {
        return MovieClipDb.getMovieClipsToReview(movieId);
    }

    public List<MovieClip> getMovieClipsReviewed(String movieId) {
        return MovieClipDb.getMovieClipsReviewed(movieId);
    }

    public List<MovieClip> getMovieClips(String artistId, String movieId) {
        return MovieClipDb.getMovieClips(artistId, movieId);
    }

    public MovieClip getMovieLastAddedClip(String movieId) {
        Optional<MovieClip> movieClip = MovieClipDb.getMovieLastAddedClip(movieId);
        if (movieClip.isPresent()) {
            return movieClip.get();
        } else {
            return null;
        }
    }

    public Optional<MovieClip> getMovieClip(String clipId) {
        return MovieClipDb.getMovieClip(clipId);
    }

    public Optional<MovieClip> getPrevMovieClip(String clipId) {
        MovieClip current = getMovieClip(clipId).get();
        return MovieClipDb.getMovieClipByEndTimeAndMovieId(current.getMovieId(), current.getStartTime());
    }

    public Optional<MovieClip> getNextMovieClip(String clipId) {
        MovieClip current = getMovieClip(clipId).get();
        return MovieClipDb.getMovieClipByStartTimeAndMovieId(current.getMovieId(), current.getEndTime());
    }

    public MovieClip tagArtistToMovieClip(String clipId, String artistId) {
        return MovieClipDb.tagArtistToMovieClip(clipId, artistId);
    }

    public MovieClip addMovieClip(String movieId, MovieClip clip) {
        return MovieClipDb.addMovieClip(movieId, clip);
    }

    public MovieClip updateMovieClip(String clipId, MovieClip clip) {
        clip.setClipId(clipId);
        return MovieClipDb.updateMovieClip(clip);
    }

    public MovieClip removeMovieClip(String movieClipId) {
        return MovieClipDb.removeMovieClip(movieClipId);
    }

    public List<Artist> getArtists(String clipId) {
        MovieClip clip = getMovieClip(clipId).get();
        return ArtistDb.getArtists(clip.getArtistIds());
    }
}
