package cz.johnyapps.eddiehostopky.dialogs;

import android.content.Context;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import cz.johnyapps.eddiehostopky.tools.Logger;
import cz.johnyapps.eddiehostopky.R;
import cz.johnyapps.eddiehostopky.tools.SoftKeyboardUtils;

public class TextInputDialog {
    private static final String TAG = "TextInputDialog";

    @NonNull
    private final Context context;
    @Nullable
    private AlertDialog dialog;

    public TextInputDialog(@NonNull Context context) {
        this.context = context;
    }

    public void show(@NonNull String title,
                     @Nullable String hint,
                     int inputType,
                     @NonNull OnConfirmListener onConfirmListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setView(R.layout.dialog_text_input)
                .setTitle(title)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    SoftKeyboardUtils.hideKeyboard(((AlertDialog) dialog).getCurrentFocus());
                    EditText valueEditText = ((AlertDialog) dialog).findViewById(R.id.valueEditText);
                    assert valueEditText != null;
                    submitValue(valueEditText.getText().toString(), onConfirmListener);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) ->  {
                    SoftKeyboardUtils.hideKeyboard(((AlertDialog) dialog).getCurrentFocus());
                });

        dialog = builder.create();
        dialog.show();

        AppCompatEditText valueEditText = dialog.findViewById(R.id.valueEditText);
        assert valueEditText != null;
        valueEditText.setHint(hint);
        valueEditText.setInputType(inputType);
        valueEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                SoftKeyboardUtils.hideKeyboard(v);

                AppCompatEditText editText = (AppCompatEditText) v;
                submitValue(editText.getText() == null ? null : editText.getText().toString(), onConfirmListener);
                return true;
            }

            return false;
        });

        SoftKeyboardUtils.showKeyboard(valueEditText);
    }

    public void submitValue(@Nullable String value, @NonNull OnConfirmListener onConfirmListener) {
        Logger.d(TAG, "Submitted value: %s", value);
        onConfirmListener.onConfirm(value);

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public interface OnConfirmListener {
        void onConfirm(@Nullable String value);
    }
}
