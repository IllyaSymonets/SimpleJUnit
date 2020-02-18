package com.ss.samples;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;

import com.ss.samples.CacheServiceImpl.AbstractCachedEntity;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CacheServiceImplTest_Illia {

    private CacheServiceImpl cacheService;
    private Consumer<AbstractCachedEntity> handlerMock;
    private Function<String, Object> sourceFunctionMock;

    @Before
    public void before() {
        cacheService = new CacheServiceImpl();
        handlerMock = Mockito.mock(Consumer.class);
        sourceFunctionMock = Mockito.mock(Function.class);
        cacheService.setSourceFunction(sourceFunctionMock);
        cacheService.setHandler(handlerMock);
    }

    @Test
    public void putAndGetHappyPath() {

        long testValue = currentTimeMillis();

        cacheService.put("TEST-PUT", testValue);

        assertEquals(testValue, cacheService.get("TEST-PUT"));
    }

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

    }

    @Test
    public void getSourceFunctionWorksCorrectlyTest() {

        long testValue=System.currentTimeMillis();
        Mockito.when(sourceFunctionMock.apply("TEST-GET"))
            .thenReturn(testValue);

        assertEquals(testValue, cacheService.get("TEST-GET"));

    }
}