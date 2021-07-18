package cz.johnyapps.eddiehostopky;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cz.johnyapps.eddiehostopky.settings.SettingItem;
import cz.johnyapps.eddiehostopky.settings.SettingsFactory;
import cz.johnyapps.eddiehostopky.settings.setting.BooleanSetting;
import cz.johnyapps.eddiehostopky.tools.Logger;

public class MainViewModel extends AndroidViewModel {
    @NonNull
    private final SettingsFactory settingsFactory;

    @NonNull
    private final MutableLiveData<StopwatchState> matchStopwatchState = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<StopwatchState> roundStopwatchState = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<StopwatchState> punishmentOneStopwatchState = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<StopwatchState> punishmentTwoStopwatchState = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<List<SettingItem>> settings = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<SettingItem> lastChangedSetting = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);

        settingsFactory = new SettingsFactory(application);

        fetchSettings();
        loadStopwatchStates();
    }

    private void fetchSettings() {
        settings.setValue(settingsFactory.load(this::setLastChangedSetting));
    }

    private void loadStopwatchStates() {
        setMatchStopwatchState(new StopwatchState());
        setRoundStopwatchState(new StopwatchState());
        setPunishmentOneStopwatchState(new StopwatchState());
        setPunishmentTwoStopwatchState(new StopwatchState());
    }

    @NonNull
    public LiveData<StopwatchState> getMatchStopwatchState() {
        return matchStopwatchState;
    }

    public void setMatchStopwatchState(@NonNull StopwatchState matchStopwatchState) {
        this.matchStopwatchState.setValue(matchStopwatchState);
    }

    @NonNull
    public LiveData<StopwatchState> getRoundStopwatchState() {
        return roundStopwatchState;
    }

    public void setRoundStopwatchState(@NonNull StopwatchState roundStopwatchState) {
        this.roundStopwatchState.setValue(roundStopwatchState);
    }

    @NonNull
    public LiveData<StopwatchState> getPunishmentOneStopwatchState() {
        return punishmentOneStopwatchState;
    }

    public void setPunishmentOneStopwatchState(@NonNull StopwatchState punishmentOneStopwatchState) {
        this.punishmentOneStopwatchState.setValue(punishmentOneStopwatchState);
    }

    @NonNull
    public LiveData<StopwatchState> getPunishmentTwoStopwatchState() {
        return punishmentTwoStopwatchState;
    }

    public void setPunishmentTwoStopwatchState(@NonNull StopwatchState punishmentTwoStopwatchState) {
        this.punishmentTwoStopwatchState.setValue(punishmentTwoStopwatchState);
    }

    @NonNull
    public LiveData<List<SettingItem>> getSettings() {
        return settings;
    }

    public void setSettings(@Nullable List<SettingItem> settings) {
        this.settings.setValue(settings);
    }

    @NonNull
    public LiveData<SettingItem> getLastChangedSetting() {
        return lastChangedSetting;
    }

    public void setLastChangedSetting(@Nullable SettingItem lastChangedSetting) {
        if (lastChangedSetting != null) {
            Logger.d("TAG", "setLastChangedSetting: %s %s", lastChangedSetting.getTitle(getApplication()), ((BooleanSetting) lastChangedSetting).getValue());
        }

        this.lastChangedSetting.setValue(lastChangedSetting);
    }
}
