package com.examples.tunes;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> reqPermLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted ->{
        if(isGranted){
            scanAudio();
        }
        else{
            Log.e("Tunes","Storage Permission Not Given!");
        }
    });
//    TextView songDsp;
    private List<Song> allSongs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        songDsp = findViewById(R.id.songdsp);
        checkPermAndLoad();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkPermAndLoad(){
        String permission = Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU?
                Manifest.permission.READ_MEDIA_AUDIO:Manifest.permission.READ_EXTERNAL_STORAGE;
        if(ContextCompat.checkSelfPermission(this,permission)==PackageManager.PERMISSION_GRANTED){
            scanAudio();
        }
        else{
            reqPermLauncher.launch(permission);
        }
    }

//    StringBuilder songsText = new StringBuilder();

    private void scanAudio(){
        ContentResolver contentResolver = getContentResolver();
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC +" != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE+" ASC";

        Cursor cursor = contentResolver.query(audioUri,projection,selection,null,sortOrder);

        if(cursor!=null){
            while (cursor.moveToNext()){
                int idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                int titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                int artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                int albumIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);

                long id = cursor.getLong(idCol);
                String title = cursor.getString(titleCol);
                String artist = cursor.getString(artistCol);
                long albumId = cursor.getLong(albumIdCol);

                Uri contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,String.valueOf(id));
                Log.d("Tunes", "Found Song: "+title+" by "+ artist);
                Uri albumArtUri = Uri.parse("content://media/external/audio/albumart/" + albumId);
//                songsText.append(title).append(" - ").append(artist).append("\n");
                Song currentSong = new Song(title, artist, contentUri, albumArtUri);
                allSongs.add(currentSong);
            }
            cursor.close();
//            songDsp.setText(songsText.toString());
//            songDsp.setText(allSongs.toString());
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        SongAdapter adapter = new SongAdapter(allSongs);
        recyclerView.setAdapter(adapter);

    }
}