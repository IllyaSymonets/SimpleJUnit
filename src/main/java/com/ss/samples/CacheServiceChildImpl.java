package com.ss.samples;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class CacheServiceChildImpl extends CacheServiceImpl {

    @Setter
    int maxCapacity = 100000;
    double percentToDelete = 0.15;
    double elementsToDelete = (1 - percentToDelete) * maxCapacity;

    @Getter
    class CachedEntityChild extends AbstractCachedEntity {

        int counterOfUsage = 0;

        public CachedEntityChild(String key, Object value) {
            super(key, value);
        }
    }

    @Override
    protected AbstractCachedEntity newEntity(String key, Object value) {

        return new CachedEntityChild(key, value);
    }

    @Override
    protected void updateStatistic(AbstractCachedEntity entity) {

        CachedEntityChild cachedEntity = (CachedEntityChild) entity;
        cachedEntity.counterOfUsage++;
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
