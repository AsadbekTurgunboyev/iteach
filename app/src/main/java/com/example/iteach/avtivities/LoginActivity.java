package com.example.iteach.avtivities;

import static com.example.iteach.Const.KEY;
import static com.example.iteach.Const.NAME;
import static com.example.iteach.Const.PASSWORD;
import static com.example.iteach.Const.ROLE;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iteach.MainActivity;
import com.example.iteach.PrefData.PreferenceData;
import com.example.iteach.R;
import com.example.iteach.model.LoginModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout txtName, txtPass;
    MaterialButton btnKirish;
    Dialog dialog;
    FirebaseDatabase database;
    DatabaseReference myRef;
    PreferenceData data;
    List<LoginModel> loginModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setDialog();
        data = new PreferenceData(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        btnKirish.setOnClickListener(this);
    }


    private void initViews() {
        txtName = findViewById(R.id.textInputLayout);
        txtPass = findViewById(R.id.txtPas);
        btnKirish = findViewById(R.id.btnKirish);
    }

    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(Objects.requireNonNull(txtName.getEditText()).getText().toString())) {
            txtName.setError("Foydalanuvchi nomini kiriting");
            return;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(txtPass.getEditText()).getText().toString())) {
            txtPass.setError("Parolni kiriting");
            return;
        }
        dialog.show();
        myRef.addListenerForSingleValueEvent(listener);


    }

    private void setDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            int i = 0;
            for (DataSnapshot ds : snapshot.getChildren()) {
                LoginModel model = ds.getValue(LoginModel.class);
                if (model != null) {
                    if (model.getName().equals(txtName.getEditText().getText().toString().trim()) && model.getPassword().equals(txtPass.getEditText().getText().toString().trim())) {
                        data.saveData(NAME, model.getName());
                        data.saveData(PASSWORD, model.getPassword());
                        data.saveData(KEY, ds.getKey());
                        data.saveData(ROLE, model.getRole());
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Ma'lumotlar tasdiqlandi!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                        break;
                    }
                    i++;
                }

            }
            if (i >= snapshot.getChildrenCount()) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "Ma'lumotar topilmadi!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            dialog.dismiss();
            Toast.makeText(LoginActivity.this, "Xatolik yuz berdi.", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onStart() {
        super.onStart();

        if (!(data.getData(KEY)).isEmpty()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}