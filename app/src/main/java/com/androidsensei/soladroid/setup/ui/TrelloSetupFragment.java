package com.androidsensei.soladroid.setup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrelloSetupFragment extends SolaDroidBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trello_setup, container, false);
    }
}
