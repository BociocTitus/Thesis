package app.com.titus.communityapp.util.factory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

public class DialogFactory {

    private static Dialog dialog = null;

    public static Dialog createDialog(String message, String positiveMessage,
                                      String negativeMessage, Activity activity, Runnable confirm,
                                      Runnable cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setPositiveButton(positiveMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        confirm.run();
                    }
                })
                .setNegativeButton(negativeMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancel.run();
                    }
                });

        return builder.create();
    }
}
