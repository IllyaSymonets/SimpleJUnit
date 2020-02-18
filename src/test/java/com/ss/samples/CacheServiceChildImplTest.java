package com.ss.samples;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CacheServiceChildImplTest {

    private CacheServiceChildImpl testCache;

    @Before
    public void prepareDataForTest() {
        testCache = new CacheServiceChildImpl();
        testCache.setMaxCapacity(100);
    }

    @Test
    public void counterWorksCorrect() {
    }

    @Test
    public void putWithCleaningCorrect() {
        for (int i = 0; i < 101; i++) {
            CacheServiceChildImpl.CachedEntityChild cachedEntity = new CacheServiceChildImpl.CachedEntityChild(String.valueOf(i), new Object());
            int number = (int) (Math.random() * 300 + 10);
            cachedEntity.setCounterOfUsage(number);
            testCache.put(String.valueOf(i), cachedEntity);
        }
        assertEquals(86, testCache.size());
    }

    @Test
    public void updateStatistic(){

    }
}
