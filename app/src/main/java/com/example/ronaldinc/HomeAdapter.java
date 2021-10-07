package com.example.ronaldinc;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeView> {

    List<Integer> imageList;
    List<String> imageDescriptionList;
    Context mContext;


    public HomeAdapter(List<Integer> imageList, List<String> imageDescriptionList) {
        this.imageList = imageList;
        this.imageDescriptionList = imageDescriptionList;
    }

    //before passing anything create a view class
    //finally implement methods

    @NonNull
    @Override
    public HomeView onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //pass the layout resource file ie row_home
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home,parent,false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home,parent,false);
        mContext = parent.getContext();

        return  new HomeView(view);//pass the object we created
        //We have adapter ready so lets keep the data ready ie in main activity page

        //OR
        //HomeView holder = new HomeView(view);
       // return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull HomeView holder, final int position) {

        //passing values
        holder.image.setImageResource(imageList.get(position));
        holder.imageDescr.setText(imageDescriptionList.get(position));

        //finally run
        holder.hCV.setOnClickListener(view -> {
            String page = imageDescriptionList.get(position);
            if(page.equals(mContext.getResources().getString(R.string.rooms))){
                Intent intent0=new Intent(mContext.getApplicationContext(),ReportView.class);
                intent0.putExtra("requestUrl","rs.php");
                intent0.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(intent0);              }
            if(page.equals(mContext.getResources().getString(R.string.tenants))){
                Intent intent0=new Intent(mContext.getApplicationContext(),ReportView.class);
                intent0.putExtra("requestUrl","tr.php");
                intent0.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(intent0);             }
            if(page.equals(mContext.getResources().getString(R.string.add_room))){
                Intent intent = new Intent(mContext.getApplicationContext(),AddRoomActivity.class);
                mContext.startActivity(intent);
            }
            if(page.equals(mContext.getResources().getString(R.string.add_tenant))){
                Intent intent = new Intent(mContext.getApplicationContext(),AddTenantActivity.class);
                mContext.startActivity(intent);            }
            if(page.equals(mContext.getResources().getString(R.string.rent_payment))){
                Intent intent = new Intent(mContext.getApplicationContext(),PayRentActivity.class);
                mContext.startActivity(intent);
            }
            if(page.equals(mContext.getResources().getString(R.string.reports))){
                Intent intent0=new Intent(mContext.getApplicationContext(),ReportView.class);
                intent0.putExtra("requestUrl","rcr.php");
                intent0.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(intent0);            }
            if(page.equals(mContext.getResources().getString(R.string.logout))){
                Toast.makeText(mContext, "You Will be logged out", Toast.LENGTH_SHORT).show();
            }
            if(page.equals(mContext.getResources().getString(R.string.apartment))){
                Toast.makeText(mContext, "Savanna Apartments", Toast.LENGTH_LONG).show();
           }
//            if(page.equals(mContext.getResources().getString(R.string.report))){
//                Toast.makeText(mContext, "report", Toast.LENGTH_SHORT).show();
//            }
//            if(page.equals(mContext.getResources().getString(R.string.log))){
//                Toast.makeText(mContext, "logout", Toast.LENGTH_SHORT).show();
//            }
        });

    }
    @Override
    public int getItemCount() {
        return imageList.size();
    }

    //before passing anything create a view class
    public class HomeView extends RecyclerView.ViewHolder{
        ImageView image;
        TextView imageDescr;
        CardView hCV;
    //create constructor of that view
        public HomeView(@NonNull View itemView) {
            super(itemView);

            imageDescr = itemView.findViewById(R.id.imageDescription);
            image = itemView.findViewById(R.id.image);
            hCV = itemView.findViewById(R.id.hCV);
        }
    }
}