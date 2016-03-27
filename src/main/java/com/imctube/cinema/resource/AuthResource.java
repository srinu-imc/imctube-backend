package com.imctube.cinema.resource;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import com.imctube.cinema.db.UserDb;
import com.imctube.cinema.db.utils.AuthUtils;
import com.imctube.cinema.db.utils.JavaToJsonConverter;
import com.imctube.cinema.db.utils.PasswordService;
import com.imctube.cinema.model.ErrorMessage;
import com.imctube.cinema.model.Token;
import com.imctube.cinema.model.User;
import com.imctube.cinema.model.User.Provider;
import com.nimbusds.jose.JOSEException;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    public static final String CLIENT_ID_KEY = "client_id", REDIRECT_URI_KEY = "redirect_uri",
            CLIENT_SECRET = "client_secret", CODE_KEY = "code", GRANT_TYPE_KEY = "grant_type",
            AUTH_CODE = "authorization_code";

    public static final String CONFLICT_MSG = "There is already a %s account that belongs to you",
            NOT_FOUND_MSG = "User not found", LOGING_ERROR_MSG = "Wrong email and/or password",
            UNLINK_ERROR_MSG = "Could not unlink %s account because it is your only sign-in method";

    private static final Client client = ClientBuilder.newClient();
    private static final String FB_SECRET = "3aba0ce25c097f51618eff27e3bc8f23";
    // private static final String FB_SECRET = "ec0aa5dff58f304acaef53a58115b1d2";
    private static final String GOOGLE_SECRET = "lvDp2NUP2A3ztnzOfp2TB19H";
    public static final ObjectMapper MAPPER = new ObjectMapper();

    @POST
    @Path("login")
    public Response login(@Valid final User user, @Context final HttpServletRequest request) throws JOSEException {
        final Optional<User> foundUser = UserDb.getUserByEmail(user.getEmail());
        if (foundUser.isPresent() && PasswordService.checkPassword(user.getPassword(), foundUser.get().getPassword())) {
            final Token token = AuthUtils.createToken(request.getRemoteHost(), foundUser.get().getId());
            return Response.ok().entity(JavaToJsonConverter.convert(token)).build();
        }
        return Response.status(Status.UNAUTHORIZED)
                .entity(JavaToJsonConverter.convert(new ErrorMessage(LOGING_ERROR_MSG))).build();
    }

    @POST
    @Path("signup")
    public Response signup(@Valid final User user, @Context final HttpServletRequest request) throws JOSEException {
        final Optional<User> foundUser = UserDb.getUserByEmail(user.getEmail());
        if (foundUser.isPresent()) {
            return Response.status(Status.CONFLICT)
                    .entity(JavaToJsonConverter.convert(new ErrorMessage(String.format(CONFLICT_MSG, user.getEmail()))))
                    .build();
        }
        user.setPassword(PasswordService.hashPassword(user.getPassword()));
        final User savedUser = UserDb.addUser(user);
        final Token token = AuthUtils.createToken(request.getRemoteHost(), savedUser.getId());
        return Response.status(Status.CREATED).entity(JavaToJsonConverter.convert(token)).build();
    }

    @POST
    @Path("facebook")
    public Response loginFacebook(@Valid final Payload payload, @Context final HttpServletRequest request)
            throws JsonParseException, IOException, ParseException, JOSEException {
        final String accessTokenUrl = "https://graph.facebook.com/v2.3/oauth/access_token";
        final String graphApiUrl = "https://graph.facebook.com/v2.3/me";

        Response response;
        // Step 1. Exchange authorization code for access token.
        response = client.target(accessTokenUrl).queryParam(CLIENT_ID_KEY, payload.getClientId())
                .queryParam(REDIRECT_URI_KEY, payload.getRedirectUri()).queryParam(CLIENT_SECRET, FB_SECRET)
                .queryParam(CODE_KEY, payload.getCode()).request("text/plain").accept(MediaType.TEXT_PLAIN).get();

        Map<String, Object> responseEntity = getResponseEntity(response);

        response = client.target(graphApiUrl).queryParam("access_token", responseEntity.get("access_token"))
                .queryParam("expires_in", responseEntity.get("expires_in")).request("text/plain").get();

        final Map<String, Object> userInfo = getResponseEntity(response);

        // Step 3. Process the authenticated the user.
        return processUser(request, Provider.FACEBOOK, userInfo.get("id").toString(), userInfo.get("name").toString());
    }

    @POST
    @Path("google")
    public Response loginGoogle(@Valid final Payload payload, @Context final HttpServletRequest request)
            throws JOSEException, ParseException, JsonParseException, IOException {
        final String accessTokenUrl = "https://accounts.google.com/o/oauth2/token";
        final String peopleApiUrl = "https://www.googleapis.com/plus/v1/people/me/openIdConnect";
        Response response;

        // Step 1. Exchange authorization code for access token.
        final MultivaluedMap<String, String> accessData = new MultivaluedHashMap<String, String>();
        accessData.add(CLIENT_ID_KEY, payload.getClientId());
        accessData.add(REDIRECT_URI_KEY, payload.getRedirectUri());
        accessData.add(CLIENT_SECRET, GOOGLE_SECRET);
        accessData.add(CODE_KEY, payload.getCode());
        accessData.add(GRANT_TYPE_KEY, AUTH_CODE);
        response = client.target(accessTokenUrl).request().post(Entity.form(accessData));
        accessData.clear();

        // Step 2. Retrieve profile information about the current user.
        final String accessToken = (String) getResponseEntity(response).get("access_token");
        response = client.target(peopleApiUrl).request("text/plain")
                .header(AuthUtils.AUTH_HEADER_KEY, String.format("Bearer %s", accessToken)).get();
        final Map<String, Object> userInfo = getResponseEntity(response);

        // Step 3. Process the authenticated the user.
        return processUser(request, Provider.GOOGLE, userInfo.get("sub").toString(), userInfo.get("name").toString());
    }

    @XmlRootElement
    public static class Payload {
        String clientId;

        String redirectUri;

        String code;

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getClientId() {
            return clientId;
        }

        public String getRedirectUri() {
            return redirectUri;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return String.format("clientId %s, redirectUrl %s, code %s", clientId, redirectUri, code);
        }
    }

    private Map<String, Object> getResponseEntity(final Response response) throws JsonParseException, IOException {
        return MAPPER.readValue(response.readEntity(String.class), new TypeReference<Map<String, Object>>() {
        });
    }

    private Response processUser(final HttpServletRequest request, final Provider provider, final String id,
            final String displayName) throws JOSEException, ParseException {
        final Optional<User> user = UserDb.getUserByProviderId(provider, id);

        // Step 3a. If user is already signed in then link accounts.
        User userToSave;
        final String authHeader = request.getHeader(AuthUtils.AUTH_HEADER_KEY);
        if (authHeader != null && !authHeader.isEmpty()) {
            if (user.isPresent()) {
                return Response.status(Status.CONFLICT).entity(JavaToJsonConverter
                        .convert(new ErrorMessage(String.format(CONFLICT_MSG, provider.capitalize())))).build();
            }

            final String subject = AuthUtils.getSubject(authHeader);
            final Optional<User> foundUser = UserDb.getUserByProviderId(provider, subject);
            if (!foundUser.isPresent()) {
                return Response.status(Status.NOT_FOUND)
                        .entity(JavaToJsonConverter.convert(new ErrorMessage(NOT_FOUND_MSG))).build();
            }

            userToSave = foundUser.get();
            userToSave.setProviderId(provider, id);
            if (userToSave.getDisplayName() == null) {
                userToSave.setDisplayName(displayName);
            }
            userToSave = UserDb.addUser(userToSave);
        } else {
            // Step 3b. Create a new user account or return an existing one.
            if (user.isPresent()) {
                userToSave = user.get();
            } else {
                userToSave = new User();
                userToSave.setProviderId(provider, id);
                userToSave.setDisplayName(displayName);
                userToSave = UserDb.addUser(userToSave);
            }
        }

        final Token token = AuthUtils.createToken(request.getRemoteHost(), userToSave.getId());
        return Response.ok().entity(JavaToJsonConverter.convert(token)).build();
    }
}
