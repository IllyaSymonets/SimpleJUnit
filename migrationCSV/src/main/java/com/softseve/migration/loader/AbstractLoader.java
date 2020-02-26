package com.softseve.migration.loader;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLoader<T> implements Loader<T> {

    private int customBatchSize;

    public AbstractLoader(int customBatchSize) {
        if (customBatchSize > 0) this.customBatchSize = customBatchSize;
    }

    @Override
    public abstract void load(List<T> data);

    protected int getCustomPartSize() {
        return customBatchSize;
    }

    protected int getOperationNumber(List<T> data, int partSize) {
        int operationNum;
        if (data.size() % partSize == 0) {
            operationNum = data.size() / partSize;
        } else {
            operationNum = data.size() / partSize + 1;
        }
        return operationNum;
    }

    protected List<List<T>> getDataParts(List<T> data, int partSize) {
        List<List<T>> patientResults = new ArrayList<>();
        int numberOfOperations = getOperationNumber(data, partSize);
        for (int i = 0; i < numberOfOperations; i++) {
            int start = i * partSize;
            int end = start + partSize;
            if (end > data.size()) end = data.size();

            patientResults.add(data.subList(start, end));
        }
        return patientResults;
    }
}
