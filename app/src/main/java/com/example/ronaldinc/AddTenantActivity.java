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

import com.android.volley.Request;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTenantActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog pDialog;
    Vibrator v;
    LinearLayout LL;

    private Spinner room_spinner;
    private JSONArray result;
    private ArrayList<String> arrayList;

    String selectedRoom,t; int estate_id;
    final String getEmptyRoomsURL = ApiClient.BASE_URL +"getEmptyRooms.php";

    EditText et_tenant_name,id_number,phone_number,deposit_amount;

    Button add_tenant;

    String added_by;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tenant);

        added_by = "1";

        room_spinner=findViewById(R.id.room_spinner);
        LL=findViewById(R.id.mll);
        et_tenant_name=findViewById(R.id.et_tenant_name);
        id_number=findViewById(R.id.id_number);
        phone_number=findViewById(R.id.phone_number);
        deposit_amount=findViewById(R.id.deposit_amount);
        add_tenant=findViewById(R.id.add_tenant);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        add_tenant.setOnClickListener(this);
        estate_id = 1;
        Intent intent = getIntent();
       // estate_id = intent.getIntExtra("estate_id",0);

        //estate_id = 1;


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Add Tenant");

        getEmptyRooms();
        room_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) {
                    selectedRoom = "";
                }else {
                    selectedRoom = room_spinner.getItemAtPosition(room_spinner.getSelectedItemPosition()).toString().trim();

                    String s1 = selectedRoom;
                    String[] sp = s1.split(" ");
                    t = sp[2];
                }
                // Toast.makeText(AddTenantActivity.this, t, Toast.LENGTH_SHORT).show();
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

    private void getEmptyRooms() {
        arrayList = new ArrayList<>();
        displayLoader1();
        StringRequest request = new StringRequest(Request.Method.POST, getEmptyRoomsURL, response -> {
            pDialog.dismiss();
            JSONObject j;
            try {
                j = new JSONObject(response);
                result = j.getJSONArray("result");
                pushResults(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            v.vibrate(100);
            pDialog.dismiss();
            Snackbar snackbar = Snackbar
                    .make(LL, "Check your Internet Connection", Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.YELLOW).setAction("RETRY", view -> {
                        startActivity(getIntent());
                        finish();
                        overridePendingTransition(0, 0);
                    });
            snackbar.show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("estate_id", String.valueOf(estate_id));

                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
    private void pushResults(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                arrayList.add(json.getString("room_number"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        room_spinner.setAdapter(new ArrayAdapter<>(AddTenantActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayList));

    }
    private void displayLoader1() {
        pDialog = new ProgressDialog(AddTenantActivity.this,R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading Empty Rooms ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
    public void validateInput() {
        String et_tenant_nameSTR = et_tenant_name.getText().toString().trim();
        String phone_numberSTR = phone_number.getText().toString().trim();

        String id_numberSTR = id_number.getText().toString().trim();
        String deposit_amountSTR = deposit_amount.getText().toString().trim();


        if (TextUtils.isEmpty(et_tenant_nameSTR)) {
            et_tenant_name.setError("Tenant Name Cannot be Empty!");
            et_tenant_name.requestFocus();
            v.vibrate(100);
            add_tenant.setEnabled(true);
            return;
        }
        if (!phone_numberSTR.equals("")) {
            if (phone_numberSTR.length() < 10) {
                phone_number.setError("Phone Number must have 10 characters");
                phone_number.requestFocus();
                v.vibrate(100);
                add_tenant.setEnabled(true);
                return;
            }
        }
        if(selectedRoom.equals("")){
            Toast.makeText(this, "Please Select Room", Toast.LENGTH_SHORT).show();
            v.vibrate(100);
            add_tenant.setEnabled(true);
            return;
        }
        AddTenant(et_tenant_nameSTR,id_numberSTR,phone_numberSTR,deposit_amountSTR);
        Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT).show();
    }

    private void AddTenant(String et_tenant_nameSTR,String id_numberSTR, String phone_numberSTR, String deposit_amountSTR) {
        displayLoader();

        Api api = ApiClient.getClient().create(Api.class);
        Call<Model> registerTenant = api.addTenant(et_tenant_nameSTR,id_numberSTR,phone_numberSTR,deposit_amountSTR,t.trim(),estate_id+"",added_by);

        registerTenant.enqueue(new Callback<Model>() {
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
        room_spinner.setSelection(0);
        et_tenant_name.setText("");
        id_number.setText("");
        phone_number.setText("");
        deposit_amount.setText("");


        finish();
        Intent intent = new Intent(this, AddTenantActivity.class);
        //intent.putExtra("estate_id", 1);
        startActivity(intent);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(AddTenantActivity.this,R.style.MyAlertDialogStyle);
        pDialog.setMessage("Adding Room please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v==add_tenant){
            validateInput();
        }
    }
}