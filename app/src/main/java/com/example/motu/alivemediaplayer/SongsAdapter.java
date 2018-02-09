package com.example.motu.alivemediaplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by motu on 9/2/18.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongHolder> {

    ArrayList<SongsInfo> my_songs;
    Context context;

    OnitemClickListener onitemClickListener;

    SongsAdapter(Context context, ArrayList<SongsInfo> my_songs){
            this.context = context;
            this.my_songs = my_songs;
    }

    public interface OnitemClickListener{
        void onItemClick(Button b, View v, SongsInfo obj, int position);
    }

    public void setOnitemClickListener(OnitemClickListener onitemClickListener){
        this.onitemClickListener = onitemClickListener;
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View myview = LayoutInflater.from(context).inflate(R.layout.row_song,viewGroup, false);
        return new SongHolder(myview);
    }

    @Override
    public void onBindViewHolder(final SongHolder songHolder, final int i) {
        final SongsInfo c = my_songs.get(i);
        songHolder.SongName.setText(c.SongName);
        songHolder.ArtistName.setText(c.ArtistName);
        songHolder.btnaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onitemClickListener != null){
                    onitemClickListener.onItemClick(songHolder.btnaction,v,c,i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {

        TextView SongName, ArtistName;
        Button btnaction;
        public SongHolder(View itemView) {
            super(itemView);
            SongName = (TextView) itemView.findViewById(R.id.evSongName);
            ArtistName = (TextView) itemView.findViewById(R.id.tvHardDiskName);
            btnaction = (Button) itemView.findViewById(R.id.bt_action);
        }
    }
}
