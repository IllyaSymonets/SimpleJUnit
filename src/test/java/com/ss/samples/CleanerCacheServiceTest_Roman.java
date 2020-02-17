package com.ss.samples;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ss.samples.CleanerCacheService.StatisticsCacheEntity;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerCacheServiceTest_Roman {

    private static final int MAX_CAPACITY = 100000;
    private CleanerCacheService cacheService;
    private Map<String, StatisticsCacheEntity> cacheMock;
    private Statistics statisticsMock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        cacheService = new CleanerCacheService();
        cacheMock = mock(HashMap.class);
        cacheService.setCache(cacheMock);
        statisticsMock = mock(Statistics.class);
    }

    @Test
    public void happyPathTest() {
        cacheService.put("TEST-GET","GET");
        Assert.assertEquals("GET", cacheService.get("TEST-GET"));
    }

    @Test
    public void cleanMethodInvoked() {
        for (int i = 0; i < 10000; i++) {
            cacheService.put(String.valueOf(i), i);
        }

        when(cacheMock.size()).thenReturn(MAX_CAPACITY);
        cacheService.put(String.valueOf(1), 1);
        int expectedSize = 1;

        Assert.assertEquals(expectedSize, cacheService.getCache().size());
    }

    @Test
    public void cleanOnlyByNumberOfUsesTest() {
        for (int i = 0; i < 10000; i++) {
            cacheService.put(String.valueOf(i), i);
        }

        for (int i = 0; i < 10; i++) {
            cacheService.get(String.valueOf(100));
        }
        when(statisticsMock.getNumberOfUses()).thenReturn(10L);
        when(cacheMock.size()).thenReturn(MAX_CAPACITY);
        cacheService.put(String.valueOf(1), 1);
        int expectedSize = 2;

        Assert.assertEquals(expectedSize, cacheService.getCache().size());
    }
}