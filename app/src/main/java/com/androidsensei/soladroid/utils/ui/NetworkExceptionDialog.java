package com.androidsensei.soladroid.utils.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.androidsensei.soladroid.R;

/**
 * Dialog for displaying network access messages or errors.
 * <p/>
 * Created by mihai on 7/1/15.
 */
public class NetworkExceptionDialog extends DialogFragment {
    /**
     * The dialog tag.
     */
    private static final String NETWORK_EXCEPTION_DIALOG_TAG = "NetworkExceptionDialog";

    /**
     * Bundle key for referring the message string id.
     */
    private static final String MESSAGE_STRING_KEY = "MESSAGE_STRING_KEY";

    /**
     * The message we want to display in the dialog. This is present when the dialog is used to display network errors.
     * For WiFi disabled we have a default message.
     */
    private String message;

    /**
     * The number of instances this dialog has currently shown.
     */
    private static int instanceCount;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (message != null) {
            builder.setTitle(R.string.network_off_title)
                    .setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    instanceCount--;
                }
            });
        } else {
            builder.setTitle(R.string.network_off_title).setMessage(R.string.network_off_message)
                    .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            instanceCount--;
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    instanceCount--;
                }
            });
        }

        return builder.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            message = savedInstanceState.getString(MESSAGE_STRING_KEY);
        } else {
            Bundle args = getArguments();
            if (args != null) {
                message = args.getString(MESSAGE_STRING_KEY);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Bundle args = getArguments();
        if (args != null) {
            outState.putAll(getArguments());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        instanceCount++;
        Log.d("r1k0", "show instanceCount: " + instanceCount);
        super.show(manager, tag);

    }

    /**
     * Creates NetworkExceptionDialog instances with the given arguments.
     *
     * @return an instance of the NetworkExceptionDialog
     */
    public static NetworkExceptionDialog createInstance() {
        NetworkExceptionDialog instance = new NetworkExceptionDialog();

        return instance;
    }

    /**
     * Creates NetworkExceptionDialog instances with the given arguments.
     *
     * @param message the message string to be displayed in the dialog.
     * @return an instance of the NetworkExceptionDialog
     */
    public static NetworkExceptionDialog createInstance(String message) {
        NetworkExceptionDialog instance = new NetworkExceptionDialog();
        Bundle args = new Bundle();

        args.putString(MESSAGE_STRING_KEY, message);
        instance.setArguments(args);

        return instance;
    }

    /**
     * Show the NetworkException dialog with removing it from the back stack if previously shown.
     *
     * @param fragmentManager the fragment manager responsible for keeping track of the fragments
     * @param dialog          the dialog instance to be shown
     */
    public static void showDialog(FragmentManager fragmentManager, NetworkExceptionDialog dialog) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(NETWORK_EXCEPTION_DIALOG_TAG);
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.addToBackStack(null).commit();
        if (!(instanceCount > 0)) {
            dialog.show(fragmentManager, NETWORK_EXCEPTION_DIALOG_TAG);
        }
    }

}
