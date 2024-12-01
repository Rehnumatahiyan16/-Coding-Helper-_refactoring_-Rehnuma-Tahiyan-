package code_clone;

import IO.Filewriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PreProcessing {

    private Filewriter fileWriter;
    private Porter_stemmer stemmer;
    private String keywordFilePath;

    public PreProcessing(Filewriter fileWriter, Porter_stemmer stemmer, String keywordFilePath) {
        this.fileWriter = fileWriter;
        this.stemmer = stemmer;
        this.keywordFilePath = keywordFilePath;
    }

    public String processFile(String filename, String content, String outputPath) throws IOException {
        String cleanedContent = cleanContent(content);
        String processedContent = stemWords(cleanedContent);
        return fileWriter.createProcessFile(filename, processedContent.trim(), outputPath);
    }

    private String cleanContent(String content) throws IOException {
        String noPunctuation = removePunctuation(content);
        String noKeywords = removeKeywords(noPunctuation);
        return removeExtraSpaces(noKeywords);
    }

    private String removePunctuation(String content) {
        return content.replaceAll("\\p{Punct}", " ");
    }

    private String removeExtraSpaces(String content) {
        return content.trim().replaceAll("\\s+", " ");
    }

    private String removeKeywords(String content) throws IOException {
        List<String> keywords = loadKeywords();
        StringBuilder result = new StringBuilder();

        for (String word : content.split(" ")) {
            if (!keywords.contains(word.trim())) {
                result.append(word).append(" ");
            }
        }

        return result.toString().trim();
    }

    private List<String> loadKeywords() throws IOException {
        List<String> keywords = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(keywordFilePath)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            String[] keywordArray = new String(data).trim().split("\\s+");
            for (String keyword : keywordArray) {
                keywords.add(keyword);
            }
        }
        return keywords;
    }

    private String stemWords(String content) {
        StringBuilder result = new StringBuilder();
        for (String word : content.split(" ")) {
            result.append(stemmer.stemWord(word)).append(" ");
        }
        return result.toString().trim();
    }
}
