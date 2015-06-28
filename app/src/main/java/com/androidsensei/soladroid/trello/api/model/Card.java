package com.androidsensei.soladroid.trello.api.model;

import java.io.Serializable;

/**
 * Class that models a Trello card (task).
 *
 * Created by mihai on 5/24/15.
 */
public class Card implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return id.equals(card.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
