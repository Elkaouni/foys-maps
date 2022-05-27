package com.example.playfigros.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.playfigros.R;
import com.example.playfigros.Song;

public class SongListAdapter {/*extends RecyclerView.Adapter<SongListAdapter.MyviewHolder> {

        private ArrayList<Song> songList = new ArrayList<>(); //get from database

        @Override
        public int getItemCount() {
                return songList.size();
                }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.list_item, parent, false);
                return new MyViewHolder(view);
                }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
                Song song = (Song) song.get(position);
                holder.name.setText(song.getName());
                holder.author.setText(song.getAuthor());
                holder.cover.setImageResource(song.getCover());
        }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        private final TextView author;
        private final TextView name;
        private final ImageView cover;
        private Song current_sing;
        public MyViewHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.song_name);
            author = itemView.findViewById(R.id.song_athor);
            cover=  itemView.findViewById(R.id.song_cover);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    current_sing = (Song) current_sing.get(getLayoutPosition());
                        Intent intent =
                }
            });
        }

    }*/

}