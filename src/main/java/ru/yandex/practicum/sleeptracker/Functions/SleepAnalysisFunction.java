package ru.yandex.practicum.sleeptracker.Functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface // Маркерный интерфейс для удобства
public interface SleepAnalysisFunction extends Function<List<SleepingSession>, SleepAnalysisResult> {
}