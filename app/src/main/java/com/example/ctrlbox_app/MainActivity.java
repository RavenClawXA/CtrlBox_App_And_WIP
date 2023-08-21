package com.example.ctrlbox_app;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.Obfuscator;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView txt_boxid, txt_date, txt_status, txt_result_add, txt_result_add_log, txt_result_update, sendto, textVendor, textTo, viewVendor;

    private RetrofitAPI retrofitAPI;
    private Button btn_in;
    private Button btn_out;
    private Button btn_add;
    private Button btn_pur;
    private Spinner spn_event;

    @SuppressLint({"MissingInflatedId", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewVendor = findViewById(R.id.viewVendor);
        txt_boxid = findViewById(R.id.text_boxid);
        textVendor = findViewById(R.id.textVendor);
        textTo = findViewById(R.id.textTo);
        txt_date = findViewById(R.id.text_date);
        txt_status = findViewById(R.id.text_status);
        txt_result_add = findViewById(R.id.txt_result_add);
        txt_result_add_log = findViewById(R.id.txt_result_add_log);
        txt_result_update = findViewById(R.id.txt_result_update);
        sendto = findViewById(R.id.sendto);
        ImageView box_img = findViewById(R.id.imagebox);
        TextView timeTextView = findViewById(R.id.clockView);
        TimeClock timeClock = new TimeClock(timeTextView);
        spn_event = findViewById(R.id.spinner_event);
        timeClock.start();

        final String num_BoxId = (getIntent().getStringExtra("ScannedData"));
        txt_boxid.setText(num_BoxId);

        Calendar calendar;
        calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        String datetime = iso8601Format.format(calendar.getTime());


        Button bbtn = findViewById(R.id.Backbtn);
        bbtn.setBackground(getDrawable(R.drawable.button_color));
        btn_in = findViewById(R.id.btn_in);
        btn_in.setBackground(getDrawable(R.drawable.button_color));
        btn_out = findViewById(R.id.btn_out);
        btn_out.setBackground(getDrawable(R.drawable.button_color));
        btn_add = findViewById(R.id.addbtn);
        btn_add.setBackground(getDrawable(R.drawable.button_color));
        btn_pur = findViewById(R.id.btn_pur);
        btn_pur.setBackground(getDrawable(R.drawable.button_color));

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://49.0.65.4:3002/ctrl/")
        //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.10.166:5000/ctrl/")
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<List<Datamodels>> call = retrofitAPI.getActiveLogbox(num_BoxId);

        call.enqueue(new Callback<List<Datamodels>>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<Datamodels>> call, @NonNull Response<List<Datamodels>> response) {

                List<Datamodels> datamodels = response.body();

                //Datamodels foundDatamodel = null ;
                // Log.d("", "Logcess51 " + get.getBoxId() + " " + get.getVendor() + " " + get.getTransDate() + " " + get.getTransType());
                //foundDatamodel = (Datamodels) datamodels;

                if (datamodels != null && datamodels.size() > 0) {
                    Datamodels foundDatamodel = datamodels.get(0);
                    Log.d("", "Logcess52 " + foundDatamodel.getBoxId() + " " + foundDatamodel.getTransDate() + " " + foundDatamodel.getTransType());
                    txt_date.setText(foundDatamodel.getTransDate());
                    txt_status.setText(foundDatamodel.getTransType());

                    if (foundDatamodel.getTransType().equals("In")
                            || foundDatamodel.getTransType().equals("Out")
                            || foundDatamodel.getTransType().equals("Reg")
                            && foundDatamodel.getGetFrom().equals("CYF")
                            || foundDatamodel.getGetFrom().equals("PUR"))
                    {
                        Log.d("", "Logcesstest "+"เข้า1");


                        if (foundDatamodel.getTransType().equals("Reg")&&foundDatamodel.getGetFrom().equals("CYF")) {

                            Log.d("", "Logcesstest "+"เข้า2");//เจอกล่องข้างในส่งออกได้
                            Showboxowner(num_BoxId);
                            //textVendor.setText(foundDatamodel.getGetFrom());
                            textTo.setText(foundDatamodel.getGetFrom());
                            btn_add.setVisibility(View.INVISIBLE);
                            btn_in.setVisibility(View.INVISIBLE);
                            viewVendor.setText("Vendor");

                            sendto.setText("GetFrom :");
                            if (foundDatamodel.getTransType().equals("Reg")) {
                                Log.d("", "Logcesstest "+"เข้า3");
                                txt_status.setText("In");

                                btn_in.setVisibility(View.INVISIBLE);
                            }
                            if (btn_add.getVisibility() == View.INVISIBLE) {
                                spn_event.setVisibility(View.INVISIBLE);

                            }

                        }
                        if (foundDatamodel.getTransType().equals("In")&&foundDatamodel.getSendTo().equals("CYF")) {
                            spn_event.setVisibility(View.INVISIBLE);
                            textTo.setVisibility(View.INVISIBLE);
                            sendto.setVisibility(View.INVISIBLE);
                            btn_add.setVisibility(View.INVISIBLE);
                            btn_in.setVisibility(View.INVISIBLE);
                            btn_out.setVisibility(View.INVISIBLE);
                            textVendor.setText(foundDatamodel.getSendTo());
                            btn_pur.setVisibility(View.VISIBLE);
                        }

                        if (foundDatamodel.getTransType().equals("Out") || foundDatamodel.getGetFrom().equals("PUR")) { //เจอกล่องข้างนอกรับเข้าได้
                            textVendor.setText(foundDatamodel.getSendTo());
                            textTo.setText("CYF");
                            btn_add.setVisibility(View.INVISIBLE);
                            btn_out.setVisibility(View.INVISIBLE);
                            btn_pur.setVisibility(View.INVISIBLE);
                            spn_event.setVisibility(View.INVISIBLE);
                            viewVendor.setText("From");
                            sendto.setText("Sendto :");
                            btn_in.setVisibility(View.VISIBLE);
                        }
                        /*btn_add.setVisibility(View.INVISIBLE);
                        btn_in.setVisibility(View.INVISIBLE);
                        sendto.setText("GetFrom :");*/


                        if (foundDatamodel.getSendTo().equals("PUR") && foundDatamodel.getTransType().equals("In")) {
                            viewVendor.setText("Vendor");
                            textVendor.setText("รอนำส่ง");
                            textTo.setText(foundDatamodel.getGetFrom());
                            sendto.setText("GetFrom :");
                            btn_in.setVisibility(View.INVISIBLE);
                            btn_add.setVisibility(View.INVISIBLE);
                            btn_pur.setVisibility(View.INVISIBLE);
                            spn_event.setVisibility(View.INVISIBLE);
                            sendto.setVisibility(View.VISIBLE);
                            textTo.setVisibility(View.VISIBLE);
                            btn_out.setVisibility(View.VISIBLE);
                        }
                    }
                } else {//ไม่เจอกล่องโชว์ให้reg
                    Log.d("", "Logcess52 " + "0");
                    textVendor.setVisibility(View.INVISIBLE);
                    btn_in.setVisibility(View.INVISIBLE);
                    btn_out.setVisibility(View.INVISIBLE);
                    btn_pur.setVisibility(View.INVISIBLE);
                    //textVendor.setText("CYF");
                    sendto.setText("GetFrom :");
                    textTo.setText("CYF");


                    String check = num_BoxId;
                    int count = check.length();
                    Log.d("Mainactivity", "logcount" + count);
                    int pattern = 25;
                    //  boolean match = check.matches(" [A-Za-z]{3}\\d{6}-\\d{3}-\\d{3}[-]\\d{7}");
                    if (count <= pattern ) {

                        btn_in.setVisibility(View.INVISIBLE);
                        btn_out.setVisibility(View.INVISIBLE);
                        btn_add.setVisibility(View.INVISIBLE);
                        btn_pur.setVisibility(View.INVISIBLE);
                        fetchVendorDataForSpinner();
                    } else {
                        Log.d("", "Logcess52 " + "0");
                        Toast.makeText(MainActivity.this, "Box data is empty Reject", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Datamodels>> call, Throwable t) {
                txt_result_add.setText(t.getMessage());
            }

        });

        box_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Boxtable.class);
                intent.putExtra("Boxtableid", num_BoxId);
                startActivity(intent);
                finish();
            }
        });

        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_status.setText("In");
                txt_date.setText(datetime);
                //updateBoxTrans(txt_boxid.getText().toString(),textTo.getText().toString(),textVendor.getText().toString(),"-",txt_date.getText().toString(),txt_status.getText().toString());
                Log.d("Mainactivity", "text" + textTo.getText() + textVendor.getText());
                addLogBox(txt_boxid.getText().toString(), textVendor.getText().toString(), textTo.getText().toString(), txt_status.getText().toString());
                btn_in.setVisibility(View.INVISIBLE);
            }
        });

        btn_out.setOnClickListener(view ->

        {

            Intent intent = new Intent(MainActivity.this, SendActivity.class);
            intent.putExtra("num_BoxId", txt_boxid.getText().toString());
            intent.putExtra("Vendor", textVendor.getText().toString());
            startActivity(intent);

//                txt_status.setText("Out");
//                txt_date.setText(datetime);
//                updateBoxTrans(txt_boxid.getText().toString(),spn_vendor.getSelectedItem().toString(),"-",spn_event.getSelectedItem().toString(),txt_date.getText().toString(),txt_status.getText().toString());
//                addLogBox(txt_boxid.getText().toString(),spn_vendor.getSelectedItem().toString(),"-",spn_event.getSelectedItem().toString(),txt_date.getText().toString(),txt_status.getText().toString());
//                btn_out.setVisibility(View.INVISIBLE);

        });

        btn_add.setOnClickListener(v -> {
            txt_status.setText("Reg");
            txt_date.setText(datetime);
            //textTo.setText("CYF");

            // Move addBoxCtrl to a background thread
            new Thread(() -> {
                addBoxCtrl(txt_boxid.getText().toString(), "", spn_event.getSelectedItem().toString());

                // After the background task is done, update UI on the main thread
                v.post(() -> {
                    addLogBox(txt_boxid.getText().toString(), "CYF", "CYF", "Reg");
                    btn_add.setVisibility(View.INVISIBLE);
                    btn_pur.setVisibility(View.VISIBLE);
                });
            }).start();
            spn_event.setVisibility(View.INVISIBLE);
        });

        btn_pur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_pur.setVisibility(View.INVISIBLE);
                btn_out.setVisibility(View.VISIBLE);
                textVendor.setVisibility(View.VISIBLE);
                viewVendor.setText("Vendor");
                textVendor.setText("รอนำส่ง");
                txt_status.setText("In");
                sendto.setText("GetFrom");
                txt_date.setText(datetime);
                addLogBox(txt_boxid.getText().toString(), "CYF", "PUR", "In");

            }
        });

    }

    //-------------------------------------Button Update---------------------------------------------------//
    public void updateBoxTrans(String BoxId, String GetFrom, String SendTo, String TransDate, String TransType) {
        Datamodels modal_updateBoxTrans = new Datamodels(BoxId, GetFrom, SendTo, TransDate, TransType);
        final String num_BoxId = (getIntent().getStringExtra("ScannedData"));
        Call<List<Datamodels>> call3 = retrofitAPI.updateBoxTrans(num_BoxId, modal_updateBoxTrans);
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
    }

    //------------------------//
    public void addBoxCtrl(String BoxId, String BoxName, String Vendor) {
        Datamodels_BoxCtrl datamodels_boxCtrl = new Datamodels_BoxCtrl(BoxId, BoxName, Vendor);
        Call<Datamodels_BoxCtrl> call4 = retrofitAPI.addBoxCtrl(datamodels_boxCtrl);
        call4.enqueue(new Callback<Datamodels_BoxCtrl>() {
            @Override
            public void onResponse(Call<Datamodels_BoxCtrl> call, Response<Datamodels_BoxCtrl> response) {
                txt_result_add.setText("addBoxCtrl success");
            }

            @Override
            public void onFailure(Call<Datamodels_BoxCtrl> call, Throwable t) {
                txt_result_add.setText("addBoxCtrl error : " + t.getMessage());
            }
        });
    }

    public void addBoxTrans(String BoxId, String GetFrom, String SendTo, String TransDate, String TransType) {
        Datamodels datamodels = new Datamodels(BoxId, GetFrom, SendTo, TransDate, TransType);
        Call<Datamodels> call5 = retrofitAPI.addBoxTrans(datamodels);
        call5.enqueue(new Callback<Datamodels>() {
            @Override
            public void onResponse(Call<Datamodels> call, Response<Datamodels> response) {
                txt_result_add.setText("addBoxTrans success");
            }

            @Override
            public void onFailure(Call<Datamodels> call, Throwable t) {
                txt_result_add.setText("addBoxTrans error : " + t.getMessage());
            }
        });
    }

    public void addLogBox(String BoxId, String GetFrom, String SendTo, String TransType) {
        Datamodels_Logbox datamodels_logbox = new Datamodels_Logbox(BoxId, GetFrom, SendTo, TransType);
        Call<Datamodels_Logbox> call6 = retrofitAPI.addLogBox(datamodels_logbox);
        call6.enqueue(new Callback<Datamodels_Logbox>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Datamodels_Logbox> call, Response<Datamodels_Logbox> response) {
                txt_result_add_log.setText("AddLogBox success");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Call<Datamodels_Logbox> call, @NonNull Throwable t) {
                txt_result_add_log.setText("AddLogBox error: " + t.getMessage());
            }
        });

    }

    private void fetchVendorDataForSpinner() {
        Call<List<Datamodels_Vendors>> call8 = retrofitAPI.getAllVendor();
        call8.enqueue(new Callback<List<Datamodels_Vendors>>() {

            @Override
            public void onResponse(Call<List<Datamodels_Vendors>> call, Response<List<Datamodels_Vendors>> response) {
                List<Datamodels_Vendors> vendorlist = response.body();
                if (vendorlist != null && vendorlist.size() > 0) {
                    String[] vendors = new String[vendorlist.size()];
                    for (int i = 0; i < vendorlist.size(); i++) {
                        vendors[i] = vendorlist.get(i).getVendor();
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, vendors);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                    spn_event.setAdapter(spinnerArrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Datamodels_Vendors>> call, Throwable t) {

            }
        });

    }
    private void Showboxowner(String BoxId) {
        Call<List<Datamodels_BoxCtrl>> call3 = retrofitAPI.getBoxowner(BoxId);
        call3.enqueue(new Callback<List<Datamodels_BoxCtrl>>() {
            @Override
            public void onResponse(Call<List<Datamodels_BoxCtrl>> call, Response<List<Datamodels_BoxCtrl>> response) {
                    List<Datamodels_BoxCtrl> datamodels_boxCtrlList = response.body();
                    if (datamodels_boxCtrlList != null && datamodels_boxCtrlList.size() > 0) {
                        Datamodels_BoxCtrl foundDatamodel_Boxctrl = datamodels_boxCtrlList.get(0);
                        String vendor = foundDatamodel_Boxctrl.getVendor();
                        textVendor.setText(vendor);
                        Log.d("Mainactivity", "logcess10 " + vendor);
                    } else {
                        // Handle the case where the list is empty or no data is found
                    }

            }

            @Override
            public void onFailure(Call<List<Datamodels_BoxCtrl>> call, Throwable t) {
                // Handle the failure case
            }
        });
    }
}



