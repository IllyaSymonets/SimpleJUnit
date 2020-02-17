package com.ss.samples;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CacheServiceImplChildTest_Irina {

    private CacheServiceImpl_Irina testCache;
    private CacheServiceImpl_Irina.CachedEntity entityMock;
    private StatisticInfo infoMock;

    @Before
    private CacheServiceImpl prepareDataForTest() {
        testCache = new CacheServiceImpl_Irina();
        entityMock = Mockito.mock(CacheServiceImpl_Irina.CachedEntity.class);
        infoMock = Mockito.mock(StatisticInfo.class);
        return testCache;
    }

    @Test
    public void checkSizeCorrect(){}

    @Test
    public void collectGarbageWithPercentageOfFrequency(){}

    @Test
    public void changeTheStatisticInfoWithNewGet() {
        testCache.getCacheEntity().put("1", entityMock);
        testCache.get("1");

    }

    @Test
    public void collectGarbageByFrequencyTest() {
        testCache.put("1", entityMock);
        testCache.put("2", entityMock);

    }
}
