package com.artec.mobile.clienti.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public class User {
    @JsonIgnore
    private String id;
    String email;
    String username;
    Map<String, Client> clients;

    public User() {
    }

    public User(String email, String username, Map<String, Client> clients) {
        this.email = email;
        this.username = username;
        this.clients = clients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Client> getClients() {
        return clients;
    }

    public void setClients(Map<String, Client> clients) {
        this.clients = clients;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;

        if (obj instanceof  User){
            User user = (User)obj;
            equal = this.email.equals(user.getEmail());
        }

        return equal;
    }
}
