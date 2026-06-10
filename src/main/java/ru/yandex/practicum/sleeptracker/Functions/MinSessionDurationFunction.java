package ru.yandex.practicum.sleeptracker.Functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MinSessionDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Минимальная продолжительность сессии", "нет данных");
        }

        long minMinutes = sessions.stream()
                .mapToLong(session -> Duration.between(session.getSleepStart(), session.getWakeUp()).toMinutes())
                .min()
                .orElse(0);

        return new SleepAnalysisResult("Минимальная продолжительность сессии", minMinutes + " мин");
    }
}