package com.examples.tunes;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;

public class MusicService extends Service{
    public static MediaPlayer mediaPlayer;
    public static int currentIndex = -1;

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        if (intent!=null){
            String action = intent.getAction();

            if ("PLAY_NEW_SONG".equals(action)){
                int index = intent.getIntExtra("SONG_INDEX",0);
                playSong(index);
            }
        }
        return START_STICKY;
    }

    private void playSong(int index){

        if (currentIndex == index && mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
            showNotification(MainActivity.allSongs.get(index).getTitle());
            return;
        }

        this.currentIndex=index;
        Song currentSong = MainActivity.allSongs.get(index);

        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this,currentSong.getSongUri());
        if (mediaPlayer!=null){
            mediaPlayer.start();
            showNotification(currentSong.getTitle());
        }
    }

    private void showNotification(String songTitle){
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("FROM_NOTIFICATION",true);
        intent.putExtra("SONG_POSITION",currentIndex);
        intent.setAction("PLAYING_SONG_" + currentIndex);

        android.app.TaskStackBuilder stackBuilder = android.app.TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this,MainActivity.class));
        stackBuilder.addNextIntent(intent);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(this,TunesApp.CHANNEL_ID)
                .setContentTitle("Tunes")
                .setContentText("Now Playing"+ songTitle)
                .setSmallIcon(R.drawable.music)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(1,notification);
    }

    @Override
    public void onDestroy(){
        if (mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        super.onTaskRemoved(rootIntent);
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;

            stopForeground(true);
            stopSelf();
        }
    }
}
