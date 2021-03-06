package cz.johnyapps.eddiehostopky.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.johnyapps.eddiehostopky.R;
import cz.johnyapps.eddiehostopky.StopwatchState;

public class StopwatchView extends LinearLayout {
    private boolean vertical = true;
    private boolean hideStartPauseButton = false;
    private boolean reversedButtons = false;
    @NonNull
    private String label = null;

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
    @Nullable
    private StopwatchState.OnRestartListener onRestartListener;

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
        startPauseStopwatchButton.setVisibility(hideStartPauseButton ? GONE : VISIBLE);

        restartStopwatchButton = findViewById(R.id.restartStopwatchButton);
        restartStopwatchButton.setOnClickListener(v -> reset());

        stopwatchState.setOnRunningListener(onRunningListener);

        if (reversedButtons) {
            reverseLayout();
        }
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

    public void reset() {
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
        stopwatchState.setOnRestartListener(onRestartListener);

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

    public void setOnRestartListener(@Nullable StopwatchState.OnRestartListener onRestartListener) {
        this.onRestartListener = onRestartListener;

        if (stopwatchState != null) {
            stopwatchState.setOnRestartListener(onRestartListener);
        }
    }

    public void setHideStartPauseButton(boolean hideStartPauseButton) {
        this.hideStartPauseButton = hideStartPauseButton;

        if (startPauseStopwatchButton != null) {
            startPauseStopwatchButton.setVisibility(hideStartPauseButton ? GONE : VISIBLE);
        }
    }

    public boolean isHideStartPauseButton() {
        return hideStartPauseButton;
    }

    public void setReversedButtons(boolean reversedButtons) {
        boolean prevValue = this.reversedButtons;
        this.reversedButtons = reversedButtons;

        if (prevValue != reversedButtons) {
            reverseLayout();
        }
    }

    private void reverseLayout() {
        if (!vertical) {
            removeAllViews();
            if (reversedButtons) {
                LayoutInflater.from(getContext()).inflate(R.layout.view_stopwatch_horizontal_reversed, this, true);
            } else {
                LayoutInflater.from(getContext()).inflate(R.layout.view_stopwatch_horizontal, this, true);
            }

            AppCompatTextView labelTextView = findViewById(R.id.labelTextView);
            labelTextView.setText(label);

            timeTextView = findViewById(R.id.timeTextView);
            timeTextView.setText(getResources().getString(R.string.stopwatchView_default));

            startPauseStopwatchButton = findViewById(R.id.startPauseStopwatchButton);
            startPauseStopwatchButton.setOnClickListener(v -> startOrPause());
            startPauseStopwatchButton.setVisibility(hideStartPauseButton ? GONE : VISIBLE);

            restartStopwatchButton = findViewById(R.id.restartStopwatchButton);
            restartStopwatchButton.setOnClickListener(v -> reset());
        } else {
            LinearLayout mainLayout = findViewById(R.id.buttonsLayout);

            if (mainLayout != null) {
                List<View> views = new ArrayList<>();

                for (int i = 0; i < mainLayout.getChildCount(); i++) {
                    views.add(mainLayout.getChildAt(0));
                }

                mainLayout.removeAllViews();

                for (View view : views) {
                    mainLayout.addView(view);
                }
            }
        }
    }
}
