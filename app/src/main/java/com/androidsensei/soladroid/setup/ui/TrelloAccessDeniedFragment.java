package com.androidsensei.soladroid.setup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;

/**
 * This fragment will be displayed to the user when she denies access to her Trello account.
 *
 * Created by mihai on 5/22/15.
 */
public class TrelloAccessDeniedFragment extends SolaDroidBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trello_acces_denied, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupOkButton((Button) getView().findViewById(R.id.trello_denied_ok_button));
    }

    /**
     * Sets up what happens when you click the "ok" button, namely the authorization process should be re-started.
     *
     * @param button the button to set the click listener on
     */
    private void setupOkButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contract.showAuthFragment();
            }
        });
    }
}
