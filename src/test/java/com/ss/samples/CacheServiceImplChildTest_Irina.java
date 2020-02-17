package com.ss.samples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.mockito.Mockito.mock;

public class CacheServiceImplChildTest_Irina {

    private CacheServiceImpl testCache;
    private Function<String, Object> sourceFunctionMock;
    private Consumer<CacheServiceImpl.AbstractCachedEntity> handlerMock;
    private CacheServiceImpl_Irina.CachedEntity entityMock;
    private StatisticInfo infoMock;

    @Before
    private CacheServiceImpl prepareDataForTest() {
        testCache = new CacheServiceImpl_Irina();
        sourceFunctionMock = mock(Function.class);
        handlerMock = mock(Consumer.class);
        entityMock = mock(CacheServiceImpl_Irina.CachedEntity.class);
        infoMock = mock(StatisticInfo.class);
        testCache.setHandler(handlerMock);
        testCache.setSourceFunction(sourceFunctionMock);
        testCache.instance = new HashMap<>(2);
        return testCache;
    }

    @Test
    public void collectGarbageByFrequencyTest() {
        testCache.instance.put("1", entityMock);
        testCache.instance.put("2", entityMock);

    }

    @Test
    public void _putToCacheWithCollectingGarbage() {

    }

    @Test
    public void getFromCacheWithIncrementCountOfTouch() {

    }
}
