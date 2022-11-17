package com.abdullahqaisar.aesthetica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import com.abdullahqaisar.aesthetica.Fragments.CameraFragment;
import com.abdullahqaisar.aesthetica.Fragments.EditPictureFragment;
import com.abdullahqaisar.aesthetica.Fragments.WallpaperFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static Context context;

    private ChipNavigationBar chipNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        chipNavigationBar = findViewById(R.id.chipNavigation);
        chipNavigationBar.setItemSelected(R.id.editor, false);
        chipNavigationBar.setItemSelected(R.id.camera, false);
        chipNavigationBar.setItemSelected(R.id.wallpapers, true);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Fragment, new WallpaperFragment()).commit();

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.wallpapers:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Fragment, new WallpaperFragment()).commit();
                        break;
                    case R.id.camera:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Fragment, new CameraFragment()).commit();
                        break;
                    case R.id.editor:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Fragment, new EditPictureFragment()).commit();
                        break;
                }
            }
        });
    }


}