package com.ss.samples;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CacheServiceImplChildTest_Irina {

    private CacheServiceImpl_Irina testCache;

    private CacheServiceImpl_Irina.CachedEntity entityMock1;
    private CacheServiceImpl_Irina.CachedEntity entityMock2;
    private CacheServiceImpl_Irina.CachedEntity entityMock3;
    private CacheServiceImpl_Irina.CachedEntity entity;

    private StatisticInfo statMock1;
    private StatisticInfo statMock2;
    private StatisticInfo statMock3;


    @Before
    public void prepareDataForTest() {
        testCache = new CacheServiceImpl_Irina();

        entityMock1 = mock(CacheServiceImpl_Irina.CachedEntity.class);
        entityMock2 = mock(CacheServiceImpl_Irina.CachedEntity.class);
        entityMock3 = mock(CacheServiceImpl_Irina.CachedEntity.class);
        statMock1 = mock(StatisticInfo.class);
        statMock2 = mock(StatisticInfo.class);
        statMock3 = mock(StatisticInfo.class);
        testCache.setMapCapacity(3);
    }

    @Test
    public void collectGarbageByLeastUsedEntityCorrect() {
        testCache.put("1", entityMock1);
        testCache.put("2", entityMock2);
        testCache.put("3", entityMock3);
        Mockito.when(statMock1.getFrequencyOfTouch()).thenReturn(12.4);
        Mockito.when(statMock2.getFrequencyOfTouch()).thenReturn(2.0);
        Mockito.when(statMock3.getFrequencyOfTouch()).thenReturn(0.15);

        testCache.put("4", entityMock1);

        assertTrue(testCache.getCacheEntity().keySet().contains("1") &&
                testCache.getCacheEntity().keySet().contains("2") &&
                testCache.getCacheEntity().keySet().contains("4") &&
                testCache.getCacheEntity().size() == 3);
    }

//    @Test
//    public void getHappyPath() {
//        testCache._put("1", entity);
//        CacheServiceImpl_Irina.CachedEntity expected = entity;
//        assertEquals(expected, testCache.get("1"));
//    }
}
