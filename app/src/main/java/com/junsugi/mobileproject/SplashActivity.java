package com.junsugi.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        Handler hd = new Handler();
        hd.postDelayed(new Splashhandler(), 2000); // 1초 후에 hd handler 실행  2000ms = 2초
    }

    private class Splashhandler implements Runnable{
        public void run(){
            //최초 한 번만 실행하도록 설정하는 부분
            prefs = getSharedPreferences("checkFirst", MODE_PRIVATE);
            boolean checkFirst = prefs.getBoolean("checkFirst", true);
            if (checkFirst) {
                prefs.edit().putBoolean("checkFirst", false).apply();
                Intent newIntent = new Intent(getApplicationContext(), GuideActivity.class);
                startActivity(newIntent);
                finish();
            } else {
                Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(newIntent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

}
