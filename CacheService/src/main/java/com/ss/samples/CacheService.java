package com.ss.samples;

public interface CacheService <T> {
    void put(String key, T data);
    T get(String key);
}
