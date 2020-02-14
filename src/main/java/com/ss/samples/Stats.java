package com.ss.samples;

import lombok.Getter;

@Getter
public class Stats {

    private int countOfUses = 0;
    private long timeOfLastAccess = System.currentTimeMillis();

    public void update() {
        countOfUses++;
        timeOfLastAccess = System.currentTimeMillis();
    }
}
