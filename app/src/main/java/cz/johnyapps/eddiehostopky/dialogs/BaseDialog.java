package cz.johnyapps.eddiehostopky.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import cz.johnyapps.eddiehostopky.tools.Logger;

public abstract class BaseDialog<VALUE> {
    @NonNull
    private static final String TAG = "BaseDialog";

    @Nullable
    private AlertDialog alertDialog;
    @NonNull
    private final Context context;

    public BaseDialog(@NonNull Context context) {
        this.context = context;
    }

    public void submitValue(@Nullable VALUE value,
                            @NonNull OnConfirmListener<VALUE> onConfirmListener) {
        Logger.d(TAG, "Submitted value: %s", value);
        onConfirmListener.onConfirm(value);

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @NonNull
    public Context getContext() {
        return context;
    }

    @Nullable
    protected AlertDialog getAlertDialog() {
        return alertDialog;
    }

    protected void setAlertDialog(@Nullable AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    public interface OnConfirmListener<VALUE> {
        void onConfirm(@Nullable VALUE value);
    }
}
