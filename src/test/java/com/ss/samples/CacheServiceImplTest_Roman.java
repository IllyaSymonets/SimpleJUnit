package com.ss.samples;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ss.samples.CacheServiceImpl.AbstractCachedEntity;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CacheServiceImplTest_Roman {

    private Function<String, Object> sourceFunctionMock;
    private Consumer<AbstractCachedEntity> handlerMock;

    @Test
    public void getHappyPath() {
        long testValue = System.currentTimeMillis();
        CacheServiceImpl impl = prepareDataForTest("TEST-GET", testValue);
        assertEquals(testValue, impl.get("TEST-GET"));
    }

    @Test
    public void putHappyPath() {
        long testValue = System.currentTimeMillis();
        CacheServiceImpl impl = prepareDataForTest("TEST-PUT", testValue);
        assertEquals(testValue, impl.instance.get("TEST-PUT").getValue());
    }

    @Test(expected = RuntimeException.class)
    public void getMethodThrowRuntimeExceptionTest() {
        CacheServiceImpl cacheService = prepareDataForTest("TEST-1", 10);
        cacheService.get("TEST_2");
    }

    //GIVEN, WHEN, THEN spaces
    @Test
    public void getRealValueIsUsedTest() {
        CacheServiceImpl cacheService = prepareDataForTest("TEST-1", 10);
        when(sourceFunctionMock.apply(Mockito.eq("TEST-2"))).thenReturn(100);

        Assert.assertEquals(cacheService.get("TEST-2"), 100);
    }

    //no need test
    @Test
    public void getRealValueIsUsedOnlyOnceTest() {
        CacheServiceImpl cacheService = prepareDataForTest("TEST-1", 10);

        when(sourceFunctionMock.apply(Mockito.eq("TEST-2"))).thenReturn(100);
        cacheService.get("TEST-2");
        cacheService.get("TEST-2");
        cacheService.get("TEST-2");

        verify(sourceFunctionMock, times(1)).apply("TEST-2");
    }

    //no need TEST-2 flow
    @Test
    public void handlerKeyExistIsUsedTest() {
        CacheServiceImpl cacheService = prepareDataForTest("TEST-1", 10);

        cacheService.put("TEST-1", 10);
        cacheService.put("TEST-2", 10);
        cacheService.put("TEST-2", 10);

        verify(handlerMock, times(2)).accept(Mockito.any());
    }

    /*can be before method @Before
    no need to declare each test
     */
    @SuppressWarnings("unchecked")
    private CacheServiceImpl prepareDataForTest(String key, long testValue) {
        //can be declared with Mocks
        CacheServiceImpl impl = new CacheServiceImpl();
        sourceFunctionMock = mock(Function.class);
        impl.setSourceFunction(sourceFunctionMock);
        handlerMock = mock(Consumer.class);
        impl.setHandler(handlerMock);
        impl.put(key, testValue);
        return impl;
    }
}