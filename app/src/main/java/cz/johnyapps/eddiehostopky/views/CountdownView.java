package cz.johnyapps.eddiehostopky.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.johnyapps.eddiehostopky.R;
import cz.johnyapps.eddiehostopky.StopwatchState;

public class CountdownView extends StopwatchView {
    private static final int COUNT_FROM_DEFAULT = 30;

    private long countFrom;
    private long alertSecondsBeforeEnd = 0;

    @Nullable
    private OnCountdownCompleteListener onCountdownCompleteListener;

    @NonNull
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            StopwatchState stopwatchState = getStopwatchState();

            if (stopwatchState.isRunning()) {
                long remaining = drawFrame();

                if (remaining > 0) {
                    getTimeHandler().postDelayed(this, 1000);
                } else if (onCountdownCompleteListener != null) {
                    stopwatchState.reset();
                    drawFrame();
                }

                if (remaining == alertSecondsBeforeEnd) {
                    onCountdownCompleteListener.onComplete();
                }
            }
        }
    };

    public CountdownView(Context context) {
        super(context);
    }

    public CountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountdownView(Context context, AttributeSet attrs, int theme) {
        super(context, attrs, theme);
    }

    @Override
    public void init(@Nullable AttributeSet attrs, int theme) {
        super.init(attrs, theme);

        if (attrs != null) {
            final TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CountdownView, theme, 0);

            countFrom = array.getInt(R.styleable.CountdownView_countFrom, COUNT_FROM_DEFAULT);

            array.recycle();
        }

        timeTextView.setText(String.format("%ss", countFrom));
    }

    @Override
    protected long drawFrame() {
        StopwatchState stopwatchState = getStopwatchState();
        if (stopwatchState != null) {
            long seconds = stopwatchState.getRunningFor() / 1000;
            long count = countFrom - seconds;

            timeTextView.setText(String.format("%ss", count));
            return count;
        }

        return countFrom;
    }

    @Override
    protected void startOrPause() {
        StopwatchState stopwatchState = getStopwatchState();

        if (stopwatchState.getRunningFor() / 1000 >= countFrom) {
            stopwatchState.reset();
        }

        super.startOrPause();
    }

    @Override
    public void reset() {
        super.reset();
        timeTextView.setText(getResources().getString(R.string.countdownView_time, String.valueOf(countFrom)));
    }

    @Override
    @NonNull
    public Runnable getRunnable() {
        return runnable;
    }

    public interface OnCountdownCompleteListener {
        void onComplete();
    }

    public void setOnCountdownCompleteListener(@Nullable OnCountdownCompleteListener onCountdownCompleteListener) {
        this.onCountdownCompleteListener = onCountdownCompleteListener;
    }

    public void setAlertSecondsBeforeEnd(long alertSecondsBeforeEnd) {
        this.alertSecondsBeforeEnd = alertSecondsBeforeEnd;
    }
}
