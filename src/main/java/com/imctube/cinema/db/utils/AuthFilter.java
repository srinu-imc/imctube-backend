package com.imctube.cinema.db.utils;

import java.io.IOException;
import java.text.ParseException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.joda.time.DateTime;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

@Authorize
@Provider
public class AuthFilter implements ContainerRequestFilter {

    private static final String AUTH_ERROR_MSG = "Please make sure your request has an Authorization header",
            EXPIRE_ERROR_MSG = "Token has expired", JWT_ERROR_MSG = "Unable to parse JWT",
            JWT_INVALID_MSG = "Invalid JWT token";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authHeader = requestContext.getHeaderString(AuthUtils.AUTH_HEADER_KEY);

        if (authHeader == null || authHeader.isEmpty() || authHeader.split(" ").length != 2) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(AUTH_ERROR_MSG).build());
            return;
        } else {
            JWTClaimsSet claimSet = null;
            try {
                claimSet = (JWTClaimsSet) AuthUtils.decodeToken(authHeader);
            } catch (ParseException e) {
                requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).entity(JWT_ERROR_MSG).build());
                return;
            } catch (JOSEException e) {
                requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).entity(JWT_INVALID_MSG).build());
                return;
            }

            // ensure that the token is not expired
            if (new DateTime(claimSet.getExpirationTime()).isBefore(DateTime.now())) {
                requestContext
                        .abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(EXPIRE_ERROR_MSG).build());
            }
        }
    }
}