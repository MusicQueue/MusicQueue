package com.example.musicqueue.ui.songs;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class SongsModel extends AbstractSongs {

    private String name;
    private String artist;
    private long votes;
    private String docid;
    private String queueId;

    public SongsModel() {}

    public SongsModel(@Nullable String name, @Nullable String artist,
                      long votes, @Nullable String docid, @Nullable String queueId) {
        this.name = name;
        this.artist = artist;
        this.votes = votes;
        this.docid = docid;
        this.queueId = queueId;
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
    public void setQueueId(String s) { this.queueId = s; }

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

    @Override
    @Nullable
    public String getQueueId() { return this.queueId; }
}
