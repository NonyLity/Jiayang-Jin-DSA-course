<<<<<<< HEAD
import java.io.*;
import java.util.*;

class BookStatistics {
    private Map<String, Integer> wordCounts;
    private Set<String> wordsToIgnore;

    public BookStatistics() {
        this.wordCounts = new HashMap<>();
        this.wordsToIgnore = new HashSet<>();
    }

    public void setSource(String fileName, String ignoreWordsFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(ignoreWordsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordsToIgnore.add(line.trim().toLowerCase(Locale.ROOT));
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+"); // Split by whitespace to get individual words
                for (String word : words) {
                    word = word.toLowerCase(Locale.ROOT).replaceAll("[^a-zA-Z]", ""); // Remove non-alphabetic characters
                    if (word.length() >= 2 && !wordsToIgnore.contains(word)) {
                        wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }
    }

    public List<Map.Entry<String, Integer>> getTopWords(int n) {
        List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordCounts.entrySet());
        sortedWords.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        return sortedWords.subList(0, Math.min(n, sortedWords.size()));
    }

    public int getTotalWordCount() {
        return wordCounts.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getUniqueWordCount() {
        return wordCounts.size();
    }

    public void report(int n) {
        List<Map.Entry<String, Integer>> topWords = getTopWords(n);
        System.out.println("Top " + n + " words:");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : topWords) {
            System.out.println(rank + ". " + entry.getKey() + ": " + entry.getValue());
            rank++;
        }
        System.out.println("Total word count: " + getTotalWordCount());
        System.out.println("Unique word count: " + getUniqueWordCount());
    }

    public static void main(String[] args) {
        BookStatistics bookStats = new BookStatistics();
        try {
            bookStats.setSource("book.txt", "ignore_words.txt");
            bookStats.report(100); // Report the top 100 words
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
=======
package oy.tol.tira.books;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Really bad book. Uses linear arrays with O(n^2) loops within loops,
 * bubble sort etc.
 */
class BadBookImplementation implements Book {

    private class WordCount {
        WordCount() {
            word = "";
            count = 0;
        }
        String word;
        int count;
    }

    private static final int MAX_WORDS = 100000;
    private static final int MAX_WORD_LEN = 100;
    private WordCount[] words = null;
    private String bookFile = null;
    private String wordsToIgnoreFile = null;
    private WordFilter filter = null;
    private int uniqueWordCount = 0;
    private int totalWordCount = 0;
    private int ignoredWordsTotal = 0;
    private long loopCount = 0;

    @Override
    public void setSource(String fileName, String ignoreWordsFile) throws FileNotFoundException {
        // Check if both files exist. If not, throw an exception.
        boolean success = false;
        if (checkFile(fileName)) {
            bookFile = fileName;
            if (checkFile(ignoreWordsFile)) {
                wordsToIgnoreFile = ignoreWordsFile;
                success = true;
            }
        }
        if (!success) {
            throw new FileNotFoundException("Cannot find the specified files");
        }
    }

    @Override
    public void countUniqueWords() throws IOException, OutOfMemoryError {
        if (bookFile == null || wordsToIgnoreFile == null) {
            throw new IOException("No file(s) specified");
        }
        // Reset the counters
        uniqueWordCount = 0;
        totalWordCount = 0;
        loopCount = 0;
        ignoredWordsTotal = 0;
        // Create an array for the words.
        words = new WordCount[MAX_WORDS];
        for (int index = 0; index < MAX_WORDS; index++) {
            words[index] = new WordCount();
        }
        // Create the filter class to handle filtering.
        filter = new WordFilter();
        // Read the words to filter.
        filter.readFile(wordsToIgnoreFile);

        // Start reading from the book file using UTF-8.
        FileReader reader = new FileReader(bookFile, StandardCharsets.UTF_8);
        int c;
        // Array holds the code points of the UTF-8 encoded chars.
        int[] array = new int[MAX_WORD_LEN];
        int currentIndex = 0;
        while ((c = reader.read()) != -1) {
            // If the char is a letter, then add it to the array...
            if (Character.isLetter(c)) {
                array[currentIndex] = c;
                currentIndex++;
            } else {
                // ...otherwise a word break was met, so handle the word just read.
                if (currentIndex > 0) {
                    // If a word was actually read, then create a string out of the codepoints,
                    // normalizing the word to lowercase.
                    String word = new String(array, 0, currentIndex).toLowerCase(Locale.ROOT);
                    // Reset the counter for the next word read.
                    currentIndex = 0;
                    addToWords(word);
                }
            }
        }
        // Must check the last word in the file too. There may be chars in the array 
        // not yet handled, when read() returns -1 to indicate EOF.
        if (currentIndex > 1) {
            String word = new String(array, 0, currentIndex).toLowerCase(Locale.ROOT);
            addToWords(word);
        }
        // Close the file reader.
        reader.close();
    }

    private void addToWords(String word) throws OutOfMemoryError {
        // Filter out too short words or words in filter list.
        if (!filter.shouldFilter(word) && word.length() >= 2) {
            boolean handled = false;
            // Go through the words found so far...
            // NB: ...the !handled in evaluating if loop should be continued...
            for (int index = 0; index < MAX_WORDS && !handled; index++) {
                loopCount++;
                // If we meet an empty word, this word was not found so far so add it then.
                if (words[index].word.length() == 0) {
                    words[index].word = word;
                    words[index].count = 1;
                    uniqueWordCount += 1;
                    totalWordCount += 1;
                    handled = true;
                    // If array is full, then abort after cleaning up.
                    if (uniqueWordCount >= MAX_WORDS) {
                        throw new OutOfMemoryError("No room for more words in array");
                    }
                } else if (words[index].word.equals(word)) {
                    // Word was found so update the counts, flag.
                    words[index].count += 1;
                    totalWordCount += 1;
                    handled = true;
                }
            }
            // If word was not added or an existing count updated, array is full.
            if (!handled) {
                throw new OutOfMemoryError("No room for more words in array");
            }
        } else {
            ignoredWordsTotal++;
        }
    }

    @Override
    public void report() {
        if (words == null) {
            System.out.println("*** No words to report! ***");
            return;
        }
        System.out.println("Listing words from a file: " + bookFile);
        System.out.println("Ignoring words from a file: " + wordsToIgnoreFile);
        System.out.println("Sorting the results...");
        // First sort the array
        sortWords();
        System.out.println("...sorted.");

        for (int index = 0; index < 100; index++) {
            if (words[index].word.length() == 0) {
                break;
            }
            String word = String.format("%-20s", words[index].word).replace(' ', '.');
            System.out.format("%4d. %s %6d%n", index + 1, word, words[index].count);
        }
        System.out.println("Count of words in total: " + totalWordCount);
        System.out.println("Count of unique words:    " + uniqueWordCount);
        System.out.println("Count of words to ignore:    " + filter.ignoreWordCount());
        System.out.println("Ignored words count:      " + ignoredWordsTotal);
        System.out.println("How many times the inner loop rolled: " + loopCount);
    }

    @Override
    public void close() {
        bookFile = null;
        wordsToIgnoreFile = null;
        words = null;
        if (filter != null) {
            filter.close();
        }
        filter = null;
    }

    @Override
    public int getUniqueWordCount() {
        return uniqueWordCount;
    }

    @Override
    public int getTotalWordCount() {
        return totalWordCount;
    }

    @Override
    public String getWordInListAt(int position) {
        if (words != null && position >= 0 && position < uniqueWordCount) {
            return words[position].word;
        }
        return null;
    }

    @Override
    public int getWordCountInListAt(int position) {
        if (words != null && position >= 0 && position < uniqueWordCount) {
            return words[position].count;
        }
        return -1;
    }

    private boolean checkFile(String fileName) {
        if (fileName != null) {
            File file = new File(fileName);
            if (file.exists() && !file.isDirectory()) {
                return true;
            }
        }
        return false;
    }

    private void sortWords() {
        // Uses bubble sort which is a lousy one, but hey this is BadBookImplementation.
        int sortSize = uniqueWordCount;
        int index = 0;
        int newSize = 0;
        do {
            newSize = 0;
            for (index = 1; index < sortSize; index++) {
                if (words[index - 1].count < words[index].count) {
                    WordCount temp = words[index - 1];
                    words[index - 1] = words[index];
                    words[index] = temp;
                    newSize = index;
                }
            }
            sortSize = newSize;
        } while (sortSize > 1);
    }

}
>>>>>>> cc340666fd8c5c8810949d018411f358ecf5c61c
