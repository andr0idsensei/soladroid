package com.androidsensei.soladroid.trello.api;

import java.util.List;

/**
 * Created by mihai on 5/23/15.
 */
public class Board {
    private String id;
    private String name;

    @Override
    public String toString() {
        return "Board{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
