package cz.johnyapps.eddiehostopky.settings;

import androidx.annotation.NonNull;

public interface OnSettingChangedListener<VALUE> {
    void onChange(@NonNull Setting<VALUE> setting);
}
