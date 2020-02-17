package com.ss.samples;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ss.samples.CleanerCacheService.StatisticsCacheEntity;
import java.time.Instant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerCacheServiceTest_Roman {

    private static final long TIME_TO_LIVE = 5 * 60;
    private CleanerCacheService cacheService;
    private Statistics statisticsMock;

    @Before
    public void setUp() {
        cacheService = new CleanerCacheService();
        statisticsMock = mock(Statistics.class);
    }

    @Test
    public void happyPathTest() {
        cacheService.put("TEST", "111");
        Assert.assertEquals("111", cacheService.get("TEST"));
    }

    @Test
    public void cleanMethodInvoked() {
        for (int i = 0; i < 100_001; i++) {
            cacheService.put(String.valueOf(i), i);
        }

        int expectedSize = 1;

        Assert.assertEquals(expectedSize, cacheService.getCache().size());
    }

    @Test
    public void cleanOnlyNotUsedEntityTest() {
        StatisticsCacheEntity statisticsCacheEntity = new StatisticsCacheEntity("TEST-VALUE");
        statisticsCacheEntity.setStatistics(statisticsMock);

        when(statisticsMock.getNumberOfUses()).thenReturn(10L);
        when(statisticsMock.getLastAccessTime())
            .thenReturn(Instant.now().minusSeconds(TIME_TO_LIVE));

        cacheService.getCache().put("TEST", statisticsCacheEntity);

        for (int i = 0; i < 100_000; i++) {
            cacheService.put(String.valueOf(i), i);
        }

        Assert.assertEquals("TEST-VALUE", cacheService.get("TEST"));
    }

    @Test(expected = RuntimeException.class)
    public void cleanByNumbersOfUsesFilterTest() {
        StatisticsCacheEntity statisticsCacheEntity = new StatisticsCacheEntity("TEST-VALUE");
        statisticsCacheEntity.setStatistics(statisticsMock);

        when(statisticsMock.getNumberOfUses()).thenReturn(0L);
        when(statisticsMock.getLastAccessTime())
            .thenReturn(Instant.now().minusSeconds(TIME_TO_LIVE));

        cacheService.getCache().put("TEST", statisticsCacheEntity);

        for (int i = 0; i < 100_000; i++) {
            cacheService.put(String.valueOf(i), i);
        }

        Assert.assertEquals("TEST-VALUE", cacheService.get("TEST"));
    }

    @Test (expected = RuntimeException.class)
    public void cleanByLastAccessTimeFilterTest() {
        StatisticsCacheEntity statisticsCacheEntity = new StatisticsCacheEntity("TEST-VALUE");
        statisticsCacheEntity.setStatistics(statisticsMock);

        when(statisticsMock.getNumberOfUses()).thenReturn(10L);
        when(statisticsMock.getLastAccessTime()).thenReturn(Instant.now());

        cacheService.getCache().put("TEST", statisticsCacheEntity);

        for (int i = 0; i < 100_000; i++) {
            cacheService.put(String.valueOf(i), i);
        }

        Assert.assertEquals("TEST-VALUE", cacheService.get("TEST"));
    }
}