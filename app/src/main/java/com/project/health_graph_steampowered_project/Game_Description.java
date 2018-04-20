package com.project.health_graph_steampowered_project;

/**
 * Created by apiiit-rkv on 18/4/18.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;

public class Game_Description extends AppCompatActivity {
    Bundle bd;
    private String murl;
    RecyclerView recycle;
    String kk;
    TextView tv_name,tv_req_age,tv_con_support,tv_short_description,tv_support_lan,tv_mac_req,tv_linux_req;
    TextView tv_pc_req;
    ImageView iv;
    LinearLayout req_layoout;
    TextView req_text,tv_about_game,tv_descprtion,support,published;
    String name,req_age,controller_support,short_description,support_languages,header_image,webiste,count,pc_req,mac_req,linux_req,release_date,support_info,about_game,descprtion;
    boolean cost;
    String[] arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_description);
        bd=getIntent().getExtras();
        murl = "https://store.steampowered.com/api/appdetails/?appids="+bd.getString("key");
        tv_name=(TextView)findViewById(R.id.name);
        if(!isNetworkAvailable())
            Toast.makeText(this, "Please Enable Internet Connection", Toast.LENGTH_SHORT).show();
        tv_req_age=(TextView)findViewById(R.id.required_age);
        tv_con_support=(TextView)findViewById(R.id.controller_support);
        tv_short_description=(TextView)findViewById(R.id.short_description);
        tv_support_lan=(TextView)findViewById(R.id.support_language);
        tv_mac_req=(TextView)findViewById(R.id.tv_mac_req);
        tv_linux_req=(TextView)findViewById(R.id.tv_linux_req);
        tv_pc_req=(TextView)findViewById(R.id.tv_pc_req);
        tv_about_game=(TextView)findViewById(R.id.tv_about_game);
        req_text=(TextView)findViewById(R.id.req_text);
        tv_descprtion=(TextView)findViewById(R.id.tv_descprtion);
        req_layoout=(LinearLayout)findViewById(R.id.req_layout);
        support=(TextView)findViewById(R.id.support);
        published=(TextView)findViewById(R.id.published);
        recycle=(RecyclerView)findViewById(R.id.recycle);
        iv=(ImageView)findViewById(R.id.header_image);
        AsyncFetch a=new AsyncFetch();
        a.execute();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private class AsyncFetch extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(Game_Description.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JSON_Generator sh = new JSON_Generator();
            String jsonStr = sh.makeServiceCall(murl);
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject b=jsonObj.getJSONObject(""+bd.getString("key"));
                JSONObject c=b.getJSONObject("data");
                name=c.getString("name");
                req_age=c.getString("required_age");
                controller_support=c.getString("controller_support");
                short_description=c.getString("short_description");
                cost=c.getBoolean("is_free");
                support_languages=c.getString("supported_languages");
                header_image=c.getString("header_image");
                webiste=c.getString("website");
                about_game=c.getString("about_the_game");
                release_date =c.getJSONObject("release_date").getString("date");
                support_info=c.getJSONObject("support_info").getString("url");
                pc_req=c.getJSONObject("pc_requirements").getString("recommended");
                mac_req=c.getJSONObject("mac_requirements").getString("recommended");
                linux_req=c.getJSONObject("linux_requirements").getString("recommended");
                descprtion=c.getString("detailed_description");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(jsonStr);
                JSONArray screenshots=jsonObj.getJSONObject(""+bd.getString("key")).getJSONObject("data").getJSONArray("screenshots");
                arr=new String[screenshots.length()];
                int i;
                for(i=0;i<screenshots.length();i++){
                    JSONObject json_data = screenshots.getJSONObject(i);
                    arr[i]=json_data.getString("path_thumbnail");
                }
                count=screenshots.length()+"ll";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            kk=jsonStr;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pdLoading.dismiss();
            getSupportActionBar().setTitle(name+"");
            ScreenShotAdapter ad=new ScreenShotAdapter(Game_Description.this,arr);
            recycle.setAdapter(ad);
            recycle.setLayoutManager(new LinearLayoutManager(Game_Description.this));
            tv_name.setText(name);
            Glide.with(getApplicationContext()).load(header_image)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .into(iv);
            if(req_age!=null) {
                if (Integer.parseInt(req_age) <= 0)
                    tv_req_age.setText("No Age Limit");
            }
            else
                tv_req_age.setText(""+req_age);
            String text="Not Mentioned";
            if(controller_support!=null)
            text = String.valueOf(controller_support.charAt(0)).toUpperCase() + controller_support.subSequence(1, controller_support.length());
            tv_con_support.setText(""+text);
            tv_short_description.setText(short_description);
            if(support_languages!=null) {
                support_languages = Html.fromHtml(support_languages) + "";
                support_languages = support_languages.replace("*", "");
                support_languages = support_languages.replace("\n", "");
            }
            if(linux_req!=null)
            linux_req=Html.fromHtml(linux_req)+"";
            if(pc_req!=null)
            pc_req=Html.fromHtml(pc_req)+"";
            if(mac_req!=null)
            mac_req=Html.fromHtml(mac_req)+"";
            tv_support_lan.setText(""+support_languages+".");
                tv_linux_req.setText(linux_req);
                tv_mac_req.setText(mac_req);
                tv_pc_req.setText(pc_req);
                if(linux_req==null&&mac_req==null&&pc_req==null)
                {
                    req_layoout.setVisibility(View.GONE);
                    req_text.setText("Requirements (Recommended) : Not Specified");
                }
                else {
                    if (linux_req==null)
                        tv_linux_req.setText("Not Specified");
                    if (pc_req==null)
                        tv_pc_req.setText("Not Specified");
                    if (mac_req==null)
                        tv_mac_req.setText("Not Specified");
                }
            if(about_game!=null)
            about_game=Html.fromHtml(about_game)+"";
            tv_about_game.setText(about_game);
            if(descprtion!=null) {
                descprtion = Html.fromHtml(descprtion) + "";
                tv_descprtion.setText(descprtion);
            }
            else
            tv_descprtion.setText("No Descprtion");
            published.setText(release_date);
            support.setText(support_info);
            pdLoading.dismiss();
        }

    }
}
