package com.ss.samples;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CacheServiceChildImpl extends CacheServiceImpl {

    private int maxCapacity = 100000;
    private double percentToDelete = 0.15;
    private int elementsToDelete = (int) (percentToDelete * maxCapacity);

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        elementsToDelete = (int) (percentToDelete * maxCapacity);
    }

    @Getter
    @Setter
    static class CachedEntityChild extends AbstractCachedEntity {

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
        while (size() >= maxCapacity) {
            List<CachedEntityChild> children = convertElements(getActualValues());
            children.sort(Comparator.comparingInt(CachedEntityChild::getCounterOfUsage));
            cleanCache(children);
        }
        super._put(key, value);
    }

    private List<CachedEntityChild> convertElements(List<AbstractCachedEntity> items) {

        List<CachedEntityChild> children = new ArrayList<>();
        for (AbstractCachedEntity entity : items) {
            children.add((CachedEntityChild) entity);
        }
        return children;
    }

    private void cleanCache(List<CachedEntityChild> children) {
        for (int i = 0; i < elementsToDelete; i++) {
            remove(children.get(0).getKey());
            children.remove(0);
        }

        for (CachedEntityChild entity : children) {
            entity.counterOfUsage = 0;
        }
    }
}
