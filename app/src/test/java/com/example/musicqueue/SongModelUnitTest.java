package com.example.musicqueue;

import com.example.musicqueue.models.Song;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SongModelUnitTest {

    private Song song;

    @Before
    public void setUp() {
        song = new Song(
                "DAMN.",
                "Kendrick Lamar",
                Integer.toUnsignedLong(128),
                "docid",
                "queueid"
        );
    }

    @Test
    public void testGetName() {
        assertEquals(song.getName(), "DAMN.");
    }

    @Test
    public void testGetArtist() {
        assertEquals(song.getArtist(), "Kendrick Lamar");
    }

    @Test
    public void testGetVotes() {
        assertEquals(song.getVotes(), 128);
    }

    @Test
    public void testGetDocid() {
        assertEquals(song.getDocId(), "docid");
    }

    @Test
    public void testGetQueueid() {
        assertEquals(song.getQueueId(), "queueid");
    }

    @Test
    public void testSetName() {
        String newName = "new name";

        song.setName(newName);

        assertEquals(song.getName(), newName);
    }
}
