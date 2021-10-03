package cz.johnyapps.eddiehostopky;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.util.List;

import cz.johnyapps.eddiehostopky.settings.SettingIds;
import cz.johnyapps.eddiehostopky.settings.SettingItem;
import cz.johnyapps.eddiehostopky.settings.SettingsFactory;
import cz.johnyapps.eddiehostopky.settings.setting.BooleanSetting;
import cz.johnyapps.eddiehostopky.settings.setting.PlusMinusSetting;
import cz.johnyapps.eddiehostopky.tools.Logger;
import cz.johnyapps.eddiehostopky.tools.SharedPrefsNames;
import cz.johnyapps.eddiehostopky.tools.SharedPrefsUtils;
import cz.johnyapps.eddiehostopky.views.CountdownView;
import cz.johnyapps.eddiehostopky.views.StopwatchView;

public class StopwatchFragment extends Fragment {
    private static final String TAG = "StopwatchFragment";
    private static final int VIBRATION_LENGTH = 1000;

    private MainViewModel viewModel;
    private SharedPreferences generalPrefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setupViewModel();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        generalPrefs = SharedPrefsUtils.getGeneralPrefs(root.getContext());

        setupRoundCountdown(root);
        setupObservers(root);
        setupMatchStopwatch(root);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.nav_menu, menu);

        menu.findItem(R.id.ceskyLakrosMenuItem).setOnMenuItemClickListener(item -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requireContext().getString(R.string.ceskyLakrosURL))));
            return false;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void playSound() {
        AssetFileDescriptor assetFileDescriptor = getResources().openRawResourceFd(R.raw.time_out);

        if (assetFileDescriptor == null) {
            return;
        }

        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            mediaPlayer.start();
        } catch (IOException | IllegalStateException e) {
            Logger.e(TAG, "setupMediaPlayer: Failed to setup media player", e);
        }
    }

    private void stopAll(@NonNull View root) {
        StopwatchView matchStopWatch = root.findViewById(R.id.matchStopWatch);
        matchStopWatch.getStopwatchState().pause();

        CountdownView roundCountdown = root.findViewById(R.id.roundCountdown);
        roundCountdown.getStopwatchState().pause();

        StopwatchView punishmentOneStopwatch = root.findViewById(R.id.punishmentOneStopwatch);
        punishmentOneStopwatch.getStopwatchState().pause();

        StopwatchView punishmentTwoStopWatch = root.findViewById(R.id.punishmentTwoStopWatch);
        punishmentTwoStopWatch.getStopwatchState().pause();
    }

    private void resumeAllPaused(@NonNull View root) {
        StopwatchView matchStopWatch = root.findViewById(R.id.matchStopWatch);
        matchStopWatch.getStopwatchState().resumeIfPaused();

        CountdownView roundCountdown = root.findViewById(R.id.roundCountdown);
        roundCountdown.getStopwatchState().resumeIfPaused();

        StopwatchView punishmentOneStopwatch = root.findViewById(R.id.punishmentOneStopwatch);
        punishmentOneStopwatch.getStopwatchState().resumeIfPaused();

        StopwatchView punishmentTwoStopWatch = root.findViewById(R.id.punishmentTwoStopWatch);
        punishmentTwoStopWatch.getStopwatchState().resumeIfPaused();
    }

    private void setupMatchStopwatch(@NonNull View root) {
        StopwatchView matchStopWatch = root.findViewById(R.id.matchStopWatch);
        matchStopWatch.setOnRunningListener(running -> {
            List<SettingItem> settings = viewModel.getSettings().getValue();
            BooleanSetting stopAllWhenGameStoppedSetting = (BooleanSetting) SettingItem.findSetting(settings,
                    SettingIds.STOP_ALL_WHEN_GAME_STOPPED);
            BooleanSetting attackTimerAlwaysOnSetting = (BooleanSetting) SettingItem.findSetting(settings,
                    SettingIds.ATTACK_TIMER_ALWAYS_ON);

            if (stopAllWhenGameStoppedSetting != null &&
                    (stopAllWhenGameStoppedSetting.getValue() == null ||
                            stopAllWhenGameStoppedSetting.getValue())) {
                if (running) {
                    resumeAllPaused(root);
                } else {
                    stopAll(root);
                }
            }

            if (attackTimerAlwaysOnSetting != null &&
                    SettingsFactory.simplify(attackTimerAlwaysOnSetting.getValue(), SettingsFactory.ATTACK_TIMER_ALWAYS_ON_DEF)) {
                CountdownView roundCountdown = root.findViewById(R.id.roundCountdown);

                if (running) {
                    roundCountdown.getStopwatchState().start();
                } else {
                    roundCountdown.getStopwatchState().pause();
                }
            }
        });
        matchStopWatch.setOnRestartListener(() -> {
            CountdownView roundCountdown = root.findViewById(R.id.roundCountdown);
            if (roundCountdown.isHideStartPauseButton()) {
                roundCountdown.reset();
            }
        });
    }

    private void setupRoundCountdown(@NonNull View root) {
        CountdownView roundCountdown = root.findViewById(R.id.roundCountdown);
        roundCountdown.setAlertSecondsBeforeEnd(generalPrefs.getInt(SharedPrefsNames.ALERT_BEFORE_ATTACK_END, SettingsFactory.ALERT_BEFORE_ATTACK_END_DEF));
        roundCountdown.setOnCountdownCompleteListener(() -> {
            Vibrator vibrator = (Vibrator) root.getContext().getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_LENGTH, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(VIBRATION_LENGTH);
            }

            playSound();
        });
        roundCountdown.setOnRestartListener(() -> {
            StopwatchView matchStopWatch = root.findViewById(R.id.matchStopWatch);

            if (matchStopWatch.getStopwatchState().isRunning() &&
                    roundCountdown.isHideStartPauseButton()) {
                matchStopWatch.getStopwatchState().start();
            }
        });

        List<SettingItem> settings = viewModel.getSettings().getValue();
        BooleanSetting attackTimerAlwaysOnSetting = (BooleanSetting) SettingItem.findSetting(settings,
                SettingIds.ATTACK_TIMER_ALWAYS_ON);

        if (attackTimerAlwaysOnSetting != null) {
            roundCountdown.setHideStartPauseButton(SettingsFactory.simplify(attackTimerAlwaysOnSetting.getValue(), SettingsFactory.ATTACK_TIMER_ALWAYS_ON_DEF));
        }

        BooleanSetting attackTimerResetOtherTimeSetting = (BooleanSetting) SettingItem.findSetting(settings,
                SettingIds.ATTACK_TIMER_RESET_OTHER_SIDE);

        if (attackTimerResetOtherTimeSetting != null) {
            Logger.d(TAG, "setupRoundCountdown: %s", attackTimerResetOtherTimeSetting.getValue());
            roundCountdown.setReversedButtons(SettingsFactory.simplify(attackTimerResetOtherTimeSetting.getValue(), SettingsFactory.ATTACK_TIMER_RESET_OTHER_SIDE_DEF));
        }
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        viewModel = provider.get(MainViewModel.class);
    }

    private void setupObservers(@NonNull View root) {
        viewModel.getMatchStopwatchState().observe(getViewLifecycleOwner(), stopwatchState -> {
            if (stopwatchState != null) {
                StopwatchView matchStopWatch = root.findViewById(R.id.matchStopWatch);
                matchStopWatch.setStopwatchState(stopwatchState);
            } else {
                Logger.w(TAG, "setupObservers: Match StopwatchState changed to null");
            }
        });
        viewModel.getRoundStopwatchState().observe(getViewLifecycleOwner(), stopwatchState -> {
            if (stopwatchState != null) {
                CountdownView roundCountdown = root.findViewById(R.id.roundCountdown);
                roundCountdown.setStopwatchState(stopwatchState);
            } else {
                Logger.w(TAG, "setupObservers: Round StopwatchState changed to null");
            }
        });
        viewModel.getPunishmentOneStopwatchState().observe(getViewLifecycleOwner(), stopwatchState -> {
            if (stopwatchState != null) {
                StopwatchView punishmentOneStopwatch = root.findViewById(R.id.punishmentOneStopwatch);
                punishmentOneStopwatch.setStopwatchState(stopwatchState);
            } else {
                Logger.w(TAG, "setupObservers: Punishment one StopwatchState changed to null");
            }
        });
        viewModel.getPunishmentTwoStopwatchState().observe(getViewLifecycleOwner(), stopwatchState -> {
            if (stopwatchState != null) {
                StopwatchView punishmentTwoStopWatch = root.findViewById(R.id.punishmentTwoStopWatch);
                punishmentTwoStopWatch.setStopwatchState(stopwatchState);
            } else {
                Logger.w(TAG, "setupObservers: Punishment two StopwatchState changed to null");
            }
        });
        viewModel.getLastChangedSetting().observe(getViewLifecycleOwner(), settingItem -> {
            Logger.i(TAG, "setupObservers: Setting changed: %s",
                    settingItem == null ? "null" : settingItem.getTitle(root.getContext()));

            if (settingItem != null) {
                CountdownView roundCountdown = root.findViewById(R.id.roundCountdown);

                if (settingItem.getId() == SettingIds.ALERT_BEFORE_ATTACK_END) {
                    PlusMinusSetting setting = (PlusMinusSetting) settingItem;
                    roundCountdown.setAlertSecondsBeforeEnd(SettingsFactory.simplify(setting.getValue(), SettingsFactory.ALERT_BEFORE_ATTACK_END_DEF));
                }

                if (settingItem.getId() == SettingIds.ATTACK_TIMER_ALWAYS_ON) {
                    BooleanSetting setting = (BooleanSetting) settingItem;
                    roundCountdown.setHideStartPauseButton(SettingsFactory.simplify(setting.getValue(), SettingsFactory.ATTACK_TIMER_ALWAYS_ON_DEF));
                }

                if (settingItem.getId() == SettingIds.ATTACK_TIMER_RESET_OTHER_SIDE) {
                    BooleanSetting setting = (BooleanSetting) settingItem;
                    roundCountdown.setReversedButtons(SettingsFactory.simplify(setting.getValue(), SettingsFactory.ATTACK_TIMER_RESET_OTHER_SIDE_DEF));
                }
            }
        });
    }
}
