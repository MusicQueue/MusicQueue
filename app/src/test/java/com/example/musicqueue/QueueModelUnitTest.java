package com.example.musicqueue;

import com.example.musicqueue.models.Queue;
import com.google.firebase.Timestamp;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QueueModelUnitTest {

    private Queue testQueue;
    private Timestamp timestamp;

    @Before
    public void setUp(){
        timestamp = Timestamp.now();
        testQueue = new Queue(
                "Rounders",
                "On the strip",
                "",
                timestamp,
                Integer.toUnsignedLong(0)
        );
    }

    @Test
    public void testNameGet(){
        Assert.assertEquals(testQueue.getName(), "Rounders");
    }

    @Test
    public void testLocation(){
        Assert.assertEquals(testQueue.getLocation(),"On the strip");
    }

    @Test
    public void testTimestamp(){
        Assert.assertEquals(testQueue.getCreated().compareTo(timestamp), 0);
    }

    @Test
    public void testSongCount(){
        Assert.assertEquals(testQueue.getSongCount().intValue(), 0);
    }

    @After
    public void teardown(){
        testQueue = null;
    }

    
}
