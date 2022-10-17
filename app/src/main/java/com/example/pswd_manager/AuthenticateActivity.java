package com.example.pswd_manager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

public class AuthenticateActivity extends Activity {
    private EditText pinNumber;
    private Button confirmPin;
    private Button resetPasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        confirmPin = findViewById(R.id.pin_btn);
        confirmPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinNumber = (EditText) findViewById(R.id.etPinNumber);
                String pin = pinNumber.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                String pinNum = sharedPreferences.getString("pinNumber", "");

                if(!pin.equals("")){
                    if(pin.equals(pinNum)){
                        Toast.makeText(AuthenticateActivity.this, "Succeed authenticated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AuthenticateActivity.this,MainActivity.class);
                        startActivity(intent);
                        AuthenticateActivity.this.finish();
                    }
                    else {
                        Toast.makeText(AuthenticateActivity.this, "Failed authenticated", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AuthenticateActivity.this, "Empty input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetPasswordBtn = findViewById(R.id.reset_password_btn);
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getApplication());
                View authenticateView = inflater.inflate(R.layout.dialog_safety_authentication, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AuthenticateActivity.this)
                        .setTitle("Authenticate User")
                        .setView(authenticateView)
                        .setPositiveButton("Confirm", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EditText birthdayMonth = (EditText) authenticateView.findViewById(R.id.birthdayMonth);
                                String birthdayMonth_txt = birthdayMonth.getText().toString();
                                EditText fatherBirthdayMonth = (EditText) authenticateView.findViewById(R.id.fatherBirthdayMonth);
                                String fatherBirthdayMonth_txt = fatherBirthdayMonth.getText().toString();
                                EditText motherBirthdayMonth = (EditText) authenticateView.findViewById(R.id.motherBirthdayMonth);
                                String motherBirthdayMonth_txt = motherBirthdayMonth.getText().toString();
                                EditText favouriteColor = (EditText) authenticateView.findViewById(R.id.favouriteColor);
                                String favouriteColor_txt = favouriteColor.getText().toString();

                                if (birthdayMonth_txt.equals("") || fatherBirthdayMonth_txt.equals("") || motherBirthdayMonth_txt.equals("") || favouriteColor_txt.equals("")) {
                                    Toast.makeText(AuthenticateActivity.this, "Empty input", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                                    String birthdayMonth_saved = sharedPreferences.getString("birthdayMonth", "");
                                    String fatherBirthdayMonth_saved = sharedPreferences.getString("fatherBirthdayMonth", "");
                                    String motherBirthdayMonth_saved = sharedPreferences.getString("motherBirthdayMonth", "");
                                    String favouriteColor_saved = sharedPreferences.getString("favouriteColor", "");

                                    if(birthdayMonth_txt.equals(birthdayMonth_saved) && fatherBirthdayMonth_txt.equals(fatherBirthdayMonth_saved) && motherBirthdayMonth_txt.equals(motherBirthdayMonth_saved) && favouriteColor_txt.equals(favouriteColor_saved)){
                                        Toast.makeText(AuthenticateActivity.this, "Succeed authenticated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AuthenticateActivity.this, SetAuthenticatePasswordActivity.class);
                                        intent.putExtra("title","resetAuthentication");
                                        startActivity(intent);
                                        AuthenticateActivity.this.finish();
                                    }
                                    else {
                                        Toast.makeText(AuthenticateActivity.this, "Failed authenticated", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            }
                        });
            }
        });
    }
}
