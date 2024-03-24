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