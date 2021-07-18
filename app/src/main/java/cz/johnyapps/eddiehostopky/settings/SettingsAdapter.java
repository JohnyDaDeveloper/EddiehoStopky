package cz.johnyapps.eddiehostopky.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cz.johnyapps.eddiehostopky.R;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {
    private static final int VIEW_TYPE_CATEGORY_TITLE = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    @NonNull
    private final LayoutInflater inflater;
    @NonNull
    private List<SettingItem> settings;

    public SettingsAdapter(@NonNull Context context, @Nullable List<SettingItem> settings) {
        this.inflater = LayoutInflater.from(context);
        this.settings = settings == null ? new ArrayList<>() : settings;
    }

    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root;

        if (viewType == VIEW_TYPE_CATEGORY_TITLE) {
            root = inflater.inflate(R.layout.item_settings_category, parent, false);
        } else {
            root = inflater.inflate(R.layout.item_settings, parent, false);
        }

        return new SettingsViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
        SettingItem settingItem = settings.get(position);
        holder.getTitleTextView().setText(settingItem.getTitle(holder.itemView.getContext()));

        if (!settingItem.isCategoryTitle()) {
            AppCompatTextView valueTextView = holder.getValueTextView();
            valueTextView.setText(settingItem.getTextValue(valueTextView.getContext()));
        }

        holder.setDividerVisibility();
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    @Override
    public int getItemViewType(int position) {
        return settings.get(position).isCategoryTitle() ? VIEW_TYPE_CATEGORY_TITLE : VIEW_TYPE_ITEM;
    }

    public void update(@Nullable List<SettingItem> settings) {
        this.settings = settings == null ? new ArrayList<>() : settings;
        notifyDataSetChanged();
    }

    public void update(@Nullable SettingItem settingItem) {
        if (settingItem != null) {
            int i = 0;

            for (SettingItem item : this.settings) {
                if (item.getId() == settingItem.getId()) {
                    notifyItemChanged(i);
                    return;
                }

                i++;
            }
        }

        notifyDataSetChanged();
    }

    public class SettingsViewHolder extends RecyclerView.ViewHolder {
        public SettingsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> settings.get(getAdapterPosition()).click(v.getContext()));
        }

        public void setDividerVisibility() {
            View divider = itemView.findViewById(R.id.divider);
            if (divider != null) {
                int pos = getAdapterPosition();

                if (pos + 1 >= settings.size() || settings.get(pos + 1).isCategoryTitle()) {
                    divider.setVisibility(View.VISIBLE);
                } else {
                    divider.setVisibility(View.GONE);
                }
            }
        }

        public AppCompatTextView getTitleTextView() {
            return itemView.findViewById(R.id.settingsItemTitleTextView);
        }

        public AppCompatTextView getValueTextView() {
            return itemView.findViewById(R.id.settingsItemValueTextView);
        }
    }
}
