package com.example.motu.alivemediaplayer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<SongsInfo> my_songs = new ArrayList<SongsInfo>();
    RecyclerView recyclerView;
    SeekBar seekBar;
    SongsAdapter songsAdapter;
    MediaPlayer alivemediaPlayer;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        seekBar = (SeekBar)findViewById(R.id.seekBar);

//        final SongsInfo s = new SongsInfo("Dil Juunglee"," Tanishk Bagchi",
//                "View more at: http://www.hungama.com/album/dil-juunglee/33545605/");
//        my_songs.add(s);

        songsAdapter = new SongsAdapter(this,my_songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(songsAdapter);

        songsAdapter.setOnitemClickListener(new SongsAdapter.OnitemClickListener() {
            @Override
            public void onItemClick(final Button b, View v, final SongsInfo obj, int position) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {


                try {

                    if (b.getText().toString().equals("Stop")){
                        b.setText("Play");
                        alivemediaPlayer.stop();
                        alivemediaPlayer.reset();
                        alivemediaPlayer.release();
                        alivemediaPlayer = null;
                    }else {

                        alivemediaPlayer = new MediaPlayer();
                        alivemediaPlayer.setDataSource(obj.getSongUrl());
                        alivemediaPlayer.prepareAsync();
                        alivemediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                                seekBar.setProgress(0);
                                seekBar.setMax(mp.getDuration());
                                b.setText("Stop");
                            }
                        });
                    }
                }catch (IOException e){}

                    }
                };
                handler.postDelayed(r,1000);
            }
        });

        CheckPermission();
        Thread tr = new MyThread();
        tr.start();

    }

    public class MyThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                if (alivemediaPlayer!=null) {
                    seekBar.setProgress(alivemediaPlayer.getCurrentPosition());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void CheckPermission(){
        if (Build.VERSION.SDK_INT >= 23){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1234);
            return;
        }
        }else {
            loadSongs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1234:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadSongs();
                }else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    CheckPermission();
                }
                break;

                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void loadSongs(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = getContentResolver().query(uri, null, selection, null,null);

        if (cursor!=null){
            if (cursor.moveToFirst()){
                do {

                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                SongsInfo s = new SongsInfo(name,artist,url);
                my_songs.add(s);
                }while (cursor.moveToNext());
                }
                cursor.close();
            songsAdapter = new SongsAdapter(this,my_songs);
            }
        }
    }
