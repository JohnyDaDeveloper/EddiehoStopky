package cz.johnyapps.eddiehostopky;

import androidx.annotation.Nullable;

import cz.johnyapps.eddiehostopky.tools.Logger;

public class StopwatchState {
    private static final String TAG = "StopwatchState";

    private boolean running = false;
    private boolean pausedByGameStopwatch = false;
    private long startTime = -1;
    private long pauseTime = -1;

    @Nullable
    private OnRunningListener onRunningListener;
    @Nullable
    private OnRestartListener onRestartListener;

    public StopwatchState() {

    }

    public void pause() {
        if (running) {
            startOrPause();
            pausedByGameStopwatch = true;
        }
    }

    public void resumeIfPaused() {
        if (!running && pausedByGameStopwatch) {
            startOrPause();
        }
    }

    public void start() {
        pausedByGameStopwatch = false;
        running = true;

        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }

        if (onRunningListener != null) {
            onRunningListener.running(true);
        }
    }

    public void startOrPause() {
        pausedByGameStopwatch = false;

        if (running) {
            running = false;
            pauseTime = System.currentTimeMillis();
        } else {
            running = true;

            if (startTime == -1) {
                startTime = System.currentTimeMillis();
            }
        }

        if (onRunningListener != null) {
            onRunningListener.running(running);
        }
    }

    public void reset() {
        running = false;
        pausedByGameStopwatch = false;
        startTime = -1;
        pauseTime = -1;

        if (onRunningListener != null) {
            onRunningListener.running(running);
        }

        if (onRestartListener != null) {
            onRestartListener.onRestart();
        }
    }

    public long getRunningFor() {
        if (pauseTime != -1) {
            startTime += System.currentTimeMillis() - pauseTime;
            pauseTime = running ? -1 : System.currentTimeMillis();
        }

        return startTime == -1 ? 0 : System.currentTimeMillis() - startTime;
    }

    public boolean isRunning() {
        return running;
    }

    public interface OnRunningListener {
        void running(boolean running);
    }

    public void setOnRunningListener(@Nullable OnRunningListener onRunningListener) {
        this.onRunningListener = onRunningListener;

        if (onRunningListener != null) {
            onRunningListener.running(running);
        }
    }

    public interface OnRestartListener {
        void onRestart();
    }

    public void setOnRestartListener(@Nullable OnRestartListener onRestartListener) {
        this.onRestartListener = onRestartListener;
    }
}
