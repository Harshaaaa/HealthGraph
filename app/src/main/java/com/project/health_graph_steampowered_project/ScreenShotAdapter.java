package com.project.health_graph_steampowered_project;

/**
 * Created by apiiit-rkv on 20/4/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class ScreenShotAdapter extends RecyclerView.Adapter<ScreenShotAdapter.MyOwnHolder> {

    String [] data1;
    Context ctx;
    public ScreenShotAdapter(Context ct, String s1[])
    {
        ctx=ct;
        data1=s1;
    }
    @Override
    public MyOwnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater myinflater=LayoutInflater.from(ctx);
        View MyOwnView=myinflater.inflate(R.layout.screenshots,parent,false);
        return new MyOwnHolder(MyOwnView);
    }

    @Override
    public void onBindViewHolder(MyOwnHolder holder, int position) {
        Glide.with(ctx).load(data1[position])
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(holder.myimage);

    }
    @Override
    public int getItemCount() {
        return data1.length;
    }
    public class MyOwnHolder extends RecyclerView.ViewHolder{
        ImageView myimage;
        public MyOwnHolder(View itemView) {
            super(itemView);
            myimage=(ImageView)itemView.findViewById(R.id.screenshotimg);
        }
    }
}