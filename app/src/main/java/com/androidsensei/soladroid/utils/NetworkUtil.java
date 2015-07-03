package com.androidsensei.soladroid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
}
