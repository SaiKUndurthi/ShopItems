package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private MyApplication myApplication;
    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginBtn;
    private Button mRegister;
    private String userEmail;
    private String userPassword;

    public static AppDatabase db;
    private List<Login> tempList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myApplication = (MyApplication) getApplication();
        mEmail = findViewById(R.id.edt_email);
        mPassword = findViewById(R.id.edt_password);
        mLoginBtn = findViewById(R.id.btn_login);
        mRegister = findViewById(R.id.btn_signup);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "shopping-app").build();

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate
                // shared pref
                // scrolling activity
                userEmail = mEmail.getText().toString();
                userPassword = mPassword.getText().toString();
                final Login login = new Login(null,null,userEmail,userPassword);

                attemptLogin(login) ;


            }
        });

//        mLoginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // validate
//                // shared pref
//                // scrolling activity
//                if(attemptLogin()) {
//                    Intent intent = new Intent(LoginActivity.this, ScrollingActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword) && !myApplication.doesUserEmailExist(userEmail)) {
//                    myApplication.setUserInfo(userEmail, userPassword);
//                } else {
//                    // display error
//                    Toast.makeText(LoginActivity.this,
//                            "Please check your login credentials.", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }

    private void attemptLogin(final Login login) {
        mEmail.setError(null);
        mPassword.setError(null);

//        userEmail = mEmail.getText().toString();
//        userPassword = mPassword.getText().toString();


        Thread save = new Thread(new Runnable() {
            @Override
            public void run() {
                tempList = db.loginDao().getAllUsers();
//                Login found = db.loginDao().findByName(login.getEmail());
//                if(found == null && !TextUtils.isEmpty(login.getEmail()) && !TextUtils.isEmpty(login.getPassword()) && !checkLoginInfo(login.getEmail(),login.getPassword())) {
//                    Thread save = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            db.loginDao().insertUser(login);
//
//                            Intent intent = new Intent(LoginActivity.this, ScrollingActivity.class);
//                            startActivity(intent);
//                            Toast.makeText(LoginActivity.this,
//                                    "New User registered", Toast.LENGTH_LONG).show();
//                            finish();
//                        }
//                    });
//                    save.start();
//
//                    //CODE WITHOUT DB
//                    //myApplication.setUserInfo(userEmail, userPassword);
////                }else if( TextUtils.isEmpty(found.email) || TextUtils.isEmpty(found.password) ){
////                    Toast.makeText(LoginActivity.this,
////                            "Credentials can't be empty.", Toast.LENGTH_LONG).show();
////                }
//                }else
                if(checkLoginInfo(tempList,login.getEmail(),login.getPassword())){
                    Intent intent = new Intent(LoginActivity.this, ScrollingActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    // display error
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,
                                    "Please check your login credentials.", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        });
        save.start();



// CODE WITHOUT DATABASE
//        if(TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
//            return false;
//        } else {
//            return checkLoginInfo(userEmail, userPassword);
//        }
    }

    private boolean checkLoginInfo(List<Login>list, String email, String password) {

            if(list.isEmpty()) return false;
            int temp =list.size();

            for(int i = 0; i < list.size(); i++){
                if(email.equals(list.get(i).getEmail())){
                    list.get(i);
                    return password.equals(list.get(i).getPassword());
                }
            }
            return false;
        }
}
