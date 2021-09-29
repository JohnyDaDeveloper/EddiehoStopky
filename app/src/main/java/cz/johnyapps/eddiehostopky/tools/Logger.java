package cz.johnyapps.eddiehostopky.tools;

import android.util.Log;

import androidx.annotation.NonNull;

public class Logger {
    @NonNull
    private static final String PREFIX = "Logger: ";

    public static void v(@NonNull String TAG, @NonNull String message) {
        Log.v(PREFIX + TAG, message);
    }

    public static void v(@NonNull String TAG, @NonNull String message, @NonNull Object... params) {
        Log.v(PREFIX + TAG, String.format(message, params));
    }

    public static void d(@NonNull String TAG, @NonNull String message) {
        Log.d(PREFIX + TAG, message);
    }

    public static void d(@NonNull String TAG, @NonNull String message, @NonNull Exception e) {
        Log.d(PREFIX + TAG, message, e);
    }

    public static void d(@NonNull String TAG, @NonNull String message, @NonNull Object... params) {
        Log.d(PREFIX + TAG, String.format(message, params));
    }

    public static void d(@NonNull String TAG, @NonNull String message, @NonNull Exception e, @NonNull Object... params) {
        Log.d(PREFIX + TAG, String.format(message, params), e);
    }

    public static void i(@NonNull String TAG, @NonNull String message) {
        Log.i(PREFIX + TAG, message);
    }

    public static void i(@NonNull String TAG, @NonNull String message, @NonNull Object... params) {
        Log.i(PREFIX + TAG, String.format(message, params));
    }

    public static void w(@NonNull String TAG, @NonNull String message) {
        Log.w(PREFIX + TAG, message);
    }

    public static void w(@NonNull String TAG, @NonNull String message, @NonNull Exception e) {
        Log.w(PREFIX + TAG, message, e);
    }

    public static void w(@NonNull String TAG, @NonNull String message, @NonNull Object... params) {
        Log.w(PREFIX + TAG, String.format(message, params));
    }

    public static void w(@NonNull String TAG, @NonNull String message, @NonNull Exception e, @NonNull Object... params) {
        Log.w(PREFIX + TAG, String.format(message, params), e);
    }

    public static void e(@NonNull String TAG, @NonNull String message) {
        Log.e(PREFIX + TAG, message);
    }

    public static void e(@NonNull String TAG, @NonNull String message, @NonNull Exception e) {
        Log.e(PREFIX + TAG, message, e);
    }

    public static void e(@NonNull String TAG, @NonNull String message, @NonNull Object... params) {
        Log.e(PREFIX + TAG, String.format(message, params));
    }

    public static void e(@NonNull String TAG, @NonNull String message, @NonNull Exception e, @NonNull Object... params) {
        Log.e(PREFIX + TAG, String.format(message, params), e);
    }
}
