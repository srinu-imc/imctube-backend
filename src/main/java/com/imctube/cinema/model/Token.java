package com.imctube.cinema.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Token {
    String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}