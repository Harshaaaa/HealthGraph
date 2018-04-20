package com.project.health_graph_steampowered_project;

/**
 * Created by apiiit-rkv on 18/4/18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.Collections;
import java.util.List;

public class GameRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<GameData> data= Collections.emptyList();
    Context con;
    public GameRecyclerView(Context context, List<GameData> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.single_game_list, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        final GameData current=data.get(position);
        myHolder.GameName.setText(current.gameName);
        myHolder.playercount.setText("No of Players: " + current.playercount);
        Glide.with(context).load(current.imgUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(myHolder.thumbnail);
        myHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent=new Intent(con,Game_Description.class);
                intent.putExtra("key",current.id);
                con.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        TextView GameName;
        ImageView thumbnail;
        TextView playercount;
        public MyHolder(View itemView) {
            super(itemView);
            con=itemView.getContext();
            GameName= (TextView) itemView.findViewById(R.id.GameName);
            thumbnail= (ImageView) itemView.findViewById(R.id.thumbnail);
            playercount = (TextView) itemView.findViewById(R.id.playercount);
        }

    }

}
