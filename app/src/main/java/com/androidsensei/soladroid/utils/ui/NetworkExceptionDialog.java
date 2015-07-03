package com.androidsensei.soladroid.utils.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.androidsensei.soladroid.R;

/**
 * Dialog for displaying network access messages or errors.
 * <p/>
 * Created by mihai on 7/1/15.
 */
public class NetworkExceptionDialog extends DialogFragment {
    /**
     * Bundle key for referring the message string id.
     */
    private static final String MESSAGE_STRING_KEY = "MESSAGE_STRING_KEY";

    /**
     * The message we want to display in the dialog. This is present when the dialog is used to display network errors.
     * For WiFi disabled we have a default message.
     */
    private String message;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (message != null) {
            builder.setTitle(R.string.network_off_title)
                    .setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setTitle(R.string.network_off_title).setMessage(R.string.network_off_message)
                    .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
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
            message = args.getString(MESSAGE_STRING_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(getArguments());
        super.onSaveInstanceState(outState);
    }

    /**
     * Creates NetworkExceptionDialog instances with the given arguments.
     *
     * @return an instance of the NetworkExceptionDialog
     */
    public NetworkExceptionDialog createInstance() {
        NetworkExceptionDialog instance = new NetworkExceptionDialog();

        return instance;
    }

    /**
     * Creates NetworkExceptionDialog instances with the given arguments.
     *
     * @param message the message string to be displayed in the dialog.
     * @return an instance of the NetworkExceptionDialog
     */
    public NetworkExceptionDialog createInstance(String message) {
        NetworkExceptionDialog instance = new NetworkExceptionDialog();
        Bundle args = new Bundle();

        args.putString(MESSAGE_STRING_KEY, message);
        instance.setArguments(args);

        return instance;
    }

}
