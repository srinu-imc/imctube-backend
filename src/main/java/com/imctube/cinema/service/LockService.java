package com.imctube.cinema.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.imctube.cinema.db.LockDb;
import com.imctube.cinema.model.Lock;
import com.imctube.cinema.model.Lock.Type;
import com.imctube.cinema.model.User;

import jersey.repackaged.com.google.common.collect.Sets;

public class LockService {

    public LockService() {
    }

    public List<Lock> getMovieLocks() {
        return LockDb.getMovieLocks();
    }

    public List<Lock> getMovieClipLocks() {
        return LockDb.getMovieClipLocks();
    }

    public Set<String> getLockedMovieIds() {
        Set<String> lockedMovieIds = Sets.newHashSet();
        for (Lock lock : getMovieLocks()) {
            lockedMovieIds.add(lock.getMovieId());
        }
        return lockedMovieIds;
    }

    public Set<String> getLockedMovieClipIds() {
        Set<String> lockedClipIds = Sets.newHashSet();
        for (Lock lock : getMovieClipLocks()) {
            lockedClipIds.add(lock.getClipId());
        }
        return lockedClipIds;
    }

    public Optional<Lock> getLockByMovieId(String movieId) {
        return LockDb.getLockByMovieId(movieId);
    }

    public Optional<Lock> getLockByMovieClipId(String clipId) {
        return LockDb.getLockByMovieClipId(clipId);
    }

    public Optional<Lock> getMovieLockByUserId(String userId) {
        return LockDb.getMovieLockByUserId(userId);
    }

    public Optional<Lock> getMovieClipLockByUserId(String userId) {
        return LockDb.getMovieClipLockByUserId(userId);
    }

    public Lock lockMovie(String movieId, String userId) {
        Lock lock = new Lock(userId).setMovieId(movieId);
        return LockDb.addLock(lock);
    }

    public Lock lockMovieClip(String clipId, String userId) {
        Lock lock = new Lock(userId).setClipId(clipId);
        return LockDb.addLock(lock);

    }

    public Lock addSoftLockToMovieClip(String clipId) {
        Optional<Lock> optionalLock = getLockByMovieClipId(clipId);
        if (optionalLock.isPresent()) {
            // increment soft lock by one more
            Lock lock = optionalLock.get();
            lock.increaseLockCount();
            return LockDb.updateLock(optionalLock.get());
        } else {
            Lock lock = new Lock(User.SYSTEM_USER_ID).setClipId(clipId);
            lock.setLockType(Type.SOFT_LOCK);
            lock.setCount(1);
            return LockDb.addLock(lock);
        }
    }

    public Lock removeSoftLockFromMovieClip(String clipId) {
        Optional<Lock> optionalLock = getLockByMovieClipId(clipId);
        if (optionalLock.isPresent()) {
            Lock lock = optionalLock.get();
            lock.decreaseLockCount();
            if (lock.getCount() != 0) {
                return LockDb.updateLock(optionalLock.get());
            } else {
                return LockDb.removeMovieClipLockByClipId(clipId);
            }
        } else {
            return null;
        }
    }

    public Lock updateMovieLock(String movieId, String userId) {
        Lock lock = new Lock(userId).setMovieId(movieId);
        return LockDb.updateLock(lock);
    }

    public Lock updateMovieClipLock(String clipId, String userId) {
        Lock lock = new Lock(userId).setClipId(clipId);
        return LockDb.updateLock(lock);
    }

    public Lock removeMovieLockByUserId(String userId) {
        return LockDb.removeMovieLockByUserId(userId);
    }

    public Lock removeMovieClipLockByUserId(String userId) {
        return LockDb.removeMovieClipLockByUserId(userId);
    }
}
