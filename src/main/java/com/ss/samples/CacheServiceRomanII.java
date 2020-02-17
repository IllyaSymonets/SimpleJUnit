package com.ss.samples;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class CacheServiceRomanII extends CacheServiceImpl {

    int maxElementsCount = 100;

    class InternaCachedEntity extends AbstractCachedEntity {
        int usage;
        LocalDateTime datetime = LocalDateTime.now();
        InternaCachedEntity(String key, Object value) {
            super(key, value);
        }
    }

    @Override
    protected void _put(String key, AbstractCachedEntity value) {
        if (size() >= maxElementsCount) {
            List<AbstractCachedEntity> items = getActualValues();
            items.sort(new Comparator<AbstractCachedEntity>() {
                @Override
                public int compare(AbstractCachedEntity o1, AbstractCachedEntity o2) {
                    return 0; //
                }
            });
            while (items.size() >= maxElementsCount) {
                remove(items.get(0).getKey());
            }
        }
        super._put(key, value);
    }

    protected AbstractCachedEntity createEntityInstance(String key, Object value) {
        return new InternaCachedEntity(key, value);
    }

    @Override
    protected void updateStats(AbstractCachedEntity entity) {
        InternaCachedEntity stats = (InternaCachedEntity) entity;
        stats.usage++;
    }
}
