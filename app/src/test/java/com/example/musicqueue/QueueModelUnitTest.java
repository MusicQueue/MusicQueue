package com.example.musicqueue;

import com.example.musicqueue.models.Queue;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class QueueModelUnitTest {

    private Queue testQueue;
    private Timestamp timestamp;

    @Before
    public void setUp(){
        Map<String, Boolean> map = new HashMap<>();
        map.put("user", true);

        timestamp = Timestamp.now();

        testQueue = new Queue(
                "Rounders",
                new GeoPoint(1.0, 1.0),
                timestamp,
                Integer.toUnsignedLong(0),
                map,
                "test user"
        );
    }

    /* ---------- NEW TEST CASES FOR SPRINT 3 ---------- */

    /**
     * NEW TEST CASE FOR SPRINT 3
     *
     * tests getting the geopoint location from the queue model
     */
    @Test
    public void testLocation() {
        Assert.assertEquals(testQueue.getLocation(),new GeoPoint(1.0, 1.0));
    }

    /**
     * NEW TEST CASE FOR SPRINT 3
     *
     * tests getting the favorites map from the queue model
     */
    @Test
    public void testfavoritesMap() {
        Assert.assertTrue(testQueue.getFavoritesMap().get("user"));
    }

    /**
     * NEW TEST CASE FOR SPRINT 3
     *
     * tests getting the creator from the queue model
     */
    @Test
    public void testGetCreator() {
        Assert.assertEquals(testQueue.getCreator(), "test user");
    }

    /* ---------- END SPRINT 3 TEST CASES ---------- */

    /* ---------- OLD TEST CASES FROM SPRINT 2 ---------- */

    /**
     * TEST CASE SPRINT 2
     *
     * tests the getting the name from the queue model
     */
    @Test
    public void testNameGet() {
        Assert.assertEquals(testQueue.getName(), "Rounders");
    }

    /**
     * TEST CASE FROM SPRINT 2
     *
     * tests getting the timestamp from the queue model
     */
    @Test
    public void testTimestamp() {
        Assert.assertEquals(testQueue.getCreated().compareTo(timestamp), 0);
    }

    /**
     * TEST CASE FROM SPRINT 2
     *
     * tests getting the song count from the queue model
     */
    @Test
    public void testSongCount() {
        Assert.assertEquals(testQueue.getSongCount().intValue(), 0);
    }

    /**
     * TEST CASE FROM SPRINT 2
     *
     * tests setting a new name in the queue model
     */
    @Test
    public void testSetName() {
        String newName = "new name";

        testQueue.setName(newName);

        Assert.assertEquals(testQueue.getName(), newName);
    }

    @After
    public void teardown(){
        testQueue = null;
    }

    
}
