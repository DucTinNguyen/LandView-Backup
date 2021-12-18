package com.example.landview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    final String TAG = "ChangePassword";
    EditText oldPass, newPass, edtRePass;
    Button btnChangePass;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_password);

        //ánh xạ
        oldPass = findViewById(R.id.edtOldPass);
        newPass = findViewById(R.id.edtNewPass);
        edtRePass = findViewById(R.id.edtRepeatNewPass);
        btnChangePass = findViewById(R.id.btnChange);

        mAuth = FirebaseAuth.getInstance();

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangePass();
            }
        });
    }

    private void onClickChangePass() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(checkPasswordInput() == true){
            AuthCredential cred = EmailAuthProvider.getCredential(user.getEmail().toString(), oldPass.getText().toString());
            user.reauthenticate(cred).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
//                        Toast.makeText(ChangePassword.this, "Succeed to re-authenticate", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Re-authentication success");
                        updatePassword(user);
                    }else{
                        Toast.makeText(ChangePassword.this, "Your old password is incorrect!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(ChangePassword.this, "Something went wrong, please double check the information you gave us.", Toast.LENGTH_LONG).show();
        }

    }
    private void updatePassword(FirebaseUser user){
        user.updatePassword(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(ChangePassword.this, MainActivity.class));
                    Toast.makeText(ChangePassword.this, "Change password successful!", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Change Password Succeessful");
                }else{
                    Toast.makeText(ChangePassword.this, "Failed to change password!", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Failed to change password");
                }
            };
        });
    }

    private boolean checkPasswordInput(){
        String oldPassText = oldPass.getText().toString();
        String newPassText = newPass.getText().toString();
        String rePassText = edtRePass.getText().toString();
        if(TextUtils.isEmpty(oldPassText)){
            oldPass.setError("Old password can't be left empty!!");
            return false;
        }else if(TextUtils.isEmpty(newPassText)){
            newPass.setError("New password can't be left empty!!");
            return false;
        }else if(TextUtils.isEmpty(rePassText)){
            edtRePass.setError("You have to enter new password one more time!!");
            return false;
        }else if(!newPassText.equals(rePassText)){
            return false;
        }
        return true;
    }
}