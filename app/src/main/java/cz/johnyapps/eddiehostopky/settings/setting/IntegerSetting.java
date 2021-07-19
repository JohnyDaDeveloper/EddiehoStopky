package cz.johnyapps.eddiehostopky.settings.setting;

import android.content.Context;
import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import cz.johnyapps.eddiehostopky.tools.Logger;
import cz.johnyapps.eddiehostopky.dialogs.TextInputDialog;
import cz.johnyapps.eddiehostopky.settings.OnSettingChangedListener;
import cz.johnyapps.eddiehostopky.settings.Setting;

public class IntegerSetting extends Setting<Integer> {
    private static final String TAG = "IntegerSetting";
    private final int minValue;
    private final int maxValue;
    @StringRes
    private int textValueSuffix;

    public IntegerSetting(int id,
                          @StringRes int titleId,
                          int minValue,
                          int maxValue,
                          @Nullable Integer value,
                          @Nullable OnSettingChangedListener<Integer> onSettingChangedListener) {
        super(id, titleId, value, onSettingChangedListener);
        this.minValue = Math.min(minValue, maxValue);
        this.maxValue = Math.max(minValue, maxValue);
    }

    public IntegerSetting setTextValueSuffix(@StringRes int textValueSuffix) {
        this.textValueSuffix = textValueSuffix;
        return this;
    }

    @Nullable
    @Override
    public String valueToText(@Nullable Integer integer, @NonNull Context context) {
        return integer + (textValueSuffix == 0 ? "" : context.getResources().getString(textValueSuffix));
    }

    @Override
    public void onClick(@NonNull Context context) {
        TextInputDialog textInputDialog = new TextInputDialog(context);
        textInputDialog.show(getTitle(context),
                String.valueOf(getValue()),
                InputType.TYPE_CLASS_NUMBER,
                value -> {
                    try {
                        int intVal = value == null ? 0 : Integer.parseInt(value);
                        intVal = Math.min(maxValue, intVal);
                        setValue(Math.max(minValue, intVal));
                    } catch (Exception e) {
                        Logger.w(String.format("%s (id: %s)", TAG, getId()), "onClick: failed to convert '%s' to integer", e, value);
                    }
                });
    }
}
