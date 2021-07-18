package cz.johnyapps.eddiehostopky.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import java.util.Locale;

import cz.johnyapps.eddiehostopky.R;
import cz.johnyapps.eddiehostopky.StopwatchState;
import cz.johnyapps.eddiehostopky.tools.Logger;


public class StopwatchView extends LinearLayout {
    private boolean vertical = true;

    protected AppCompatTextView timeTextView;
    protected AppCompatImageView startPauseStopwatchButton;
    protected AppCompatImageView restartStopwatchButton;

    @NonNull
    private StopwatchState stopwatchState = new StopwatchState();
    @NonNull
    private final Handler timeHandler = new Handler();
    @NonNull
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (stopwatchState.isRunning()) {
                drawFrame();
                timeHandler.postDelayed(this, 0);
            }
        }
    };
    @Nullable
    private StopwatchState.OnRunningListener otherOnRunningListener;
    @NonNull
    private final StopwatchState.OnRunningListener onRunningListener = running -> {
        Drawable drawable;

        if (running) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pause, getContext().getTheme());

            if (vertical) {
                restartStopwatchButton.setVisibility(GONE);
            }

            timeHandler.postDelayed(getRunnable(), 0);
        } else {
            drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.play, getContext().getTheme());
            restartStopwatchButton.setVisibility(VISIBLE);
        }

        startPauseStopwatchButton.setImageDrawable(drawable);

        if (otherOnRunningListener != null) {
            otherOnRunningListener.running(running);
        }
    };

    public StopwatchView(Context context) {
        super(context);
        init(null, 0);
    }

    public StopwatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public StopwatchView(Context context, AttributeSet attrs, int theme) {
        super(context, attrs, theme);
        init(attrs, theme);
    }

    public void init(@Nullable AttributeSet attrs, int theme) {
        String label = null;

        if (attrs != null) {
            final TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.StopwatchView, theme, 0);

            label = array.getString(R.styleable.StopwatchView_label);
            int orientation = array.getInt(R.styleable.StopwatchView_android_orientation, 1);
            vertical = orientation == 1;

            array.recycle();
        }

        if (vertical) {
            LayoutInflater.from(getContext()).inflate(R.layout.view_stopwatch_vertical, this, true);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.view_stopwatch_horizontal, this, true);
        }


        AppCompatTextView labelTextView = findViewById(R.id.labelTextView);
        labelTextView.setText(label);

        timeTextView = findViewById(R.id.timeTextView);
        timeTextView.setText(getResources().getString(R.string.stopwatchView_default));

        startPauseStopwatchButton = findViewById(R.id.startPauseStopwatchButton);
        startPauseStopwatchButton.setOnClickListener(v -> startOrPause());

        restartStopwatchButton = findViewById(R.id.restartStopwatchButton);
        restartStopwatchButton.setOnClickListener(v -> reset());

        stopwatchState.setOnRunningListener(onRunningListener);
    }

    protected long drawFrame() {
        if (stopwatchState != null) {
            long hundredths = (stopwatchState.getRunningFor()) / 10;
            long seconds = hundredths / 100;
            long minutes = seconds / 60;
            hundredths %= 100;
            seconds %= 60;

            timeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, hundredths));
        }

        return 0;
    }

    protected void startOrPause() {
        stopwatchState.startOrPause();
    }

    protected void reset() {
        stopwatchState.reset();
        timeTextView.setText(getResources().getString(R.string.stopwatchView_default));
        startPauseStopwatchButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.play, getContext().getTheme()));
    }

    @NonNull
    public StopwatchState getStopwatchState() {
        return stopwatchState;
    }

    public void setStopwatchState(@NonNull StopwatchState stopwatchState) {
        this.stopwatchState = stopwatchState;

        stopwatchState.setOnRunningListener(onRunningListener);

        if (stopwatchState.isRunning()) {
            timeHandler.postDelayed(getRunnable(), 0);
        } else {
            drawFrame();
        }
    }

    @NonNull
    public Runnable getRunnable() {
        return runnable;
    }

    @NonNull
    public Handler getTimeHandler() {
        return timeHandler;
    }

    public void setOnRunningListener(@Nullable StopwatchState.OnRunningListener onRunningListener) {
        this.otherOnRunningListener = onRunningListener;
    }
}
