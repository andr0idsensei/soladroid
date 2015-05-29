package com.androidsensei.soladroid.trello.api.model;

/**
 * Class that models a Trello card (task).
 *
 * Created by mihai on 5/24/15.
 */
public class Card implements Model {
    private String id;

    private String name;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
