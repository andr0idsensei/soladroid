package com.androidsensei.soladroid.trello.api.model;

/**
 * This interface should be implemented by the model classes in order to have a simple way to return names and ids.
 * Especially useful for UI adapters.
 *
 * Created by mihai on 5/24/15.
 */
public interface Model {
    /**
     * @return the model data name.
     */
    String getName();

    /**
     * @return the model data id - used for Trello calls.
     */
    String getId();
}
