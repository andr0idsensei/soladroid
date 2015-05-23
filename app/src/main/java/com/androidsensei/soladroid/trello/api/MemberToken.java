package com.androidsensei.soladroid.trello.api;

/**
 * This class models a Trello member token with the data we need in our application. It's not a full representation of
 * the JSON response coming from Trello requests.
 *
 * Created by mihai on 5/23/15.
 */
public class MemberToken {
    /**
     * The token id.
     */
    private String id;

    /**
     * The Trello member id for which this token is.
     */
    private String idMember;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }

    @Override
    public String toString() {
        return "MemberToken{" +
                "id='" + id + '\'' +
                ", idMember='" + idMember + '\'' +
                '}';
    }
}
