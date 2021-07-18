package cz.johnyapps.eddiehostopky.settings;

import android.content.Context;

import androidx.annotation.NonNull;

public interface SettingChoice {
    int getId();
    String getTitle(@NonNull Context context);
}
