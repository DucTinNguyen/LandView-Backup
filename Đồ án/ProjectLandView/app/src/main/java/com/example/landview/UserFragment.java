package com.example.landview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.landview.UserFragmentSection.UserPlansActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class UserFragment extends Fragment {
    private ImageView imguser;
    private TextView txtName,txtInformation;
    private Button btnLogout, btnChangePass, btnPolicy, btnPlan;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user, container, false);
        imguser = view.findViewById(R.id.imgUser);
        txtName = view.findViewById(R.id.userName);
        txtInformation = view.findViewById(R.id.inforUser);
        btnChangePass = view.findViewById(R.id.btnChange);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnPolicy = view.findViewById(R.id.btnPolicy);
        btnPlan = view.findViewById(R.id.btnPlan);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Lấy username từ firestore
        takeUserName();

        //sét sự kiện ảnh
        imguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialog();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), SplashScreen.class));
            }
        });

        btnPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TermsOfServices.class));
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChangePassword.class));
            }
        });
        btnPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), UserPlansActivity.class));
            }
        });
        return view;
    }

    private void displayDialog() {
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.layout_custom_dialog_user);
        dialog.show();
    }

    private void takeUserName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        db.collection("users").whereEqualTo("UID", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        txtName.setText(documentSnapshot.getData().get("username").toString());
//                        Log.e(TAG, documentSnapshot.getId() + " -> " + documentSnapshot.getData());
                    }
                }else{
                    Toast.makeText(getContext(), "Failed to take username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}