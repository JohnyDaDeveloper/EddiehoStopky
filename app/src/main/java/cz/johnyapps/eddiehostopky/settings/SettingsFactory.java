package cz.johnyapps.eddiehostopky.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cz.johnyapps.eddiehostopky.R;
import cz.johnyapps.eddiehostopky.settings.setting.BooleanSetting;
import cz.johnyapps.eddiehostopky.settings.setting.PlusMinusSetting;
import cz.johnyapps.eddiehostopky.tools.SharedPrefsNames;
import cz.johnyapps.eddiehostopky.tools.SharedPrefsUtils;

public class SettingsFactory {
    @NonNull
    private final Context context;
    @NonNull
    private final SharedPreferences generalPrefs;

    public static final int ALERT_BEFORE_ATTACK_END_DEF = 5;
    public static final int ALERT_BEFORE_ATTACK_END_MIN = 0;
    public static final int ALERT_BEFORE_ATTACK_END_MAX = 30;

    public SettingsFactory(@NonNull Context context) {
        this.context = context;
        this.generalPrefs = SharedPrefsUtils.getGeneralPrefs(context);
    }

    public List<SettingItem> load(@NonNull OnSettingItemChangedListener onSettingItemChangedListener) {
        boolean showAppreciation = generalPrefs.getBoolean(SharedPrefsNames.SHOW_APPRECIATION, true);
        boolean stopAllWhenGameStopped = generalPrefs.getBoolean(SharedPrefsNames.STOP_ALL_WHEN_GAME_STOPPED, true);
        int alertBeforeAttackEnd = generalPrefs.getInt(SharedPrefsNames.ALERT_BEFORE_ATTACK_END, ALERT_BEFORE_ATTACK_END_DEF);
        boolean attackTimerAlwaysOn = generalPrefs.getBoolean(SharedPrefsNames.ATTACK_TIMER_ALWAYS_ON, false);

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
        settings.add(new BooleanSetting(SettingIds.STOP_ALL_WHEN_GAME_STOPPED,
                R.string.settingStopAllWhenGameStopped,
                stopAllWhenGameStopped,
                R.string.yes,
                R.string.no,
                setting -> {
                    generalPrefs.edit().putBoolean(SharedPrefsNames.STOP_ALL_WHEN_GAME_STOPPED,
                            simplify(setting.getValue(), stopAllWhenGameStopped)).apply();
                    onSettingItemChangedListener.onChange(setting);
                }));
        settings.add(new BooleanSetting(SettingIds.ATTACK_TIMER_ALWAYS_ON,
                R.string.settingAttackTimerAlwaysOn,
                attackTimerAlwaysOn,
                R.string.yes,
                R.string.no,
                setting -> {
                    generalPrefs.edit().putBoolean(SharedPrefsNames.ATTACK_TIMER_ALWAYS_ON,
                            simplify(setting.getValue(), attackTimerAlwaysOn)).apply();

                    onSettingItemChangedListener.onChange(setting);
                }));
        settings.add(new PlusMinusSetting(SettingIds.ALERT_BEFORE_ATTACK_END,
                R.string.settingAlertBeforeAttackEnd,
                alertBeforeAttackEnd,
                ALERT_BEFORE_ATTACK_END_DEF,
                ALERT_BEFORE_ATTACK_END_MIN,
                ALERT_BEFORE_ATTACK_END_MAX,
                setting -> {
                    generalPrefs.edit().putInt(SharedPrefsNames.ALERT_BEFORE_ATTACK_END,
                            simplify(setting.getValue(), alertBeforeAttackEnd)).apply();
                    onSettingItemChangedListener.onChange(setting);
                }).setTextValueSuffix(R.string.seconds));

        return settings;
    }

    public static boolean simplify(@Nullable Boolean aBoolean, boolean defaultValue) {
        return aBoolean == null ? defaultValue : aBoolean;
    }

    public static int simplify(@Nullable Integer integer, int defaultValue) {
        return integer == null ? defaultValue : integer;
    }
}
