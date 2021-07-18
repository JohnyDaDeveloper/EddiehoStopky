package cz.johnyapps.eddiehostopky.settings;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class CategoryTitle extends SettingItem {
    public CategoryTitle(@StringRes int titleId) {
        super(SettingIds.CATEGORY_TITLE, titleId, true);
    }

    @Nullable
    @Override
    public String getTextValue(@NonNull Context context) {
        return null;
    }

    @Override
    public void onClick(@NonNull Context context) {

    }
}
