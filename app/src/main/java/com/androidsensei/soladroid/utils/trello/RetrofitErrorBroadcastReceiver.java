package com.androidsensei.soladroid.utils.trello;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.androidsensei.soladroid.trello.api.service.TrelloCallsService;
import com.androidsensei.soladroid.utils.NetworkUtil;

import retrofit.RetrofitError;

/**
 * Broadcast receiver that listens for Retrofit errors sent from the TrelloCallsService. When such error occurs, it
 * will display a dialog with the error message to the user.
 * <p/>
 * Created by mihai on 7/3/15.
 */
public class RetrofitErrorBroadcastReceiver extends BroadcastReceiver {
    /**
     * The fragment manager used for displaying the network error dialog.
     */
    private FragmentManager fragmentManager;

    /**
     * Broadcast receiver constructor with the fragment manager as argument.
     *
     * @param fragmentManager the fragment manager
     */
    public RetrofitErrorBroadcastReceiver(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TrelloCallsService.ACTION_RETROFIT_ERROR_BROADCAST.equals(intent.getAction())) {
            RetrofitError retrofitError = (RetrofitError) intent.getSerializableExtra(TrelloCallsService.EXTRA_RETROFIT_ERROR);
            if (retrofitError != null) {
                NetworkUtil.showNetworkExceptionDialog(fragmentManager, retrofitError);
            }
        }
    }
}
