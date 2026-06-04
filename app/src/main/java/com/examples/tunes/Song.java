package com.examples.tunes;
import android.net.Uri;
public class Song {
    private String title;
    private String artist;
    private Uri songUri;
    private Uri albumArtUri;

    public Song(String title,String artist,Uri songUri,Uri albumArtUri){
        this.title=title;
        this.artist=artist;
        this.songUri=songUri;
        this.albumArtUri=albumArtUri;
    }

    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public Uri getSongUri(){return songUri;}
    public Uri getAlbumArtUri(){return albumArtUri;}
}
