package com.example.musicqueue.models;

import androidx.annotation.Nullable;

import java.util.Map;

public abstract class AbstractSongs {

    @Nullable
    public abstract String getName();

    public abstract void setName(@Nullable String name);

    @Nullable
    public abstract String getArtist();

    public abstract void setArtist(@Nullable String artist);

    public abstract long getVotes();

    public abstract void setVotes(long votes);

    @Nullable
    public abstract String getDocId();

    public abstract void setDocId(@Nullable String docid);

    @Nullable
    public abstract String getQueueId();

    public abstract void setQueueId(@Nullable String docid);

    @Nullable
    public abstract String getOwnerUid();

    public abstract void setOwnerUid(@Nullable String ownerUid);

    public abstract Map<String, Boolean> getVotersMap();

    public abstract void setVotersMap(Map<String, Boolean> map);
}
