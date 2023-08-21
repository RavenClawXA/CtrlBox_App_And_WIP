package com.example.ctrlbox_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.Obfuscator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Addvendor extends AppCompatActivity {

    private RetrofitAPI retrofitAPI;
    Button add_btn, bbtn;
    EditText textVendor,text_vendorname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvendor);

        textVendor = findViewById(R.id.textVendor);
        text_vendorname = findViewById(R.id.text_vendorname);

        add_btn = findViewById(R.id.addvendor_btn);
        bbtn = findViewById(R.id.bbtn);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://49.0.65.4:3002/ctrl/")
        //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.10.166:5000/ctrl/")
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Addvendor(textVendor.getText().toString(),text_vendorname.getText().toString());
            }
        });

        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void Addvendor(String Vendor,String VendorName){
        Datamodels_Vendors datamodels_vendors = new Datamodels_Vendors(Vendor, VendorName);
        Call<Datamodels_Vendors> call = retrofitAPI.addVendor(datamodels_vendors);
        call.enqueue(new Callback<Datamodels_Vendors>() {
            @Override
            public void onResponse(Call<Datamodels_Vendors> call, Response<Datamodels_Vendors> response) {
                Toast.makeText(Addvendor.this, "Add Vendor Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Datamodels_Vendors> call, Throwable t) {
                Toast.makeText(Addvendor.this, "Add Vendor Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }
}