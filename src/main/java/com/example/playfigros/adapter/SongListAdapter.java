package com.example.playfigros.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playfigros.MainActivity;
import com.example.playfigros.R;
import com.example.playfigros.Song;
import com.example.playfigros.Start;

import java.util.ArrayList;
import java.util.Arrays;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MyViewHolder> {
    private ArrayList<Song> songList = new ArrayList<>(Arrays.asList(
            new Song("Disorder", "Hyun ft. Yuri",R.drawable.disorder_cover, R.drawable.disorder_map, R.raw.disorder_hyun),
            new Song("Chronos", "Ike Iveland Cover",R.drawable.chornos_cepheid_cover, R.drawable.chronos_map,R.raw.chronos_cepheid),
            new Song("Senbozakura", "Hatsune Miku",R.drawable.senbozakura_cover, R.drawable.senbozakura_map,R.raw.senbonzakura),
            new Song("Gurenge", "Lisa",R.drawable.gurenge_cover, R.drawable.gurenge_map,R.raw.gurenge_lisa)
            ));


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.song_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song song = (Song) songList.get(position);
        if(holder.name != null)
            holder.name.setText(song.getName());
        if(holder.author != null)
            holder.author.setText(song.getAuthor());
        if(holder.cover != null) {
            holder.cover.setImageResource(song.getCover());
        }

        holder.map_bg = song.getWallpaper();
        holder.song_mp3 = song.getSong();
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView author;
        public final TextView name;
        public final ImageView cover;
        private final Context context;
        private int map_bg;
        private int song_mp3;
        public Song current_sing;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            author = itemView.findViewById(R.id.item_author);
            cover=  itemView.findViewById(R.id.item_cover);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    current_sing = (Song) songList.get(getLayoutPosition());
                    Intent intent = new Intent( context , Start.class);
                    passExtras(intent);
                    context.startActivity(intent);

                    /*new AlertDialog.Builder(context)
                            .setTitle(current_sing.getName()+"\n"+current_sing.getAuthor())
                            .show(); */
                }
            });
        }

        public void passExtras(Intent intent){
            intent.putExtra("song_title", name.getText());
            intent.putExtra("song_cover", current_sing.getCover());
            intent.putExtra("song_author", author.getText());
            intent.putExtra("song_map_bg", current_sing.getWallpaper());
            intent.putExtra("song_mp3", current_sing.getSong());
        }

    }
}