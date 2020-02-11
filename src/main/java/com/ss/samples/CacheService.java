package com.ss.samples;

public interface CacheService {
    void put(String key, Object data);
    Object get(String key);
}
