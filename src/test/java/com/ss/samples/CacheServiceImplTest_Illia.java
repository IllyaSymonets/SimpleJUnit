package com.ss.samples;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(CacheServiceImpl.class)
public class CacheServiceImplTest_Illia {

    CacheServiceImpl cacheService;
    CacheServiceImpl cacheServiceSpy;
    Function<String, Object> sourceFunctionTest =
        Mockito.mock(Function.class);
    Consumer<String> handlerTest =
        Mockito.mock(Consumer.class);


    @Before
    public void before() {
        cacheService = new CacheServiceImpl();
        cacheServiceSpy = Mockito.spy(cacheService);
        CacheServiceImpl.instance = new HashMap<>();
    }

    @Test(expected = RuntimeException.class)
    public void getRealValueRuntimeExceptionTest() {

        cacheService.get("TEST-GET");
        Mockito.verify(cacheServiceSpy).getRealValue("TEST-GET");
    }

    @Test
    public void getRealValueTest() {

        Object testObject = new Object();
        Mockito.when(cacheServiceSpy.
            getRealValue("TEST-GET")).thenReturn(testObject);

        assertEquals(testObject, cacheServiceSpy.get("TEST-GET"));
    }

    @Test(expected = NullPointerException.class)
    public void handleKeyExistsTest() {

        cacheService.put("TEST-PUT", currentTimeMillis());
        cacheService.put("TEST-PUT", currentTimeMillis());
        Mockito.verify(cacheServiceSpy).handleKeyExists("TEST-GET");
    }

    @Test
    public void putHappyPath() {

        long testValue = currentTimeMillis();

        CacheServiceImpl impl = prepareDataForTest("TEST-PUT", testValue);

        assertEquals(testValue, CacheServiceImpl.instance.get("TEST-PUT"));
    }

    @Test
    public void getHappyPath() {

        long testValue = currentTimeMillis();

        CacheServiceImpl impl = prepareDataForTest("TEST-GET", testValue);

        assertEquals(testValue, impl.get("TEST-GET"));
    }

    @Test
    public void sourceFunctionTest() throws Exception {

        FieldSetter.setField(cacheService,
            cacheService.getClass().
                getDeclaredField("sourceFunction"),
            sourceFunctionTest);
        Object testObject = new Object();

        Mockito.when(sourceFunctionTest.apply("TEST-GET")).thenReturn(testObject);

        assertEquals(testObject, cacheService.get("TEST-GET"));
    }

    @Test
    public void handlerTest() throws Exception {

        FieldSetter.setField(cacheService,
            cacheService.getClass().
                getDeclaredField("handler"),
            handlerTest);

        cacheService.put("TEST-PUT", 1);
        cacheService.put("TEST-PUT", 2);

        assertTrue(Objects.nonNull(handlerTest)
            && cacheService.get("TEST-PUT").equals(2));
    }

    private CacheServiceImpl prepareDataForTest(String key, long testValue) {

        CacheServiceImpl impl = new CacheServiceImpl();
        impl.put(key, testValue);
        return impl;
    }
}