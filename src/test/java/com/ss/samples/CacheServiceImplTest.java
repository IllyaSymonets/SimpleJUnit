package com.ss.samples;

import static org.junit.Assert.*;

import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

public class CacheServiceImplTest {

    CacheServiceImpl cacheService;
    CacheServiceImpl cacheServiceSpy;

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

        cacheService.put("TEST-PUT", System.currentTimeMillis());
        cacheService.put("TEST-PUT", System.currentTimeMillis());
        Mockito.verify(cacheServiceSpy).handleKeyExists("TEST-GET");
    }

    @Test
    public void putHappyPath() {

        long testValue = System.currentTimeMillis();

        CacheServiceImpl impl = prepareDataForTest("TEST-PUT", testValue);

        assertEquals(testValue, impl.instance.get("TEST-PUT"));
    }

    @Test
    public void getHappyPath() {

        long testValue = System.currentTimeMillis();

        CacheServiceImpl impl = prepareDataForTest("TEST-GET", testValue);

        assertEquals(testValue, impl.get("TEST-GET"));
    }

    private CacheServiceImpl prepareDataForTest(String key, long testValue) {

        CacheServiceImpl impl = new CacheServiceImpl();
        impl.put(key, testValue);
        return impl;
    }
}