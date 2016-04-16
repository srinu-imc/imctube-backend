package com.imctube.cinema.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClipViewCount {

    String clipId;

    long count;

    public ClipViewCount() {
    }

    public ClipViewCount(String clipId) {
        this.clipId = clipId;
        this.count = 0;
    }

    public String getClipId() {
        return clipId;
    }

    public void setClipId(String clipId) {
        this.clipId = clipId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void incrCount() {
        this.count++;
    }
}
