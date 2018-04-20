    package com.project.health_graph_steampowered_project;

    /**
    * Created by apiiit-rkv on 18/4/18.
    */

    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.design.widget.TextInputLayout;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.text.TextUtils;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;
    import com.google.android.gms.auth.api.Auth;
    import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
    import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
    import com.google.android.gms.auth.api.signin.GoogleSignInResult;
    import com.google.android.gms.common.ConnectionResult;
    import com.google.android.gms.common.SignInButton;
    import com.google.android.gms.common.api.GoogleApiClient;
    import com.google.android.gms.common.api.OptionalPendingResult;
    import com.google.android.gms.common.api.ResultCallback;
    import com.google.android.gms.common.api.Status;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;

    public class Sign_In extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton gsignin;
    String personName,personPhotoUrl,email;
    FirebaseAuth auth;
    FirebaseUser fireuser;
    ProgressDialog progressDialog;
    EditText ed;
    TextInputLayout textInputLayout;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        gsignin = (SignInButton) findViewById(R.id.login);
        ed=(EditText)findViewById(R.id.emailaddress);
        textInputLayout=(TextInputLayout)findViewById(R.id.password);
        auth=FirebaseAuth.getInstance();
        fireuser=auth.getCurrentUser();
        progressDialog=new ProgressDialog(this);
        if(!isNetworkAvailable())
            Toast.makeText(this, "Please Enable Internet Connection", Toast.LENGTH_SHORT).show();
        getSupportActionBar().hide();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                gsignin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isNetworkAvailable())
                            Toast.makeText(Sign_In.this, "Please Enable Internet Connection", Toast.LENGTH_SHORT).show();
                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent,SIGN_IN);
                    }
                });
            }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Are willing to close APP?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), Splash_Screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        }).setNegativeButton("No", null).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (auth.getCurrentUser()!=null)
            checkIfEmailVerified();
        else if (opr.isDone()) {
            startActivity(new Intent(this,Steam_Powered_Home.class));
        }
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            Toast.makeText(Sign_In.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,Steam_Powered_Home.class));
        }
        else
        {
            Toast.makeText(this, "Please Verify Your Mail", Toast.LENGTH_SHORT).show();
            sendVerificationEmail();
            FirebaseAuth.getInstance().signOut();
        }
    }
    public void LoginTOSteam(View view) {
        if(!isNetworkAvailable())
            Toast.makeText(this, "Please Enable Internet Connection", Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("Loging You In.....");
        String name=ed.getText().toString().trim();
        String pass=textInputLayout.getEditText().getText().toString().trim();
        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Please Enter mail and password", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            progressDialog.show();
            progressDialog.setCancelable(false);
            auth.signInWithEmailAndPassword(name, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (!task.isSuccessful()) {
                                Toast.makeText(Sign_In.this, "Incoorrect Details..", Toast.LENGTH_SHORT).show();

                            } else {
                                checkIfEmailVerified();
                            }
                        }
                    });
        }

    }
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Sign_In.this, "Sended to your mailid", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                });
    }
     private void handleSignInResult(GoogleSignInResult result)
     {
            if (result.isSuccess()) {
                    Toast.makeText(this, "Login SucessFull", Toast.LENGTH_SHORT).show();
                    GoogleSignInAccount acct = result.getSignInAccount();;
                    personName = acct.getDisplayName();
                    personPhotoUrl = acct.getPhotoUrl().toString();
                    email = acct.getEmail();
                    startActivity(new Intent(this,Steam_Powered_Home.class));
                }
      }
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == SIGN_IN) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleSignInResult(result);
                }
            }
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void createAcoount(View view) {
        startActivity(new Intent(this,Registration.class));
    }
    }
