package com.example.iteach.avtivities;

import static android.R.layout.simple_spinner_item;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.iteach.HomeFragment.adapter.LoanAdapter;
import com.example.iteach.HomeFragment.adapter.WarehouseAdapter;
import com.example.iteach.R;
import com.example.iteach.model.LoanModel;
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
import java.util.Objects;

public class MyLoansActivity extends AppCompatActivity {

    FloatingActionButton btn_add;
    RecyclerView rv_loans;
    LoanAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_loans);

        initViews();

        Objects.requireNonNull(getSupportActionBar()).setTitle("Mening qarzlarim");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Finance").child("MyLoans");
        ArrayList<LoanModel> list = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LoanModel model = ds.getValue(LoanModel.class);
                    list.add(model);
                }

                adapter = new LoanAdapter(MyLoansActivity.this, list);
                rv_loans.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyLoansActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        rootRef.addListenerForSingleValueEvent(eventListener);
    }

    private void initViews() {

        rv_loans = findViewById(R.id.rv_loans);
    }
}
