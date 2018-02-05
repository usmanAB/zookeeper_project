package fr.esipe.usman.models;

import java.util.List;
import java.util.UUID;

public class Client {

    private int vote;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    public int getVote() {
        return vote;
    }


    public void setVote() {
        this.vote = vote+1;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String login;

    public Client() {
    }

    public Client(int vote, String login) {
        this.vote = vote;
        this.login = login;
        this.id = String.valueOf(UUID.randomUUID());

    }
}
