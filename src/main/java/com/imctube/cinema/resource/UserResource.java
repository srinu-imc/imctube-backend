package com.imctube.cinema.resource;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.imctube.cinema.db.utils.AuthUtils;
import com.imctube.cinema.db.utils.Authorize;
import com.imctube.cinema.model.User;
import com.imctube.cinema.service.UserService;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    public static UserService userService = new UserService();

    @Authorize
    @GET
    public User getUser(@Context final HttpServletRequest request) {
        Optional<User> OptionalUser = userService.getUser(request.getHeader(AuthUtils.AUTH_HEADER_KEY));
        if (OptionalUser.isPresent()) {
            return new User(OptionalUser.get().getId(), OptionalUser.get().getDisplayName());
        }

        return null;
    }
}
