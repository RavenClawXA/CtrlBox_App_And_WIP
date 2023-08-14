package com.example.ctrlbox_app;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;
import java.util.SplittableRandom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendActivity extends AppCompatActivity {
    ImageView truck_imageC;
    RetrofitAPI retrofitAPI;
    Button backbtn, btn_out;
    Spinner spn_event;
    TextView date, status, txt_result_update, txt_result_add_log, textVendor, event,viewVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        //String[] vendorlist = {};

        date = findViewById(R.id.text_date);
        status = findViewById(R.id.text_status);
        spn_event = findViewById(R.id.spinner_event);
        //txt_result_update = findViewById(R.id.txt_result_update);
        txt_result_add_log = findViewById(R.id.txt_result_add_log);
        textVendor = findViewById(R.id.textVendor);
        event = findViewById(R.id.event);
        viewVendor = findViewById(R.id.viewVendor);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setBackground(getDrawable(R.drawable.button_color));
        btn_out = findViewById(R.id.btn_out);
        btn_out.setBackground(getDrawable(R.drawable.button_color));
        truck_imageC = findViewById(R.id.truckimage);

        TextView timeTextView = findViewById(R.id.clockView);
        TimeClock timeClock = new TimeClock(timeTextView);
        timeClock.start();

        Calendar calendar;
        calendar = Calendar.getInstance();
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        String datetime = iso8601Format.format(calendar.getTime());

        Intent rec = getIntent();
        String boxid = rec.getStringExtra("num_BoxId");
        String vendor = rec.getStringExtra("Vendor");

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.10.166:5000/ctrl/")
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<List<Datamodels_Vendors>> call8 = retrofitAPI.getAllVendor();
        call8.enqueue(new Callback<List<Datamodels_Vendors>>() {
            @Override
            public void onResponse(@NonNull Call<List<Datamodels_Vendors>> call, @NonNull Response<List<Datamodels_Vendors>> response) {
                List<Datamodels_Vendors> vendorlist = response.body();
                if (vendorlist != null && vendorlist.size() > 0) {
                    String[] vendors = new String[vendorlist.size()];
                    for (int i = 0; i < vendorlist.size(); i++) {
                        vendors[i] = vendorlist.get(i).getVendor();

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(SendActivity.this, android.R.layout.simple_list_item_activated_1, vendors);

                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                        spn_event.setAdapter(spinnerArrayAdapter);
                    }

                }
                viewVendor.setText("From");
                textVendor.setText(vendor);
                event.setText("Sendto :");

                if(vendor.equals("รอนำส่ง")){
                    textVendor.setText("PUR");
                }
                backbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (btn_out.getVisibility() == View.VISIBLE) {
                            onBackPressed();
                        } else {
                            Intent intent = new Intent(SendActivity.this, ScanActivity.class);
                            startActivity(intent);
                        }
                    }
                });

                truck_imageC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SendActivity.this,Addvendor.class);
                        startActivity(intent);

                    }
                });

                btn_out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        status.setText("Out");
                        date.setText(datetime);
                        Log.d("SendActiviy", "logid 1" + datetime);
                        //updateBoxTrans(boxid,textVendor.getText().toString(),"-",spn_event.getSelectedItem().toString(),date.getText().toString(),status.getText().toString());
                        addLogBox(boxid, "-", spn_event.getSelectedItem().toString(), status.getText().toString());
                        btn_out.setVisibility(View.INVISIBLE);
                    }


                   /* public void updateBoxTrans(String BoxId, String Vendor, String GetFrom, String SendTo, String TransDate, String TransType) {
                        Datamodels modal_updateBoxTrans = new Datamodels(BoxId, Vendor);

                        Call<List<Datamodels>> call3 = retrofitAPI.updateBoxTrans(boxid, modal_updateBoxTrans);
                        call3.enqueue(new Callback<List<Datamodels>>() {
                            @Override
                            public void onResponse(Call<List<Datamodels>> call, Response<List<Datamodels>> response) {
                                txt_result_update.setText("success updateBoxTrans: ");
                            }

                            @Override
                            public void onFailure(Call<List<Datamodels>> call, Throwable t) {
                                txt_result_update.setText("updateBoxTrans Error: " + t.getMessage());
                            }
                        });
                    }*/

                    public void addLogBox(String BoxId, String From, String To, String TransType) {
                        Datamodels_Logbox datamodels_logbox = new Datamodels_Logbox(BoxId, "PUR", To, TransType);
                        Call<Datamodels_Logbox> call6 = retrofitAPI.addLogBox(datamodels_logbox);

                        call6.enqueue(new Callback<Datamodels_Logbox>() {
                            @Override
                            public void onResponse(@NonNull Call<Datamodels_Logbox> call, @NonNull Response<Datamodels_Logbox> response) {
                                txt_result_add_log.setText("AddLogBox success"); // ประกาศข้างบนยังอ่ะ
                            }

                            @Override
                            public void onFailure(@NonNull Call<Datamodels_Logbox> call, @NonNull Throwable t) {
                                txt_result_add_log.setText("AddLogBox error: " + t.getMessage());
                            }
                        });

                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<Datamodels_Vendors>> call, @NonNull Throwable t) {

            }
        });

    }

}