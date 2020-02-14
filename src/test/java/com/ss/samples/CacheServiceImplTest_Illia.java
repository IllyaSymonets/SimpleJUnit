package com.ss.samples;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;

import com.ss.samples.CacheServiceImpl.AbstractCachedEntity;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CacheServiceImplTest_Illia {

    CacheServiceImpl cacheService;
    Consumer<AbstractCachedEntity> handlerMock;
    Function<String, Object> sourceFunctionMock;
//unnecessarily to have such method with prepareDataForTest(String key, long testValue). In some tests they execute at the same time.
    @Before
    public void before() {
        cacheService = new CacheServiceImpl();
        handlerMock = Mockito.mock(Consumer.class);
        sourceFunctionMock = Mockito.mock(Function.class);
        cacheService.setSourceFunction(sourceFunctionMock);
        cacheService.setHandler(handlerMock);
        cacheService.instance = new HashMap<>();
    }
//here executes @before method, creates an empty HashMap and other redundant instances
    @Test
    public void putHappyPath() {

        long testValue = currentTimeMillis();

        CacheServiceImpl impl = prepareDataForTest("TEST-PUT", testValue);

        assertEquals(testValue, impl.instance.get("TEST-PUT").getValue());
    }
//here executes @before method, creates an empty HashMap and other redundant instances
    @Test
    public void getHappyPath() {

        long testValue = currentTimeMillis();

        CacheServiceImpl impl = prepareDataForTest("TEST-GET", testValue);

        assertEquals(testValue, impl.get("TEST-GET"));
    }
//here executes @before method, creates an empty HashMap and other redundant instances
    @Test
    public void putIsHandlerExecutesTest() {

        long testValue = currentTimeMillis();

        cacheService.put("TEST-PUT", testValue);
        cacheService.put("TEST-PUT", testValue);

        Mockito.verify(handlerMock).accept(Mockito.any());
    }

    @Test(expected = RuntimeException.class)
    public void getRuntimeExceptionTest() {

        cacheService.get("TEST-GET");
    //extra empty line
    }

    @Test
    public void getSourceFunctionWorksCorrectlyTest() {

        long testValue=System.currentTimeMillis();
        Mockito.when(sourceFunctionMock.apply("TEST-GET"))
            .thenReturn(testValue);

        assertEquals(testValue, cacheService.get("TEST-GET"));
        //extra empty line
    }

    private CacheServiceImpl prepareDataForTest(String key, long testValue) {

        CacheServiceImpl impl = new CacheServiceImpl();
        impl.put(key, testValue);
        return impl;
    }
}