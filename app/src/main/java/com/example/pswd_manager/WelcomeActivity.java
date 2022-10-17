package com.example.pswd_manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                String pinNum = sharedPreferences.getString("pinNumber", "");
                if (pinNum.equals("")){
                    Intent intent = new Intent(WelcomeActivity.this, SetAuthenticatePasswordActivity.class);
                    intent.putExtra("title","newAuthentication");
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
                else{
                    Intent intent = new Intent(WelcomeActivity.this,AuthenticateActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
            }
        }, 3000);
    }
}
