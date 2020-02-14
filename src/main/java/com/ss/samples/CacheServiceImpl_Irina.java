package com.ss.samples;

public class CacheServiceImpl_Irina extends CacheServiceImpl implements GarbageCollector {
    @Override
    public void collectGarbage() {

    }

    class CachedEntity extends AbstractCachedEntity{
        CachedEntity(Object value) {
            super(value);
        }
    }
}
