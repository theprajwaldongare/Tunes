package com.examples.tunes;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PlayerActivity extends AppCompatActivity {
//    TextView songPlay;
    private FloatingActionButton playPauseBtn;
    private SeekBar songProgress;
    private TextView songName, artistName;
    private ImageView albumArt;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player);

        Intent incomeIntent = getIntent();
//        songPlay = findViewById(R.id.songPlay);
        albumArt=findViewById(R.id.albumArt);
        songName = findViewById(R.id.songName);
        artistName = findViewById(R.id.artistName);
        playPauseBtn = findViewById(R.id.playPause);
        songProgress = findViewById(R.id.songProgress);


        if (incomeIntent!=null){
            String title = incomeIntent.getStringExtra("SONG_TITLE");
            String artist = incomeIntent.getStringExtra("SONG_ARTIST");
            String uriString = incomeIntent.getStringExtra("SONG_URI");
            String albumArtString = incomeIntent.getStringExtra("ALBUM_ART_URI");

            if (albumArtString!=null){
                Uri albumArtUri = Uri.parse(albumArtString);
                Glide.with(this).load(albumArtUri).into(albumArt);
            } else {
                albumArt.setImageResource(R.drawable.albumart);
            }

            if (title!=null){
                songName.setText(title);
            }
            if (artistName!=null){
                artistName.setText(artist);
            }

            if (uriString!=null){

                Uri playUri = Uri.parse(uriString);
                playMusic(playUri);


                Log.d("TunesApp", "Playing: " + title + " from URI: " + playUri.toString());
//                songPlay.setText("Playing: " + title + "URI: " + playUri.toString());
            }
        }

        playPauseBtn.setOnClickListener(v->{
            if (mediaPlayer!=null){
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
                } else{
                    mediaPlayer.start();
                    playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        songProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean byUser) {
                if (mediaPlayer!=null && byUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void playMusic(Uri songUri){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer=MediaPlayer.create(this,songUri);
        if (mediaPlayer!=null){
            mediaPlayer.start();
            playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
            songProgress.setMax(mediaPlayer.getDuration());
            updateSeekBar();

            mediaPlayer.setOnCompletionListener(m->{
                playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
                songProgress.setProgress(0);
            });
        } else{
            Log.d("TunesApp", "Error in Audio");
        }

    }

    private void updateSeekBar(){
        if (mediaPlayer!=null){
            songProgress.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            },1000);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
}