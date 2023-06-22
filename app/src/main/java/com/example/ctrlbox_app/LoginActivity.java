package com.example.ctrlbox_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText txt_user;
    EditText txt_password;
    Button btn_login;
    RetrofitAPI retrofitAPI;
    TextView txt_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.10.76:5000/") // Replace with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitAPI = retrofit.create(RetrofitAPI.class);

        txt_user = findViewById(R.id.txt_user);
        txt_password = findViewById(R.id.txt_pass);
        txt_result = findViewById(R.id.txt_result);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txt_user.getText().toString();
                String password = txt_password.getText().toString();

                // Create the login request object
                LoginRequest loginRequest = new LoginRequest(username, password);

                // Make the API call
                Call<LoginResponse> call = retrofitAPI.login(loginRequest);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            // Login successful
                            LoginResponse loginResponse = response.body();
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            // Start the main activity or perform any other desired actions
                            Intent intent = new Intent(LoginActivity.this,SelectActivity.class);
                            startActivity(intent);
                            intent.putExtra("USERNAME",username);
                            startActivity(intent);
                            txt_password.setText("");
                        } else {
                            // Login failed
                            txt_password.setText("");
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // Handle the failure case
                        txt_result.setText(t.getMessage());
                    }
                });
            }
        });
    }
}