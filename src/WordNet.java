import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;


public class WordNet {
	private HashMap<String, Set<Integer>> nounsMap;
	private HashMap<Integer, String> id2Synset;
    private SAP sap;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
    	if (synsets == null || synsets.length() == 0 || hypernyms == null || hypernyms.length() == 0) {
    		throw new java.lang.NullPointerException();
    		}
    		
        int maxNum = 0;
        nounsMap = new HashMap<String, Set<Integer>>();
        id2Synset = new HashMap<Integer, String>();
        In insyn = new In(synsets);
	    String line = "";
	    while ((line = insyn.readLine()) != null) {
	    	maxNum++;
	    	String[] content = line.split(",");
	        String[] noun = content[1].split(" ");
	        int number = Integer.parseInt(content[0]);
	        id2Synset.put(number, content[1]);
	        for (int i = 0; i < noun.length; i++) {
	        	if (nounsMap.containsKey(noun[i])) {
	                    nounsMap.get(noun[i]).add(number);
	                }
	        	else {
	                  	HashSet<Integer> IDs = new HashSet<Integer>();
	                    IDs.add(number);
	                    nounsMap.put(noun[i], IDs);
	                	}
	                
	            	}
	        	}
	    
	    Digraph G = new Digraph(maxNum);
        
        boolean[] hasAncestor = new boolean[maxNum];
        In inhyper = new In(hypernyms);
	    line = "";
	    while ((line = inhyper.readLine()) != null) {
	            String[] hypers = line.split(",");
	            int curNum = Integer.parseInt(hypers[0]);
	            hasAncestor[curNum] = true;
	            
	            for (int i = 1; i < hypers.length; i++) {
	            	int hyper = Integer.parseInt(hypers[i]);
	                G.addEdge(curNum, hyper);
	                
	                
	            }
	        }
	        
        	DirectedCycle directedCycle = new DirectedCycle(G);
        	if (directedCycle.hasCycle()) {
        		throw new java.lang.IllegalArgumentException();
        	}
        	int rootNum = 0;
        	for (boolean b : hasAncestor) {
        		if (!b) {
        			rootNum++;
        		}
        		if (rootNum > 1) {
        			throw new java.lang.IllegalArgumentException();
        		}
        	}
        	sap = new SAP(G);
        	}
       

   // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsMap.keySet();
    }
       

   // is the word a WordNet noun?
    public boolean isNoun(String word) {
    	if (word == null || word.length() == 0)
    		throw new java.lang.NullPointerException();
        return nounsMap.containsKey(word);
    }

   // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
    	if (nounA == null || nounA.length() == 0 || nounB == null || nounB.length() == 0)
    		throw new java.lang.NullPointerException();
    	if (!isNoun(nounA) || !isNoun(nounB)) {
    		throw new java.lang.IllegalArgumentException();
    	}
    	Iterable<Integer> aNum = nounsMap.get(nounA);
    	Iterable<Integer> bNum = nounsMap.get(nounB);
    	return sap.length(aNum, bNum);
    }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
    	if (nounA == null || nounA.length() == 0 || nounB == null || nounB.length() == 0)
    		throw new java.lang.NullPointerException();
    	if (!isNoun(nounA) || !isNoun(nounB)) {
    		throw new java.lang.IllegalArgumentException();
    	}
    	Iterable<Integer> aNum = nounsMap.get(nounA);
    	Iterable<Integer> bNum = nounsMap.get(nounB);
    	int ancestor = sap.ancestor(aNum, bNum);
    	return id2Synset.get(ancestor);
    }

   // do unit testing of this class
   public static void main(String[] args) {
       WordNet W = new WordNet("/Users/yingying/introcs/wordnet/synsets15.txt", "/Users/yingying/introcs/wordnet/hypernyms15Path.txt");
      
       Iterator m = W.nouns().iterator();
       while (m.hasNext()) {
           System.out.println((String) m.next());
       }
       System.out.println(W.isNoun("G"));
       System.out.println(W.distance("b", "a"));
   }
}