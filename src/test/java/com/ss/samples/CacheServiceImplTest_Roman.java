package com.ss.samples;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CacheServiceImplTest_Roman {

    @Spy
    private CacheServiceImpl cacheServiceSpy;

    @Mock
    private Function<String, Object> sourceFunctionMock;

    @Mock
    private Consumer<String> handlerMock;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getRealValueIsUsedMethodTest() {
        expectedException.expectMessage("Value was not found!");

        cacheServiceSpy.get("TEST-1");

        verify(cacheServiceSpy).getRealValue("TEST-1");
    }

    @Test(expected = NullPointerException.class)
    public void handleKeyExistsIsUsedMethodTest() {
        long testValue = System.currentTimeMillis();
        cacheServiceSpy.put("TEST", testValue);
        cacheServiceSpy.put("TEST", testValue);

        verify(cacheServiceSpy).handleKeyExists("TEST");
    }

    @Test
    public void sourceFunctionTest() throws NoSuchFieldException {
        CacheServiceImpl cacheService = new CacheServiceImpl();

        FieldSetter.setField(cacheService,
            cacheService.getClass().getDeclaredField("sourceFunction"), sourceFunctionMock);

        when(sourceFunctionMock.apply("KEY")).thenReturn(new Object());
        sourceFunctionMock.apply("KEY");
        verify(sourceFunctionMock).apply("KEY");

    }

    @Test(expected = StackOverflowError.class)
    public void handlerTest() throws NoSuchFieldException {
        CacheServiceImpl cacheService = new CacheServiceImpl();

        FieldSetter.setField(cacheService,
            cacheService.getClass().getDeclaredField("handler"), handlerMock);

        doThrow(StackOverflowError.class).when(handlerMock).accept("KEY");
        handlerMock.accept("KEY");
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