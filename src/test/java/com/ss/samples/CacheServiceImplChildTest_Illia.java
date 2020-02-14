package com.ss.samples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ss.samples.CacheServiceImpl.AbstractCachedEntity;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CacheServiceImplChildTest_Illia {

    private CacheServiceImplChild cacheService;
    private Consumer<AbstractCachedEntity> handlerMock;
    private Function<String, Object> sourceFunctionMock;
    private CacheServiceImplChild.AbstractCachedEntityChild entityMock;
    private CacheServiceImplChild.AbstractCachedEntityChild anotherEntityMock;
    private Stats statsMock;
    private Stats anotherStatsMock;

    @Before
    public void before() {

        cacheService = new CacheServiceImplChild();
        cacheService.setCacheSize(3);
        cacheService.setCountOfUses(2);
        cacheService.setSecondsToLive(2);
        handlerMock = Mockito.mock(Consumer.class);
        sourceFunctionMock = Mockito.mock(Function.class);
        cacheService.setSourceFunction(sourceFunctionMock);
        cacheService.setHandler(handlerMock);
        entityMock = Mockito.mock(CacheServiceImplChild.
            AbstractCachedEntityChild.class);
        anotherEntityMock = Mockito.mock(CacheServiceImplChild.
            AbstractCachedEntityChild.class);
        statsMock = Mockito.mock(Stats.class);
        anotherStatsMock = Mockito.mock(Stats.class);
    }

    @Test
    public void getAndPutHappyPath() {

        long testValue = System.currentTimeMillis();

        cacheService.put("TEST-GET", testValue);

        assertEquals(cacheService.get("TEST-GET"), testValue);
    }

    @Test
    public void cleanCacheByCounts() {

        cacheService.getInstance().put("TEST-PUT1", entityMock);
        cacheService.getInstance().put("TEST-PUT2", entityMock);
        cacheService.put("TEST-PUT3", new Object());

        Mockito.when(statsMock.getCountOfUses()).thenReturn(2);
        Mockito.when(entityMock.getStats()).thenReturn(statsMock);
        cacheService.put("TEST-PUT4", new Object());

        assertTrue(cacheService.getInstance()
            .keySet().contains("TEST-PUT1") &&
            cacheService.getInstance()
                .keySet().contains("TEST-PUT2") &&
            cacheService.getInstance()
                .keySet().contains("TEST-PUT4") &&
            cacheService.getInstance()
                .size() == 3);
    }

    @Test
    public void cleanCacheByTime() throws InterruptedException {

        cacheService.getInstance().put("TEST-PUT1", entityMock);
        cacheService.getInstance().put("TEST-PUT2", entityMock);
        cacheService.getInstance().put("TEST-PUT3", anotherEntityMock);

        Mockito.when(statsMock.getCountOfUses()).thenReturn(2);
        Mockito.when(statsMock.getTimeOfLastAccess())
            .thenReturn(System.currentTimeMillis());
        Mockito.when(anotherStatsMock.getCountOfUses()).thenReturn(2);
        Mockito.when(anotherStatsMock.getTimeOfLastAccess()).
            thenReturn(System.currentTimeMillis() - 2100);
        Mockito.when(entityMock.getStats()).thenReturn(statsMock);
        Mockito.when(anotherEntityMock.getStats())
            .thenReturn(anotherStatsMock);

        cacheService.put("TEST-PUT4", new Object());

        assertTrue(cacheService.getInstance()
            .keySet().contains("TEST-PUT1") &&
            cacheService.getInstance()
                .keySet().contains("TEST-PUT2") &&
            cacheService.getInstance()
                .keySet().contains("TEST-PUT4") &&
            cacheService.getInstance()
                .size() == 3);
    }

}