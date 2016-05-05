package thronlie.balinasoft.by.sacredheartschoolthronlie.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import thronlie.balinasoft.by.sacredheartschoolthronlie.R;

public class SplashScreenActivity extends Activity {

    private static final long SPLASH_SCREEN_DELAY = 1 * 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }

            }
        }, SPLASH_SCREEN_DELAY);
    }
}