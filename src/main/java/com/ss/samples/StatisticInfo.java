package com.ss.samples;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Getter
public class StatisticInfo implements Comparable<StatisticInfo> {

    private Instant startTime;
    private int numberOfTouch;
    private double frequencyOfTouch;

    public StatisticInfo() {
        startTime = Instant.now();
        numberOfTouch = 1;
        frequencyOfTouch = 1;
    }

    public double getFrequencyOfTouch() {
        return frequencyOfTouch;
    }

    public void incrementTheStatistic() {
        this.numberOfTouch++;
        this.frequencyOfTouch = numberOfTouch / Duration.between(startTime, Instant.now()).getSeconds();
    }

    @Override
    public int compareTo(StatisticInfo o) {
//        return frequencyOfTouch.compareTo(o.getFrequencyOfTouch());
        return 0;
    }
}
