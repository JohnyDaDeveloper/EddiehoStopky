package cz.johnyapps.eddiehostopky;

import androidx.annotation.Nullable;

public class StopwatchState {
    private boolean running = false;
    private long startTime = -1;
    private long pauseTime = -1;

    @Nullable
    private OnRunningListener onRunningListener;

    public StopwatchState() {

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

            if (pauseTime != -1) {
                startTime += System.currentTimeMillis() - pauseTime;
                pauseTime = -1;
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
        return System.currentTimeMillis() - startTime;
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
