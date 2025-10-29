# Design Details

## Graph Implementation

My directed graph class WordNetGraph is designed to store the relationship (hypernym and hyponym) between synsets.  


The underlying data structure is adjacency list.  
First, the synsets are labeled as integers starting from 0. This relationship is maintained by the map `synsetIndex`,
which maps the index of a synset in the datafile to its index in the graph. When querying from outside, we always use
the index in the file, but in method implementation, we always use the list index.  

Next, before being stored, the synset is processed so that it shows the words it contains. This relationship is maintained by
the map `synsetContents`, which maps the synset index to the words. The words are represented as a list of strings.  

Finally, it comes with the adjacency list, which is the map `adjacencyList` that maps the index of a synset to the list of 
its children. The children are represented by a HashSet of synset indexes.  

## Workflow

Our HyponymsHandler literally takes in words queries and return the corresponding children or parents subject to some input
limitations. When creating a handler instance, we first build our graph based on the datafile, which includes initializing
a WordNetGraph and fill in the graph information. 

Here we list some of the important methods of graphs.

### addNode(synsetIndex)
When adding a node to the graph, we first check whether the graph already contains the node, which requires a search in 
the `synsetIndex`. If there is not, we add it to `synsetIndex`. But how do we know the list index of a synset? The solution is 
to use a pointer which increases by 1 each time a node is added.  

Also, we have to initialize a children set for it.

### addEdge(v, w)
This method creates an edge pointing from v to w.  

### parents(v)
To support parent search, we create a parent set map `parentList` to store the information of parents. It's updated together with 
`adjacencyList` when edges are added.

And here are some important methods that use the graph to implement our HyponymsHandler.

### getHyponyms(v)
To get the hyponyms of a word, we first have to find what node(s) contains it. The graph class has a method called
`nodeContainingWord`. It goes through the graph nodes and search for nodes whose synset contains the word. The `synsetContents` 
relationship is used along the way.  

Next, the method `getAllChildren` returns a set of all children nodes of a node(including itself). After that, call
`getWordFromSetOfNodes` and find the corresponding words. Take the union of word sets to find all hyponyms.


(There's another time-saving approach at the cost of space. When building a graph, we can maintain a map from each word to the set of synsets that 
contain it. We consider not using this for now.)



















