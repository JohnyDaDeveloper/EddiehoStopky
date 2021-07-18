package cz.johnyapps.eddiehostopky.tools;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SharedPrefsUtils {
    @NonNull
    public static SharedPreferences getGeneralPrefs(@NonNull Context context) {
        return context.getSharedPreferences(SharedPrefsNames.GENERAL, Context.MODE_PRIVATE);
    }
}
