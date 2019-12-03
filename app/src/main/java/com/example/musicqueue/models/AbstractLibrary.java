package com.example.musicqueue.models;

import androidx.annotation.Nullable;

public abstract class AbstractLibrary {

    @Nullable
    public abstract String getName();

    public abstract void setName(@Nullable String s);

    @Nullable
    public abstract String getArtist();

    public abstract void setArtist(@Nullable String s);

    @Nullable
    public abstract String getOwnerUid();

    public abstract void setOwnerUid(@Nullable String s);

    @Nullable
    public abstract String getDocid();

    public abstract void setDocid(@Nullable String s);

}
