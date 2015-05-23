package com.androidsensei.soladroid.trello.api.model;


/**
 * This class models a Trello board with the information we need to display in the application. It's not a full representation of
 * the JSON response coming from Trello requests.
 *
 * Created by mihai on 5/23/15.
 */
public class Board {
    /**
     * The board id.
     */
    private String id;

    /**
     * The board name.
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
        return "Board{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
