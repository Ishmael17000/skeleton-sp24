package ngrams;

import edu.princeton.cs.algs4.In;

import java.sql.Time;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private final WordMap wordMap;
    private final YearMap yearMap;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordMap = new WordMap(wordsFilename);
        yearMap = new YearMap(countsFilename);
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries rtnSeries = new TimeSeries();
        TimeSeries wordSeries = wordMap.get(word);
        for (int year : wordSeries.years()) {
            if (year >= startYear && year <= endYear) {
                rtnSeries.put(year, wordSeries.get(year));
            }
        }
        return rtnSeries;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        return countHistory(word, -1, Integer.MAX_VALUE);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries rtnSeries = new TimeSeries();
        for (int year : yearMap.keySet()) {
            rtnSeries.put(year, yearMap.get(year));
        }
        return rtnSeries;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!wordMap.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries totalCount = new TimeSeries(yearMap, startYear, endYear);
        TimeSeries wordAllTime = (TimeSeries) wordMap.get(word).clone();
        TimeSeries wordSelectedTime = new TimeSeries(wordAllTime, startYear, endYear);
        return wordSelectedTime.dividedBy(totalCount);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        if (!wordMap.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries totalCount = (TimeSeries) yearMap.clone();
        TimeSeries wordCount = (TimeSeries) wordMap.get(word).clone();
        return wordCount.dividedBy(totalCount);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries totalWordsCount = new TimeSeries();
        for (String word : words) {
            TimeSeries currentWordSeriesRestricted = new TimeSeries(wordMap.get(word), startYear, endYear);
            totalWordsCount = totalWordsCount.plus(currentWordSeriesRestricted);
        }

        TimeSeries yearCount = (TimeSeries) yearMap.clone();
        return totalWordsCount.dividedBy(yearCount);
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        return summedWeightHistory(words, -1, Integer.MAX_VALUE);
    }


    // Stores data of wordwise TimeSeries.
    private class WordMap extends HashMap<String, TimeSeries> {
        private WordMap(String wordsFilename) {
            super();
            createFromFiles(wordsFilename);
        }

        // Add the words data from wordsFilename to the WordMap.
        private void createFromFiles(String wordsFilename) {
            In wordsFile = new In(wordsFilename);

            while (!wordsFile.isEmpty()) {
                String nextLine = wordsFile.readLine();
                String[] splitLine = nextLine.split("\t");
                String word = splitLine[0];
                int year = Integer.parseInt(splitLine[1]);
                int count = Integer.parseInt(splitLine[2]);

                // Add a pair of data.
                this.addYear(word, year, count);
            }
        }


        // Add a pair of (year, count) to the TimeSeries of word.
        private void addYear(String word, int year, int count) {
            if (!this.containsKey(word)) {
                this.put(word, new TimeSeries());
            }
            this.get(word).put(year, (double)count);
        }
    }

    // Store the total year statistics.
    private class YearMap extends TimeSeries {

        private YearMap(String countsFilename) {
            super();
            createFromFiles(countsFilename);
        }

        // Add yearwise data from file;
        private void createFromFiles(String countsFilename) {
            In countsFile = new In(countsFilename);

            while (!countsFile.isEmpty()) {
                String nextLine = countsFile.readLine();
                String[] splitLine = nextLine.split(",");

                int year = Integer.parseInt(splitLine[0]);
                double count = Double.parseDouble(splitLine[1]);
                addYear(year, count);
            }
        }

        // Add a pair (year, count) to the HashMap.
        private void addYear(int year, double count) {
            this.put(year, count);
        }
    }

    // Convert a string of integer to int.
//    private int stringToInt(String s) {
//        int total = 0;
//        int index = 0;
//        int l = s.length();
//        while (index < l) {
//            int num = s.charAt(index);
//            total = 10 * total + num;
//            index += 1;
//        }
//        return total;
//    }
}
