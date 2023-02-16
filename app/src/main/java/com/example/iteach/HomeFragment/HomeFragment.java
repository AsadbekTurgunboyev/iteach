package com.example.iteach.HomeFragment;

import static com.example.iteach.Const.currencyFormatter;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iteach.HomeFragment.adapter.PersonAdapter;
import com.example.iteach.R;


public class HomeFragment extends Fragment {
    TextView valueFinance;
    RecyclerView personRec;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ValueAnimator animator = ValueAnimator.ofInt(0, 23000000);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                valueFinance.setText(currencyFormatter(animation.getAnimatedValue().toString()));
            }
        });
        animator.start();
        personRec.setAdapter(new PersonAdapter(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container,    false);
        initViews(v);

        return v;
    }

    private void initViews(View v) {
        valueFinance = v.findViewById(R.id.valueFinance);
        personRec = v.findViewById(R.id.perRec);
    }
}