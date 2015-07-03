package com.androidsensei.soladroid.utils;

import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.androidsensei.soladroid.utils.ui.NetworkExceptionDialog;

import retrofit.RetrofitError;

/**
 * Utility class for doing various network related stuff such as checking if WiFi/Cellular network is available.
 * <p/>
 * Created by mihai on 7/3/15.
 */
public final class NetworkUtil {
    /**
     * Utility class so we need no instances.
     */
    private NetworkUtil() {
    }

    /**
     * Checks if we have network availability for Internet access.
     *
     * @param context the context in which this call is made
     * @return true if there is a network interface available, false if not
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null;
    }

    /**
     * Display the network exception dialog when dealing with Retrofit errors.
     *
     * @param fragmentManager the fragment manager
     * @param error           the Retrofit error
     */
    public static void showNetworkExceptionDialog(FragmentManager fragmentManager, RetrofitError error) {
        NetworkExceptionDialog dialog = null;

        if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
            dialog = NetworkExceptionDialog.createInstance();

        } else {
            dialog = NetworkExceptionDialog.createInstance(error.getLocalizedMessage());
        }

        NetworkExceptionDialog.showDialog(fragmentManager, dialog);
    }

    /**
     * Show the network exception dialog for dealing with disabled Internet connection.
     *
     * @param fragmentManager the fragment manager
     */
    public static void showNetworkExceptionDialog(FragmentManager fragmentManager) {
        NetworkExceptionDialog dialog = NetworkExceptionDialog.createInstance();
        NetworkExceptionDialog.showDialog(fragmentManager, dialog);
    }
}
