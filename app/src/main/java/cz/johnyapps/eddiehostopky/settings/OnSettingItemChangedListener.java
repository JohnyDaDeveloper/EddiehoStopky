package cz.johnyapps.eddiehostopky.settings;

import androidx.annotation.NonNull;

public interface OnSettingItemChangedListener {
    void onChange(@NonNull SettingItem settingItem);
}
