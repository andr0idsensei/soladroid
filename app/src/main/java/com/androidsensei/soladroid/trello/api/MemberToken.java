package com.androidsensei.soladroid.trello.api;

/**
 * Created by mihai on 5/23/15.
 */
public class MemberToken {
    private String id;
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
