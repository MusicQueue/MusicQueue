package com.example.musicqueue;

import com.example.musicqueue.ui.songs.SongsModel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SongModelUnitTest {

    private SongsModel songsModel;

    @Before
    public void setUp() {
        songsModel = new SongsModel(
                "DAMN.",
                "Kendrick Lamar",
                Integer.toUnsignedLong(128),
                "docid",
                "queueid"
        );
    }

    @Test
    public void testGetName() {
        assertEquals(songsModel.getName(), "DAMN.");
    }

    @Test
    public void testGetArtist() {
        assertEquals(songsModel.getArtist(), "Kendrick Lamar");
    }

    @Test
    public void testGetVotes() {
        assertEquals(songsModel.getVotes(), 128);
    }

    @Test
    public void testGetDocid() {
        assertEquals(songsModel.getDocId(), "docid");
    }

    @Test
    public void testGetQueueid() {
        assertEquals(songsModel.getQueueId(), "queueid");
    }

    @Test
    public void testSetName() {
        String newName = "new name";

        songsModel.setName(newName);

        assertEquals(songsModel.getName(), newName);
    }
}
