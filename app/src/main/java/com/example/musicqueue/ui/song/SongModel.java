package com.example.musicqueue.ui.song;

import androidx.annotation.Nullable;

import com.example.musicqueue.models.AbstractSong;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class SongModel extends AbstractSong {

    private String songName;
    private String artistName;
    private int songRank;
    private String docid;

    public SongModel() {}

    public SongModel(@Nullable String songName, @Nullable String artistName,
                     @Nullable int songRank, @Nullable String docid) {
        this.songName = songName;
        this.artistName = artistName;
        this.songRank = songRank;
        this.docid = docid;
    }

    @Override
    public void setSongName(String s) { this.songName = s; }

    @Override
    public void setArtistName(String s) { this.artistName = s; }

    @Override
    public void setSongRank(int i) { this.songRank = i; }

    @Override
    public void setDocId(String s) { this.docid = s; }

    @Override
    @Nullable
    public String getSongName() { return this.songName; }

    @Override
    @Nullable
    public String getArtistName() { return this.artistName; }

    @Override
    @Nullable
    public int getSongRank() { return this.songRank; }

    @Override
    @Nullable
    public String getDocId() { return this.docid; }
}
