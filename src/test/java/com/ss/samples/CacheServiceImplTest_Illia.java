package com.ss.samples;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CacheServiceImplTest_Illia {

    CacheServiceImpl cacheService;
    Consumer<String> handlerMock;
    Function<String, Object> sourceFunctionMock;

    @Before
    public void before() {
        cacheService = new CacheServiceImpl();
        handlerMock = Mockito.mock(Consumer.class);
        sourceFunctionMock = Mockito.mock(Function.class);
        cacheService.setSourceFunction(sourceFunctionMock);
        cacheService.setHandler(handlerMock);
        cacheService.instance = new HashMap<>();
    }

    @Test
    public void putHappyPath() {

        long testValue = currentTimeMillis();

        CacheServiceImpl impl = prepareDataForTest("TEST-PUT", testValue);

        assertEquals(testValue, impl.instance.get("TEST-PUT"));
    }

    @Test
    public void getHappyPath() {

        long testValue = currentTimeMillis();

        CacheServiceImpl impl = prepareDataForTest("TEST-GET", testValue);

        assertEquals(testValue, impl.get("TEST-GET"));
    }

    @Test(expected = RuntimeException.class)
    public void putRuntimeExceptionTest() {

        long testValue = currentTimeMillis();
        cacheService.setHandler(null);

        cacheService.put("TEST-PUT", testValue);
        cacheService.put("TEST-PUT", testValue);
    }

    @Test
    public void putIsHandlerExecutesTest() {

        long testValue = currentTimeMillis();

        cacheService.put("TEST-PUT", testValue);
        cacheService.put("TEST-PUT", testValue);

        Mockito.verify(handlerMock).accept("TEST-PUT");
    }

    @Test(expected = RuntimeException.class)
    public void getRuntimeExceptionTest() {

        cacheService.get("TEST-GET");

    }

    @Test
    public void getSourceFunctionWorksCorrectlyTest() {

        long testValue=System.currentTimeMillis();
        Mockito.when(sourceFunctionMock.apply("TEST-GET"))
            .thenReturn(testValue);

        assertEquals(testValue, cacheService.get("TEST-GET"));

    }

    private CacheServiceImpl prepareDataForTest(String key, long testValue) {

        CacheServiceImpl impl = new CacheServiceImpl();
        impl.put(key, testValue);
        return impl;
    }
}