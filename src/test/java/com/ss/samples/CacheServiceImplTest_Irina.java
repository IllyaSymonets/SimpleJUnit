package com.ss.samples;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CacheServiceImplTest_Irina {

    CacheServiceImpl testCache;
    private Function<String, Object> sourceFunctionMock;
    private Consumer<String> handlerMock;

    private CacheServiceImpl prepareDataForTest(String key, long testValue) {
        testCache = new CacheServiceImpl();
        sourceFunctionMock = mock(Function.class);
        handlerMock = mock(Consumer.class);
        testCache.setHandler(handlerMock);
        testCache.setSourceFunction(sourceFunctionMock);
        testCache.put(key, testValue);
        return testCache;
    }

    //GettingPresentInstanceFromTheCache
    @Test
    public void getHappyPath() {
        long testValue = currentTimeMillis();
        CacheServiceImpl preparedCache = prepareDataForTest("TEST1", testValue);
        assertEquals(testValue, preparedCache.get("TEST1"));
    }

    @Test(expected = RuntimeException.class)
    public void getIfTheKeyNotExistsInSourceFunctionAndCache() {
        long testValue = currentTimeMillis();
        CacheServiceImpl preparedCache = prepareDataForTest("TEST", testValue);
        preparedCache.get("TEST-1");
    }

    @Test
    public void getIfTheKeyExistsInSourceFunctionButNotInCache() {
        long testValue = currentTimeMillis();
        CacheServiceImpl preparedCache = prepareDataForTest("TEST", testValue);

        long testValue1 = currentTimeMillis();
        when(sourceFunctionMock.apply("TEST1")).thenReturn(testValue1); //the behaviour for sourceFunction

        preparedCache.get("TEST1");
        assertEquals(testValue1, preparedCache.get("TEST1"));
    }

    //PuttingANewInstanceIntoACache
    @Test
    public void putHappyPath() {
        long testValue = currentTimeMillis();
        CacheServiceImpl preparedCache = prepareDataForTest("TEST", testValue);
        assertEquals(testValue, preparedCache.get("TEST"));
    }

    @Test
    public void putIfTheKeyAlreadyPresentsInTheCache() {
        long testValue = currentTimeMillis();
        CacheServiceImpl preparedCache = prepareDataForTest("TEST", testValue);
        long testValue1 = currentTimeMillis();
        preparedCache.put("TEST1", testValue1);
        assertEquals(testValue1, preparedCache.get("TEST1"));
    }

    //when getting instance not presents in the cache but exists in sourceFunction - need to fix
    @Test
    public void sourceFunctionAppliesOnes() {
        long testValue = currentTimeMillis();
        CacheServiceImpl preparedCache = prepareDataForTest("TEST", testValue);
        long testValue1 = currentTimeMillis();
        when(sourceFunctionMock.apply("TEST1")).thenReturn(testValue1); //the behaviour for sourceFunction
        //how to put an instance to sourceFunction?
        preparedCache.get("TEST-1");
        verify(sourceFunctionMock, times(1)).apply("TEST1");
    }

    //when put an instance with the same key to cache
    @Test
    public void handlerAcceptedOnes() {
        long testValue = currentTimeMillis();
        CacheServiceImpl preparedCache = prepareDataForTest("TEST", testValue);
        long testValue1 = currentTimeMillis();
        preparedCache.put("TEST", testValue1);
        verify(handlerMock, times(1)).accept("TEST");
    }
}
