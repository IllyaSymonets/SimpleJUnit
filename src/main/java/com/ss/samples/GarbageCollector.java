package com.ss.samples;

import java.util.Map;

public interface GarbageCollector {

    void collectGarbageByFrequency(Map<String, CacheServiceImpl_Irina.CachedEntity> instance);

    void collectGarbageByLeastUsedEntity(Map<String, CacheServiceImpl_Irina.CachedEntity> instance);
}
