package com.example.ronaldinc.extras;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("addRoom.php")
    @FormUrlEncoded
    Call<Model> addRoom(
            @Field("selectedRoomType") String selectedRoomType,
            @Field("et_room_number") String et_room_number,
            @Field("monthly_price") String monthly_price,
            @Field("deposit_amount") String deposit_amount,
            @Field("estate_id") String estate_id,
            @Field("added_by") String added_by);

    @POST("addTenant.php")
    @FormUrlEncoded
    Call<Model> addTenant(
            @Field("et_tenant_name") String et_tenant_name,
            @Field("id_number") String id_number,
            @Field("phone_number") String phone_number,
            @Field("deposit_amount") String deposit_amount,
            @Field("room_number") String room_number,
            @Field("estate_id") String estate_id,
            @Field("added_by") String added_by);

    @POST("process_rent.php")
    @FormUrlEncoded
    Call<Model> processRent(
            @Field("paymentMethod") String paymentMethod,
            @Field("rent_amount") String rent_amount,
            @Field("transaction_code") String transaction_code,
            @Field("processed_by") String processed_by,
            @Field("tenant_id") String tenant_id);

    @GET("searchTenants.php")
    Call<List<TenantSearchModel>> searchTenants(
            @Query("key") String keyword
    );

}