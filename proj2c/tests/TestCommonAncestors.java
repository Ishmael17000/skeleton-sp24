import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import main.AutograderBuddy;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class TestCommonAncestors {
    public static final String WORDS_FILE = "data/ngrams/very_short.csv";
    public static final String LARGE_WORDS_FILE = "data/ngrams/top_14377_words.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";
    public static final String LARGE_SYNSET_FILE = "data/wordnet/synsets.txt";
    public static final String LARGE_HYPONYM_FILE = "data/wordnet/hyponyms.txt";

    public static final String HYPONYM_FILE_11 = "data/wordnet/hyponyms11.txt";
    public static final String SYNSET_FILE_11 = "data/wordnet/synsets11.txt";

    /** This is an example from the spec for a common-ancestors query on the word "adjustment".
     * You should add more tests for the other spec examples! */
    @Test
    public void testSpecAdjustment() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE, WORDS_FILE, TOTAL_COUNTS_FILE);
        List<String> words = List.of("adjustment");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[adjustment, alteration, event, happening, modification, natural_event, occurrence, occurrent]";
        assertThat(actual).isEqualTo(expected);
    }


    NgordnetQueryHandler myHandler = AutograderBuddy.getHyponymsHandler(
            SYNSET_FILE_11, HYPONYM_FILE_11, WORDS_FILE, TOTAL_COUNTS_FILE );


    @Test
    public void testEmptyWords() {
        List<String> words = new ArrayList<>();
        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);

        String actual = myHandler.handle(nq);
        String expected = "[]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testAncestors() {
        List<String> words1 = List.of("demotion");
        NgordnetQuery nq1 = new NgordnetQuery(words1, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);

        String actual1 = myHandler.handle(nq1);
        String expected1 = "[action, change, demotion]";
        assertThat(actual1).isEqualTo(expected1);

        List<String> words2 = List.of("actifed");
        NgordnetQuery nq2 = new NgordnetQuery(words2, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);

        String actual2 = myHandler.handle(nq2);
        String expected2 = "[actifed, antihistamine, nasal_decongestant]";
        assertThat(actual2).isEqualTo(expected2);
    }

    @Test
    public void testWrongInput() {
        List<String> words1 = List.of("???", "What'sThis");
        NgordnetQuery nq1 = new NgordnetQuery(words1, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);

        String actual1 = myHandler.handle(nq1);
        String expected1 = "[]";
        assertThat(actual1).isEqualTo(expected1);
    }
}
