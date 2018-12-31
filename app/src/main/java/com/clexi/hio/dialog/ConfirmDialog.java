package com.clexi.hio.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.clexi.hio.R;

/**
 * Created by yousef on 9/26/2016.
 */

public class ConfirmDialog extends DialogFragment
{
    private AlertDialog    dialog;
    private DialogListener listener;
    private String         title;
    private String         message;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setIcon(R.mipmap.ic_launcher)
                // Add Dialog Title
                .setTitle(title != null ? title : "")
                // Add Dialog Message
                .setMessage(message)
                // Add action buttons
                .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        dismiss();

                        listener.onPositiveButtonClicked();
                    }
                })
                .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        listener.onNegativeButtonClicked();
                    }
                });

        dialog = builder.create();
        return dialog;
    }

    public ConfirmDialog setListener(DialogListener listener)
    {
        this.listener = listener;
        return this;
    }

    public ConfirmDialog setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public ConfirmDialog setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public interface DialogListener
    {
        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }
}
