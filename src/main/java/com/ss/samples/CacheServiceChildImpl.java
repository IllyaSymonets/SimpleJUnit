package com.ss.samples;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;

public class CacheServiceChildImpl extends CacheServiceImpl {

    int maxCapacity = 100000;
    double percentToDelete = 0.15;
    double elementsToDelete = (1 - percentToDelete) * maxCapacity;

    @Getter
    class CachedEntityChild extends AbstractCachedEntity {

        int counterOfUsage;

        public CachedEntityChild(String key, Object value, int counterOfUsage) {
            super(key, value);
            this.counterOfUsage = counterOfUsage;
        }
    }

    @Override
    protected void _put(String key, AbstractCachedEntity value) {
        if (size() < maxCapacity) {
            List<AbstractCachedEntity> items = getActualValues();

            List<CachedEntityChild> children = new ArrayList<>();

            for (AbstractCachedEntity entity : items) {
                children.add((CachedEntityChild) entity);
            }
            children.sort(Comparator.comparingInt(CachedEntityChild::getCounterOfUsage));
            for (int i = 0; i < elementsToDelete; i++) {
                remove(children.get(0).getKey());
                children.remove(0);
            }
        }
        super._put(key, value);
    }
}
