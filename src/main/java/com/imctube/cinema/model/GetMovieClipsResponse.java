package com.imctube.cinema.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetMovieClipsResponse {

    boolean hasMoreClips;

    List<MovieClip> clips;

    public GetMovieClipsResponse() {
    }

    public List<MovieClip> getClips() {
        return clips;
    }

    public void setClips(List<MovieClip> clips) {
        this.clips = clips;
    }

    public boolean isHasMoreClips() {
        return hasMoreClips;
    }

    public void setHasMoreClips(boolean hasMoreClips) {
        this.hasMoreClips = hasMoreClips;
    }
}
