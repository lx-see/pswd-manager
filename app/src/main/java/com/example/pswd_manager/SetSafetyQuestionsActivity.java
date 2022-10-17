package com.example.pswd_manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetSafetyQuestionsActivity extends Activity {
    private EditText birthdayMonth;
    private EditText fatherBirthdayMonth;
    private EditText motherBirthdayMonth;
    private EditText favouriteColor;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_safety_questions);
        confirmBtn = findViewById(R.id.safety_question_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthdayMonth = (EditText) findViewById(R.id.birthdayMonth);
                String birthdayMonth_txt = birthdayMonth.getText().toString();
                fatherBirthdayMonth = (EditText) findViewById(R.id.fatherBirthdayMonth);
                String fatherBirthdayMonth_txt = fatherBirthdayMonth.getText().toString();
                motherBirthdayMonth = (EditText) findViewById(R.id.motherBirthdayMonth);
                String motherBirthdayMonth_txt = motherBirthdayMonth.getText().toString();
                favouriteColor = (EditText) findViewById(R.id.favouriteColor);
                String favouriteColor_txt = favouriteColor.getText().toString();

                if (birthdayMonth_txt.equals("") || fatherBirthdayMonth_txt.equals("") || motherBirthdayMonth_txt.equals("") || favouriteColor_txt.equals("")) {
                    Toast.makeText(SetSafetyQuestionsActivity.this, "Empty input", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                    SharedPreferences.Editor edits = sharedPreferences.edit();
                    edits.putString("birthdayMonth",birthdayMonth_txt);
                    edits.putString("fatherBirthdayMonth",fatherBirthdayMonth_txt);
                    edits.putString("motherBirthdayMonth",motherBirthdayMonth_txt);
                    edits.putString("favouriteColor",favouriteColor_txt);
                    edits.commit();
                    Toast.makeText(SetSafetyQuestionsActivity.this, "Succeed set safety questions", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SetSafetyQuestionsActivity.this,MainActivity.class);
                    startActivity(intent);
                    SetSafetyQuestionsActivity.this.finish();
                }
            }
        });
    }
}
