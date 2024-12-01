package code_clone;

import java.util.Locale;

public class PorterStemmer {

    public String stemWord(String word) {
        if (word == null || word.isEmpty()) return word;

        String stem = word.toLowerCase(Locale.ENGLISH);
        if (stem.length() < 3) return stem;

        stem = stemStep1a(stem);
        stem = stemStep1b(stem);
        stem = stemStep1c(stem);
        stem = stemStep2(stem);
        stem = stemStep3(stem);
        stem = stemStep4(stem);
        stem = stemStep5a(stem);
        stem = stemStep5b(stem);

        return stem;
    }

    private String stemStep1a(String input) {
        if (input.endsWith("sses")) return input.substring(0, input.length() - 2);
        if (input.endsWith("ies")) return input.substring(0, input.length() - 2);
        if (input.endsWith("ss")) return input;
        if (input.endsWith("s")) return input.substring(0, input.length() - 1);
        return input;
    }

    private String stemStep1b(String input) {
        if (input.endsWith("eed")) {
            String stem = input.substring(0, input.length() - 1);
            if (getM(getLetterTypes(stem)) > 0) return stem;
            return input;
        }
        if (input.endsWith("ed")) {
            String stem = input.substring(0, input.length() - 2);
            if (getLetterTypes(stem).contains("v")) return step1b2(stem);
            return input;
        }
        if (input.endsWith("ing")) {
            String stem = input.substring(0, input.length() - 3);
            if (getLetterTypes(stem).contains("v")) return step1b2(stem);
            return input;
        }
        return input;
    }

    private String step1b2(String input) {
        if (input.endsWith("at") || input.endsWith("bl") || input.endsWith("iz")) return input + "e";
        if (getLastDoubleConsonant(input) != 0) return input.substring(0, input.length() - 1);
        if (getM(getLetterTypes(input)) == 1 && isStarO(input)) return input + "e";
        return input;
    }

    private char getLastDoubleConsonant(String input) {
        if (input.length() < 2) return 0;
        char last = input.charAt(input.length() - 1);
        char secondLast = input.charAt(input.length() - 2);
        if (last == secondLast && getLetterType((char) 0, last) == 'c') return last;
        return 0;
    }

    private boolean isStarO(String input) {
        if (input.length() < 3) return false;
        char last = input.charAt(input.length() - 1);
        if (last == 'w' || last == 'x' || last == 'y') return false;
        return getLetterTypes(input.substring(input.length() - 3)).equals("cvc");
    }

    private String getLetterTypes(String input) {
        StringBuilder letterTypes = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char letterType = getLetterType(i == 0 ? 0 : input.charAt(i - 1), input.charAt(i));
            if (letterTypes.length() == 0 || letterTypes.charAt(letterTypes.length() - 1) != letterType) {
                letterTypes.append(letterType);
            }
        }
        return letterTypes.toString();
    }

    private char getLetterType(char prev, char letter) {
        if ("aeiou".indexOf(letter) != -1) return 'v';
        if (letter == 'y' && (prev == 0 || "aeiou".indexOf(prev) != -1)) return 'c';
        return 'c';
    }

    private int getM(String letterTypes) {
        if (letterTypes.length() < 2) return 0;
        return (letterTypes.startsWith("c") ? letterTypes.length() - 1 : letterTypes.length()) / 2;
    }
}
