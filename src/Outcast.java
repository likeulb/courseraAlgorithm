import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	private WordNet wordnet;
	
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
		   
	}
	   
	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		   int[] distance = new int[nouns.length];
		   for (int i = 0; i < nouns.length; i++) {
			   for (int j = i+1; j < nouns.length; j++) {
				   int dis = wordnet.distance(nouns[i], nouns[j]);
				   distance[i] += dis;
				   distance[j] += dis;
			   }
		   }
		   int index = 0;
		   for (int i = 1; i < distance.length; i++) {
			   if (distance[i] > distance[index]) {
				   index = i;
			   }
		   }
		   return nouns[index];
	}
	   
	// see test client below
	public static void main(String[] args) {
		String[] ls = { "/Users/yingying/introcs/wordnet/synsets6.txt", "/Users/yingying/introcs/wordnet/hypernyms6TwoAncestors.txt" };
		//WordNet wordnet = new WordNet(ls[0], ls[1]);
		WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
	        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
		   
	}
	
}


