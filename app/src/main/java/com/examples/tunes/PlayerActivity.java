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

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PlayerActivity extends AppCompatActivity {
//    TextView songPlay;
//private FloatingActionButton playPauseBtn;

    private SeekBar songProgress;
    private TextView songName, artistName;
    private ImageView albumArt;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private ImageButton playPauseBtn,nextBtn,prevBtn;
    private int currentPosition = 0;
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

        nextBtn = findViewById(R.id.nextBtn);
        prevBtn = findViewById(R.id.prevBtn);




        if (incomeIntent!=null){
            currentPosition = getIntent().getIntExtra("SONG_POSITION", 0);
            loadSong(currentPosition);

//            String title = incomeIntent.getStringExtra("SONG_TITLE");
//            String artist = incomeIntent.getStringExtra("SONG_ARTIST");
//            String uriString = incomeIntent.getStringExtra("SONG_URI");
//            String albumArtString = incomeIntent.getStringExtra("ALBUM_ART_URI");


//            if (albumArtString!=null){
//                Uri albumArtUri = Uri.parse(albumArtString);
//                Glide.with(this).load(albumArtUri).centerCrop().transform(new com.bumptech.glide.load.resource.bitmap.RoundedCorners(40)).placeholder(R.drawable.albumart).error(R.drawable.albumart).into(albumArt);;
//                Glide.with(this).asBitmap().load(albumArtUri).into(new com.bumptech.glide.request.target.CustomTarget<android.graphics.Bitmap>(){
//                    @Override
//                    public void onResourceReady(@androidx.annotation.NonNull android.graphics.Bitmap resource, @androidx.annotation.Nullable com.bumptech.glide.request.transition.Transition<? super android.graphics.Bitmap> transition) {
//                        androidx.palette.graphics.Palette.from(resource).generate(palette -> {
//                            if (palette!=null){
////                                int defColor = android.graphics.Color.parseColor("#000000");
////                                int extColor = palette.getDarkVibrantColor(defColor);
////                                findViewById(R.id.main).setBackgroundColor(extColor);
//
//
//                                int defaultColor = android.graphics.Color.parseColor("#121212");
//                                int darkVibrant = palette.getDarkVibrantColor(defaultColor);
//                                int dominant = palette.getDominantColor(defaultColor);
//                                int muted = palette.getMutedColor(defaultColor);
//
//                                int[] gradientColors = {dominant,darkVibrant, muted};
//
//                                android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable(
//                                        android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
//                                        gradientColors
//                                );
//
//                                findViewById(R.id.main).setBackground(gradientDrawable);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onLoadCleared(@androidx.annotation.Nullable android.graphics.drawable.Drawable placeholder) {
//                    }
//
//                });
//            } else {
//                // it not work due to not null string as albumartstring is always not null
//                albumArt.setImageResource(R.drawable.albumart);
//            }
//
//            if (title!=null){
//                songName.setText(title);
//            }
//            if (artist!=null){
//                artistName.setText(artist);
//            }
//
//            if (uriString!=null){
//
//                Uri playUri = Uri.parse(uriString);
//                playMusic(playUri);
//
//
//                Log.d("TunesApp", "Playing: " + title + " from URI: " + playUri.toString());
////                songPlay.setText("Playing: " + title + "URI: " + playUri.toString());
//            }
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

        nextBtn.setOnClickListener(v -> {
            currentPosition = (currentPosition + 1) % MainActivity.allSongs.size();
            loadSong(currentPosition);
        });

        prevBtn.setOnClickListener(v -> {
            currentPosition = (currentPosition - 1 < 0) ? (MainActivity.allSongs.size() - 1) : (currentPosition - 1);
            loadSong(currentPosition);
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

    private void loadSong(int index){

        Song currentSong = MainActivity.allSongs.get(index);
        String title = currentSong.getTitle();
        String artist = currentSong.getArtist();
        String uriString = currentSong.getSongUri().toString();
        String albumArtString = currentSong.getAlbumArtUri().toString();

        if (title!=null){
            songName.setText(title);
        }
        if (artist!=null){
            artistName.setText(artist);
        }

        if (uriString!=null){

            Uri playUri = Uri.parse(uriString);
            playMusic(playUri);


            Log.d("TunesApp", "Playing: " + title + " from URI: " + playUri.toString());
//                songPlay.setText("Playing: " + title + "URI: " + playUri.toString());
        }


        if (albumArtString!=null){
            Uri albumArtUri = Uri.parse(albumArtString);
            Glide.with(this).load(albumArtUri).centerCrop().transform(new com.bumptech.glide.load.resource.bitmap.RoundedCorners(40)).placeholder(R.drawable.albumart).error(R.drawable.albumart).into(albumArt);;
            Glide.with(this).asBitmap().load(albumArtUri).into(new com.bumptech.glide.request.target.CustomTarget<android.graphics.Bitmap>(){
                @Override
                public void onResourceReady(@androidx.annotation.NonNull android.graphics.Bitmap resource, @androidx.annotation.Nullable com.bumptech.glide.request.transition.Transition<? super android.graphics.Bitmap> transition) {
                    androidx.palette.graphics.Palette.from(resource).generate(palette -> {
                        if (palette!=null){
//                                int defColor = android.graphics.Color.parseColor("#000000");
//                                int extColor = palette.getDarkVibrantColor(defColor);
//                                findViewById(R.id.main).setBackgroundColor(extColor);


                            int defaultColor = android.graphics.Color.parseColor("#121212");
                            int darkVibrant = palette.getDarkVibrantColor(defaultColor);
                            int dominant = palette.getDominantColor(defaultColor);
                            int muted = palette.getMutedColor(defaultColor);

                            int[] gradientColors = {dominant,darkVibrant, muted};

                            android.graphics.drawable.GradientDrawable gradientDrawable = new android.graphics.drawable.GradientDrawable(
                                    android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                                    gradientColors
                            );

                            findViewById(R.id.main).setBackground(gradientDrawable);
                        }
                    });
                }

                @Override
                public void onLoadCleared(@androidx.annotation.Nullable android.graphics.drawable.Drawable placeholder) {
                }

            });
        } else {
            // it not work due to not null string as albumartstring is always not null
            albumArt.setImageResource(R.drawable.albumart);
        }

    }


    private void playMusic(Uri songUri){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer=MediaPlayer.create(this,songUri);
        if (mediaPlayer!=null){
            mediaPlayer.start();
//            playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
            playPauseBtn.setImageResource(R.drawable.pausebtn);
            songProgress.setMax(mediaPlayer.getDuration());
            updateSeekBar();

            mediaPlayer.setOnCompletionListener(m->{
//                playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
                playPauseBtn.setImageResource(R.drawable.playbtn);
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