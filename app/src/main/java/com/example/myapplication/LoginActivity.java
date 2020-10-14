package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "login")
class Login
{
    @PrimaryKey
    @NonNull
    public String email;
    @ColumnInfo
    public String firstname;
    @ColumnInfo
    public String lastname;
    @ColumnInfo
    public String password;
}
@Dao
interface LoginDao
{
//    @Query("SELECT * FROM user")
//    List<User> getAll();
//
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);
//
//    @Insert
//    void insertAll(User... users);
//
//    @Delete
//    void delete(User user);
    @Query("SELECT * from login")
    ArrayList<Login> getAllUsers();

    @Query("SELECT * FROM login WHERE email LIKE :n LIMIT 1")
    Login findByName(String n);

    @Insert
    void insertUser(Login user);

    //@Query("SELECT * FROM login WHERE ")
}

@Database(entities = {LoginActivity.class}, version = 1)
abstract class AppDatabase extends RoomDatabase {
    public abstract LoginDao loginDao();
}

public class LoginActivity extends AppCompatActivity {

    private MyApplication myApplication;
    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginBtn;
    private String userEmail;
    private String userPassword;

    AppDatabase db = Room.databaseBuilder(getApplicationContext(),
            AppDatabase.class, "shopping-app").build();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myApplication = (MyApplication) getApplication();
        mEmail = findViewById(R.id.edt_email);
        mPassword = findViewById(R.id.edt_password);
        mLoginBtn = findViewById(R.id.btn_login);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate
                // shared pref
                // scrolling activity
                if(attemptLogin()) {
                    Intent intent = new Intent(LoginActivity.this, ScrollingActivity.class);
                    startActivity(intent);
                    finish();
                } else if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword) && !myApplication.doesUserEmailExist(userEmail)) {
                    myApplication.setUserInfo(userEmail, userPassword);
                } else {
                    // display error
                    Toast.makeText(LoginActivity.this,
                            "Please check your login credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean attemptLogin() {
        mEmail.setError(null);
        mPassword.setError(null);

        userEmail = mEmail.getText().toString();
        userPassword = mPassword.getText().toString();


        if(TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
            return false;
        } else {
            return checkLoginInfo(userEmail, userPassword);
        }
    }

    private boolean checkLoginInfo(String email, String password) {

        String userPassword = myApplication.getUserInfo(email);

        return password.equals(userPassword);
    }
}
