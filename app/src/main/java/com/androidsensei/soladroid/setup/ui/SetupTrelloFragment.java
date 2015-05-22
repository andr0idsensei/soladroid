package com.androidsensei.soladroid.setup.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsensei.soladroid.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupTrelloFragment extends Fragment {

    public SetupTrelloFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup, container, false);
    }
}
