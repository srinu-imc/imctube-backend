package com.imctube.cinema.service;

import java.text.ParseException;
import java.util.Optional;

import com.imctube.cinema.db.UserDb;
import com.imctube.cinema.db.utils.AuthUtils;
import com.imctube.cinema.model.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

public class UserService {

    public UserService() {
    }

    public Optional<User> getUser(String authHeader) {
        JWTClaimsSet claimSet = null;
        try {
            claimSet = (JWTClaimsSet) AuthUtils.decodeToken(authHeader);
        } catch (ParseException | JOSEException e) {
            System.out.println("We should be able to get claimset here, something wrong");
        }

        return UserDb.getUserById(claimSet.getSubject());
    }
}
