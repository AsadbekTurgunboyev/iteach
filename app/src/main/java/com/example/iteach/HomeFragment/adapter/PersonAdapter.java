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
import com.example.iteach.model.UserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {

    Context context;
    List<UserModel> list;

    public PersonAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == 0){
            holder.ic_add.setVisibility(View.VISIBLE);
            holder.circle_person.setImageResource(R.drawable.shap);
            holder.txt_name.setTextColor(ContextCompat.getColor(context,R.color.primaryColor));
            holder.itemView.setOnClickListener(view -> showBottom());
        }else {
            holder.ic_add.setVisibility(View.GONE);
            holder.circle_person.setImageResource(R.drawable.bear);
            holder.itemView.setOnClickListener(view -> clickUser(position -1));
        }

    }




    @Override
    public int getItemCount() {
        return 7;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circle_person;
        ImageView ic_add;
        TextView txt_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            circle_person = itemView.findViewById(R.id.circle_person);
            ic_add = itemView.findViewById(R.id.ic_add);
            txt_name = itemView.findViewById(R.id.txt_name);

        }
    }
    private void showBottom() {
    }
    private void clickUser(int i) {


    }
}
