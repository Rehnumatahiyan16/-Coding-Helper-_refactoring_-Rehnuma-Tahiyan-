package code_clone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TfIdfCalculate {

    private List<String[]> fileWordsProject1 = new ArrayList<>();
    private List<String[]> fileWordsProject2 = new ArrayList<>();
    private Map<String, Double> idfMap = new HashMap<>();
    private List<String> allTermsProject1 = new ArrayList<>();
    private List<String> allTermsProject2 = new ArrayList<>();
    private List<String> combinedTerms = new ArrayList<>();
    private List<String> processProjectFiles = new ArrayList<>();

    public List<String[]> readFilesFromPath(String path) throws IOException {
        List<String[]> fileWords = new ArrayList<>();
        File directoryPath = new File(path);
        File[] files = directoryPath.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".txt")) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    fileWords.add(content.toString().trim().split(" "));
                    processProjectFiles.add(content.toString().trim());
                }
            }
        }
        return fileWords;
    }

    public void extractUniqueWords(List<String[]> fileWords, List<String> allTerms) {
        for (String[] terms : fileWords) {
            for (String term : terms) {
                if (!allTerms.contains(term)) {
                    allTerms.add(term);
                }
            }
        }
    }

    public void calculateIdf() {
        combinedTerms.addAll(allTermsProject1);
        combinedTerms.addAll(allTermsProject2);
        for (String term : combinedTerms) {
            double idf = new getTfIdf().getIdf(processProjectFiles, term);
            idfMap.put(term, idf);
        }
    }

    public List<double[]> calculateTfIdf(List<String[]> fileWords) {
        List<double[]> tfIdfVectors = new ArrayList<>();
        for (String[] words : fileWords) {
            double[] tfIdfVector = new double[combinedTerms.size()];
            int index = 0;
            for (String term : combinedTerms) {
                double tf = new getTfIdf().getTf(words, term);
                double idf = idfMap.getOrDefault(term, 0.0);
                tfIdfVector[index++] = tf * idf;
            }
            tfIdfVectors.add(tfIdfVector);
        }
        return tfIdfVectors;
    }

    public void processProjects(String path1, String path2) throws IOException {
        fileWordsProject1 = readFilesFromPath(path1);
        fileWordsProject2 = readFilesFromPath(path2);

        extractUniqueWords(fileWordsProject1, allTermsProject1);
        extractUniqueWords(fileWordsProject2, allTermsProject2);

        calculateIdf();

        List<double[]> tfIdfVectorsProject1 = calculateTfIdf(fileWordsProject1);
        List<double[]> tfIdfVectorsProject2 = calculateTfIdf(fileWordsProject2);

        // Optionally store or process the tf-idf vectors further
        // Example: tfIdfVectorsProject1.forEach(tfidf -> process(tfidf));
    }
}
