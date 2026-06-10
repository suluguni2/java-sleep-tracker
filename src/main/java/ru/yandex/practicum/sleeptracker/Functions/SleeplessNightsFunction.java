package ru.yandex.practicum.sleeptracker.Functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SleeplessNightsFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", "нет данных");
        }

        LocalDateTime periodStart = sessions.stream()
                .map(SleepingSession::getSleepStart)
                .min(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDateTime periodEnd = sessions.stream()
                .map(SleepingSession::getWakeUp)
                .max(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDate firstNightDate = determineFirstNightDate(periodStart);
        LocalDate lastNightDate = determineLastNightDate(periodEnd, sessions);

        long sleeplessNights = Stream.iterate(firstNightDate,
                        date -> !date.isAfter(lastNightDate),
                        date -> date.plusDays(1))
                .filter(date -> isSleeplessNight(date, sessions))
                .count();

        return new SleepAnalysisResult("Количество бессонных ночей", String.valueOf(sleeplessNights));
    }

    private LocalDate determineFirstNightDate(LocalDateTime periodStart) {
        if (periodStart.getHour() >= 12) {
            return periodStart.toLocalDate().plusDays(1);
        } else {
            return periodStart.toLocalDate();
        }
    }

    private LocalDate determineLastNightDate(LocalDateTime periodEnd, List<SleepingSession> sessions) {
        LocalDate endDate = periodEnd.toLocalDate();

        boolean hadNightSleep = sessions.stream()
                .anyMatch(session -> {
                    LocalDateTime nightStart = endDate.atStartOfDay();
                    LocalDateTime nightEnd = endDate.atTime(6, 0);
                    LocalDateTime sessionStart = session.getSleepStart();
                    LocalDateTime sessionEnd = session.getWakeUp();
                    return sessionStart.isBefore(nightEnd) && sessionEnd.isAfter(nightStart);
                });

        if (hadNightSleep) {
            return endDate;
        } else {
            return endDate.plusDays(1);
        }
    }

    private boolean isSleeplessNight(LocalDate nightDate, List<SleepingSession> sessions) {
        LocalDateTime nightStart = nightDate.atStartOfDay();
        LocalDateTime nightEnd = nightDate.atTime(6, 0);

        return sessions.stream()
                .noneMatch(session -> {
                    LocalDateTime sessionStart = session.getSleepStart();
                    LocalDateTime sessionEnd = session.getWakeUp();
                    return sessionStart.isBefore(nightEnd) && sessionEnd.isAfter(nightStart);
                });
    }
}