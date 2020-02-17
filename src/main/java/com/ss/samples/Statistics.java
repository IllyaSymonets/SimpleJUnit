package com.ss.samples;

import java.time.Instant;
import lombok.Data;

@Data
public class Statistics {

    private Instant lastAccessTime;
    private long numberOfUses;

    public Statistics() {
        this.lastAccessTime = Instant.now();
        this.numberOfUses = 0;
    }

    protected void updateTime() {
        this.lastAccessTime = Instant.now();
    }

    protected void updateNumberOfUses() {
        this.numberOfUses++;
    }
}
