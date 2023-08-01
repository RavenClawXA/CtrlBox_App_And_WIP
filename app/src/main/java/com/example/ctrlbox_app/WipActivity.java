package com.example.ctrlbox_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;



public class WipActivity extends AppCompatActivity {
    TextView txt_job, txt_item, txt_username;
    EditText txt_qty;
    Button btn_scanwip, btn_camera;
    ImageView picture;
    private RetrofitAPI retrofitAPI;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wip);

        txt_job = findViewById(R.id.txt_job);
        txt_item = findViewById(R.id.txt_item);
        txt_qty = findViewById(R.id.txt_qty);
        txt_username = findViewById(R.id.txt_username);
        picture = findViewById(R.id.Pic_View);
        btn_scanwip = findViewById(R.id.btn_scanwip);
        //btn_savewip = findViewById(R.id.btn_savewip);
        btn_camera = findViewById(R.id.btn_camera);

        Intent rec = getIntent();
        String Username = rec.getStringExtra("USERNAME");
        txt_username.setText(Username);

//        btn_savewip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (txt_job.getText().toString().isEmpty() && txt_item.getText().toString().isEmpty() && txt_qty.getText().toString().isEmpty()) {
//                    Toast.makeText(WipActivity.this, "กรุณาสแกน QR Code", Toast.LENGTH_SHORT).show();
//                    return;
//                }else if (txt_qty.getText().toString().isBlank()){
//                    Toast.makeText(WipActivity.this, "กรุณาใส่จำนวน", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                //postData(txt_job.getText().toString(), txt_item.getText().toString(), txt_qty.getText().toString(), txt_username.getText().toString());
//
//                int delayMillis = 2000;   //2sec
//                btn_savewip.setEnabled(false);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        btn_savewip.setEnabled(true);
//                    }
//                    },delayMillis);
//            }
//        });

        btn_scanwip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startScanning();
            }
        });

        initializeRetrofit();
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_job.getText().toString().isEmpty() && txt_item.getText().toString().isEmpty() && txt_qty.getText().toString().isEmpty()) {
                    Toast.makeText(WipActivity.this, "กรุณาสแกน QR Code", Toast.LENGTH_SHORT).show();
                    return;
                }else if (txt_qty.getText().toString().isBlank()){
                    Toast.makeText(WipActivity.this, "กรุณาใส่จำนวน", Toast.LENGTH_SHORT).show();
                    return;
                }
                dispatchTakePictureIntent();
            }
        });
    }
    private void startScanning() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String scanResult = result.getContents();
                if(scanResult.contains("||")) {
                    String[] inputArr = scanResult.split("\\|\\|");
                        txt_job.setText(inputArr[0]);
                        String job = txt_job.getText().toString();
                        job = job.trim();
                        txt_job.setText(job);

                        txt_item.setText(inputArr[3]);
                        String item = txt_item.getText().toString();
                        item =item.trim();
                        txt_item.setText(item);
                }
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.0.65.4:3002/")
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        String src_item = txt_item.getText().toString();
        Log.d("test", "pic" + src_item);
        Call<List<ApiResponse>> call2 = retrofitAPI.GetPicture(src_item);
        call2.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                if (response.isSuccessful()) {
                    List<ApiResponse> apiResponse = response.body();

                    if (apiResponse != null && !apiResponse.isEmpty()) {
                        ApiResponse firstResponse = apiResponse.get(0);
                        picture.setImageBitmap(null);
                        if (firstResponse != null && firstResponse.getPicture() != null && firstResponse.getPicture().getData() != null) {
                            List<Integer> data = firstResponse.getPicture().getData();
                            byte[] byteArray = new byte[data.size()];
                            for (int i = 0; i < data.size(); i++) {
                                byteArray[i] = data.get(i).byteValue();
                            }
                            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            picture.setImageBitmap(bitmap);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                Toast.makeText(WipActivity.this, "error" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            // Convert Bitmap to File
//            File imageFile = bitmapToFile(imageBitmap);
//
//            // Upload the image file
//            uploadImage(imageFile);

            // Get the image file from the current photo path
            File imageFile = new File(currentPhotoPath);

            // Upload the image file
            uploadImage(imageFile);

        }
    }
//    private void postData (String Job, String Item, String Quantity, String Recipient){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://49.0.65.4:3002/")
//                .addConverterFactory(new NullOnEmptyConverterFactory())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
//        Datamodels_Wip dataWip = new Datamodels_Wip(Job, Item, Quantity, Recipient);
//        Call<Datamodels_Wip> call = retrofitAPI.PostDataWip(dataWip);
//        call.enqueue(new Callback<Datamodels_Wip>() {
//            @Override
//            public void onResponse(Call<Datamodels_Wip> call, Response<Datamodels_Wip> response) {
//                if (response.isSuccessful()) {
//                    Log.d("Test", "imgtest5:");
//                    Toast.makeText(WipActivity.this, "บันทึกข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
//                    txt_job.setText("");
//                    txt_item.setText("");
//                    txt_qty.setText("");
//                    picture.setImageBitmap(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Datamodels_Wip> call, Throwable t) {
//                Toast.makeText(WipActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("Test", "imgtest6: " + " " + t.getMessage());
//            }
//        });
//    }

    private void initializeRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.0.65.4:3002/") // Replace with your API endpoint URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitAPI = retrofit.create(RetrofitAPI.class);
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.ctrlbox_app.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void uploadImage(File imageFile) {
        if (imageFile == null) {
            Toast.makeText(WipActivity.this, "upload image is value NULL" , Toast.LENGTH_LONG).show();
            return;
        }
        // Set additional parameters
        String job = txt_job.getText().toString();
        String item = txt_item.getText().toString();
        String quantity = txt_qty.getText().toString();
        String recipient = txt_username.getText().toString();
        //String camera = "Your Camera Value";

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);
        RequestBody jobRequestBody = RequestBody.create(MediaType.parse("text/plain"), job);
        RequestBody itemRequestBody = RequestBody.create(MediaType.parse("text/plain"), item);
        RequestBody quantityRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(quantity));
        RequestBody recipientRequestBody = RequestBody.create(MediaType.parse("text/plain"), recipient);
        //RequestBody cameraRequestBody = RequestBody.create(MediaType.parse("text/plain"), camera);

        Call<ResponseBody> call = retrofitAPI.uploadPicture(filePart, jobRequestBody, itemRequestBody, quantityRequestBody, recipientRequestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Image uploaded successfully
                    Toast.makeText(WipActivity.this, "บันทึกข้อมูลสำเร็จ", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Image uploaded successfully");
                    txt_job.setText("");
                    txt_item.setText("");
                    txt_qty.setText("");
                    picture.setImageBitmap(null);
                } else {
                    // Handle API error
                    Toast.makeText(WipActivity.this, "API error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "API error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle network or other errors
                Toast.makeText(WipActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Network error: " + t.getMessage());
            }
        });
    }
}