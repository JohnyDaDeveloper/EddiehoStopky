package cz.johnyapps.eddiehostopky.settings.setting;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import cz.johnyapps.eddiehostopky.dialogs.PlusMinusDialog;
import cz.johnyapps.eddiehostopky.settings.OnSettingChangedListener;
import cz.johnyapps.eddiehostopky.settings.Setting;

public class PlusMinusSetting extends Setting<Integer> {
    private final int defaultValue;
    private final int min;
    private final int max;
    @StringRes
    private int suffixId = 0;

    public PlusMinusSetting(int id,
                            @StringRes int titleId,
                            int value,
                            int defaultValue,
                            int min,
                            int max,
                            @Nullable OnSettingChangedListener<Integer> onSettingChangedListener) {
        super(id, titleId, value, onSettingChangedListener);
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
    }

    public PlusMinusSetting setTextValueSuffix(@StringRes int suffixId) {
        this.suffixId = suffixId;
        return this;
    }

    @Nullable
    @Override
    public String valueToText(@Nullable Integer integer, @NonNull Context context) {
        if (suffixId == 0) {
            return String.valueOf(integer == null ? defaultValue : integer);
        } else {
            return String.format("%s%s",
                    integer == null ? defaultValue : integer,
                    context.getResources().getString(suffixId));
        }
    }

    @Override
    public void onClick(@NonNull Context context) {
        int value = getValue() == null ? defaultValue : getValue();

        PlusMinusDialog plusMinusDialog = new PlusMinusDialog(context);
        plusMinusDialog.show(getTitle(context), value, min, max, this::setValue);
    }
}
