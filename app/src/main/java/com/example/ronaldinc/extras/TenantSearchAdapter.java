package com.example.ronaldinc.extras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ronaldinc.R;

public class TenantSearchAdapter extends RecyclerView.Adapter<TenantSearchAdapter.ViewHolder> {
    private Context mCtx;
    private java.util.List<TenantSearchModel> List;

    private TenantSearchAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(TenantSearchAdapter.OnItemClickListener listener){
        mListener = listener;
    }
    public TenantSearchAdapter(Context mCtx, java.util.List<TenantSearchModel> List) {
        this.mCtx = mCtx;
        this.List = List;
    }

    @NonNull
    @Override
    public TenantSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.tenant_list_rent_pay, null);
        return new TenantSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TenantSearchAdapter.ViewHolder holder, int position) {
        TenantSearchModel tenantSearchModel = List.get(position);
        holder.tenantInfo.setText(tenantSearchModel.getTenant_name());
    }
    @Override
    public int getItemCount() {
        return List.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tenantInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenantInfo = itemView.findViewById(R.id.tenant_info);

            itemView.setOnClickListener(v -> {
                if(mListener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }
}


