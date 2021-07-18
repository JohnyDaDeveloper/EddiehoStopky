package cz.johnyapps.eddiehostopky.settings.setting;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import cz.johnyapps.eddiehostopky.settings.OnSettingChangedListener;
import cz.johnyapps.eddiehostopky.settings.Setting;

public class BooleanSetting extends Setting<Boolean> {
    private static final int VIBRATION_LENGTH = 50;

    @StringRes
    private final int ifTrueStringId;
    @StringRes
    private final int ifFalseStringId;

    public BooleanSetting(int id,
                          @StringRes int titleId,
                          @NonNull Boolean aBoolean,
                          @StringRes int ifTrueStringId,
                          @StringRes int ifFalseStringId,
                          @Nullable OnSettingChangedListener<Boolean> onSettingChangedListener) {
        super(id, titleId, aBoolean, onSettingChangedListener);
        this.ifTrueStringId = ifTrueStringId;
        this.ifFalseStringId = ifFalseStringId;
    }

    @Nullable
    @Override
    public String valueToText(@Nullable Boolean aBoolean, @NonNull Context context) {
        return aBoolean == null ?
                context.getResources().getString(ifTrueStringId) :
                aBoolean ?
                        context.getResources().getString(ifTrueStringId) :
                        context.getResources().getString(ifFalseStringId);
    }

    @Override
    public void onClick(@NonNull Context context) {
        Boolean value = getValue();
        setValue(value != null && !value);
    }

    @Override
    public void click(@NonNull Context context) {
        super.click(context);

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_LENGTH, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(VIBRATION_LENGTH);
        }
    }
}
