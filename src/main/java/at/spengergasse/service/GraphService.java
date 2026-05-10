package at.spengergasse.service;

import at.spengergasse.model.Graph;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GraphService {

    public Graph parseCsvQuadratic(InputStream inputStream) {

        List<List<Object>> matrix = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            int expectedSize = -1;
            int rowIndex = 0;

            while ((line = reader.readLine()) != null) {

                line = line.replaceAll("[|;]+", ",")
                        .replaceAll(",+", ",")
                        .replaceAll("^,|,$", "");

                String[] parts = line.split(",");

                List<Object> row = new ArrayList<>();

                for (String p : parts) {
                    String token = p.trim();

                    if (token.isEmpty()) {
                        row.add(null);
                    } else if (token.matches("-?\\d+")) {
                        row.add(Integer.parseInt(token));
                    } else {
                        row.add(token);
                    }
                }

                if (expectedSize == -1) {
                    expectedSize = row.size();
                }

                if (row.size() != expectedSize) {
                    throw new IllegalArgumentException(
                            "Keine quadratische Matrix: Zeile " + rowIndex +
                                    " hat " + row.size() + " statt " + expectedSize
                    );
                }

                matrix.add(row);
                rowIndex++;
            }

            if (matrix.size() != expectedSize) {
                throw new IllegalArgumentException(
                        "Keine quadratische Matrix: Zeilen=" +
                                matrix.size() + ", Spalten=" + expectedSize
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("Es sind nur quadratische Matrizen erlaubt");
        }

        return new Graph(matrix);
    }
}