package com.example.ctrlbox_app;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitAPI {

       /* @PUT("BoxCtrl/update/{id}")
        Call<List<Datamodels>> getPut(@Path("id") String BoxId, @Body Datamodels datamodels);

        @GET("BoxCtrl")
        Call<List<Datamodels>> getAllBC();

        @GET("BoxCtrl/{id}")
        Call<List<Datamodels>> getPosts(@Path("id") String BoxId);

        @GET("TransBox/get")
        Call<List<Datamodels>>getAllTB();

        @POST("TransBox/add")
        Call<Datamodels> addtoTB(@Body Datamodels datamodels);

        @DELETE("BoxCtrl/delete/{id}")
        Call<Datamodels_addBoxCtrl> del_BC(@Path("id") String BoxId);

        @POST("Logbox/add")
        Call<Datamodels> addHistory(@Body Datamodels datamodels);

        @POST("add")
        Call<Datamodels> addtoBC(@Body Datamodels datamodels);

        @DELETE("TransBox/delete/{id}")
        Call<Datamodels_addBoxCtrl> del_TB(@Path("id") String BoxId);  */

        //-----------BoxTrans---------------//
        @GET("LogBox/getlast/{id}")
        Call<List<Datamodels>> getActiveLogbox(@Path("id") String BoxId);

        @PUT("BoxTrans/update/{id}")
        Call<List<Datamodels>> updateBoxTrans(@Path("id") String BoxId, @Body Datamodels datamodels);

        @POST("BoxTrans/add")
        Call<Datamodels> addBoxTrans(@Body Datamodels datamodels);

        //-----------BoxCtrl--------------------//
        @GET("BoxCtrl")
        Call<List<Datamodels_BoxCtrl>> getBoxCtrl ();
        @POST("BoxCtrl/add")
        Call<Datamodels_BoxCtrl> addBoxCtrl(@Body Datamodels_BoxCtrl datamodels_boxCtrl);

        //-----------LogBox-----------------//
        @POST("LogBox/add")
        Call<Datamodels_Logbox> addLogBox(@Body Datamodels_Logbox datamodels_logbox);

        @GET("LogBox/get/{id}")
       Call<List<Datamodels>> getLogById(@Path("id") String BoxId);

        //-----------Vendor-----------------//
        @GET("Vendor/get")
        Call<List<Datamodels_Vendors>> getAllVendor();

        @POST("Vendor/add")
        Call<Datamodels_Vendors> addVendor(@Body Datamodels_Vendors datamodels_vendors);

        //--------------login---------------//
        @POST("login")
        Call<LoginResponse> login(@Body LoginRequest loginRequest);

        //-----------------Wip---------------//
        @POST("scanwip/add")
        Call<Datamodels_Wip> PostDataWip(@Body Datamodels_Wip datamodels_wip);

        @GET("scanwip/get/picture/{id}")
        Call<List<ApiResponse>> GetPicture(@Path("id") String item);

        @Multipart
        @POST("scanwip/upload")
        Call<ResponseBody> uploadPicture(
                @Part MultipartBody.Part file,
                @Part("job") RequestBody job,
                @Part("item") RequestBody item,
                @Part("quantity") RequestBody quantity,
                @Part("recipient") RequestBody recipient);
}
