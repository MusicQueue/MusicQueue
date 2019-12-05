package com.example.musicqueue;

import com.example.musicqueue.models.Song;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SongModelUnitTest {

    private Song song;

    @Before
    public void setUp() {
        Map<String, Boolean> map = new HashMap<>();
        map.put("user", true);

        song = new Song(
                "DAMN.",
                "Kendrick Lamar",
                Integer.toUnsignedLong(128),
                "docid",
                "queueid",
                "ownerUid",
                map
        );
    }

    /* ---------- NEW TEST CASES FOR SPRINT 3 ---------- */

    /**
     * NEW TEST CASE FOR SPRINT 3
     *
     * tests getting the owner uid
     */
    @Test
    public void testGetownerUid() {
        Assert.assertEquals(song.getOwnerUid(), "ownerUid");
    }

    /**
     * NEW TEST CASE FOR SPRINT 3
     *
     * tests getting the voters map from the song model
     */
    @Test
    public void testGetVoterMap() {
        Assert.assertTrue(song.getVotersMap().get("user"));
    }

    /* ---------- END TEST CASE FOR SPRINT 3 ---------- */


    /* ---------- OLD TEST CASES FROM SPRINT 2 ---------- */

    /**
     * OLD TEST CASE FROM SPRINT 2
     *
     * tests getting the name from the song model
     */
    @Test
    public void testGetName() {
        assertEquals(song.getName(), "DAMN.");
    }

    /**
     * OLD TEST CASE FROM SPRINT 2
     *
     * tests getting the artist from the song model
     */
    @Test
    public void testGetArtist() {
        assertEquals(song.getArtist(), "Kendrick Lamar");
    }

    /**
     * OLD TEST CASE FROM SPRINT 2
     *
     * tests getting the vote count from the song model
     */
    @Test
    public void testGetVotes() {
        assertEquals(song.getVotes(), 128);
    }

    /**
     * OLD TEST CASE FROM SPRINT 2
     *
     * tests getting the doc id from the song model
     */
    @Test
    public void testGetDocid() {
        assertEquals(song.getDocId(), "docid");
    }

    /**
     * OLD TEST CASE FROM SPRINT 2
     *
     * tests getting the queue id from the song model
     */
    @Test
    public void testGetQueueid() {
        assertEquals(song.getQueueId(), "queueid");
    }

    /**
     * OLD TEST CASE FROM SPRINT 2
     *
     * tests setting anew name in the song model
     */
    @Test
    public void testSetName() {
        String newName = "new name";

        song.setName(newName);

        assertEquals(song.getName(), newName);
    }
}
