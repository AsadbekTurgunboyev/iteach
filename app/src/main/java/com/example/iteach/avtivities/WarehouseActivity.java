package com.example.iteach.avtivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iteach.HomeFragment.adapter.PersonAdapter;
import com.example.iteach.HomeFragment.adapter.WarehouseAdapter;
import com.example.iteach.R;
import com.example.iteach.model.LoanModel;
import com.example.iteach.model.PaymentReceiverModel;
import com.example.iteach.model.Resource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class WarehouseActivity extends AppCompatActivity {

    FloatingActionButton btn_add_resource;
    RecyclerView rv_resources;

    String action = "";

    WarehouseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        initViews();

        action = getIntent().getStringExtra("action");
        if (action.equals("give_product") ) {

            Objects.requireNonNull(getSupportActionBar()).setTitle("Mahsulot o'tkazish");

            btn_add_resource.setVisibility(View.INVISIBLE);

        }else {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Ombor");

        }
        btn_add_resource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Resources");
        ArrayList<Resource> list = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Resource model = ds.getValue(Resource.class);
                    list.add(model);
                }

                adapter = new WarehouseAdapter(WarehouseActivity.this, list, action);
                rv_resources.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(WarehouseActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        rootRef.addListenerForSingleValueEvent(eventListener);
    }

    private void showBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.add_resource_bottom_sheet);

        TextInputLayout name = bottomSheetDialog.findViewById(R.id.edt_resource_name);
        TextInputLayout store_name = bottomSheetDialog.findViewById(R.id.edt_store_name);
        TextInputLayout quantity = bottomSheetDialog.findViewById(R.id.edt_resource_quantity);
        TextView overall_price = bottomSheetDialog.findViewById(R.id.txt_overall_price);
        TextInputLayout price = bottomSheetDialog.findViewById(R.id.edt_resource_price);
        TextInputLayout payment = bottomSheetDialog.findViewById(R.id.edt_payment);
        MaterialButton btn_add = bottomSheetDialog.findViewById(R.id.btn_add_resource);

        String txt_overall_price = "";

        if (price.getEditText().getText().toString() == ""){
            Toast.makeText(WarehouseActivity.this, "Birinchi bir kiloga narxini kiriting!", Toast.LENGTH_SHORT).show();
        } else {
            quantity.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    overall_price.setText(String.valueOf(Integer.parseInt(quantity.getEditText().getText().toString()) * Integer.parseInt(price.getEditText().getText().toString())) + " so'm");
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = name.getEditText().getText().toString().trim();
                String txt_store_name = store_name.getEditText().getText().toString().trim();
                String txt_quantity = quantity.getEditText().getText().toString().trim();
                String txt_price = price.getEditText().getText().toString().trim();
                String txt_payment = payment.getEditText().getText().toString().trim();

                if (txt_name != "" && txt_store_name != "" && txt_quantity != "" && txt_price != "" && txt_payment != ""){

                    if (Integer.parseInt(txt_payment) > Integer.parseInt(txt_overall_price)){
                        Toast.makeText(WarehouseActivity.this, "Ortiqcha pul tolandi!", Toast.LENGTH_SHORT).show();
                    } else {
                        String loan = String.valueOf(Integer.parseInt(txt_price) - Integer.parseInt(txt_payment));

                        if (Integer.parseInt(loan) == 0){

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("money");

                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String money_there = snapshot.getValue().toString();

                                    String new_balance = String.valueOf(Integer.parseInt(money_there) - Integer.parseInt(txt_payment));

                                    reference.setValue(new_balance).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Resources").child(txt_name);

                                            Resource r_model = new Resource(txt_name, txt_store_name, txt_quantity, txt_price, txt_overall_price, txt_payment);
                                            reference1.setValue(r_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(WarehouseActivity.this, "Saqlandi!", Toast.LENGTH_SHORT).show();
                                                    bottomSheetDialog.dismiss();
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(WarehouseActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("money");

                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String money_there = snapshot.getValue().toString();

                                    String new_balance = String.valueOf(Integer.parseInt(money_there) - Integer.parseInt(txt_payment));

                                    reference.setValue(new_balance);

                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Finance").child("MyLoans").child(txt_store_name);

                                    LoanModel model = new LoanModel(txt_store_name, txt_price, txt_overall_price, txt_payment, loan);

                                    reference1.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()){

                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Resources").child(txt_name);

                                                Resource r_model = new Resource(txt_name, txt_store_name, txt_quantity, txt_price, txt_overall_price, txt_payment);
                                                reference2.setValue(r_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(WarehouseActivity.this, "Saqlandi!", Toast.LENGTH_SHORT).show();
                                                        bottomSheetDialog.dismiss();
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(WarehouseActivity.this, "Xatolik!", Toast.LENGTH_SHORT).show();
                                                bottomSheetDialog.dismiss();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(WarehouseActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();
                                }
                            });

                        }
                    }

                } else {
                    Toast.makeText(WarehouseActivity.this, "Ma'lumotlarni kiriting!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bottomSheetDialog.show();
    }

    private void initViews() {
        btn_add_resource = findViewById(R.id.btn_addResource);
        rv_resources = findViewById(R.id.rv_resources);
    }
}