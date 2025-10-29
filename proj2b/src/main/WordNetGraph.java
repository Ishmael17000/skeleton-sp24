package main;

import org.apache.commons.lang3.NotImplementedException;

import java.lang.classfile.constantpool.IntegerEntry;
import java.util.*;

public class WordNetGraph {
    /*
        The graph uses adjacency list as its underlying data structure.
        The nodes are represented as a list of integers, with a Map<Integer, Integer> specifying
        which synset is contained in a node.
        Each synset is represented as an integer. The map wordIndex specifies the words corresponding to the integers.


        The information of wordIndex and graph topology are given in by two files. The synsets are processed in advance.
        When initializing a WordNetGraph, the wordIndex should be passed in as a map. In the HyponymsHandler, we read the
        other file (namely hyponyms) and pass in the graph topology.

     */

    /** Initialize an empty graph. */
    public WordNetGraph(Map<Integer, Set<String>> synsetContents) {
        this.size = 0;
        this.insertPointer = 0;
        this.synsetIndex = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.parentList = new HashMap<>();
        this.synsetContents = synsetContents;
    }


    /** Add a node containing the giving words (represented by synset index). */
    public void addNode(int index) {
        if (!contains(index)) {
            synsetIndex.put(index, insertPointer);
            indexSynset.put(insertPointer, index);
            adjacencyList.put(index, new HashSet<>());
            parentList.put(index, new HashSet<>());
            insertPointer += 1;
            size += 1;
        }
    }


    /** Add an edge pointing from v to w. */
    public void addEdge(int vIndex, int wIndex) {
        if (!contains(vIndex) || !contains(wIndex)) {
            throw new NoSuchElementException("Two existing vertices are required");
        }
        adjacencyList.get(vIndex).add(wIndex);
        parentList.get(wIndex).add(vIndex);
    }


    /** Return all the vertices v is pointing to. */
    public Iterable<Integer> verticesFrom(int v) {
        return adjacencyList.get(v);
    }

    /** Return all the vertices pointing at v. */
    public Iterable<Integer> verticesTo(int v) {
        return parentList.get(v);
    }


    /** Return whether the graph contains a node of the synset. */
    public boolean contains(int index) {
        return synsetIndex.containsValue(index);
    }

    public int getSize() {
        return size;
    }

    /** Return the set of children of v. Use BFS. */
    private Set<Integer> getAllChildren(Integer v) {
        throw new NotImplementedException();
    }

    /** Return the union of children of all node in nodes. */
    private Set<Integer> getAllChildren(List<Integer> nodes) {
        Set<Integer> children = new HashSet<>();
        for (int node : nodes) {
            Set<Integer> currentChildren = getAllChildren(node);
            children.addAll(currentChildren);
        }
        return children;
    }

    /** Given a list of synset indexes, return the set of non-repeating words contained in it. */
    private Set<String> getWordFromSetOfNodes(Set<Integer> nodes) {
        Set<String> words = new HashSet<>();
        for (int node : nodes) {
            int synset = indexSynset.get(node);
            Set<String> synsetContent = synsetContents.get(synset);
            words.addAll(synsetContent);
        }
        return words;
    }

    /** Returns a list of graph node indexes whose corresponding synset contains word. */
    private List<Integer> nodesContainingWord(String word) {
        List<Integer> nodeList = new ArrayList<>();
        for (int node : synsetIndex.keySet()) {
            if (synsetContents.get(node).contains(word)) {
                nodeList.add(synsetIndex.get(node));
            }
        }
        return nodeList;
    }

    /** Find all hyponyms of a word */
    public Set<String> findHyponyms(String word) {
        List<Integer> nodes = nodesContainingWord(word);
        Set<Integer> allChildren = getAllChildren(nodes);
        return getWordFromSetOfNodes(allChildren);
    }



    private Map<Integer, Integer> synsetIndex; // The synset indexes are stored as integers starting from 0.
    private Map<Integer, Integer> indexSynset;
    private Map<Integer, HashSet<Integer>> adjacencyList;
    private int size;
    private Map<Integer, HashSet<Integer>> parentList; // Store the parents of vertices.
    private int insertPointer;
    private final Map<Integer, Set<String>> synsetContents;

}
