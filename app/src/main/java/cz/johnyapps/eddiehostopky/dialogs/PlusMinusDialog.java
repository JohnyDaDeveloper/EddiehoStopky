package cz.johnyapps.eddiehostopky.dialogs;

import android.content.Context;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import cz.johnyapps.eddiehostopky.R;
import cz.johnyapps.eddiehostopky.tools.Logger;
import cz.johnyapps.eddiehostopky.tools.SoftKeyboardUtils;

public class PlusMinusDialog extends BaseDialog<Integer> {
    @NonNull
    private static final String TAG = "PlusMinusDialog";
    private int value = 0;
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public PlusMinusDialog(@NonNull Context context) {
        super(context);
    }

    public void show(@NonNull String title,
                     int value,
                     int min,
                     int max,
                     @NonNull OnConfirmListener<Integer> onConfirmListener) {
        this.value = value;
        this.min = min;
        this.max = max;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(title)
                .setView(R.layout.dialog_plus_minus)
                .setPositiveButton(R.string.ok, (dialog, which) -> onConfirmListener.onConfirm(this.value))
                .setNegativeButton(R.string.cancel, (dialog, which) -> {});

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        setAlertDialog(alertDialog);

        AppCompatButton plusButton = alertDialog.findViewById(R.id.plusButton);
        assert plusButton != null;
        plusButton.setOnClickListener(v -> changeValue(1));

        AppCompatButton minusButton = alertDialog.findViewById(R.id.minusButton);
        assert minusButton != null;
        minusButton.setOnClickListener(v -> changeValue(-1));

        AppCompatEditText numberEditText = alertDialog.findViewById(R.id.numberEditText);
        assert numberEditText != null;
        numberEditText.setText(String.valueOf(value));
        numberEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                SoftKeyboardUtils.hideKeyboard(v);

                AppCompatEditText editText = (AppCompatEditText) v;
                String strVal = editText.getText() == null ? null : editText.getText().toString();
                int val = this.value;

                if (strVal != null) {
                    try {
                        val = Integer.parseInt(strVal);
                        val = Math.max(val, min);
                        val = Math.min(val, max);
                    } catch (Exception e) {
                        Logger.w(TAG, "show: Failed to parse value '%s'", e, strVal);
                    }
                }

                this.value = val;
                editText.setText(String.valueOf(val));
                return true;
            }

            return false;
        });
    }

    private void changeValue(int add) {
        if (getAlertDialog() != null && getAlertDialog().isShowing()) {
            if (value + add >= min && value + add <= max) {
                value += add;

                AppCompatEditText numberEditText = getAlertDialog().findViewById(R.id.numberEditText);
                assert numberEditText != null;
                numberEditText.setText(String.valueOf(value));
            }
        }
    }
}
