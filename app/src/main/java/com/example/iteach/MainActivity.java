package com.example.iteach;

import static com.example.iteach.Const.ROLE;
import static com.example.iteach.Const.ROLE_ADMIN;
import static com.example.iteach.Const.ROLE_SUPERADMIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.iteach.HomeFragment.HomeFragment;
import com.example.iteach.PrefData.PreferenceData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    FloatingActionButton fbnAddUser;
    PreferenceData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        data = new PreferenceData(this);
        bottomNavigationView.setOnItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
        fbnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                AKDialogFragment newFragment = new AKDialogFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
            }
        });

    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNav);
        frameLayout = findViewById(R.id.frame);
        fbnAddUser = findViewById(R.id.btnAddPerson);

    }

    NavigationBarView.OnItemSelectedListener navListener = item -> {

        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.icHome:
                selectedFragment = new HomeFragment();
                break;
            case R.id.transac:
                break;
            case R.id.charPie:
                break;

        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,selectedFragment).commit();
        }
        return true;
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (data.getData(ROLE).equals(ROLE_ADMIN) || data.getData(ROLE).equals(ROLE_SUPERADMIN)){
            fbnAddUser.setVisibility(View.VISIBLE);
        }else {
            fbnAddUser.setVisibility(View.GONE);
        }
    }
}