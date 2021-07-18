package cz.johnyapps.eddiehostopky.activities;

import android.os.Bundle;

import cz.johnyapps.eddiehostopky.R;

public class MainActivity extends NavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}