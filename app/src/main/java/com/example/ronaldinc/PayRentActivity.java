package com.example.ronaldinc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ronaldinc.extras.Api;
import com.example.ronaldinc.extras.ApiClient;
import com.example.ronaldinc.extras.Model;
import com.example.ronaldinc.extras.TenantSearchAdapter;
import com.example.ronaldinc.extras.TenantSearchModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayRentActivity extends AppCompatActivity implements View.OnClickListener , TenantSearchAdapter.OnItemClickListener {

    private ProgressDialog pDialog;
    Vibrator v;
    LinearLayout LL;

    private Spinner rent_spinner;
    private JSONArray result;
    private ArrayList<String> arrayList;

    Button btn_pay_rent;
    EditText transaction_code, rent_amount, search_tenant;
    TextView tenant_i, balInfo;

    SearchView searchView;

    RecyclerView recyclerView;

    String tenant_ID;

    final String getPaymentMethodURL = ApiClient.BASE_URL + "getPaymentMethod.php";
    String selectedPaymentMethod;
    TextInputLayout trn_Text;

    String added_by;

    private RecyclerView.LayoutManager layoutManager;
    private List<TenantSearchModel> tenants;
    private TenantSearchAdapter adapter;

    private String currEstate, currHouse, currTenant;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_rent);

        added_by = "1";

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Rent Form");

        rent_spinner = findViewById(R.id.rent_spinner);
        transaction_code = findViewById(R.id.transaction_code);
        btn_pay_rent = findViewById(R.id.btn_pay_rent);
        rent_amount = findViewById(R.id.rent_amount);
        trn_Text = findViewById(R.id.trn_Text);
        recyclerView = findViewById(R.id.tenantSearch_recycler);
        searchView = findViewById(R.id.searchView);
        tenant_i = findViewById(R.id.tenant_i);
        balInfo = findViewById(R.id.balInfo);
//        printReceiptBtn = findViewById(R.id.printReceipt);


        transaction_code.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        LL = findViewById(R.id.mll);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        tenants = new ArrayList<>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchUsers(newText);
                return false;
            }
        });

        btn_pay_rent.setOnClickListener(this);

        getPaymentMethods();
        rent_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) {
                    selectedPaymentMethod = "";
                } else {
                    selectedPaymentMethod = rent_spinner.getItemAtPosition(rent_spinner.getSelectedItemPosition()).toString().trim();
                    if (selectedPaymentMethod.equals("Cash Payment")) {
                        trn_Text.setEnabled(false);
                    } else {
                        trn_Text.setEnabled(true);
                    }
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

    private void getPaymentMethods() {
        arrayList = new ArrayList<>();
        displayLoader1();
        StringRequest stringRequest = new StringRequest(getPaymentMethodURL, response -> {
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

    private void pushResults(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                arrayList.add(json.getString("meta_value"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        rent_spinner.setAdapter(new ArrayAdapter<>(PayRentActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayList));
        fetchUsers("");
    }

    private void displayLoader1() {
        pDialog = new ProgressDialog(PayRentActivity.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading Payment Methods ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == btn_pay_rent) {
            validateInput();
        }
    }


    public void validateInput() {
        final String rent_amountSTR = rent_amount.getText().toString().trim();
        final String transaction_codeSTR = transaction_code.getText().toString().trim();
        final String tId = tenant_ID;

        if (TextUtils.isEmpty(tenant_ID)) {
            Toast.makeText(this, "Please Select tenant", Toast.LENGTH_SHORT).show();
            v.vibrate(100);
            btn_pay_rent.setEnabled(true);
            return;
        }

        if (selectedPaymentMethod.equals("")) {
            Toast.makeText(this, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
            v.vibrate(100);
            btn_pay_rent.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(rent_amountSTR)) {
            rent_amount.setError("Rent Amount Cannot be empty!");
            rent_amount.requestFocus();
            v.vibrate(100);
            btn_pay_rent.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(transaction_codeSTR) && !selectedPaymentMethod.equals("Cash Payment")) {
            transaction_code.setError("Fill Transaction Code");
            transaction_code.requestFocus();
            v.vibrate(100);
            btn_pay_rent.setEnabled(true);
            return;
        }


        processRent(rent_amountSTR, transaction_codeSTR);
    }

    private void processRent(String rent_amountSTR, String transaction_codeSTR) {
        displayLoader();

        Api api = ApiClient.getClient().create(Api.class);
        Call<Model> payRent = api.processRent(selectedPaymentMethod, rent_amountSTR, transaction_codeSTR, "1", tenant_ID);

        payRent.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                pDialog.dismiss();
                boolean err = response.body().isError();
                if (!err) {
                    resetElements();
                } else {
                    v.vibrate(100);
                }
                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                v.vibrate(100);
                pDialog.dismiss();

                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetElements() {
        searchView.setQueryHint("Tenant Id, Name, nat_id, phone");
        searchView.setQuery("", true);
        rent_spinner.setSelection(0);
        rent_amount.setText("");
        transaction_code.setText("");
        tenant_ID = "";
        tenant_i.setText("");
        balInfo.setText("");
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(PayRentActivity.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Rent being processed please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void fetchUsers(String key) {

        if (key.equals("")) {
            //Toast.makeText(getApplicationContext(), "search tenants", Toast.LENGTH_SHORT).show();
            tenant_i.setText("");
            balInfo.setText("");
        } else {
            recyclerView.setVisibility(View.VISIBLE);

            Api apiInterface = ApiClient.getClient().create(Api.class);
            Call<List<TenantSearchModel>> call = apiInterface.searchTenants(key);

            call.enqueue(new Callback<List<TenantSearchModel>>() {
                @Override
                public void onResponse(Call<List<TenantSearchModel>> call, Response<List<TenantSearchModel>> response) {
                    pDialog.dismiss();
                    tenants = response.body();
                    adapter = new TenantSearchAdapter(PayRentActivity.this, tenants);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(PayRentActivity.this);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<TenantSearchModel>> call, Throwable t) {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error\n" + t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemClick(int position) {
        TenantSearchModel clickedItem = tenants.get(position);
        //Toast.makeText(this, clickedItem.getTenant_name(), Toast.LENGTH_SHORT).show();
        tenant_ID = "" + clickedItem.getTenant_id();

        tenant_i.setText(clickedItem.getTenant_name() + " { " + clickedItem.getEstate_name() + ", Rm - " + clickedItem.getRoom_number() + " - @Kes" + clickedItem.getMonthly_price() + " }");
        balInfo.setText("Bal Kes (" + clickedItem.getBalance() + "), Last paid on " + clickedItem.getDate_processed());

        searchView.setQuery(clickedItem.getTenant_name(), true);
        recyclerView.setVisibility(View.GONE);

        currEstate = clickedItem.getEstate_name();
        currHouse = clickedItem.getRoom_number();
        currTenant = clickedItem.getTenant_name();
    }
}