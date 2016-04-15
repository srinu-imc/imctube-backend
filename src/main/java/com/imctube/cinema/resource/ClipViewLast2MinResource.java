package com.imctube.cinema.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.imctube.cinema.model.ClipViewLast2Min;
import com.imctube.cinema.service.ClipViewLast2MinService;

@Path("clipViewLast2Min")
//@Authorize
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClipViewLast2MinResource {

    private static ClipViewLast2MinService clipViewLast2MinService = new ClipViewLast2MinService();

    @GET
    public List<ClipViewLast2Min> getClipViewCounts() {
        return clipViewLast2MinService.getClipViewLast2Mins();
    }

    @POST
    @Path("/{clipId}/{host}")
    public void addClipView(@PathParam("clipId") String clipId, @PathParam("host") String host) {
        clipViewLast2MinService.insertClipView(clipId, host);
    }
}
