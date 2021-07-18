package cz.johnyapps.eddiehostopky.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cz.johnyapps.eddiehostopky.MainViewModel;
import cz.johnyapps.eddiehostopky.R;

public class SettingsFragment extends Fragment {
    private MainViewModel viewModel;
    private SettingsAdapter settingsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        setupSettingsRecycler(root);
        setupObservers();
        return root;
    }

    private void setupViewModel() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(MainViewModel.class);
    }

    private void setupObservers() {
        viewModel.getSettings().observe(getViewLifecycleOwner(), settingsItems -> settingsAdapter.update(settingsItems));
        viewModel.getLastChangedSetting().observe(getViewLifecycleOwner(), settingItem -> settingsAdapter.update(settingItem));
    }

    private void setupSettingsRecycler(@NonNull View root) {
        settingsAdapter = new SettingsAdapter(root.getContext(), viewModel.getSettings().getValue());

        RecyclerView settingsRecyclerView = root.findViewById(R.id.settingsRecyclerView);
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        settingsRecyclerView.setAdapter(settingsAdapter);
    }
}
