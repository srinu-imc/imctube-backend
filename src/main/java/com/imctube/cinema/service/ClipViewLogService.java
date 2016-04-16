package com.imctube.cinema.service;

import java.util.List;

import com.imctube.cinema.db.ClipViewLogDb;
import com.imctube.cinema.model.ClipViewLog;

public class ClipViewLogService {

    public ClipViewLogService() {
    }

    public List<ClipViewLog> getClipViewLogs() {
        return ClipViewLogDb.getClipViewLogs();
    }

    public void addClipViewLog(ClipViewLog viewLog) {
        ClipViewLogDb.insertClipView(viewLog);
    }

    public void addClipViewLog(String clipId, String host, String userId) {
        ClipViewLogDb.insertClipView(clipId, host, userId);
    }
}
