package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private final Map<Integer, Set<String>> synsetContents; // The map between list indexes and the words they contain.
    private final WordNetGraph wordNetGraph;


    public HyponymsHandler(Map<Integer, Set<String>> synsetContents, WordNetGraph wordNetGraph) {
        this.synsetContents = synsetContents;
        this.wordNetGraph = wordNetGraph;
    }



    @Override
    public String handle(NgordnetQuery q) {
        // Suppose for now q is a single word.
        List<String> words = q.words();
        for (String word : words) {

        }
        return "Hello";
    }


    private Map<Integer, Set<String>> createSynsetContentsFromFile(String synsetsFile) {
        Map<Integer, Set<String>> synsetContents = new HashMap<>();
        In in = new In(synsetsFile);

        while (!in.isEmpty()) {
            String nextLine = in.readLine();
            String[] splitLine = nextLine.split(",");
            int synset = Integer.parseInt(splitLine[0]);
            Set<String> stringSet = stringToSynset(splitLine[1]);
            synsetContents.put(synset, stringSet);
        }

        return synsetContents;
    }

    private Set<String> stringToSynset(String s) {
        String[] words = s.split(" ");
        return new HashSet<>(Arrays.asList(words));
    }



}
