package com.example.musicqueue.ui.song;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class SongModel extends AbstractSong {

    private String name;
    private String artist;
    private long votes;
    private String docid;

    public SongModel() {}

    public SongModel(@Nullable String name, @Nullable String artist,
                     long votes, @Nullable String docid) {
        this.name = name;
        this.artist = artist;
        this.votes = votes;
        this.docid = docid;
    }

    @Override
    public void setName(String s) { this.name = s; }

    @Override
    public void setArtist(String s) { this.artist = s; }

    @Override
    public void setVotes(long i) { this.votes = i; }

    @Override
    public void setDocId(String s) { this.docid = s; }

    @Override
    @Nullable
    public String getName() { return this.name; }

    @Override
    @Nullable
    public String getArtist() { return this.artist; }

    @Override
    public long getVotes() { return this.votes; }

    @Override
    @Nullable
    public String getDocId() { return this.docid; }
}
