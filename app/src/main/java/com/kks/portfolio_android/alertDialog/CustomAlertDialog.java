package com.kks.portfolio_android.alertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import com.kks.portfolio_android.R;

public class CustomAlertDialog {

    public void alertDialog_checked(String message, Context context){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context, R.style.myDialogTheme);
        alertDialog.setTitle(R.string.check_id_overlap);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton
                (R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        final android.app.AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void alertDialog_Unchecked(String message, Context context) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context, R.style.myDialogTheme);
        alertDialog.setTitle(R.string.check_id_overlap);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton
                (R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        final android.app.AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
