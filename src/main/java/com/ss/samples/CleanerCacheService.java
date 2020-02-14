package com.ss.samples;

import java.time.Instant;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CleanerCacheService extends AbstractCacheService {

    private static final long MAX_CAPACITY = 100000;
    private static final long TIME_TO_LIVE = 60 * 5;
    private static final long NUMBER_OF_TIMES_USED = 10;

    @Override
    protected void _put(String key, AbstractCachedEntity value) {
        if (isFull()) {
           super.setInstance(clean(super.getInstance()));
        }

        long totalApplying = value.getStatistics().getTotalApplying();
        totalApplying++;
        value.getStatistics().setTotalApplying(totalApplying);

        super._put(key, value);
    }

    private Map<String, AbstractCachedEntity> clean(Map<String, AbstractCachedEntity> instance) {

        return instance.entrySet().stream()
            .filter(entity -> entity.getValue().getStatistics().getCreationTime()
                .isAfter(Instant.now().plusSeconds(TIME_TO_LIVE)))
            .filter(entity -> entity.getValue().getStatistics().getTotalApplying() <
                NUMBER_OF_TIMES_USED)
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private boolean isFull() {
        return super.getInstance().size() == MAX_CAPACITY;
    }

     static class StatisticsCacheEntity extends AbstractCachedEntity{
        Statistics statistics;

         public StatisticsCacheEntity(Object value) {
             super(value);
             this.statistics = new Statistics();
         }
     }
}
