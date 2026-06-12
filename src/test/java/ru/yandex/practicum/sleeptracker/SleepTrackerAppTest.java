package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.Functions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {

    // TotalSessionsFunction

    @Test
    @DisplayName("Подсчёт сессий сна: несколько сессий")
    void testTotalSessionsFunctionWithMultipleSessions() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 1, 23, 0, 2026, 6, 2, 7, 0, SleepQuality.GOOD),
                createSession(2026, 6, 2, 23, 30, 2026, 6, 3, 7, 30, SleepQuality.NORMAL)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new TotalSessionsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("Всего сессий сна", result.getDescription());
        assertEquals("2", result.getValueAsString());
    }

    @Test
    @DisplayName("Подсчёт сессий сна: пустой список")
    void testTotalSessionsFunctionEmptyList() {
        List<SleepingSession> sessions = List.of();

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new TotalSessionsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.getValueAsString());
    }

    // MinSessionDurationFunction

    @Test
    @DisplayName("Минимальная продолжительность: несколько сессий")
    void testMinDurationMultipleSessions() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 1, 23, 0, 2026, 6, 2, 7, 0, SleepQuality.GOOD),
                createSession(2026, 6, 2, 23, 0, 2026, 6, 3, 5, 0, SleepQuality.BAD),
                createSession(2026, 6, 3, 22, 0, 2026, 6, 4, 8, 0, SleepQuality.NORMAL)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new MinSessionDurationFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("360", result.getValueAsString());
    }

    @Test
    @DisplayName("Минимальная продолжительность: пустой список")
    void testMinDurationEmptyList() {
        List<SleepingSession> sessions = List.of();

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new MinSessionDurationFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("нет данных", result.getValueAsString());
    }

    // MaxSessionDurationFunction

    @Test
    @DisplayName("Максимальная продолжительность: несколько сессий")
    void testMaxDurationMultipleSessions() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 1, 23, 0, 2026, 6, 2, 7, 0, SleepQuality.GOOD),
                createSession(2026, 6, 2, 23, 0, 2026, 6, 3, 5, 0, SleepQuality.BAD),
                createSession(2026, 6, 3, 22, 0, 2026, 6, 4, 8, 0, SleepQuality.NORMAL)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new MaxSessionDurationFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("600", result.getValueAsString());
    }

    @Test
    @DisplayName("Максимальная продолжительность: одна сессия")
    void testMaxDurationSingleSession() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 1, 23, 0, 2026, 6, 2, 7, 30, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new MaxSessionDurationFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("510", result.getValueAsString());
    }

    // AverageSessionDurationFunction

    @Test
    @DisplayName("Средняя продолжительность: несколько сессий")
    void testAverageDurationMultipleSessions() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 1, 23, 0, 2026, 6, 2, 7, 0, SleepQuality.GOOD),
                createSession(2026, 6, 2, 23, 0, 2026, 6, 3, 5, 0, SleepQuality.BAD),
                createSession(2026, 6, 3, 22, 0, 2026, 6, 4, 8, 0, SleepQuality.NORMAL)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new AverageSessionDurationFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("480", result.getValueAsString());
    }

    @Test
    @DisplayName("Средняя продолжительность: пустой список")
    void testAverageDurationEmptyList() {
        List<SleepingSession> sessions = List.of();

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new AverageSessionDurationFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("нет данных", result.getValueAsString());
    }

    // BadSleepSessionsFunction

    @Test
    @DisplayName("Плохие сессии: несколько BAD сессий")
    void testBadSessionsMultiple() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 1, 23, 0, 2026, 6, 2, 7, 0, SleepQuality.GOOD),
                createSession(2026, 6, 2, 23, 0, 2026, 6, 3, 5, 0, SleepQuality.BAD),
                createSession(2026, 6, 3, 22, 0, 2026, 6, 4, 8, 0, SleepQuality.BAD),
                createSession(2026, 6, 4, 23, 0, 2026, 6, 5, 7, 0, SleepQuality.NORMAL)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new BadSleepSessionsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("2", result.getValueAsString());
    }

    @Test
    @DisplayName("Плохие сессии: нет BAD сессий")
    void testBadSessionsNone() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 1, 23, 0, 2026, 6, 2, 7, 0, SleepQuality.GOOD),
                createSession(2026, 6, 2, 23, 0, 2026, 6, 3, 7, 0, SleepQuality.NORMAL)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new BadSleepSessionsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.getValueAsString());
    }

    // SleeplessNightsFunction

    @Test
    @DisplayName("Бессонные ночи: период логирования пересекает границу месяцев")
    void testSleeplessNightsCrossesMonthBoundary() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 10, 31, 23, 0, 2026, 11, 1, 7, 0, SleepQuality.GOOD),
                createSession(2026, 11, 1, 23, 0, 2026, 11, 2, 7, 0, SleepQuality.GOOD),
                createSession(2026, 11, 3, 14, 0, 2026, 11, 3, 15, 0, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new SleeplessNightsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("2", result.getValueAsString());
    }

    @Test
    @DisplayName("Бессонные ночи: бессонная ночь на границе месяцев")
    void testSleeplessNightsOnMonthBoundary() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 10, 31, 14, 0, 2026, 10, 31, 15, 0, SleepQuality.GOOD),
                createSession(2026, 11, 1, 14, 0, 2026, 11, 1, 15, 0, SleepQuality.GOOD),
                createSession(2026, 11, 2, 23, 0, 2026, 11, 3, 7, 0, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new SleeplessNightsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("2", result.getValueAsString());
    }

    @Test
    @DisplayName("Бессонные ночи: все ночи бессонные (дневной сон)")
    void testSleeplessNightsAllSleepless() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 1, 14, 0, 2026, 6, 1, 15, 0, SleepQuality.GOOD),
                createSession(2026, 6, 2, 14, 0, 2026, 6, 2, 15, 0, SleepQuality.GOOD),
                createSession(2026, 6, 3, 14, 0, 2026, 6, 3, 15, 0, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new SleeplessNightsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("3", result.getValueAsString());
    }

    @Test
    @DisplayName("Бессонные ночи: ни одной бессонной ночи")
    void testSleeplessNightsNoneSleepless() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 1, 23, 0, 2026, 6, 2, 7, 0, SleepQuality.GOOD),
                createSession(2026, 6, 2, 23, 0, 2026, 6, 3, 7, 0, SleepQuality.GOOD),
                createSession(2026, 6, 3, 23, 0, 2026, 6, 4, 7, 0, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new SleeplessNightsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.getValueAsString());
    }

    @Test
    @DisplayName("Бессонные ночи: смешанный случай")
    void testSleeplessNightsMixed() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 1, 23, 0, 2026, 6, 2, 7, 0, SleepQuality.GOOD),
                createSession(2026, 6, 2, 14, 0, 2026, 6, 2, 15, 0, SleepQuality.GOOD),
                createSession(2026, 6, 3, 23, 0, 2026, 6, 4, 7, 0, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new SleeplessNightsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("1", result.getValueAsString());
    }

    @Test
    @DisplayName("Бессонные ночи: сессия полностью внутри ночи (2:00-5:00)")
    void testSleeplessNightsSessionInsideNight() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 2, 2, 0, 2026, 6, 2, 5, 0, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new SleeplessNightsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.getValueAsString());
    }

    @Test
    @DisplayName("Бессонные ночи: пустой список")
    void testSleeplessNightsEmptyList() {
        List<SleepingSession> sessions = List.of();

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new SleeplessNightsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("нет данных", result.getValueAsString());
    }

    @Test
    @DisplayName("Бессонные ночи: сессия переходит через полночь (23:00-3:00)")
    void testSleeplessNightsSessionCrossesMidnight() {
        List<SleepingSession> sessions = List.of(
                createSession(2026, 6, 1, 23, 0, 2026, 6, 2, 3, 0, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new SleeplessNightsFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.getValueAsString());
    }

    // UserChronotypeFunction

    @Test
    @DisplayName("Хронотип: пользователь - сова")
    void testChronotypeOwl() {
        List<SleepingSession> sessions = List.of(
                createSession(2025, 10, 1, 23, 30, 2025, 10, 2, 9, 30, SleepQuality.GOOD),
                createSession(2025, 10, 2, 23, 45, 2025, 10, 3, 10, 0, SleepQuality.GOOD),
                createSession(2025, 10, 3, 0, 15, 2025, 10, 4, 9, 15, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new UserChronotypeFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("сова", result.getValueAsString());
    }

    @Test
    @DisplayName("Хронотип: пользователь - жаворонок")
    void testChronotypeLark() {
        List<SleepingSession> sessions = List.of(
                createSession(2025, 10, 1, 21, 0, 2025, 10, 2, 6, 0, SleepQuality.GOOD),
                createSession(2025, 10, 2, 21, 30, 2025, 10, 3, 6, 30, SleepQuality.GOOD),
                createSession(2025, 10, 3, 20, 45, 2025, 10, 4, 5, 45, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new UserChronotypeFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("жаворонок", result.getValueAsString());
    }

    @Test
    @DisplayName("Хронотип: пользователь - голубь")
    void testChronotypePigeon() {
        List<SleepingSession> sessions = List.of(
                createSession(2025, 10, 1, 22, 30, 2025, 10, 2, 7, 30, SleepQuality.GOOD),
                createSession(2025, 10, 2, 23, 0, 2025, 10, 3, 8, 0, SleepQuality.GOOD),
                createSession(2025, 10, 3, 22, 0, 2025, 10, 4, 7, 0, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new UserChronotypeFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("голубь", result.getValueAsString());
    }

    @Test
    @DisplayName("Хронотип: при равенстве типов - голубь")
    void testChronotypeTie() {
        List<SleepingSession> sessions = List.of(
                createSession(2025, 10, 1, 23, 30, 2025, 10, 2, 9, 30, SleepQuality.GOOD),
                createSession(2025, 10, 2, 21, 0, 2025, 10, 3, 6, 0, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new UserChronotypeFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("голубь", result.getValueAsString());
    }

    @Test
    @DisplayName("Хронотип: пустой список")
    void testChronotypeEmptyList() {
        List<SleepingSession> sessions = List.of();

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new UserChronotypeFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("нет данных", result.getValueAsString());
    }

    @Test
    @DisplayName("Хронотип: игнорирование бессонных ночей")
    void testChronotypeIgnoreSleeplessNights() {
        List<SleepingSession> sessions = List.of(
                createSession(2025, 10, 1, 23, 30, 2025, 10, 2, 9, 30, SleepQuality.GOOD),
                createSession(2025, 10, 3, 14, 0, 2025, 10, 3, 15, 0, SleepQuality.GOOD)
        );

        Function<List<SleepingSession>, SleepAnalysisResult> function =
                new UserChronotypeFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("сова", result.getValueAsString());
    }

    // Вспомогательный метод

    private SleepingSession createSession(
            int year1, int month1, int day1, int hour1, int minute1,
            int year2, int month2, int day2, int hour2, int minute2,
            SleepQuality quality) {
        return new SleepingSession(
                LocalDateTime.of(year1, month1, day1, hour1, minute1),
                LocalDateTime.of(year2, month2, day2, hour2, minute2),
                quality
        );
    }
}