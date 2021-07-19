package cz.johnyapps.eddiehostopky;

import androidx.annotation.Nullable;

public class StopwatchState {
    private boolean running = false;
    private long startTime = -1;
    private long pauseTime = -1;
    private boolean pausedByGameStopwatch = false;

    @Nullable
    private OnRunningListener onRunningListener;

    public StopwatchState() {

    }

    public void pause() {
        if (running) {
            pausedByGameStopwatch = true;
            startOrPause();
        }
    }

    public void resumeIfPaused() {
        if (!running && pausedByGameStopwatch) {
            pausedByGameStopwatch = false;
            startOrPause();
        }
    }

    public void startOrPause() {
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
        startTime = -1;
        pauseTime = -1;

        if (onRunningListener != null) {
            onRunningListener.running(running);
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
}
