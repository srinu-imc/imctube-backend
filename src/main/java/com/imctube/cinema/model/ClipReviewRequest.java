package com.imctube.cinema.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClipReviewRequest {
    MovieClip originalClip;
    MovieClip newClip;
    String option;

    public ClipReviewRequest() {
    }

    public MovieClip getOriginalClip() {
        return originalClip;
    }

    public void setOriginalClip(MovieClip originalClip) {
        this.originalClip = originalClip;
    }

    public MovieClip getNewClip() {
        return newClip;
    }

    public void setNewClip(MovieClip newClip) {
        this.newClip = newClip;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
