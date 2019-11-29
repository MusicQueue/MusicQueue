package com.example.musicqueue.models;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LibrarySongs extends AbstractLibrary {

    private String name;
    private String artist;
    private String ownerUid;

    public LibrarySongs() {}

    public LibrarySongs(@Nullable String name, @Nullable String artist, @Nullable String ownerUid) {
        this.name = name;
        this.artist = artist;
        this.ownerUid = ownerUid;
    }

    @Override
    public void setName(String s) { this.name = s; }

    @Override
    public void setArtist(String s) { this.artist = s; }

    @Override
    public void setOwnerUid(String s) { this.ownerUid = s; }

    @Override
    @Nullable
    public String getName() { return this.name; }

    @Override
    @Nullable
    public String getArtist() { return this.artist; }

    @Override
    @Nullable
    public String getOwnerUid() { return this.ownerUid; }

}
