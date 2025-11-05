package main;

import java.util.*;

public class WordNetGraph {

    private int size;
    private int edgeSize;
    private final Map<Integer, Set<Integer>> childrenMap; // Store the children of vertices.
    private final Map<Integer, Set<Integer>> parentMap; // Store the parents of vertices.


    /** Initialize an empty graph.
     *  childrenMap: Assign each node to its children.
     *  parentMap: Assign each node to its parents. */

    public WordNetGraph() {
        this.size = 0;
        this.edgeSize = 0;
        this.childrenMap = new HashMap<>();
        this.parentMap = new HashMap<>();
    }


    /** Add a node to the graph. */
    public void addNode(int synset) {
        if (contains(synset)) {
            System.out.println("Insertion " + synset + " failed. Node already exists.");
            return;
        }
        childrenMap.put(synset, new HashSet<>());
        parentMap.put(synset, new HashSet<>());
        size += 1;
    }


    /** Add an edge pointing from v to w. */
    public void addEdge(int v, int w) {
        if (!contains(v) || !contains(w)) {
            System.out.println("Fail to connect " + v + "to " + w + ". Both nodes must exist");
            return;
        }
        if (!connectsTo(v, w)) {
            edgeSize += 1;
            childrenMap.get(v).add(w);
            parentMap.get(w).add(v);
        } else {
            System.out.println(v + " is already connected to " + w + ".");
        }
    }


    /** Return the set of nodes. */
    public Set<Integer> nodes() {
        return childrenMap.keySet();
    }

    /** Return whether the graph contains the node. */
    public boolean contains(int synset) {
        return nodes().contains(synset);
    }

    /** Return whether the node has children. */
    public boolean hasChildren(int node) {
        return !childrenMap.get(node).isEmpty();
    }

    /** Return whether the node has parent. */
    public boolean hasParent(int node) { return !parentMap.get(node).isEmpty(); }

    /** Return whether there is an edge from node1 to node2. */
    public boolean connectsTo(int node1, int node2) {
        return (contains(node1) && childrenMap.get(node1).contains(node2));
    }


    /** Return all the children of v. */
    public Set<Integer> childrenOf(int v) {
        return childrenMap.get(v);
    }

    /** Return all the parents of v. */
    public Set<Integer> parentOf(int v) {
        return parentMap.get(v);
    }


    /** Return the number of nodes in the graph. */
    public int size() {
        return size;
    }

    /** Return all nodes that are inferior to the node. Use some graph traversal (in particular, DFS). */
    public Set<Integer> getAllChildren(int synset) {
        return getChildrenHelper(synset, new HashSet<>());
    }

    /** Return all nodes that are superior to the node. Use some graph traversal (in particular, DFS). */
    public Set<Integer> getAllParent(int synset) {
        return getParentHelper(synset, new HashSet<>());
    }


    /** Return the (non-repeating) union of inferiors of all node in the list. */
    public Set<Integer> getAllChildren(List<Integer> nodes) {
        Set<Integer> children = new HashSet<>();
        for (int node : nodes) {
            Set<Integer> currentChildren = getAllChildren(node);
            children.addAll(currentChildren);
        }
        return children;
    }

    /** Return the (non-repeating) union of inferiors of all node in the list. */
    public Set<Integer> getAllParent(List<Integer> nodes) {
        Set<Integer> parent = new HashSet<>();
        for (int node : nodes) {
            Set<Integer> currentParent = getAllParent(node);
            parent.addAll(currentParent);
        }
        return parent;
    }









    /** Show all the parent-children pairs of the graph. */
    public void printGraph() {
        for (int vertice : childrenMap.keySet()) {
            for (int children : childrenOf(vertice)) {
                System.out.println(vertice + "->" + children);
            }
        }
    }



    /** Use DFS to find all the children, children of children etc. of a node. */
    private Set<Integer> getChildrenHelper(int synset, Set<Integer> nodes) {
        // Use a set to store nodes and pass them to recursion calls.
        nodes.add(synset);
        if (hasChildren(synset)) {
            for (int node : childrenOf(synset)) {
                getChildrenHelper(node, nodes);
            }
        }
        return nodes;
    }

    /** Use DFS to find all the parent, parent of parent etc. of a node. */
    private Set<Integer> getParentHelper(int synset, Set<Integer> nodes) {
        // Use a set to store nodes and pass them to recursion calls.
        nodes.add(synset);
        if (hasParent(synset)) {
            for (int node : parentOf(synset)) {
                getParentHelper(node, nodes);
            }
        }
        return nodes;
    }
}
