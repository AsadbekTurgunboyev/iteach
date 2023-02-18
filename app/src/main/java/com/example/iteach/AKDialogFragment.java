package com.example.iteach;

import android.animation.Animator;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.example.iteach.model.PaymentReceiverModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.UUID;

public class AKDialogFragment extends DialogFragment {
    private static final String TAG = "AKDialogFragment";

    private TextInputLayout txtName, txtSurname, txtDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_ak, container, false);

        txtName = rootView.findViewById(R.id.edt_name);
        txtSurname = rootView.findViewById(R.id.edt_familiya);
        txtDesc = rootView.findViewById(R.id.edt_desc);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Dialog title");
        ImageView imageView = rootView.findViewById(R.id.imageView);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (imageView.getVisibility() != View.VISIBLE){
            int w  = container.getMeasuredWidth();
            int h = container.getMeasuredHeight();
            Bitmap bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            container.draw(canvas);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                double radius = Math.hypot(w, h);
                Animator anim = ViewAnimationUtils.createCircularReveal(container,w/2,h/2,0f, (float) radius);
                anim.setDuration(400L);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        imageView.setImageDrawable(null);
                        imageView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                anim.start();

        }

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_ak, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            // handle confirmation button click here

            if (TextUtils.isEmpty(Objects.requireNonNull(txtName.getEditText()).getText().toString()) || TextUtils.isEmpty(Objects.requireNonNull(txtSurname.getEditText()).getText().toString()) || TextUtils.isEmpty(Objects.requireNonNull(txtDesc.getEditText()).getText().toString())){
                Toast.makeText(getContext(), "Malumotlarni kiriting!", Toast.LENGTH_SHORT).show();
            } else {
                String name = txtName.getEditText().getText().toString().trim();
                String surname = txtSurname.getEditText().getText().toString().trim();
                String desc = txtDesc.getEditText().getText().toString().trim();
                String key = UUID.randomUUID().toString();

                PaymentReceiverModel model = new PaymentReceiverModel(name, surname, desc, key);

                FirebaseDatabase realtime = FirebaseDatabase.getInstance();
                DatabaseReference reference = realtime.getReference("Clients").child(key);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()){
                                    Toast.makeText(getContext(), "Malumotlar saqlandi!", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Xatolik!", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return true;
        } else if (id == android.R.id.home) {
            // handle close button click here
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
