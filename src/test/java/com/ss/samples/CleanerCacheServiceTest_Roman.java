package com.ss.samples;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
    private CleanerCacheService cacheServiceMock;
    private Map<String, StatisticsCacheEntity> cacheMock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        cacheService = new CleanerCacheService();
        cacheMock = mock(HashMap.class);
        cacheService.setCache(cacheMock);
        cacheServiceMock = mock(CleanerCacheService.class);
    }

    @Test
    public void sizeTestWithoutCleanTest() {
        for (int i = 0; i < 90_000; i++) {
            cacheService.put(String.valueOf(i), i);
        }

        int expected = 90_000;

        Assert.assertEquals(expected, cacheService.getCache().size());
    }

    @Test
    public void sizeTestAfterCleanTest() {
        for (int i = 0; i < 111_111; i++) {
            cacheService.put(String.valueOf(i), i);
        }

        int expected = 11_111;

        Assert.assertEquals(expected, cacheService.getCache().size());
    }

    @Test
    public void cleanTest() {
        for (int i = 0; i < 1000; i++) {
            cacheService.put(String.valueOf(i), i);
        }

        when(cacheMock.size()).thenReturn(MAX_CAPACITY);
        cacheService.put(String.valueOf(1), 1);
        int expected = 1;

        Assert.assertEquals(expected, cacheService.getCache().size());
    }
}