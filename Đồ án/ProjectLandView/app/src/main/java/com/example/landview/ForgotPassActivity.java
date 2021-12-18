package com.example.landview;

import android.app.ProgressDialog;
import android.app.UiAutomation;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPassActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button resetPasswordBtn;
    private EditText resetEmailEt;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_forgot_password);
        resetPasswordBtn = findViewById(R.id.reset_password_btn);
        resetEmailEt = findViewById(R.id.reset_email_et);

        // Tạo propressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = resetEmailEt.getText().toString().trim();
                if (checkData(email)) {
                    resetEmail(email);
                }
            }
        });

    }

    private void resetEmail(String email) {
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPassActivity.this, "Email send.", Toast.LENGTH_SHORT).show();
                } else {
                    // Kì lạ cái này ko bắt được exception
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPassActivity.this, "Can't send email.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // Kiểm tra địa chỉ email
    boolean checkData(String email){
        if(TextUtils.isEmpty(email)){
            resetEmailEt.setError("Trống");
            resetEmailEt.setFocusable(true);
            return false;
        } else if( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){ // sai form email
            resetEmailEt.setError("Định dạng email không đúng");
            resetEmailEt.setFocusable(true);
            return false;
        }
        return true;
    }
}
