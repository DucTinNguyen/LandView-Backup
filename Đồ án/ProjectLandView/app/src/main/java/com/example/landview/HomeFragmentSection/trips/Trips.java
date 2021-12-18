package com.example.landview.HomeFragmentSection.trips;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.landview.MainActivity;
import com.example.landview.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Trips extends AppCompatActivity  {
    EditText edtPlanName, edDes, edtDescription, edDaygo,edDayend;
    Button btnCreatePlan;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    final String TAG = "Trips";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trips);

        edtPlanName = findViewById(R.id.edtPlanName);
        edDes = findViewById(R.id.edtDes);
        edtDescription = findViewById(R.id.edtDescription);
        edDaygo = findViewById(R.id.edtDateGO);
        edDayend = findViewById(R.id.edtDateEnd);
        btnCreatePlan = findViewById(R.id.buttonCreate);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String planName = edtPlanName.getText().toString();
                String textDes = edDes.getText().toString();
                String description = edtDescription.getText().toString();
                String textdaygo = edDaygo.getText().toString();
                String textdayend = edDayend.getText().toString();

                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();

                Map<String, Object> map = new HashMap<>();
                map.put("name", planName);
                map.put("description", description);
                map.put("destination", textDes);
                map.put("dateStart", textdaygo);
                map.put("dateEnd", textdayend);

                db.collection("users").document(uid).collection("plan").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.update("id", documentReference.getId()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "Succeed to set ID and plan");
                            }
                        });
                        Toast.makeText(Trips.this, "Plan created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Trips.this, MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Trips.this, "Failed to create plan", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
}
