package com.example.iteach.HomeFragment.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iteach.R;
import com.example.iteach.model.LoanModel;
import com.example.iteach.model.Resource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {

    Context context;
    ArrayList<LoanModel> list;

    public LoanAdapter(Context context, ArrayList<LoanModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loans_item,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_store_name.setText(list.get(position).getStore_name());
        holder.txt_loan.setText(list.get(position).getMoney_left() + " so'm");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_store_name;
        TextView txt_loan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_store_name = itemView.findViewById(R.id.txt_store_name);
            txt_loan = itemView.findViewById(R.id.txt_loan_left);

        }
    }
}

