package com.ss.samples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class CacheServiceImplTest {

    CacheServiceImpl cacheService;
    CacheServiceImpl cacheServiceSpy;

    private static void accept(String s) throws Exception {
        throw new Exception("Element with key: " + s + " already exist!");
    }

    @Before
    public void before() {
        cacheService = new CacheServiceImpl();
        cacheServiceSpy = Mockito.spy(cacheService);
        CacheServiceImpl.instance = new HashMap<>();
    }

    @After
    public void after() {
        CacheServiceImpl.instance = new HashMap<>();
    }

    @Test
    public void get() throws Exception {
        User expectedObject = new User(100500, "User");
        CacheServiceImpl.instance.put(String.valueOf(expectedObject.getId()), expectedObject);
        Function<String, Object> sourceFunction = s -> expectedObject;

        setValueToPrivateField(cacheServiceSpy, "sourceFunction", sourceFunction);

        assertEquals(expectedObject, cacheService.get(String.valueOf(expectedObject.getId())));
    }

    @Test(expected = Exception.class)
    public void getIfNotExistInRealValue() throws Exception {
        User user = new User(100500, "User");
        Function<String, Object> sourceFunction = s -> null;
        setValueToPrivateField(cacheServiceSpy, "sourceFunction", sourceFunction);

        cacheService.get(String.valueOf(user.getId()));
    }

    @Test
    public void getIfNotExistButExistInRealValue() throws Exception {
        User expectedObject = new User(100500, "User");
        Function<String, Object> sourceFunction = s -> expectedObject;

        setValueToPrivateField(cacheServiceSpy, "sourceFunction", sourceFunction);

        assertEquals(expectedObject, cacheServiceSpy.get(String.valueOf(expectedObject.getId())));
    }

    @Test
    public void put() throws Exception {
        Consumer<String> handlerToMock = System.out::println;
        setValueToPrivateField(cacheServiceSpy, "handler", handlerToMock);

        User user = new User(100500, "User");
        cacheServiceSpy.put(String.valueOf(user.getId()), user);
        assertEquals(user, CacheServiceImpl.instance.get(String.valueOf(user.getId())));
    }

    @Test
    public void putIfExist() throws Exception {
        Consumer<String> handlerToMock = System.out::println;
        setValueToPrivateField(cacheServiceSpy, "handler", handlerToMock);

        User user = new User(100500, "User");
        User userTwo = new User(100500, "UserTwo");

        cacheServiceSpy.put(String.valueOf(user.getId()), user);
        cacheServiceSpy.put(String.valueOf(user.getId()), user);
        User actualUser = (User) CacheServiceImpl.instance.get(String.valueOf(user.getId()));
        assertEquals(user, actualUser);
    }

    public static class User {

        private long id;
        private String name;

        public User(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof User)) return false;

            User user = (User) o;

            if (getId() != user.getId()) return false;
            return getName() != null ? getName().equals(user.getName()) : user.getName() == null;
        }

        @Override
        public int hashCode() {
            int result = (int) (getId() ^ (getId() >>> 32));
            result = 31 * result + (getName() != null ? getName().hashCode() : 0);
            return result;
        }

    }

    private void setValueToPrivateField(Object targetObject, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field sourceFunctionField = CacheServiceImpl.class.getDeclaredField(fieldName);
        sourceFunctionField.setAccessible(true);
        sourceFunctionField.set(targetObject, value);
    }
}