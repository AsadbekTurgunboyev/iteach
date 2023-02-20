package com.example.iteach.HomeFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iteach.R;
import com.example.iteach.model.PaymentReceiverModel;
import com.example.iteach.model.Resource;
import com.example.iteach.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.MyViewHolder> {

    Context context;
    ArrayList<Resource> list;

    public WarehouseAdapter(Context context, ArrayList<Resource> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.warehouse_item,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_name.setText(list.get(position).getName());
        holder.txt_quantity.setText(list.get(position).getQuantity() + " kg");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        TextView txt_quantity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_resource_name);
            txt_quantity = itemView.findViewById(R.id.txt_resource_quantity);

        }
    }
}
