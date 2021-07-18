package cz.johnyapps.eddiehostopky.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cz.johnyapps.eddiehostopky.R;
import cz.johnyapps.eddiehostopky.settings.setting.BooleanSetting;
import cz.johnyapps.eddiehostopky.tools.SharedPrefsNames;
import cz.johnyapps.eddiehostopky.tools.SharedPrefsUtils;

public class SettingsFactory {
    @NonNull
    private final Context context;
    @NonNull
    private final SharedPreferences generalPrefs;

    public SettingsFactory(@NonNull Context context) {
        this.context = context;
        this.generalPrefs = SharedPrefsUtils.getGeneralPrefs(context);
    }

    public List<SettingItem> load(@NonNull OnSettingItemChangedListener onSettingItemChangedListener) {
        boolean showAppreciation = generalPrefs.getBoolean(SharedPrefsNames.SHOW_APPRECIATION, true);

        List<SettingItem> settings = new ArrayList<>();
        settings.add(new BooleanSetting(SettingIds.SHOW_APPRECIATION,
                R.string.settingShowAppreciation,
                showAppreciation,
                R.string.yes,
                R.string.no,
                setting -> {
                    generalPrefs.edit().putBoolean(SharedPrefsNames.SHOW_APPRECIATION,
                            simplify(setting.getValue(), showAppreciation)).apply();
                    onSettingItemChangedListener.onChange(setting);
                }));

        return settings;
    }

    public boolean simplify(@Nullable Boolean aBoolean, boolean defaultValue) {
        return aBoolean == null ? defaultValue : aBoolean;
    }
}
