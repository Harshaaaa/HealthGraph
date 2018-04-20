package com.project.health_graph_steampowered_project;

/**
 * Created by apiiit-rkv on 18/4/18.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Steam_Powered_Home extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private RecyclerView gamecycyler;
    FirebaseAuth auth;
    private GameRecyclerView mAdapter;
    private GoogleApiClient mGoogleApiClient;
    private String object="405310";
    public static int flag = 0;
    private String murl;
    String[] keys = {"405310", "730", "271590", "639170", "378540", "203160", "493490", "435150", "606890", "49600"};
    List<GameData> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steam_powered_home);
        getSupportActionBar().setTitle("Games List");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        auth=FirebaseAuth.getInstance();
        if(!isNetworkAvailable())
            Toast.makeText(this, "Please Enable Internet Connection", Toast.LENGTH_SHORT).show();

        flag=0;
        int i;
        for (i = 0; i < 10; i++) {
            MyAsyncTask a = new MyAsyncTask();
            a.execute();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Are you sure want to Exit the App?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), Splash_Screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        }).setNegativeButton("No", null).show();
    }
    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(Steam_Powered_Home.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
            pdLoading.setCancelable(false);


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            object = keys[flag];
            murl = "https://store.steampowered.com/api/appdetails/?appids=" + object;
            String url = "https://steamdb.info/api/GetGraph/?type=concurrent_week&appid=" + object;
            JSON_Generator sh = new JSON_Generator();
            String jsonStr = sh.makeServiceCall(murl);
            String playcount = sh.makeServiceCall(url);
            GameData gameData = new GameData();
            if (playcount != null) {
                try {
                    JSONObject jsonObj = new JSONObject(playcount);
                    JSONObject c = jsonObj.getJSONObject("data");
                    JSONArray contacts = c.getJSONArray("values");
                    gameData.playercount = contacts.length() + "";
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });

                } catch (Throwable tx) {

                    Toast.makeText(Steam_Powered_Home.this, jsonStr + "", Toast.LENGTH_SHORT).show();
                }
            }

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject b = jsonObj.getJSONObject(object);
                    JSONObject c = b.getJSONObject("data");
                    gameData.id = object;
                    gameData.gameName = c.getString("name");
                    ;
                    gameData.imgUrl = c.getString("header_image");
                    data.add(gameData);

                } catch (final JSONException e) {
                    Log.e("sd", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e("AG", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            flag++;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pdLoading.dismiss();

            pdLoading.dismiss();
            gamecycyler = (RecyclerView) findViewById(R.id.healgraph);
            mAdapter = new GameRecyclerView(Steam_Powered_Home.this, data);
            gamecycyler.setAdapter(mAdapter);
            gamecycyler.setLayoutManager(new LinearLayoutManager(Steam_Powered_Home.this));

        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.Logout||item.getItemId()==R.id.logoutu) {

            auth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Toast.makeText(Steam_Powered_Home.this, "SignOut...", Toast.LENGTH_SHORT).show();
                        }
                    });

            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                        }
                    });
            Intent a = new Intent(this,Sign_In.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        }
        return true;

    }
}


    

