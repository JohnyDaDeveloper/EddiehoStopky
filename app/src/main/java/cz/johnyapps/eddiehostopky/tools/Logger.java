package cz.johnyapps.eddiehostopky.tools;

import android.util.Log;

import androidx.annotation.NonNull;

public class Logger {
    public static void v(@NonNull String TAG, @NonNull String message) {
        Log.v(TAG, message);
    }

    public static void v(@NonNull String TAG, @NonNull String message, @NonNull Object... params) {
        Log.v(TAG, String.format(message, params));
    }

    public static void d(@NonNull String TAG, @NonNull String message) {
        Log.d(TAG, message);
    }

    public static void d(@NonNull String TAG, @NonNull String message, @NonNull Exception e) {
        Log.d(TAG, message, e);
    }

    public static void d(@NonNull String TAG, @NonNull String message, @NonNull Object... params) {
        Log.d(TAG, String.format(message, params));
    }

    public static void d(@NonNull String TAG, @NonNull String message, @NonNull Exception e, @NonNull Object... params) {
        Log.d(TAG, String.format(message, params), e);
    }

    public static void i(@NonNull String TAG, @NonNull String message) {
        Log.i(TAG, message);
    }

    public static void i(@NonNull String TAG, @NonNull String message, @NonNull Object... params) {
        Log.i(TAG, String.format(message, params));
    }

    public static void w(@NonNull String TAG, @NonNull String message) {
        Log.w(TAG, message);
    }

    public static void w(@NonNull String TAG, @NonNull String message, @NonNull Exception e) {
        Log.w(TAG, message, e);
    }

    public static void w(@NonNull String TAG, @NonNull String message, @NonNull Object... params) {
        Log.w(TAG, String.format(message, params));
    }

    public static void w(@NonNull String TAG, @NonNull String message, @NonNull Exception e, @NonNull Object... params) {
        Log.w(TAG, String.format(message, params), e);
    }

    public static void e(@NonNull String TAG, @NonNull String message) {
        Log.e(TAG, message);
    }

    public static void e(@NonNull String TAG, @NonNull String message, @NonNull Exception e) {
        Log.e(TAG, message, e);
    }

    public static void e(@NonNull String TAG, @NonNull String message, @NonNull Object... params) {
        Log.e(TAG, String.format(message, params));
    }

    public static void e(@NonNull String TAG, @NonNull String message, @NonNull Exception e, @NonNull Object... params) {
        Log.e(TAG, String.format(message, params), e);
    }
}
