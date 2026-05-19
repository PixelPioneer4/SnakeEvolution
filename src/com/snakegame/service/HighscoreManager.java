package com.snakegame.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighscoreManager {
    private static final String FILE_NAME = "highscores.txt";

    public static class ScoreEntry implements Comparable<ScoreEntry> {
        private String name;
        private int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() { return name; }
        public int getScore() { return score; }

        @Override
        public int compareTo(ScoreEntry o) {
            return Integer.compare(o.score, this.score); // Absteigend sortieren
        }
    }

    public void saveScore(String username, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + ";" + score);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern des Highscores: " + e.getMessage());
        }
    }

    public List<ScoreEntry> loadScores() {
        List<ScoreEntry> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    try {
                        String name = parts[0];
                        int score = Integer.parseInt(parts[1]);
                        scores.add(new ScoreEntry(name, score));
                    } catch (NumberFormatException ignored) {
                        // Ignoriere fehlerhafte Zeilen
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Highscores (ggf. existiert die Datei noch nicht): " + e.getMessage());
        }
        Collections.sort(scores);
        return scores;
    }
}
