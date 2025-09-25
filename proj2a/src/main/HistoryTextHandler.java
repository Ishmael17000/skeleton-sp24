package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private final NGramMap nGrammap;

    public HistoryTextHandler(NGramMap map) {
        nGrammap = map;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        StringBuilder response = new StringBuilder();
        for (String word : words) {
            TimeSeries ts = nGrammap.weightHistory(word, startYear, endYear);
            response.append(lineFormatBuilder(word, ts));
        }
        return response.toString();
    }

    // Return a line of response.
    private String lineFormatBuilder(String word, TimeSeries ts) {
        StringBuilder rtn = new StringBuilder(word + ": {");
        for (int year : ts.years()) {
            double value = ts.get(year);
            rtn.append(yearDataBuilder(year, value));
            rtn.append(", ");
        }
        rtn.delete(rtn.length()-2, rtn.length());
        rtn.append("}\n");
        return rtn.toString();
    }

    // Return data of a year.
    private String yearDataBuilder(int year, double value) {
        return year + "=" + value;
    }
}
