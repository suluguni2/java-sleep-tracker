package ru.yandex.practicum.sleeptracker.Functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;

public class MaxSessionDurationFunction implements SleepAnalysisFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Максимальная продолжительность сессии", "нет данных");
        }

        long maxMinutes = sessions.stream()
                .mapToLong(session -> Duration.between(session.getSleepStart(), session.getWakeUp()).toMinutes())
                .max()
                .orElse(0);

        return new SleepAnalysisResult("Максимальная продолжительность сессии", maxMinutes);
    }
}