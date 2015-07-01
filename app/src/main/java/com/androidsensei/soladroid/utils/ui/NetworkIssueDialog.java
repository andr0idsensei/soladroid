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
public class NetworkIssueDialog extends DialogFragment {
    /**
     * Bundle key for referring the title string id.
     */
    private static final String TITLE_STRING_ID_KEY = "TITLE_STRING_ID_KEY";

    /**
     * Bundle key for referring the message string id.
     */
    private static final String MESSAGE_STRING_ID_KEY = "MESSAGE_STRING_ID_KEY";

    /**
     * Bundle key for referring the info dialog flag.
     */
    private static final String INFO_DIALOG_KEY = "INFO_DIALOG_KEY";

    /**
     * The strings id for the dialog title.
     */
    private int titleId;

    /**
     * The strings id for the dialog message.
     */
    private int messageId;

    /**
     * Boolean flag telling us if this is only an info dialog or a dialog that can direct the user to the WiFi settings.
     */
    private boolean infoDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(messageId).setTitle(titleId);

        if (infoDialog) {
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
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
            titleId = savedInstanceState.getInt(TITLE_STRING_ID_KEY);
            messageId = savedInstanceState.getInt(MESSAGE_STRING_ID_KEY);
            infoDialog = savedInstanceState.getBoolean(INFO_DIALOG_KEY);
        } else {
            Bundle args = getArguments();
            titleId = args.getInt(TITLE_STRING_ID_KEY);
            messageId = args.getInt(MESSAGE_STRING_ID_KEY);
            infoDialog = args.getBoolean(INFO_DIALOG_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(getArguments());
        super.onSaveInstanceState(outState);
    }

    /**
     * Creates NetworkIssueDialog instances with the given arguments.
     *
     * @param titleId    the dialog title string id
     * @param messageId  the dialog message string id
     * @param infoDialog flag to let us know if this is an info only dialog or not
     * @return an instance of the NetworkIssueDialog
     */
    public NetworkIssueDialog createInstance(int titleId, int messageId, boolean infoDialog) {
        NetworkIssueDialog instance = new NetworkIssueDialog();
        Bundle args = new Bundle();

        args.putInt(TITLE_STRING_ID_KEY, titleId);
        args.putInt(MESSAGE_STRING_ID_KEY, messageId);
        args.putBoolean(INFO_DIALOG_KEY, infoDialog);
        instance.setArguments(args);

        return instance;
    }

}
