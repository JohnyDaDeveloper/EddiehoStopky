package cz.johnyapps.eddiehostopky.settings;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public abstract class SettingItem {
    private final int id;
    private final boolean isCategoryTitle;
    @StringRes
    private final int titleId;


    public SettingItem(int id, @StringRes int titleId, boolean isCategoryTitle) {
        this.id = id;
        this.titleId = titleId;
        this.isCategoryTitle = isCategoryTitle;
    }

    @Nullable
    public abstract String getTextValue(@NonNull Context context);

    public abstract void onClick(@NonNull Context context);

    public void click(@NonNull Context context) {
        onClick(context);
    }

    public int getId() {
        return id;
    }

    public boolean isCategoryTitle() {
        return isCategoryTitle;
    }

    @NonNull
    public String getTitle(@NonNull Context context) {
        return context.getResources().getString(titleId);
    }
}
