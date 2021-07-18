package cz.johnyapps.eddiehostopky.settings.setting;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import cz.johnyapps.eddiehostopky.R;
import cz.johnyapps.eddiehostopky.settings.OnSettingChangedListener;
import cz.johnyapps.eddiehostopky.settings.Setting;
import cz.johnyapps.eddiehostopky.settings.SettingChoice;

public class SingleChoiceSetting<ITEM extends SettingChoice> extends Setting<ITEM> {
    @NonNull
    private final ITEM[] items;

    public SingleChoiceSetting(int id,
                               @StringRes int titleId,
                               @NonNull ITEM[] items,
                               @NonNull ITEM selected,
                               @Nullable OnSettingChangedListener<ITEM> onSettingChangedListener) {
        super(id, titleId, selected, onSettingChangedListener);
        this.items = items;
    }

    @Nullable
    @Override
    public String valueToText(@Nullable ITEM item, @NonNull Context context) {
        return item == null ? null : item.getTitle(context);
    }

    @Override
    public void onClick(@NonNull Context context) {
        String[] titles = new String[items.length];
        ITEM selected = getValue();
        int selectedPos = 0;

        for (int i = 0; i < titles.length; i++) {
            ITEM item = items[i];
            titles[i] = item.getTitle(context);

            if (selected != null && item.getId() == selected.getId()) {
                selectedPos = i;
            }
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(getTitle(context))
                .setSingleChoiceItems(titles, selectedPos, (dialog, which) -> {
                    setValue(items[which]);
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {

                })
                .create().show();
    }
}
