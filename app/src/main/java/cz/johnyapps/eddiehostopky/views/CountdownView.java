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

    @Nullable
    private OnCountdownCompleteListener onCountdownCompleteListener;

    @NonNull
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            StopwatchState stopwatchState = getStopwatchState();

            if (stopwatchState.isRunning()) {
                long seconds = stopwatchState.getRunningFor() / 1000;
                long count = countFrom - seconds;

                timeTextView.setText(String.format("%ss", count));

                if (count > 0) {
                    getTimeHandler().postDelayed(this, 1000);
                } else if (onCountdownCompleteListener != null) {
                    stopwatchState.startOrPause();
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
    protected void startOrPause() {
        StopwatchState stopwatchState = getStopwatchState();

        if (stopwatchState.getRunningFor() / 1000 >= countFrom) {
            stopwatchState.reset();
        }

        super.startOrPause();
    }

    @Override
    protected void reset() {
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
}
