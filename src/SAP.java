import java.util.HashMap;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;



public class SAP {
    private final Digraph g;  
    private final int V;
 
    // constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
	   this.g = new Digraph(G); // immutable
	   this.V = this.g.V();
	   }
	// return the ancestor and the distance for a given vertex using bfs
	private HashMap<Integer, Integer> allAncestor(int v) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		LinkedList<Integer> que = new LinkedList<Integer>();
		que.add(v);
		map.put(v, 0);
		int len = 1;
		while (!que.isEmpty()) {
			int size = que.size();
			for (int i = 0; i < size; i++) {
				int cur = que.poll();
				for (int adjacent : g.adj(cur)) {
					if (!map.containsKey(adjacent) && adjacent != v) {
						map.put(adjacent, len);
						que.add(adjacent);
						}
					}
				}
			len++;
			}
		return map;
		}
	//iterate all the ancestors of v and w, return the common ancestor and distance
	private int[] shortlen(int v, int w) {
		HashMap<Integer, Integer> ancestorOfV = allAncestor(v);
		HashMap<Integer, Integer> ancestorOfW = allAncestor(w);
		int[] result = new int[2];
		int len = Integer.MAX_VALUE;
		int common = -1;
		for (int ancestor : ancestorOfV.keySet()) {
			if (ancestorOfW.containsKey(ancestor)) {
				int curLen = ancestorOfV.get(ancestor) + ancestorOfW.get(ancestor);
				if (curLen < len) {
					len = curLen;
					common = ancestor;
					}
				}
			}
		if (len == Integer.MAX_VALUE || common == -1) {
			result[0] = -1;
			result[1] = -1;
			}
		else {
			result[0] = len;
			result[1] = common;
			}
		return result;
		}
	
	private int[] shortlen(Iterable<Integer> v, Iterable<Integer> w) { 
		int minLen = Integer.MAX_VALUE;
		int ancestor = -1;
		for (int vNode: v) {
			for (int wNode: w) {
				int[] tmp = shortlen(vNode, wNode);
				if (tmp[0] != -1 && tmp[0] < minLen) {
					minLen = tmp[0];
					ancestor = tmp[1];
				}
			}
		}
		if (minLen == Integer.MAX_VALUE) {
			return new int[]{-1, -1};
		}
		return new int[]{minLen, ancestor};
		}
		
	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) { 
		int[] shortest = shortlen(v, w);
		return shortest[0];
		}
	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) { 
		int[] shortest = shortlen(v, w);
		return shortest[1];
		}
	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) { 
		if (v == null || w == null)
			throw new java.lang.NullPointerException();
		int[] shortest = shortlen(v, w);
		return shortest[0];
		}
	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null)
			throw new java.lang.NullPointerException();
		int[] shortest = shortlen(v, w);
		return shortest[1];
		}
	
	// do unit testing of this class
	public static void main(String[] args) { 
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		System.out.print(G.E());
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
   }
}