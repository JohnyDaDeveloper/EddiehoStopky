package cz.johnyapps.eddiehostopky.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cz.johnyapps.eddiehostopky.R;
import cz.johnyapps.eddiehostopky.tools.SharedPrefsNames;
import cz.johnyapps.eddiehostopky.tools.SharedPrefsUtils;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences generalPrefs = SharedPrefsUtils.getGeneralPrefs(this);
        boolean show = generalPrefs.getBoolean(SharedPrefsNames.SHOW_APPRECIATION, true);

        if (show) {
            setContentView(R.layout.activiti_splash);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }, 3000);
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }
}
