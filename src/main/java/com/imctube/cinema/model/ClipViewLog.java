package com.imctube.cinema.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClipViewLog {

    Date at;

    String clipId;

    String host;

    String userId;
    
    public ClipViewLog() {
    }

    public ClipViewLog(String clipId, String host, String userId) {
        this.clipId = clipId;
        this.host = host;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
