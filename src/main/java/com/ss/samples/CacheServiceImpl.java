package com.ss.samples;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class CacheServiceImpl implements CacheService {

    static Map<String, Object> instance = new HashMap<>();
    private Function<String, Object> sourceFunction = null;
    private Consumer<String> handler = null;

    @Override
    public void put(String key, Object value) {
        if (instance.containsKey(key)) {
            handleKeyExists(key);
        }
        _put(key, value);
    }

    @Override
    public Object get(String key) {
        if (!instance.containsKey(key)) {
            Object value = getRealValue(key);
            if (Objects.isNull(value)) {
                throw new RuntimeException("Value was not found!");
            }
            put(key, value);
        }
        return instance.get(key);
    }

    Object getRealValue(String key) {
        return sourceFunction!=null ? sourceFunction.apply(key) : null;
    }

    void handleKeyExists(String key) {
        if (Objects.isNull(handler)) {
            handler.accept(key);
        }
    }

    private void _put(String key, Object value) {
        instance.put(key, value);
    }
}
