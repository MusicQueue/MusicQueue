package com.example.musicqueue.ui.song;

import androidx.annotation.Nullable;

public abstract class AbstractSong {

    @Nullable
    public abstract String getSongName();

    public abstract void setSongName(@Nullable String songName);

    @Nullable
    public abstract String getArtistName();

    public abstract void setArtistName(@Nullable String artistName);

    @Nullable
    public abstract int getSongRank();

    public abstract void setSongRank(@Nullable int songRank);

    @Nullable
    public abstract String getDocId();

    public abstract void setDocId(@Nullable String docid);
}
