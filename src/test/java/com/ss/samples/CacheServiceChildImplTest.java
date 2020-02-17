package com.ss.samples;

import org.junit.Before;

public class CacheServiceChildImplTest {

    private CacheServiceChildImpl testCache;

    @Before
    public void prepareDataForTest() {
        testCache = new CacheServiceChildImpl();
        testCache.setMaxCapacity(10);
    }

}
