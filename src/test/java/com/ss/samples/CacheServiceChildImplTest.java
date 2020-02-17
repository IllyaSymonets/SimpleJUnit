package com.ss.samples;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class CacheServiceChildImplTest {

    private CacheServiceChildImpl testCache;

    @Before
    public void prepareDataForTest() {
        testCache = new CacheServiceChildImpl();
        testCache.setMaxCapacity(10);
    }


    @Test
    public void counterWorksCorrect() {
    }

    @Test
    public void putWithCleaningCorrect() {
        for (int i = 0; i < 1001; i++) {
            CacheServiceChildImpl.CachedEntityChild cachedEntity = new CacheServiceChildImpl.CachedEntityChild(String.valueOf(i), new Object());
            int number = (int) (Math.random() * 300 + 10);
            cachedEntity.setCounterOfUsage(number);
            testCache.put(Instant.now().toString(), cachedEntity);
        }
        assertEquals(10, testCache.size());
    }
}
