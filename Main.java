package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static double countWords(String file) {
        return file.split(" ").length;
    }

    public static double countChars(String file) {
        return file.replaceAll("\\s+", "").length();
    }

    public static double countSentences(String file) {
        return file.split("[?.!]+").length;
    }

    public static double countSyllables(String file) {
        String[] wordArray = file.split("\\s+");
        double totalSyllables = 0;

        for (String word : wordArray) {
            final Pattern p = Pattern.compile("([ayeiou]+)");
            final String lowerCase = word.toLowerCase();
            final Matcher m = p.matcher(lowerCase);
            int count = 0;
            while (m.find()) count++;
            if (lowerCase.endsWith("e")) count--;
            totalSyllables += count;
        }

        return Math.max(totalSyllables, 1);
    }

    public static double countPolySyllables(String file) {
        String[] wordArray = file.split("\\s+");
        double polySyllables = 0;

        for (String word : wordArray) {
            double syllables = countSyllables(word);
            if (syllables > 2) {
                polySyllables++;
            }
        }
        return polySyllables;
    }

    public static String FK(String file) {
        double syllablesOverWords = countSyllables(file) / countWords(file);
        double wordsOverSentences = countWords(file) / countSentences(file);
        double score = 0.39 * wordsOverSentences + 11.8 * syllablesOverWords - 15.59;
        score = (double) Math.round(score * 100.0) / 100.0;
        String output = findAgeRange(score);
        return "Flesch–Kincaid readability tests: " + score + output;
    }

    public static String ARI(String file) {
        double charsOverWords = countChars(file) / countWords(file);
        double wordsOverSentences = countWords(file) / countSentences(file);
        double score = 4.71 * charsOverWords + 0.5 * wordsOverSentences - 21.43;
        score = (double) Math.round(score * 100.0) / 100.0;
        String output = findAgeRange(score);
        return "Automated Readability Index: " + score + output;
    }

    public static String SMOG(String file) {
        double underSquareRoot = Math.sqrt(countPolySyllables(file) * (30 / countSentences(file)));
        double score = 1.043 * underSquareRoot + 3.1291;
        score = (double) Math.round(score * 100.0) / 100.0;
        String output = findAgeRange(score);
        return "Simple Measure of Gobbledygook: " + score + output;
    }

    public static String CL(String file) {
        double L = (countChars(file) / countWords(file)) * 100;
        double S = (countSentences(file) / countWords(file)) * 100;
        double score = 0.0588 * L - 0.296 * S - 15.8;
        score = (double) Math.round(score * 100.0) / 100.0;
        String output = findAgeRange(score);
        return "Coleman–Liau index: " + score + "(about " + output + ").";
    }

    public static String findAgeRange(double score) {
        long scoreRounded = (long) (Math.ceil(score + 1));
        String output = null;
        switch ((int) scoreRounded) {
            case 1:
                output = "6";
                break;
            case 2:
                output = "7";
                break;
            case 3:
                output = "8";
                break;
            case 4:
                output = "9";
                break;
            case 5:
                output = "10";
                break;
            case 6:
                output = "11";
                break;
            case 7:
                output = "12";
                break;
            case 8:
                output = "13";
                break;
            case 9:
                output = "14";
                break;
            case 10:
                output = "15";
                break;
            case 11:
                output = "16";
                break;
            case 12:
                output = "17";
                break;
            case 13:
                output = "18";
                break;
            case 14:
                output = "24";
                break;
        }
        return "(about " + output + "-year-olds)";
    }

    public static void displayStatistics(String file) {
        System.out.println("The text is:");
        System.out.println(file);
        System.out.println("Words: " + countWords(file));
        System.out.println("Sentences: " + countSentences(file));
        System.out.println("Characters: " + countChars(file));
        System.out.println("Syllables: " + countSyllables(file));
        System.out.println("Polysyllables: " + countPolySyllables(file));
    }

    public static void displayScore(String scoreName, String file) {
        if ("ARI".equals(scoreName)) {
            System.out.println(ARI(file));
        } else if ("FK".equals(scoreName)) {
            System.out.println(FK(file));
        } else if ("SMOG".equals(scoreName)) {
            System.out.println(SMOG(file));
        } else if ("CL".equals(scoreName)) {
            System.out.println(CL(file));
        } else {
            System.out.println(ARI(file));
            System.out.println(FK(file));
            System.out.println(SMOG(file));
            System.out.println(CL(file));
        }
    }

    public static void main(String[] args) {

        String pathToFile = args[0];
        Scanner scanner = new Scanner(System.in);

        try {
            String file = Files.readString(Path.of(args[0]));
            displayStatistics(file);
            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
            String scoreName = scanner.nextLine();
            System.out.println();
            displayScore(scoreName, file);

        } catch (IOException e) {
            System.out.println("File not found: " + pathToFile);
        }

    }
}