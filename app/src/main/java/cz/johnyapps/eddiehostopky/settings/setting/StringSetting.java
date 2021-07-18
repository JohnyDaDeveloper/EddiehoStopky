package cz.johnyapps.eddiehostopky.settings.setting;

import android.content.Context;
import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import cz.johnyapps.eddiehostopky.dialogs.TextInputDialog;
import cz.johnyapps.eddiehostopky.settings.OnSettingChangedListener;
import cz.johnyapps.eddiehostopky.settings.Setting;

public class StringSetting extends Setting<String> {
    public StringSetting(int id,
                         @StringRes int titleId,
                         @Nullable String s,
                         @Nullable OnSettingChangedListener<String> onSettingChangedListener) {
        super(id, titleId, s, onSettingChangedListener);
    }

    @Nullable
    @Override
    public String valueToText(@Nullable String s, @NonNull Context context) {
        return s;
    }

    @Override
    public void onClick(@NonNull Context context) {
        TextInputDialog textInputDialog = new TextInputDialog(context);
        textInputDialog.show(getTitle(context),
                getValue(),
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME,
                value -> {
                    if (value != null && !value.isEmpty()) {
                        setValue(value);
                    }
                });
    }
}
