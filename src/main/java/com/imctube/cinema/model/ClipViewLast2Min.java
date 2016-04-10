package com.imctube.cinema.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClipViewLast2Min {

    Date at;

    String clipId;

    String host;

    public ClipViewLast2Min() {
    }

    public ClipViewLast2Min(String clipId, String host) {
        this.clipId = clipId;
        this.host = host;
        this.at = new Date();
    }

    public Date getAt() {
        return at;
    }

    public void setAt(Date at) {
        this.at = at;
    }

    public String getClipId() {
        return clipId;
    }

    public void setClipId(String clipId) {
        this.clipId = clipId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
