package cz.johnyapps.eddiehostopky;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
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

import cz.johnyapps.eddiehostopky.tools.Logger;
import cz.johnyapps.eddiehostopky.views.CountdownView;
import cz.johnyapps.eddiehostopky.views.StopwatchView;

public class StopwatchFragment extends Fragment {
    private static final String TAG = "StopwatchFragment";
    private static final int VIBRATION_LENGTH = 1000;

    private MainViewModel viewModel;

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

        setupViewModel();
        setupRoundCountdown(root);
        setupObservers(root);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.nav_menu, menu);
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

    private void setupRoundCountdown(@NonNull View root) {
        CountdownView roundCountdown = root.findViewById(R.id.roundCountdown);
        roundCountdown.setOnCountdownCompleteListener(() -> {
            Vibrator vibrator = (Vibrator) root.getContext().getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_LENGTH, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(VIBRATION_LENGTH);
            }

            try {
                playSound();
            } catch (IllegalStateException e) {
                Logger.w(TAG, "setupRoundCountdown: Failed to play sound", e);
            }
        });
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(this);
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
    }
}