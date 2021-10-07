package com.example.ronaldinc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ronaldinc.extras.Api;
import com.example.ronaldinc.extras.ApiClient;
import com.example.ronaldinc.extras.Model;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRoomActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog pDialog;
    Vibrator v;
    LinearLayout LL;

    private Spinner spinner105;
    private JSONArray result;
    private ArrayList<String> arrayList;

    String selectedRoomType; int estate_id;
    final String getRoomTypeURL = ApiClient.BASE_URL+"getRoomType.php";

    EditText et_room_number,monthly_price,deposit_amount;

    Button add_rm;
    String added_by;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        //added_by = SharedPref.getInstance(this).LoggedInUserID();

        spinner105=findViewById(R.id.spinner104);
        LL=findViewById(R.id.mll);
        monthly_price=findViewById(R.id.monthly_price);
        et_room_number=findViewById(R.id.et_room_number);
        deposit_amount=findViewById(R.id.deposit_amount);
        add_rm=findViewById(R.id.add_rm);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        add_rm.setOnClickListener(this);

        //Intent intent = getIntent();
        estate_id =1;

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Add Room");

        getRoomType();
        spinner105.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) {
                    selectedRoomType = "";
                }else {
                    selectedRoomType = spinner105.getItemAtPosition(spinner105.getSelectedItemPosition()).toString().trim();
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void getRoomType() {
        arrayList = new ArrayList<>();
        displayLoader1();
        StringRequest stringRequest = new StringRequest(getRoomTypeURL, response -> {
            pDialog.dismiss();
            JSONObject j;
            try {
                j = new JSONObject(response);
                result = j.getJSONArray("result");
                pushResults(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
                error -> {
                    v.vibrate(100);
                    pDialog.dismiss();
                    Snackbar snackbar = Snackbar
                            .make(LL, "Check your Internet Connection", Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.YELLOW).setAction("RETRY", view -> {
                                startActivity(getIntent());
                                finish();
                                overridePendingTransition(0, 0);
                            });
                    snackbar.show();
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void pushResults(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                arrayList.add(json.getString("meta_value"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        spinner105.setAdapter(new ArrayAdapter<>(AddRoomActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayList));
    }

    private void displayLoader1() {
        pDialog = new ProgressDialog(AddRoomActivity.this,R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading Room Types Types ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
    public void validateInput() {
        String et_room_numberSTR = et_room_number.getText().toString().trim().replace(" ", "");;
        String monthly_priceSTR = monthly_price.getText().toString().trim();
        String deposit_amountSTR = deposit_amount.getText().toString().trim();
        String selectedRoomTypeStr = selectedRoomType;
        added_by = "1";


        if(selectedRoomType.equals("")){
            Toast.makeText(this, "Please Select Room Type", Toast.LENGTH_SHORT).show();
            v.vibrate(100);
            add_rm.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(et_room_numberSTR)) {
            et_room_number.setError("Please enter Room Number");
            et_room_number.requestFocus();
            v.vibrate(100);
            add_rm.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(monthly_priceSTR)) {
            monthly_price.setError("Please insert Room price");
            monthly_price.requestFocus();
            v.vibrate(100);
            add_rm.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(deposit_amountSTR)) {
            deposit_amount.setError("Please insert room deposit price");
            deposit_amount.requestFocus();
            v.vibrate(100);
            add_rm.setEnabled(true);
            return;
        }
        AddRoom(selectedRoomTypeStr,et_room_numberSTR,monthly_priceSTR,deposit_amountSTR,added_by);
    }

    private void AddRoom(String selectedRoomTypeStr, String et_room_numberSTR, String monthly_priceSTR, String deposit_amountSTR,String added_by) {
        displayLoader();

        String estate_idSTR0= estate_id+"";

        Api api = ApiClient.getClient().create(Api.class);
        Call<Model> addRoom = api.addRoom(selectedRoomTypeStr,et_room_numberSTR,monthly_priceSTR,deposit_amountSTR,"1","1");

        addRoom.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                pDialog.dismiss();

                boolean err = response.body().isError();
                if(!err){
                    resetElements();
                }else{
                    v.vibrate(100);
                }
                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                v.vibrate(100);
                pDialog.dismiss();

                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "Err", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void resetElements(){
        spinner105.setSelection(0);
        et_room_number.setText("");
        monthly_price.setText("");
        deposit_amount.setText("");
    }


    private void displayLoader() {
        pDialog = new ProgressDialog(AddRoomActivity.this,R.style.MyAlertDialogStyle);
        pDialog.setMessage("Adding Room please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    @Override
    public void onClick(View v) {
        if(v==add_rm){
            validateInput();
        }
    }
}