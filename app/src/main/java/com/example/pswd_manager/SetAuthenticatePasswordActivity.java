package com.example.pswd_manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetAuthenticatePasswordActivity extends Activity {
    private EditText pinNumber;
    private Button confirmPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_authenticate_password);
        confirmPin = findViewById(R.id.pin_btn);
        confirmPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinNumber = (EditText) findViewById(R.id.etPinNumber);
                String pin = pinNumber.getText().toString();
                if(!pin.equals("")){
                    SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                    SharedPreferences.Editor edits = sharedPreferences.edit();
                    edits.putString("pinNumber",pin);
                    edits.commit();
                    Toast.makeText(SetAuthenticatePasswordActivity.this, "Succeed set authenticate PIN number", Toast.LENGTH_SHORT).show();
                    String title = getIntent().getStringExtra("title");
                    if (title.equals("newAuthentication")){
                        Intent intent = new Intent(SetAuthenticatePasswordActivity.this, SetSafetyQuestionsActivity.class);
                        startActivity(intent);
                        SetAuthenticatePasswordActivity.this.finish();
                    }
                    else if (title.equals("resetAuthentication")){
                        Intent intent = new Intent(SetAuthenticatePasswordActivity.this,MainActivity.class);
                        startActivity(intent);
                        SetAuthenticatePasswordActivity.this.finish();
                    }
                }
                else {
                    Toast.makeText(SetAuthenticatePasswordActivity.this, "Empty input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
