package cz.johnyapps.eddiehostopky.settings;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public abstract class Setting<VALUE> extends SettingItem {
    @Nullable
    private VALUE value;
    @Nullable
    private OnSettingChangedListener<VALUE> onSettingChangedListener;

    public Setting(int id,
                   @StringRes int titleId,
                   @Nullable VALUE value,
                   @Nullable OnSettingChangedListener<VALUE> onSettingChangedListener) {
        super(id, titleId, false);
        this.value = value;
        this.onSettingChangedListener = onSettingChangedListener;
    }

    public Setting(int id, @StringRes int titleId, @Nullable VALUE value) {
        this(id, titleId, value, null);
    }

    public void changed() {
        if (onSettingChangedListener != null) {
            onSettingChangedListener.onChange(this);
        }
    }

    @Nullable
    public abstract String valueToText(@Nullable VALUE value, @NonNull Context context);

    @Nullable
    public VALUE getValue() {
        return value;
    }

    public void setValue(@Nullable VALUE value) {
        this.value = value;
        changed();
    }

    @Override
    @Nullable
    public String getTextValue(@NonNull Context context) {
        return valueToText(value, context);
    }

    public void setOnSettingChangedListener(@Nullable OnSettingChangedListener<VALUE> onSettingChangedListener) {
        this.onSettingChangedListener = onSettingChangedListener;
    }
}
