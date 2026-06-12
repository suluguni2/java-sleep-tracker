package ru.yandex.practicum.sleeptracker.Functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;
import ru.yandex.practicum.sleeptracker.UserChronotype;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserChronotypeFunction implements SleepAnalysisFunction {

    private static final int OWL_SLEEP_HOUR = 23;
    private static final int OWL_WAKE_HOUR = 9;
    private static final int LARK_SLEEP_HOUR = 22;
    private static final int LARK_WAKE_HOUR = 7;
    private static final int NIGHT_END_HOUR = 6;
    private static final int DAY_START_HOUR = 20;
    private static final int NOON_HOUR = 12;

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", "нет данных");
        }

        LocalDateTime periodStart = sessions.stream()
                .map(SleepingSession::getSleepStart)
                .min(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDateTime periodEnd = sessions.stream()
                .map(SleepingSession::getWakeUp)
                .max(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDate firstNight = determineFirstNight(periodStart);
        LocalDate lastNight = determineLastNight(periodEnd);

        Map<UserChronotype, Long> chronotypeCounts = Stream.iterate(firstNight,
                        date -> !date.isAfter(lastNight),
                        date -> date.plusDays(1))
                .map(nightDate -> classifyNight(nightDate, sessions))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        chronotypeCounts.putIfAbsent(UserChronotype.OWL, 0L);
        chronotypeCounts.putIfAbsent(UserChronotype.LARK, 0L);
        chronotypeCounts.putIfAbsent(UserChronotype.PIGEON, 0L);

        UserChronotype dominantType = determineDominantType(chronotypeCounts);

        return new SleepAnalysisResult("Хронотип пользователя",
                dominantType.getDisplayName());
    }

    private LocalDate determineFirstNight(LocalDateTime periodStart) {
        if (periodStart.getHour() >= NOON_HOUR) {
            return periodStart.toLocalDate().plusDays(1);
        } else {
            return periodStart.toLocalDate();
        }
    }

    private LocalDate determineLastNight(LocalDateTime periodEnd) {
        return periodEnd.toLocalDate();
    }

    private UserChronotype classifyNight(LocalDate nightDate, List<SleepingSession> sessions) {
        LocalDateTime nightStart = nightDate.atStartOfDay();
        LocalDateTime nightEnd = nightDate.atTime(NIGHT_END_HOUR, 0);

        Optional<SleepingSession> matchingSession = sessions.stream()
                .filter(session -> {
                    LocalDateTime sessionStart = session.getSleepStart();
                    LocalDateTime sessionEnd = session.getWakeUp();
                    return sessionStart.isBefore(nightEnd) && sessionEnd.isAfter(nightStart);
                })
                .findFirst();

        if (!matchingSession.isPresent()) {
            return null;
        }

        SleepingSession session = matchingSession.get();
        LocalTime sleepTime = session.getSleepStart().toLocalTime();
        LocalTime wakeTime = session.getWakeUp().toLocalTime();

        if ((sleepTime.isAfter(LocalTime.of(NIGHT_END_HOUR, 0)) ||
                sleepTime.equals(LocalTime.of(NIGHT_END_HOUR, 0))) &&
                wakeTime.isAfter(sleepTime) &&
                wakeTime.isBefore(LocalTime.of(DAY_START_HOUR, 0))) {
            return null;
        }

        boolean isOwl;
        if (sleepTime.isBefore(LocalTime.of(NIGHT_END_HOUR, 0))) {
            isOwl = !wakeTime.isBefore(LocalTime.of(OWL_WAKE_HOUR, 0));
        } else {
            isOwl = !sleepTime.isBefore(LocalTime.of(OWL_SLEEP_HOUR, 0))
                    && !wakeTime.isBefore(LocalTime.of(OWL_WAKE_HOUR, 0));
        }

        boolean isLark = sleepTime.isBefore(LocalTime.of(LARK_SLEEP_HOUR, 0)) &&
                !wakeTime.isAfter(LocalTime.of(LARK_WAKE_HOUR, 0));

        if (isOwl) {
            return UserChronotype.OWL;
        } else if (isLark) {
            return UserChronotype.LARK;
        } else {
            return UserChronotype.PIGEON;
        }
    }

    private UserChronotype determineDominantType(Map<UserChronotype, Long> counts) {
        long owlCount = counts.getOrDefault(UserChronotype.OWL, 0L);
        long larkCount = counts.getOrDefault(UserChronotype.LARK, 0L);
        long pigeonCount = counts.getOrDefault(UserChronotype.PIGEON, 0L);

        if (owlCount > larkCount && owlCount > pigeonCount) {
            return UserChronotype.OWL;
        }
        if (larkCount > owlCount && larkCount > pigeonCount) {
            return UserChronotype.LARK;
        }
        if (pigeonCount > owlCount && pigeonCount > larkCount) {
            return UserChronotype.PIGEON;
        }

        return UserChronotype.PIGEON;
    }
}