package com.example.musicqueue;

import com.example.musicqueue.models.LibrarySongs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LibrarySongsModelUnitTest {

    LibrarySongs libSong;

    @Before
    public void setUp() {
        libSong = new LibrarySongs(
                "Swimming",
                "Mac Miller",
                "ownerUid",
                "docId"
        );
    }

    /* ---------- NEW TEST CASES FOR SPRINT 3 ---------- */

    /**
     * NEW TEST CASE FOR SPRINT 3
     *
     * tests getting the song name from the library songs model
     */
    @Test
    public void testGetName() {
        Assert.assertEquals(libSong.getName(), "Swimming");
    }

    /**
     * NEW TEST CASE FOR SPRINT 3
     *
     * tests getting the artist from the library songs model
     */
    @Test
    public void testGetArtist() {
        Assert.assertEquals(libSong.getArtist(), "Mac Miller");
    }

    /**
     * NEW TEST CASE FOR SPRINT 3
     *
     * tests getting the owener uid form the library songs model
     */
    @Test
    public void testGetOwnerUid() {
        Assert.assertEquals(libSong.getOwnerUid(), "ownerUid");
    }

    /**
     * NEW TEST CASE FOR SPRINT 3
     *
     * tests getting the doc id from the library songs model
     */
    @Test
    public void testGetDocId() {
        Assert.assertEquals(libSong.getDocid(), "docId");
    }

    /**
     * NEW TEST CASE FOR SPRINT 3
     *
     * tests the setting functions in library songs model
     */
    @Test
    public void testSetName() {
        libSong.setName("DAMN.");
        libSong.setArtist("Kendrick Lamar");
        libSong.setOwnerUid("new owner uid");
        libSong.setDocid("new doc id");

        Assert.assertEquals(libSong.getName(), "DAMN.");
        Assert.assertEquals(libSong.getArtist(), "Kendrick Lamar");
        Assert.assertEquals(libSong.getOwnerUid(), "new owner uid");
        Assert.assertEquals(libSong.getDocid(), "new doc id");
    }
}
