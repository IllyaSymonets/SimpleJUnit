package com.ss.samples;

public class CleanerAbstractCacheService extends AbstractCacheService {

    private static final long MAX_CAPACITY = 100000;
    private static final long TIME_TO_LIVE = 5000 * 60 * 60;

    @Override
    protected void _put(String key, AbstractCachedEntity value) {
        if (isFull()) {
            clean();
        }
        super._put(key, value);
    }

    private void clean() {

    }

    private boolean isFull() {
        return getInstance().size() == MAX_CAPACITY;
    }
}
