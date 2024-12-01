package code_clone;

import java.util.List;

public class TfIdfCalculator {


    public double calculateTf(String[] fileContent, String term) {
        if (fileContent == null || term == null || fileContent.length == 0) {
            return 0.0;
        }

        double termCount = 0;
        for (String word : fileContent) {
            if (word.equalsIgnoreCase(term)) {
                termCount++;
            }
        }
        return termCount / fileContent.length;
    }


    public double calculateIdf(List<String> allFiles, String term) {
        if (allFiles == null || allFiles.isEmpty() || term == null) {
            return 0.0;
        }

        int docCountContainingTerm = 0;

        for (String fileContent : allFiles) {
            if (fileContent == null || fileContent.isEmpty()) {
                continue;
            }

            String[] words = fileContent.split("\\s+");
            for (String word : words) {
                if (word.equalsIgnoreCase(term)) {
                    docCountContainingTerm++;
                    break; // Move to the next file after finding the term
                }
            }
        }

        if (docCountContainingTerm == 0) {
            return 0.0; // Avoid division by zero
        }

        return 1 + Math.log((double) allFiles.size() / docCountContainingTerm);
    }
}
