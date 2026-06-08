package com.examples.tunes;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.bumptech.glide.Glide;
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{
    private List<Song> songList;

    public SongAdapter(List<Song> songList){
        this.songList=songList;
    }
    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlesong, parent,false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder,int position){
        Song currentSong = songList.get(position);
        holder.titleText.setText(currentSong.getTitle());
        holder.artistText.setText(currentSong.getArtist());
//        holder.albumArtImage.setImageURI(currentSong.getAlbumArtUri());
        Glide.with(holder.itemView.getContext())
                .load(currentSong.getAlbumArtUri())
                .centerCrop()
                .transform(new com.bumptech.glide.load.resource.bitmap.RoundedCorners(20))
                .placeholder(R.drawable.music)
                .error(R.drawable.music)
                .into(holder.albumArtImage);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(),PlayerActivity.class);
//                intent.putExtra("SONG_TITLE",currentSong.getTitle());
//                intent.putExtra("SONG_ARTIST",currentSong.getArtist());
//                intent.putExtra("SONG_URI",currentSong.getSongUri().toString());
//
//                if (currentSong.getAlbumArtUri()!=null){
//                    intent.putExtra("ALBUM_ART_URI",currentSong.getAlbumArtUri().toString());
//                }
                intent.putExtra("SONG_POSITION", position);

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, artistText;
        ImageView albumArtImage;
        public SongViewHolder(@NonNull View itemView){
            super(itemView);
            titleText = itemView.findViewById(R.id.song_title);
            artistText = itemView.findViewById(R.id.song_artist);
            albumArtImage = itemView.findViewById(R.id.album_art);
        }
    }
}
