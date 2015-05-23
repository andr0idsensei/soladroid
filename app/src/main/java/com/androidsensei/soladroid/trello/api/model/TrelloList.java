package com.androidsensei.soladroid.trello.api.model;

/**
 * This class models a Trello task list with the data we need in the app. It is not a full representation of the
 * JSON response returned by Trello requests.
 *
 * Created by mihai on 5/23/15.
 */
public class TrelloList {
    /**
     * The list id.
     */
    private String id;

    /**
     * The list name.
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TrelloList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
