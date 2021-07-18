package cz.johnyapps.eddiehostopky.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;

import cz.johnyapps.eddiehostopky.R;

public abstract class NavigationActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        toolbar = findViewById(R.id.toolbar);

        setupToolbar();
        setupNavigation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, getNavController()) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(getNavController(), getAppBarConfiguration()) || super.onSupportNavigateUp();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected NavController getNavController() {
        return Navigation.findNavController(this, R.id.navHostFragment);
    }

    private AppBarConfiguration getAppBarConfiguration() {
        return new AppBarConfiguration.Builder(getNavController().getGraph())
                .build();
    }

    private void setupNavigation() {
        NavController navController = getNavController();
        NavigationUI.setupWithNavController(toolbar, navController);
    }

    private void setupToolbar() {
        NavigationUI.setupWithNavController(toolbar, getNavController(), getAppBarConfiguration());
        setSupportActionBar(toolbar);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
