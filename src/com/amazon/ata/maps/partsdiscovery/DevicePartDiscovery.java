package com.amazon.ata.maps.partsdiscovery;

import java.util.*;

/**
 * Helps expose key words from new editions of part catalogs.
 */
public class DevicePartDiscovery {

    // --- Part A ---
    /**
     * Calculate how often each word appears in a Catalog.
     * @param catalog The catalog to calculate word frequencies for.
     * @return A Map of words that appear in the catalog to the number of times they appear.
     */
    public Map<String, Integer> calculateWordCounts(PartCatalog catalog) {
        // PARTICIPANTS: Implement calculateWordCounts()
        Map<String, Integer> wordCounts = new HashMap<>();

        for (String word : catalog.getCatalogWords()) {
            wordCounts.merge(word, 1, (oldValue, defaultValue) -> oldValue + defaultValue);
        }

        return wordCounts;
    }

    // --- Part B ---
    /**
     * Removes a word from the provided word count map.
     * @param word the word to be removed
     * @param wordCounts the map to remove the word from
     */
    public void removeWord(String word, Map<String, Integer> wordCounts) {
        // PARTICIPANTS: Implement removeWord()
        wordCounts.remove(word);
    }

    // --- Part C ---
    /**
     * Find the word that appears most frequently based on the word counts from a catalog.
     * @param wordCounts an association between a word and the total number of times it appeared in a catalog
     * @return The word that appears most frequently in the catalog to the number of times they appear.
     */
    public String getMostFrequentWord(Map<String, Integer> wordCounts) {
        // PARTICIPANTS: Implement getMostFrequentWord()
        String maxWord = "";
        int maxCount = 0;

        for (String word : wordCounts.keySet()) {
            int count = wordCounts.get(word);
            if (count > maxCount) {
                maxWord = word;
                maxCount = count;
            }
        }

        return maxWord;
    }

    // --- Part D ---
    /**
     * Calculates the TF-IDF score for each word in a catalog. The TF-IDF score for a word
     * is equal to the count * idf score. You can assume there will be an idfScore for each word
     * in wordCounts.
     * @param wordCounts - associates a count for each word from a catalog
     * @param idfScores - associates an IDF score for each word in the catalog
     * @return a map associating each word with its TF-IDF score.
     */
    public Map<String, Double> getTfIdfScores(Map<String, Integer> wordCounts, Map<String, Double> idfScores) {
        // PARTICIPANTS: Implement getTfIdfScores()
        Map<String, Double> tfIdfScores = new HashMap<>();
        for (String word : wordCounts.keySet()) {
            tfIdfScores.put(word, wordCounts.get(word) * idfScores.get(word));
        }
        return tfIdfScores;
    }

    // --- Extension 1 ---
    /**
     * Gets the 10 highest (TF-IDF) scored words for a catalog.
     *
     * @param tfIdfScores - associates a TF-IDF score for each word in a catalog
     * @return a list of the 10 highest scored words for a catalog.
     */
    public List<String> getBestScoredWords(Map<String, Double> tfIdfScores) {
        // PARTICIPANTS: Implement getBestScoredWords()
//        return tfIdfScores.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                .limit(10)
//                .map(entry -> entry.getKey())
//                .toList();

        // This solution gets it down to O(n * log k) where k is x + 1 for the top x best scored words (k = 11)
        Queue<Map.Entry<String, Double>> queue = new PriorityQueue<>(Map.Entry.comparingByValue());

        for (var entry : tfIdfScores.entrySet()) {
            queue.offer(entry);
            if (queue.size() > 10) {
                queue.poll();
            }
        }

        return queue.stream()
                .map(entry -> entry.getKey())
                .toList();
    }

    // --- Extension 2 ---
    /**
     * Calculates the IDF score for each word in a set of catalogs. The IDF score for a word
     * is equal to the inverse of the total number of times appears in all catalogs.
     * Assume df is the sum of the counts of a single word across all catalogs, then idf = 1.0/df.
     *
     * @param catalogWordCounts - a list of maps that associate a count for each word for each catalog
     * @return a map associating each word with its IDF score.
     */
    public Map<String, Double> calculateIdfScores(List<Map<String,Integer>> catalogWordCounts) {
        // PARTICIPANTS: Implement getIdfScores()
        Map<String, Double> idfMap = new HashMap<>();

        for (var catalog : catalogWordCounts) {
            for (String word : catalog.keySet()) {
                idfMap.merge(word, 1.0, (currentValue, incrementValue) -> currentValue + incrementValue);
            }
        }

        idfMap.replaceAll((k, v) -> Math.log10(catalogWordCounts.size() / v));

        return idfMap;
    }

}
