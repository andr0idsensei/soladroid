package com.androidsensei.soladroid.utils;

import android.app.Activity;
import android.app.Fragment;

import com.androidsensei.soladroid.SolaDroidFragmentContract;

/**
 * Base fragment that ensures activities including it use the SolaDroidFragmentContract.
 *
 * Created by mihai on 5/22/15.
 */
public abstract class SolaDroidBaseFragment extends Fragment {
    /**
     * The reference to the SolaDroidFragmentContract interface implementation.
     */
    protected SolaDroidFragmentContract contract;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SolaDroidFragmentContract) {
            contract = (SolaDroidFragmentContract) activity;
        } else {
            throw new IllegalArgumentException("The activity containing this fragment should implement SolaDroidFragmentContract.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contract = null;
    }
}
