package com.example.musicqueue.ui.song;

import androidx.annotation.Nullable;

public abstract class AbstractSong {

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
}
