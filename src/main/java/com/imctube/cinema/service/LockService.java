package com.imctube.cinema.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.imctube.cinema.db.LockDb;
import com.imctube.cinema.model.Lock;

import jersey.repackaged.com.google.common.collect.Sets;

public class LockService {

    public LockService() {
    }

    public List<Lock> getLocks() {
        return LockDb.getLocks();
    }

    public Set<String> getLockedMovieIds() {
        Set<String> lockedMovieIds = Sets.newHashSet();
        for (Lock lock : getLocks()) {
            lockedMovieIds.add(lock.getMovieId());
        }
        return lockedMovieIds;
    }

    public Optional<Lock> getLockByMovieId(String movieId) {
        return LockDb.getLockByMovieId(movieId);
    }

    public Optional<Lock> getLockByUserId(String userId) {
        return LockDb.getLockByUserId(userId);
    }

    public Lock addLock(String movieId, String userId) {
        Lock lock = new Lock(movieId, userId);
        return LockDb.addLock(lock);
    }

    public Lock updateLock(String movieId, String userId) {
        Lock lock = new Lock(movieId, userId);
        return LockDb.updateLock(lock);
    }
}
