package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;

public class SleepingSession {
    private final LocalDateTime sleepStart;
    private final LocalDateTime wakeUp;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime sleepStart, LocalDateTime wakeUp, SleepQuality quality) {
        this.sleepStart = sleepStart;
        this.wakeUp = wakeUp;
        this.quality = quality;
    }

    public LocalDateTime getSleepStart() {
        return sleepStart;
    }

    public LocalDateTime getWakeUp() {
        return wakeUp;
    }

    public SleepQuality getQuality() {
        return quality;
    }
}