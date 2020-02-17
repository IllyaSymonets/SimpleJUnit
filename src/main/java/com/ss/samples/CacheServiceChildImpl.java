package com.ss.samples;

public class CacheServiceChildImpl  extends CacheServiceImpl{
    int maxCapacity = 100000;

    class CachedEntityChild extends AbstractCachedEntity{
        int counterOfUsage;

        public CachedEntityChild(String key, Object value, int counterOfUsage) {
            super(key, value);
            this.counterOfUsage = counterOfUsage;
        }
    }
}
