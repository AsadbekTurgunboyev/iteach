package com.example.iteach.avtivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iteach.R;
import com.example.iteach.model.PaymentReceiverModel;
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

import java.util.Objects;

public class ClientActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    TextView txt_money_left;
    TextView txt_name;
    TextView txt_surname;
    TextView txt_desc;
    PaymentReceiverModel model;


    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            model = snapshot.getValue(PaymentReceiverModel.class);

            assert model != null;
            txt_money_left.setText(model.getMoney_left());
            txt_name.setText(model.getName());
            txt_surname.setText(model.getSurname());
            txt_desc.setText(model.getDesc());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        initViews();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Clients");
        MaterialButton btn_take_payment = findViewById(R.id.btn_take_payment_2);
        MaterialButton btn_give_product = findViewById(R.id.btn_give_product);

        String key = getIntent().getStringExtra("key");
        reference.child(key).addValueEventListener(listener);
        txt_name.setText(getIntent().getStringExtra("name"));
        txt_surname.setText(getIntent().getStringExtra("surname"));
        txt_desc.setText(getIntent().getStringExtra("desc"));

        btn_give_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientActivity.this, WarehouseActivity.class);
                intent.putExtra("action", "give_product");
                startActivity(intent);
            }
        });

        btn_take_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Integer.parseInt(txt_money_left.getText().toString()) > 0) {
                    showPaymentBottomSheet();
                } else {
                    Toast.makeText(ClientActivity.this, "Bu odam ni sizdan qarzi yo'q!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        txt_money_left = findViewById(R.id.txt_money_left);
        txt_name = findViewById(R.id.txt_client_name);
        txt_surname = findViewById(R.id.txt_client_surname);
        txt_desc = findViewById(R.id.txt_client_desc);
    }

    private void showPaymentBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.take_payment_bottom_sheet);
        bottomSheetDialog.show();

        TextInputLayout money_sum = bottomSheetDialog.findViewById(R.id.edt_money);
        MaterialButton btn_add = bottomSheetDialog.findViewById(R.id.btn_receive_payment);

        assert btn_add != null;
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert money_sum != null;
                String money_paid_string = Objects.requireNonNull(money_sum.getEditText()).getText().toString().trim();

                if (money_paid_string.isEmpty()) {

                    Toast.makeText(ClientActivity.this, "Pul miqdori kiritilmagan!", Toast.LENGTH_SHORT).show();
                } else {
                    int money_paid = Integer.parseInt(money_sum.getEditText().getText().toString());

                    String key = getIntent().getStringExtra("key");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Clients").child(key).child("money_left");

                    int db_money_left = Integer.parseInt(txt_money_left.getText().toString());

                    if (db_money_left >= money_paid) {
                        String new_money_left = String.valueOf((db_money_left - money_paid));

                        reference.setValue(new_money_left).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Toast.makeText(ClientActivity.this, "Qarz yangilandi!", Toast.LENGTH_SHORT).show();

                                    bottomSheetDialog.dismiss();
                                } else {
                                    Toast.makeText(ClientActivity.this, "Xatolik!", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ClientActivity.this, "Qarz midori dan ortiq tolandi!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}