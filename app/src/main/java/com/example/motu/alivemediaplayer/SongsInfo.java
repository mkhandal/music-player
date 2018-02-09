package com.example.motu.alivemediaplayer;

/**
 * Created by motu on 9/2/18.
 */

public class SongsInfo {
    public String SongName, ArtistName, SongUrl;

    public SongsInfo(){
    }

    public SongsInfo(String songName, String artistName, String songUrl) {
        SongName = songName;
        ArtistName = artistName;
        SongUrl = songUrl;
    }

    public String getSongName() {
        return SongName;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public String getSongUrl() {
        return SongUrl;
    }
}
