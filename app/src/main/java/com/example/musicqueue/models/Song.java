package com.example.musicqueue.models;

import androidx.annotation.Nullable;

import com.example.musicqueue.models.AbstractSongs;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Map;

@IgnoreExtraProperties
public class Song extends AbstractSongs {

    private String name;
    private String artist;
    private long votes;
    private String docid;
    private String queueId;
    private String ownerUid;
    private Map<String, Boolean> votersMap;

    public Song() {}

    public Song(@Nullable String name, @Nullable String artist,
                long votes, @Nullable String docid, @Nullable String queueId,
                @Nullable String ownerUid, Map<String, Boolean> votersMap) {
        this.name = name;
        this.artist = artist;
        this.votes = votes;
        this.docid = docid;
        this.queueId = queueId;
        this.ownerUid = ownerUid;
        this.votersMap = votersMap;
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
    public void setOwnerUid(String s) { this.ownerUid = s; }

    @Override
    public void setVotersMap(Map<String, Boolean> map) { this.votersMap = map; }

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

    @Override
    @Nullable
    public String getOwnerUid() { return this.ownerUid; }

    @Override
    public Map<String, Boolean> getVotersMap() { return this.votersMap; }
}
