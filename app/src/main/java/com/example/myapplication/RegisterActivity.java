package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import static com.example.myapplication.LoginActivity.db;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPassword;
    private EditText mFirstName;
    private EditText mLastName;
    private Button mRegisterBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirstName = findViewById(R.id.edt_firstname);
        mLastName = findViewById(R.id.edt_lastname);
        mEmail = findViewById(R.id.edt_email_reg);
        mPassword = findViewById(R.id.edt_password_reg);
        mRegisterBtn = findViewById(R.id.btn_register);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Login login = new Login(mFirstName.getText().toString(),mLastName.getText().toString(),mEmail.getText().toString(),mPassword.getText().toString());

                Thread save = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.loginDao().insertUser(login);
                        List<Login> temp = db.loginDao().getAllUsers();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(RegisterActivity.this,
                                        "New User registered", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
                save.start();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
