package com.example.landview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int SIGN_IN_WITH_GOOGLE = 333;

    // View
    private EditText editTextemailLogin, editTextpassLogin;
    private TextView txtForgot, txtSignupnow;
    private Button btnLogin;
    private ImageView fbImage, ggImage;


    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    // Google
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        //ánh xạ view
        editTextemailLogin = findViewById(R.id.edtEmail_login);
        editTextpassLogin = findViewById(R.id.edtPass_login);
        txtForgot = findViewById(R.id.txtForgot_password);
        txtSignupnow = findViewById(R.id.txtSignupNow);
        btnLogin = findViewById(R.id.btn_Login);
        fbImage = findViewById(R.id.facebook_login);
        ggImage = findViewById(R.id.gg_login);

        //gọi instance của class FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("MODE_LOGIN", MODE_PRIVATE);
        //lấy ra
        editTextemailLogin.setText(sharedPreferences.getString("Email", ""));
        //thay đổi label action bar
//        getSupportActionBar().setTitle("Login");

        //sét sự kiện button
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CheckData();
//            }
//        });

        //sự kiện textview sign up now
        txtSignupnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //sự kiện quên mật khẩu:
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);
            }
        });
        //sự kiện đăng nhập fb,gg chưa làm.
        //đăng nhập chay
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextemailLogin.getText().toString().trim();
                String password = editTextpassLogin.getText().toString();
                if (CheckData(email, password)) {
                    login(email, password);
                }
            }
        });

        // Khởi tạo progress dialog
        // Tạm thời xài cái này, trong tương lai xài ProgressBar
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        createGoogleSignIn();

        // Đăng nhập với google
        ggImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

    }
    private void createGoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    private void login(String email, String password) {
        progressDialog.show(); // gọi progressDialog
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss(); // Tắt progressDialog
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    progressDialog.dismiss(); // Tắt progressDialog
                    Log.d(TAG, "onComplete: fail to login");

                    // throw ra các exception
                    try {
                        throw task.getException();
                    }
                    // Bắt exception liên quan mạng
                    catch (FirebaseNetworkException networkException) {

                        Toast.makeText(LoginActivity.this, "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
                    }
                    // Bắt exception liên quan xác thực
                    catch (FirebaseAuthException firebaseAuth) {
                        // Lấy mã lỗi

                        String errorCode = firebaseAuth.getErrorCode();
                        switch (errorCode) {

                            // Sai mật khẩu
                            case "ERROR_WRONG_PASSWORD":
                                Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                editTextpassLogin.setError("Wrong password");
                                editTextpassLogin.requestFocus();
                                break;

                            // Tài khoản ko tồn tại
                            case "ERROR_USER_NOT_FOUND":
                                Log.d(TAG, "onComplete: Not found user");
                                Toast.makeText(LoginActivity.this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
                                editTextemailLogin.setError("User not found");
                                editTextemailLogin.requestFocus();
                                break;

                            // Lỗi khác liên quan đến đăng nhập
                            default:
                                Toast.makeText(LoginActivity.this, "Error: " + errorCode, Toast.LENGTH_SHORT).show();
                        }
                        // Lỗi khác
                    } catch (Exception e) {

                        Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean CheckData(String email, String password) {

//        if(textEmail.length()==0 || textPass.length() ==0)
//        {
//            Toast.makeText(this,"Login failed",Toast.LENGTH_LONG).show();
//        }
//        else
//        {
//            Toast.makeText(this,"Login succesfull",Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
            ///SHAREREFERENCES
            String textEmail = editTextemailLogin.getText().toString();
            String textPass = editTextpassLogin.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Email",textEmail);
            editor.putString("Password",textPass);
            editor.apply();
//        }
        if (TextUtils.isEmpty(email)) {
            editTextemailLogin.setError("Email section can't be empty");
            editTextemailLogin.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { // Nếu ko đụng định dạng email
            editTextemailLogin.setError("Bad form email address");
            editTextemailLogin.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            editTextpassLogin.setError("Password section can't be empty");
            editTextpassLogin.requestFocus();
            return false;
        }
        return true;

    }

    private void signInWithGoogle(){
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, SIGN_IN_WITH_GOOGLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SIGN_IN_WITH_GOOGLE:
                progressDialog.show();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try{
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "onActivityResult: " + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (Exception e) {
                    Log.d(TAG, "onActivityResult: Google fail.");
                }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Log.d(TAG, "onComplete: Google Sign In successful");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else{
                            progressDialog.dismiss();
                            Log.d(TAG, "onComplete: Google Sign in fail");
                            try {
                                throw task.getException();
                            }
                            catch (FirebaseNetworkException networkException){
                                Toast.makeText(LoginActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e){
                                Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }


}
