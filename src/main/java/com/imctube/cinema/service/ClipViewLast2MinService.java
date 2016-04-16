package com.imctube.cinema.service;

import java.util.List;

import com.imctube.cinema.db.ClipViewLast2MinDb;
import com.imctube.cinema.model.ClipViewLast2Min;

public class ClipViewLast2MinService {

    public ClipViewLast2MinService() {
    }

    public List<ClipViewLast2Min> getClipViewLast2Mins() {
        return ClipViewLast2MinDb.getClipViewLast2Mins();
    }

    public boolean isViewedInLast2Min(String clipId, String host) {
        return ClipViewLast2MinDb.isClipViewedInLast2Min(clipId, host);
    }

    public void insertClipView(String clipId, String host) {
        ClipViewLast2MinDb.insertClipView(clipId, host);
    }
}
