package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private final Map<Integer, Set<String>> synsetContents; // The map between list indexes and the words they contain.
    private final WordNetGraph wng;


    public HyponymsHandler(Map<Integer, Set<String>> synsetContents, WordNetGraph wordNetGraph) {
        this.synsetContents = synsetContents;
        this.wng = wordNetGraph;
    }

    /** Create a handler from files.
     *  contentFile should store the map between synset index and its content.
     *  topoFile should store the graph information among synsets. */
    public HyponymsHandler(String contentFile, String topoFile) {
        this.synsetContents = createSynsetContentsFromFile(contentFile);
        this.wng = createWordNetGraphFromFile(topoFile);
    }


    /** Show all the parent-children pairs of the graph. */
    public void printGraph() {
        wng.printGraph();
    }



    @Override
    /* Handle a request with list of words. Return all of their common hyponyms. */
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        List<Set<String>> wordSets = new ArrayList<>();
        for (String word : words) {
            wordSets.add(findHyponyms(word));
        }
        Set<String> hyponyms = findIntersection(wordSets);
        return printHyponyms(hyponyms);
    }







    /** Given the set of hyponyms, return the required string format. */
    private String printHyponyms(Set<String> words) {
        StringBuilder result = new StringBuilder("[");
        for (String word : words) {
            result.append(word).append(", ");
        }
        result.delete(result.length()-2, result.length());
        result.append("]");
        return result.toString();
    }

    /** Returns a list of graph node indexes whose corresponding synset contains word. */
    private List<Integer> nodesContainingWord(String word) {
        List<Integer> nodeList = new ArrayList<>();
        for (int node : wng.nodes()) {
            if (synsetContents.get(node).contains(word)) {
                nodeList.add(node);
            }
        }
        return nodeList;
    }

    /** Given a list of synset indexes, return the set of non-repeating words contained in it. */
    private Set<String> getWordFromSetOfNodes(Set<Integer> nodes) {
        Set<String> words = new TreeSet<>();
        for (int node : nodes) {
            Set<String> synsetContent = synsetContents.get(node);
            words.addAll(synsetContent);
        }
        return words;
    }

    /** Find all hyponyms of a word */
    private Set<String> findHyponyms(String word) {
        List<Integer> nodes = nodesContainingWord(word);
        Set<Integer> allChildren = wng.getAllChildren(nodes);
        return getWordFromSetOfNodes(allChildren);
    }

    /** Convert the hyponyms file content to a wng. */
    private WordNetGraph createWordNetGraphFromFile(String file) {
        WordNetGraph wordNetGraph1 = new WordNetGraph();
        In in = new In(file);

        while (!in.isEmpty()) {
            // Read the file by line.
            String nextLine = in.readLine();
            String[] splitLine = nextLine.split(",");
            int parent = Integer.parseInt(splitLine[0]);
            // Add nodes if not present.
            if (!wordNetGraph1.contains(parent)) {
                wordNetGraph1.addNode(parent);
            }
            for (int i = 1; i < splitLine.length; i += 1) {
                int children = Integer.parseInt(splitLine[i]);
                if (!wordNetGraph1.contains(children)) {
                    wordNetGraph1.addNode(children);
                }
                wordNetGraph1.addEdge(parent, children);
            }
        }

        return wordNetGraph1;
    }

    /** Convert the synset file content to a synsetContent map. */
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

    /** Split a string of words by spaces. Return the set of words. */
    private Set<String> stringToSynset(String s) {
        String[] words = s.split(" ");
        return new HashSet<>(Arrays.asList(words));
    }

    /** Return the intersection of a list of sets. Return an empty set if the input is empty. */
    private Set<String> findIntersection(List<Set<String>> sets) {
        if (sets.size() == 1) {
            return sets.getFirst();
        }
        Set<String> intersection = new TreeSet<>();
        for (String word : sets.getFirst()) {
            boolean isEverywhere = true;
            for (int i = 1; i < sets.size(); i ++) {
                // If there's a set that does not have this element
                if (!sets.get(i).contains(word)) {
                    isEverywhere = false;
                    break;
                }
            }
            // Every other set contains this element
            if (isEverywhere) {
                intersection.add(word);
            }
        }
        return intersection;
    }



}
