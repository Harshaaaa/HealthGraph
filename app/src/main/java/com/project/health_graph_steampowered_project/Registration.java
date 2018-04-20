package com.project.health_graph_steampowered_project;

/**
 * Created by apiiit-rkv on 18/4/18.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {
    EditText email;
    TextInputLayout password;
    private FirebaseAuth firebase;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = FirebaseAuth.getInstance();
        firebase = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.emailid);
        password = (TextInputLayout) findViewById(R.id.passwordd);
        progressDialog = new ProgressDialog(this);
        getSupportActionBar().hide();
        if(!isNetworkAvailable())
            Toast.makeText(this, "Please Enable Internet Connection", Toast.LENGTH_SHORT).show();
    }
    public void regtofire(View view) {
        if(!isNetworkAvailable())
            Toast.makeText(this, "Please Enable Internet Connection", Toast.LENGTH_SHORT).show();
        String pass = password.getEditText().getText().toString().trim();
        String mail = email.getText().toString().trim();
        if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(mail)) {
            Toast.makeText(this, "Please Fill ALL Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (pass.length()<=8)
        {
            password.setError("Please Enter Atleast 8 Charcters");
            return;
        }else {
            progressDialog.setMessage("Creating User.....");
            progressDialog.show();
            progressDialog.setCancelable(false);
            firebase.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (!task.isSuccessful()) {
                        Toast.makeText(Registration.this, "Account Not Created", Toast.LENGTH_SHORT).show();
                    } else {
                        sendVerificationEmail();

                    }
                }
            });
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(Registration.this, Sign_In.class));
                            finish();
                            Toast.makeText(Registration.this, "Click on the verification link send to your mail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
