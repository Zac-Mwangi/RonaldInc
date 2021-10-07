package com.example.ronaldinc.extras;

import com.google.gson.annotations.SerializedName;

public class TenantSearchModel {
    @SerializedName("tenant_id") private int tenant_id;
    @SerializedName("tenant_name") private String tenant_name;
    @SerializedName("balance") private String balance;
    @SerializedName("date_processed") private String date_processed;
    @SerializedName("room_number") private String room_number;
    @SerializedName("monthly_price") private String monthly_price;
    @SerializedName("estate_name") private String estate_name;

    public int getTenant_id() {
        return tenant_id;
    }

    public String getTenant_name() {
        return tenant_name;
    }

    public String getBalance() {
        return balance;
    }

    public String getDate_processed() {
        return date_processed;
    }

    public String getRoom_number() {
        return room_number;
    }

    public String getMonthly_price() {
        return monthly_price;
    }

    public String getEstate_name() {
        return estate_name;
    }
}