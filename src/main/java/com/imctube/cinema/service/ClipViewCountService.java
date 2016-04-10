package com.imctube.cinema.service;

import java.util.List;
import java.util.Optional;

import com.imctube.cinema.db.ClipViewCountDb;
import com.imctube.cinema.model.ClipViewCount;

public class ClipViewCountService {

    public ClipViewCountService() {
    }

    public List<ClipViewCount> getClipViewCounts() {
        return ClipViewCountDb.getClipViewCounts();
    }

    public List<ClipViewCount> getClipViewCounts(List<String> clipIds) {
        return ClipViewCountDb.getClipViewCounts(clipIds);
    }

    public Optional<ClipViewCount> getClipViewCount(String clipId) {
        return ClipViewCountDb.getClipViewCount(clipId);
    }

    public void incrClipViewCount(String clipId) {
        ClipViewCountDb.incrClipViewCount(clipId);
    }
}
