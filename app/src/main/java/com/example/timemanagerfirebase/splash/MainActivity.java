package com.example.timemanagerfirebase.splash;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.example.timemanagerfirebase.R;
import com.example.timemanagerfirebase.login.LoginActivity;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.Wave;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite foldingCube = new CubeGrid();
        progressBar.setIndeterminateDrawable(foldingCube);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent HomeIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(HomeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}