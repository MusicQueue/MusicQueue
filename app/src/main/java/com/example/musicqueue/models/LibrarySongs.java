package com.example.musicqueue.models;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LibrarySongs extends AbstractLibrary {

    private String name;
    private String artist;
    private String ownerUid;
    private String docid;

    public LibrarySongs() {}

    public LibrarySongs(@Nullable String name, @Nullable String artist, @Nullable String ownerUid,
                        @Nullable String docid) {
        this.name = name;
        this.artist = artist;
        this.ownerUid = ownerUid;
        this.docid = docid;
    }

    @Override
    public void setName(String s) { this.name = s; }

    @Override
    public void setArtist(String s) { this.artist = s; }

    @Override
    public void setOwnerUid(String s) { this.ownerUid = s; }

    @Override
    public void setDocid(String s) { this.docid = s; }

    @Override
    @Nullable
    public String getName() { return this.name; }

    @Override
    @Nullable
    public String getArtist() { return this.artist; }

    @Override
    @Nullable
    public String getOwnerUid() { return this.ownerUid; }

    @Override
    @Nullable
    public String getDocid() { return this.docid; }

}
