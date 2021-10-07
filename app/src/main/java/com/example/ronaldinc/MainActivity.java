package com.example.ronaldinc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerHome;
    List<Integer> imageList = new ArrayList<>();
    List<String> imageDescriptionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }
    public void initialize(){


        recyclerHome = findViewById(R.id.recyclerView);

        //Setting layout manager for our RecyclerView

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerHome.setLayoutManager(gridLayoutManager);
        //next create an adapter for the recyclerView in  HomeAdapter;

        imageList.add(R.drawable.room);
        imageList.add(R.drawable.tenantm);
        imageList.add(R.drawable.addroom);
        imageList.add(R.drawable.addtenant);
        imageList.add(R.drawable.rent);
        imageList.add(R.drawable.reports22);
        imageList.add(R.drawable.apart);
        imageList.add(R.drawable.logout22);
//        imageList.add(R.drawable.pay);
//        imageList.add(R.drawable.report2);
//        imageList.add(R.drawable.logout3);

        imageDescriptionList.add(getResources().getString(R.string.rooms));
        imageDescriptionList.add(getResources().getString(R.string.tenants));
        imageDescriptionList.add(getResources().getString(R.string.add_room));
        imageDescriptionList.add(getResources().getString(R.string.add_tenant));
        imageDescriptionList.add(getResources().getString(R.string.rent_payment));
        imageDescriptionList.add(getResources().getString(R.string.reports));
        imageDescriptionList.add(getResources().getString(R.string.apartment));
        imageDescriptionList.add(getResources().getString(R.string.logout));
//        imageDescriptionList.add(getResources().getString(R.string.rep));
//        imageDescriptionList.add(getResources().getString(R.string.report));
//        imageDescriptionList.add(getResources().getString(R.string.log));
        //after list are ready we send it to the adapter
        recyclerHome.setAdapter(new com.example.ronaldinc.HomeAdapter(imageList,imageDescriptionList));
        }
}