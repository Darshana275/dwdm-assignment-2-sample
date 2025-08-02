import java.io.*;
import java.util.*;

public class CSVPreprocessing{
    public static void main(String[] args) {
        String fileName = "dataset.csv";
        List<Double> scores = new ArrayList<>();

        // Step 1: Read CSV and remove N/A/null rows
        System.out.println("=== Task 1: Count valid rows and remove N/A / null rows ===");
        int validRows = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String scoreStr = parts[2].trim();
                    if (!scoreStr.equalsIgnoreCase("N/A") && !scoreStr.equalsIgnoreCase("null")) {
                        scores.add(Double.parseDouble(scoreStr));
                        validRows++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }


        System.out.println("Final number of valid rows: " + validRows);

        // Step 2: Binning by Boundaries (2 bins)
        System.out.println("\n=== Task 2: Binning by Boundaries (2 bins) ===");
        Collections.sort(scores);
        int binSize = (int) Math.ceil(scores.size() / 2.0);

        for (int i = 0; i < scores.size(); i += binSize) {
            int end = Math.min(i + binSize, scores.size());
            double lower = scores.get(i);
            double upper = scores.get(end - 1);
            System.out.println("Bin " + (i / binSize + 1) + ": [" + lower + ", " + upper + "]");
            for (int j = i; j < end; j++) {
                double val = scores.get(j);
                double smoothed = (Math.abs(val - lower) <= Math.abs(val - upper)) ? lower : upper;
                System.out.println(val + " -> " + smoothed);
            }
        }

        // Step 3: Normalization [0, 1]
        System.out.println("\n=== Task 3: Normalization [0, 1] ===");
        double min = Collections.min(scores);
        double max = Collections.max(scores);
        for (double score : scores) {
            double normalized = (score - min) / (max - min);
            System.out.printf("Original: %.2f -> Normalized: %.2f\n", score, normalized);
        }
    }
}
