package com.ss.samples;

import static org.junit.Assert.assertEquals;

import com.ss.samples.CacheServiceChildImpl.CachedEntityChild;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CacheServiceChildImplTest {

    private CacheServiceChildImpl testCache;
    private CacheServiceChildImpl testCacheSpy;

    @Before
    public void prepareDataForTest() {
        testCache = new CacheServiceChildImpl();
        testCache.setMaxCapacity(100);
        testCacheSpy = Mockito.spy(testCache);
    }

    @Test
    public void counterWorksCorrect() {
    }

    @Test
    public void putWithCleaningCorrect() {
        for (int i = 0; i < 101; i++) {
            CacheServiceChildImpl.CachedEntityChild cachedEntity = new CacheServiceChildImpl.CachedEntityChild(
                String.valueOf(i), new Object());
            int number = (int) (Math.random() * 300 + 10);
            cachedEntity.setCounterOfUsage(number);
            testCache.put(String.valueOf(i), cachedEntity);
        }
        assertEquals(86, testCache.size());
    }

    @Test
    public void updateStatistic() {
        testCacheSpy.put("TEST-GET", new Object());
        testCacheSpy.get("TEST-GET");
        Mockito.verify(testCacheSpy).updateStatistic(Mockito.any(
            CacheServiceChildImpl.CachedEntityChild.class));
    }

    @Test
    public void statisticsResetAfterCleaningTest() {
        CachedEntityChild cachedEntityChild = new CachedEntityChild("TEST", "TEST");
        cachedEntityChild.setCounterOfUsage(10);
        testCache.put("TEST", cachedEntityChild);

        for (int i = 0; i < 101; i++) {
            cachedEntityChild = new CachedEntityChild(String.valueOf(i), i);
            testCache.put(String.valueOf(i), cachedEntityChild);
        }
        int expectedCounterOfUsage = 0;

        Assert.assertEquals(expectedCounterOfUsage, cachedEntityChild.getCounterOfUsage());
    }
}
