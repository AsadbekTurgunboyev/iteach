package com.example.iteach.HomeFragment;

import static com.example.iteach.Const.currencyFormatter;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iteach.HomeFragment.adapter.PersonAdapter;
import com.example.iteach.R;
import com.example.iteach.avtivities.MyLoansActivity;
import com.example.iteach.avtivities.TransactionActivity;
import com.example.iteach.avtivities.WarehouseActivity;
import com.example.iteach.model.PaymentReceiverModel;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    TextView valueFinance;
    RecyclerView personRec;
    MaterialCardView oquvchilar, transactions, my_debts;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference money_ref = FirebaseDatabase.getInstance().getReference("Finance").child("Balance").child("money_sum");

        money_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int account_money = Integer.parseInt((String) snapshot.getValue());

                ValueAnimator animator = ValueAnimator.ofInt(0, account_money);
                animator.setDuration(1500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        valueFinance.setText(currencyFormatter(animation.getAnimatedValue().toString()));
                    }
                });
                animator.start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Clients");
        ArrayList<PaymentReceiverModel> list = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    PaymentReceiverModel model = ds.getValue(PaymentReceiverModel.class);
                    list.add(model);
                }

                personRec.setAdapter(new PersonAdapter(getContext(), list));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        rootRef.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container,    false);
        initViews(v);

        oquvchilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WarehouseActivity.class);
                intent.putExtra("action","ware");
                startActivity(intent);
            }
        });

        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TransactionActivity.class);
                startActivity(intent);
            }
        });

        my_debts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyLoansActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    private void initViews(View v) {
        valueFinance = v.findViewById(R.id.valueFinance);
        personRec = v.findViewById(R.id.perRec);
        oquvchilar = v.findViewById(R.id.card_oquvchilar);
        transactions = v.findViewById(R.id.card_transaction);
        my_debts = v.findViewById(R.id.card_my_loans);
    }
}