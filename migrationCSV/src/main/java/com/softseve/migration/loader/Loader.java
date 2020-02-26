package com.softseve.migration.loader;


import java.util.List;

public interface Loader<T> {

    void load(List<T> data);
}
