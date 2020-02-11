package com.ss.samples;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CacheServiceImplTest_Roman {

    @Spy
    private CacheServiceImpl cacheService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getRealValueIsUsedMethodTest() {
        expectedException.expectMessage("Value was not found!");

        cacheService.get("TEST-1");

        verify(cacheService).getRealValue("TEST-1");
    }

    @Test (expected = NullPointerException.class)
    public void handleKeyExistsIsUsedMethodTest() {
        long testValue = System.currentTimeMillis();
        cacheService.put("TEST", testValue);
        cacheService.put("TEST", testValue);

        verify(cacheService).handleKeyExists("TEST");
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