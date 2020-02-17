package com.ss.samples;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ss.samples.AbstractCacheService.AbstractCachedEntity;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class AbstractCacheServiceTest_Roman {

    private Function<String, AbstractCachedEntity> sourceFunctionMock;
    private Consumer<Object> handlerMock;

    @Test
    public void getHappyPath() {
        long testValue = System.currentTimeMillis();
        AbstractCachedEntity entity = new AbstractCachedEntity(testValue);
        AbstractCacheService impl = prepareDataForTest("TEST-GET", entity);
        assertEquals(entity, impl.get("TEST-GET"));
    }

    @Test
    public void putHappyPath() {
        long testValue = System.currentTimeMillis();
        AbstractCachedEntity entity = new AbstractCachedEntity(testValue);
        AbstractCacheService impl = prepareDataForTest("TEST-PUT", entity);
        assertEquals(entity, impl.get("TEST-PUT"));
    }

    @Test(expected = RuntimeException.class)
    public void getMethodThrowRuntimeExceptionTest() {
        AbstractCacheService cacheService = prepareDataForTest(
            "TEST-1", new AbstractCachedEntity(10));
        cacheService.get("TEST_2");
    }

    @Test
    public void getRealValueIsUsedTest() {
        AbstractCacheService cacheService = prepareDataForTest("TEST-1",
            new AbstractCachedEntity(10));

        AbstractCachedEntity entity = new AbstractCachedEntity(100);
        when(sourceFunctionMock.apply(Mockito.eq("TEST-2"))).thenReturn(entity);

        Assert.assertEquals(cacheService.get("TEST-2"), entity.getValue());
    }

    @Test
    public void getRealValueIsUsedOnlyOnceTest() {
        AbstractCacheService cacheService = prepareDataForTest("TEST-1",
            new AbstractCachedEntity(10));
        AbstractCachedEntity entity = new AbstractCachedEntity(100);
        when(sourceFunctionMock.apply(Mockito.eq("TEST-2"))).thenReturn(entity);

        cacheService.get("TEST-2");
        cacheService.get("TEST-2");
        cacheService.get("TEST-2");

        verify(sourceFunctionMock, times(1)).apply("TEST-2");
    }

    @Test
    public void handlerKeyExistIsUsedTest() {
        AbstractCacheService cacheService = prepareDataForTest("TEST-1",
            new AbstractCachedEntity(10));

        cacheService.put("TEST-1", 10);
        cacheService.put("TEST-2", 10);
        cacheService.put("TEST-2", 10);

        verify(handlerMock, times(2)).accept(Mockito.any());
    }

    @SuppressWarnings("unchecked")
    private AbstractCacheService prepareDataForTest(String key, AbstractCachedEntity entity) {
        AbstractCacheService impl = new AbstractCacheService();
        sourceFunctionMock = mock(Function.class);
        impl.setSourceFunction(sourceFunctionMock);
        handlerMock = mock(Consumer.class);
        impl.setHandler(handlerMock);
        impl.put(key, entity);
        return impl;
    }
}