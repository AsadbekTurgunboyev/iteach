package com.example.iteach.avtivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.iteach.HomeFragment.adapter.PersonAdapter;
import com.example.iteach.HomeFragment.adapter.WarehouseAdapter;
import com.example.iteach.R;
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
        TextInputLayout quantity = bottomSheetDialog.findViewById(R.id.edt_resource_quantity);
        MaterialButton btn_add = bottomSheetDialog.findViewById(R.id.btn_add_resource);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = name.getEditText().getText().toString().trim();
                String txt_quantity = quantity.getEditText().getText().toString().trim();

                if (txt_name != "" && txt_quantity != ""){

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference reference = db.getReference().child("Resources").child(txt_name);

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Resource model = new Resource(txt_name, txt_quantity);

                            reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()){
                                        Toast.makeText(WarehouseActivity.this, "Saqlandi!", Toast.LENGTH_SHORT).show();
                                        bottomSheetDialog.dismiss();
                                        recreate();

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
                        }
                    });

                } else {
                    Toast.makeText(WarehouseActivity.this, "Malumotlarni kiriting!", Toast.LENGTH_SHORT).show();
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