package ru.yandex.practicum.sleeptracker.Functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;

public class AverageSessionDurationFunction implements SleepAnalysisFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Средняя продолжительность сессии", "нет данных");
        }

        double averageMinutes = sessions.stream()
                .mapToLong(session -> Duration.between(session.getSleepStart(), session.getWakeUp()).toMinutes())
                .average()
                .orElse(0);

        return new SleepAnalysisResult("Средняя продолжительность сессии", Math.round(averageMinutes));
    }
}