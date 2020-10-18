package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.shopping_service.ShoppingService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myapplication.LoginActivity.db;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPassword;
    private EditText mFirstName;
    private EditText mLastName;
    private Button mRegisterBtn;
    Retrofit retrofit;

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
                retrofit = new Retrofit.Builder()
                        .baseUrl(ShoppingService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                        .build();

                //creating the api interface
                ShoppingService api = retrofit.create(ShoppingService.class);
                Call<String> call = api.sendRegisterinLogs(login);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String apiResponse = response.body();
                        if (response.code() == 200) {
                            Log.d("API Result:", "done");
                        } else {
                            Log.d("API Result:", "Error:" + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("API Result:", "Error:" + t.getMessage());
                    }
                });


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
