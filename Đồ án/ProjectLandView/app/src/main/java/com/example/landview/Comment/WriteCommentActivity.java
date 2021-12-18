package com.example.landview.Comment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.landview.AppUser.AppUser;
import com.example.landview.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class WriteCommentActivity extends AppCompatActivity {

    private AppUser appUser = AppUser.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionRef;
    private String type;
    private String placeId;

    private RatingBar rbReview;
    private EditText etReview;
    private Button btnUpload;
    private TextView tvRateChoice;

    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    private void initUi(){
        rbReview= findViewById(R.id.rb_review);
        etReview  = findViewById(R.id.et_review);
        btnUpload = findViewById(R.id.btn_upload_review);
        tvRateChoice = findViewById(R.id.tv_rate_choice);
    }

    private void getData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle  != null){
            type = bundle.getString("type");
            placeId = bundle.getString("placeId");
            createCollectionRef();
        } else {
            Toast.makeText(this, "Kiểm tra lại WriteCommentActivity", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    // Tạo sẵn một collection reference
    private void createCollectionRef(){
        switch (type){
            case "landscape":
                collectionRef = db.collection("landscapes").document(placeId).collection("review");
                break;
            case "hotel":
                collectionRef  = db.collection("hotels").document(placeId).collection("review");
                break;
            case "restaurant":
                collectionRef  = db.collection("restaurants").document(placeId).collection("review");
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        // Tạo progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait while loading...");
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);

        getData();
        initUi();

        // btn upload comment
        btnUpload.setOnClickListener(btnUploadClickListener);

        // Khi người dùng vote sao sẽ hiện lên chữ
        rbReview.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                tvRateChoice.setVisibility(View.VISIBLE);
               if(rating > 4){
                   tvRateChoice.setText("Tuyệt vời");
               } else if(rating > 3) {
                   tvRateChoice.setText("Tốt");
               } else if (rating > 2){
                   tvRateChoice.setText("Trung bình");
               } else if( rating > 1) {
                   tvRateChoice.setText("Tệ");
               } else {
                   tvRateChoice.setText("Cực tệ");
               }

            }
        });

    }

    private View.OnClickListener btnUploadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            float rating = rbReview.getRating();
            String comment = etReview.getText().toString();

            // Kiểm tra input của người dùng đã hợp lệ chưa
            if(rating == 0 || comment.equals("")){
                Toast.makeText(WriteCommentActivity.this, "Hãy nhận xét và rating", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show(); // show progress dialog

                // Tạo hashmap
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("userId", appUser.getUID());
                hashMap.put("avatar", appUser.getAvatar());
                hashMap.put("rating", rating);
                hashMap.put("comment", comment);
                hashMap.put("date", Timestamp.now());
                hashMap.put("username", appUser.getUserName());

                collectionRef.document(appUser.getUID()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {  // Nếu thành công

                        progressDialog.dismiss();  // bỏ progress dialog
                        Toast.makeText(WriteCommentActivity.this, "Gửi nhận xét thành công", Toast.LENGTH_SHORT).show();
                        // quay về activity trc đó
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { // nếu thất bại
                        progressDialog.dismiss();  // bỏ progress dialog
                        Toast.makeText(WriteCommentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    };
}