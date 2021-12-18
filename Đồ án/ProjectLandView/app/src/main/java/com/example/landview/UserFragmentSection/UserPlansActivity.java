package com.example.landview.UserFragmentSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.landview.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserPlansActivity extends AppCompatActivity {
    RecyclerView plansRV;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    List<PlanItem> list;
    PlansAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_plans);

        //setup cho recycler view
        plansRV  = findViewById(R.id.plansRV);
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        loadPlans();

        //setup Adapter
        adapter = new PlansAdapter(UserPlansActivity.this, list);
        plansRV.setAdapter(adapter);
        plansRV.setLayoutManager(new LinearLayoutManager(UserPlansActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter.notifyDataSetChanged();
    }

    private void loadPlans() {
        FirebaseUser user = mAuth.getCurrentUser();

        db.collection("users").document(user.getUid()).collection("plan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        PlanItem pi = new PlanItem(document.get("name").toString(), document.get("description").toString(),
                                document.get("destination").toString(), document.get("id").toString(),
                                document.get("dateEnd").toString(), document.get("dateStart").toString());
                        list.add(pi);
                    }
                }
            }
        });
    }
}