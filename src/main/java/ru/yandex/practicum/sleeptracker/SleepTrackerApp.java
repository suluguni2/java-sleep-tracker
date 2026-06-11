package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.Functions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleepTrackerApp {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> analysisFunctions;

    public SleepTrackerApp() {
        this.analysisFunctions = new ArrayList<>();
        this.analysisFunctions.add(new TotalSessionsFunction());
        this.analysisFunctions.add(new MinSessionDurationFunction());
        this.analysisFunctions.add(new MaxSessionDurationFunction());
        this.analysisFunctions.add(new AverageSessionDurationFunction());
        this.analysisFunctions.add(new BadSleepSessionsFunction());
        this.analysisFunctions.add(new SleeplessNightsFunction());
        this.analysisFunctions.add(new UserChronotypeFunction());
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Пожалуйста, укажите путь к файлу с логом сна.");
            return;
        }

        String filePath = args[0];

        try {
            List<SleepingSession> sessions = loadSleepLog(filePath);
            System.out.println("Загружено сессий сна: " + sessions.size());
            System.out.println();

            SleepTrackerApp app = new SleepTrackerApp();

            app.analysisFunctions
                    .stream()
                    .map(function -> function.apply(sessions))
                    .forEach(result ->
                            System.out.println(result.getDescription() + ": " + result.getValueAsString())
                    );

        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    private static List<SleepingSession> loadSleepLog(String filePath) throws IOException {
        return Files
                .lines(Paths.get(filePath))
                .filter(line -> !line.trim().isEmpty())
                .map(SleepTrackerApp::parseLine)
                .collect(Collectors.toList());
    }

    private static SleepingSession parseLine(String line) {
        String[] parts = line.split(";");
        LocalDateTime sleepStart = LocalDateTime.parse(parts[0].trim(), FORMATTER);
        LocalDateTime wakeUp = LocalDateTime.parse(parts[1].trim(), FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim());
        return new SleepingSession(sleepStart, wakeUp, quality);
    }
}