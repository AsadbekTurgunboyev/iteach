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

    WarehouseAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Resource> list;
 ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            list = new ArrayList<>();
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                Resource model = ds.getValue(Resource.class);
                list.add(model);
            }

            adapter = new WarehouseAdapter(WarehouseActivity.this, list);
            rv_resources.setAdapter(adapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(WarehouseActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        initViews();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Resources");
        btn_add_resource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });


        reference.addValueEventListener(eventListener);
    }

    private void showBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.add_resource_bottom_sheet);

        TextInputLayout name = bottomSheetDialog.findViewById(R.id.edt_resource_name);
        TextInputLayout quantity = bottomSheetDialog.findViewById(R.id.edt_resource_quantity);
        MaterialButton btn_add = bottomSheetDialog.findViewById(R.id.btn_add_resource);

        Objects.requireNonNull(btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = Objects.requireNonNull(Objects.requireNonNull(name).getEditText()).getText().toString().trim();
                String txt_quantity = null;
                if (quantity != null) {
                    txt_quantity = Objects.requireNonNull(quantity.getEditText()).getText().toString().trim();
                }

                if (txt_quantity != null && (!txt_name.isEmpty() || !txt_quantity.isEmpty())) {
                    Resource model = new Resource(txt_name, txt_quantity);
                    reference.push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(WarehouseActivity.this, "Saqlandi!", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            } else {
                                Toast.makeText(WarehouseActivity.this, "Xatolik!", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }
                        }
                    });

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