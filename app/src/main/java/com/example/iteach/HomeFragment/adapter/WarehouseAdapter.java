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
public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.MyViewHolder> {

    Context context;
    ArrayList<Resource> list;
    String action;

    public WarehouseAdapter(Context context, ArrayList<Resource> list, String action) {
        this.context = context;
        this.list = list;
        this.action = action;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(action, "give_product")){
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                    bottomSheetDialog.setContentView(R.layout.give_product_bottomsheet);
                    bottomSheetDialog.show();

                    MaterialButton btn_give = bottomSheetDialog.findViewById(R.id.btn_give_product_bs);
                    TextInputLayout edt_given = bottomSheetDialog.findViewById(R.id.edt_given_amount);

                    btn_give.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String txt_amount = edt_given.getEditText().getText().toString().trim();

                            if (!(txt_amount.isEmpty())){
                                int needed_amount = Integer.parseInt(txt_amount);
                                int current_amount = Integer.parseInt(list.get(position).getQuantity());

                                if (needed_amount > current_amount){
                                    Toast.makeText(context, "Sizda buncha mahsulot yoq!", Toast.LENGTH_SHORT).show();
                                } else {
                                    int updated_amount = current_amount - needed_amount;
                                    String updated_amount_string = updated_amount + "";

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Resources").child(list.get(position).getName()).child("quantity");

                                    int new_debt = Integer.parseInt(list.get(position).getPrice()) * needed_amount;

                                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreference", Context.MODE_PRIVATE);
                                    String key = sharedPreferences.getString("key", "");

                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Clients").child(key).child("money_left");

                                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int old_debt = Integer.parseInt(snapshot.getValue().toString());

                                            int overall_debt = new_debt + old_debt;

                                            reference2.setValue(String.valueOf(overall_debt));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    reference.setValue(updated_amount_string).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()){
                                                Toast.makeText(context, "Mahsulot topshirildi!", Toast.LENGTH_SHORT).show();
                                                bottomSheetDialog.dismiss();
                                            } else {
                                                Toast.makeText(context, "Xatolik!", Toast.LENGTH_SHORT).show();
                                                bottomSheetDialog.dismiss();
                                            }
                                        }
                                    });
                                }

                            } else {
                                Toast.makeText(context, "Ma'lumot kiritilmagan!", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }
                        }
                    });

                } else {
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                    bottomSheetDialog.setContentView(R.layout.update_resource_bottom_sheet);
                    bottomSheetDialog.show();

                    MaterialButton btn_add = bottomSheetDialog.findViewById(R.id.btn_update_resource);
                    TextInputLayout edt_input = bottomSheetDialog.findViewById(R.id.edt_new_amount);

                    btn_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String given_amount = edt_input.getEditText().getText().toString().trim();

                            if (!(given_amount.isEmpty())){

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Resources").child(list.get(position).getName()).child("quantity");

                                reference.setValue(given_amount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isComplete()){
                                            Toast.makeText(context, "Mahsulot miqdori yangilandi!", Toast.LENGTH_SHORT).show();
                                            bottomSheetDialog.dismiss();
                                        } else {
                                            Toast.makeText(context, "Xatolik!", Toast.LENGTH_SHORT).show();
                                            bottomSheetDialog.dismiss();
                                        }
                                    }
                                });


                            } else {
                                Toast.makeText(context, "Ma'lumot kiritilmagan!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
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
