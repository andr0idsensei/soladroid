package com.androidsensei.soladroid.trello.api.model;

/**
 * Class that models a Trello card (task).
 *
 * Created by mihai on 5/24/15.
 */
public class Card {
    private String id;

    private String name;

    private String desc;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String description) {
        this.desc = description;
    }

}
