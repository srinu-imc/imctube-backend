package com.imctube.cinema.resource;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.imctube.cinema.db.utils.Authorize;
import com.imctube.cinema.model.ClipViewCount;
import com.imctube.cinema.service.ClipViewCountService;

@Path("clipViewCounts")
@Authorize
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClipViewCountResource {

    private static ClipViewCountService clipViewCountService = new ClipViewCountService();

    @GET
    public List<ClipViewCount> getClipViewCounts() {
        return clipViewCountService.getClipViewCounts();
    }

    @GET
    @Path("/{clipId}")
    public ClipViewCount getClipViewCount(@PathParam("clipId") String clipId) {
        Optional<ClipViewCount> clipViewCount = clipViewCountService.getClipViewCount(clipId);
        if(clipViewCount.isPresent()) {
            return clipViewCount.get();
        } else {
            return null;
        }
    }

    @POST
    @Path("/{clipId}")
    public void incrClipViewCount(@PathParam("clipId") String clipId) {
        clipViewCountService.incrClipViewCount(clipId);
    }
}
