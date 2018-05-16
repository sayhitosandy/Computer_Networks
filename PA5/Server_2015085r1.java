
/*
	@Sanidhya Singal, 2015085
*/
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

//Implementation of Link State Algorithm (Dijkstra's Algo.)
public class Server_2015085r1 {

	private static final Integer INT_MAX = 100000; // Max value for an edge
													// between two nodes
	static List<String> nodes = new ArrayList<>(); // List of vertices in the
													// graph
	static HashMap<Pair<String, String>, Integer> edges = new HashMap<>(); // List
																			// of
																			// weighted
																			// edges
																			// in
																			// the
																			// graph
	static HashMap<String, HashMap<String, Integer>> adjList = new HashMap<>(); // Adjacency
																				// List

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner s = new Scanner(System.in);
		String line = new String();
		if (s.hasNextLine()) {
			line = s.nextLine();
		}
		String[] vals = line.split(" ");

		for (int i = 0; i < vals.length; i += 3) {
			if (nodes.contains(vals[i]) == false) {
				nodes.add(vals[i]);
			}
			if (nodes.contains(vals[i + 1]) == false) {
				nodes.add(vals[i + 1]);
			}
			int val;
			if (vals[i + 2].charAt(vals[i + 2].length() - 1) == '#') {
				String str = vals[i + 2].substring(0, vals[i + 2].length() - 1);
				val = Integer.parseInt(str);
			} else {
				val = Integer.parseInt(vals[i + 2]);
			}
			edges.put(new Pair<String, String>(vals[i], vals[i + 1]), val);
			edges.put(new Pair<String, String>(vals[i + 1], vals[i]), val); // Bidirectional
																			// Graph

			if (adjList.containsKey(vals[i]) == false) {
				HashMap<String, Integer> tmp = new HashMap<>();
				tmp.put(vals[i + 1], val);
				adjList.put(vals[i], tmp);
			} else {
				HashMap<String, Integer> tmp = adjList.get(vals[i]);
				tmp.put(vals[i + 1], val);
				adjList.replace(vals[i], tmp);
			}

			if (adjList.containsKey(vals[i + 1]) == false) {
				HashMap<String, Integer> tmp = new HashMap<>();
				tmp.put(vals[i], val);
				adjList.put(vals[i + 1], tmp);
			} else {
				HashMap<String, Integer> tmp = adjList.get(vals[i + 1]);
				tmp.put(vals[i], val);
				adjList.replace(vals[i + 1], tmp);
			}
		}

		System.out.println("Nodes");
		for (int i = 0; i < nodes.size(); i++) {
			System.out.print(nodes.get(i) + " ");
		}
		System.out.println("\n");

		System.out.println("Edges");
		Iterator it = edges.entrySet().iterator();
		while (it.hasNext() == true) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println("(" + ((Pair<String, String>) pair.getKey()).getLeft() + ", "
					+ ((Pair<String, String>) pair.getKey()).getRight() + "): " + pair.getValue());
		}
		System.out.println();

		System.out.println("Adjacency List");
		Iterator adjIt = adjList.entrySet().iterator();
		while (adjIt.hasNext() == true) {
			Map.Entry pair = (Map.Entry) adjIt.next();
			System.out.print(pair.getKey() + ": ");
			Iterator valIt = ((HashMap<String, Integer>) pair.getValue()).entrySet().iterator();
			while (valIt.hasNext() == true) {
				Map.Entry pair2 = (Map.Entry) valIt.next();
				System.out.print("(" + pair2.getKey() + "," + pair2.getValue() + "), ");
			}
			System.out.println();
		}
		System.out.println();

		// System.out.println("Enter source vertex: ");
		// String start = nodes.get(0);
		// if (s.hasNext()) {
		// start = s.nextLine();
		// }
		// dijkstra(start); // Link state algorithm on start vertex

		for (int i = 0; i < nodes.size(); i++) {
			dijkstra(nodes.get(i));
		}

		while (true) { // Recalculate for change in edge values or addition of
						// new nodes
			System.out.println("\n\nEnter any change in edge values");
			String st = "";
			if (s.hasNext()) {
				st = s.nextLine();
			}
			String[] new_vals = st.split(" ");

			for (int i = 0; i < new_vals.length; i += 3) {
				if (nodes.contains(new_vals[i]) == false) {
					nodes.add(new_vals[i]);
				}
				if (nodes.contains(new_vals[i + 1]) == false) {
					nodes.add(new_vals[i + 1]);
				}
				int val;
				if (new_vals[i + 2].charAt(new_vals[i + 2].length() - 1) == '#') {
					String str = new_vals[i + 2].substring(0, new_vals[i + 2].length() - 1);
					val = Integer.parseInt(str);
				} else {
					val = Integer.parseInt(new_vals[i + 2]);
				}
				boolean flag = false;
				if (edges.containsKey(new Pair<String, String>(new_vals[i], new_vals[i + 1])) == false) {
					flag = true;
					edges.put(new Pair<String, String>(new_vals[i], new_vals[i + 1]), val);
					edges.put(new Pair<String, String>(new_vals[i + 1], new_vals[i]), val);
				} else {
					edges.replace(new Pair<String, String>(new_vals[i], new_vals[i + 1]), val);
					edges.replace(new Pair<String, String>(new_vals[i + 1], new_vals[i]), val);
				}

				if (adjList.containsKey(new_vals[i]) == false) {
					HashMap<String, Integer> tmp = new HashMap<>();
					tmp.put(new_vals[i + 1], val);
					adjList.put(new_vals[i], tmp);
				} else {
					HashMap<String, Integer> tmp = adjList.get(new_vals[i]);
					if (flag == true) {
						tmp.put(new_vals[i + 1], val);
					} else {
						tmp.replace(new_vals[i + 1], val);
					}
					adjList.replace(new_vals[i], tmp);
				}

				if (adjList.containsKey(new_vals[i + 1]) == false) {
					HashMap<String, Integer> tmp = new HashMap<>();
					tmp.put(new_vals[i], val);
					adjList.put(new_vals[i + 1], tmp);
				} else {
					HashMap<String, Integer> tmp = adjList.get(new_vals[i + 1]);
					if (flag == true) {
						tmp.put(new_vals[i], val);
					} else {
						tmp.replace(new_vals[i], val);
					}
					adjList.replace(new_vals[i + 1], tmp);
				}
			}

			for (int i = 0; i < nodes.size(); i++) {
				dijkstra(nodes.get(i));
			}
		}

		// s.close();

	}

	public static void dijkstra(String s) {
		PriorityQueue<Pair<String, Integer>> q = new PriorityQueue<>(nodes.size(), new MyComparator()); // Priority
																										// Queue
																										// to
																										// keep
																										// track
																										// of
																										// unvisited
																										// nodes
		HashMap<String, Integer> d = new HashMap<>(); // Distance to the node
														// from s
		HashMap<String, String> parent = new HashMap<>(); // Parent node to any
															// node

		for (int i = 0; i < nodes.size(); i++) { // Initialize distances to
													// infinity
			if (nodes.get(i).equals(s) == false) {
				if (d.containsKey(nodes.get(i)) == false) {
					d.put(nodes.get(i), INT_MAX);
				} else {
					d.replace(nodes.get(i), INT_MAX);
				}
			}
		}
		if (d.containsKey(s) == false) { // Initialize distance of s from s to
											// be 0
			d.put(s, 0);
		} else {
			d.replace(s, 0);
		}

		System.out.println("d(v)");
		Iterator it = d.entrySet().iterator();
		while (it.hasNext() == true) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + ": " + pair.getValue());
		}
		System.out.println();

		for (int i = 0; i < nodes.size(); i++) { // Initialize parent node for
													// each node to be null
			parent.put(nodes.get(i), "");
		}

		for (int i = 0; i < nodes.size(); i++) { // Update queue with v, d(v)
			if (q.add(new Pair<String, Integer>(nodes.get(i), d.get(nodes.get(i))))) {
				// System.out.println("hi");
			}
		}

		// System.out.println("Queue");
		// // System.out.println(q.size());
		// while (q.size() > 0) {
		// Pair<String, Integer> p = q.poll();
		// System.out.println(p.getLeft() + ": " + p.getRight());
		// }
		// System.out.println();

		while (q.size() > 0) {
			Pair<String, Integer> p = q.poll();
			String u = p.getLeft();
			Integer d_u = p.getRight();

			Iterator itr = adjList.get(u).entrySet().iterator();
			while (itr.hasNext() == true) {
				Map.Entry mp = (Map.Entry) itr.next();
				String v = (String) mp.getKey();
				Integer le = (Integer) mp.getValue();

				if (d.get(v) > (d_u + le)) { // if d(v) > d(u) + l(u,v)
					if (q.remove(new Pair(v, d.get(v))) == true) {
						d.replace(v, d_u + le); // d(v) = d(u) + l(u,v)
						q.add(new Pair(v, d.get(v))); // Update queue
						parent.replace(v, u); // New parent of v is u
					}
				}

			}

			System.out.println("d(v), parent");
			it = d.entrySet().iterator();
			while (it.hasNext() == true) {
				Map.Entry pair = (Map.Entry) it.next();
				System.out.println(pair.getKey() + ": " + pair.getValue() + ", " + parent.get(pair.getKey()));
			}
			System.out.println();

		}

		System.out.println("Forwarding table for " + s);
		System.out.println("Destination: Link");
		for (int i = 0; i < nodes.size(); i++) {
			String v = nodes.get(i);
			if (v.equals(s) == true) {
				continue;
			}

			String pr = parent.get(v);
			while (pr.equals(s) == false) { // Find link for forwarding table
				v = pr;
				pr = parent.get(v);
			}
			System.out.println(nodes.get(i) + ": (" + s + ", " + v + ")");
		}
		System.out.println();
		System.out.println("------------------------------------------------------------------------");
	}
}

class MyComparator implements Comparator<Pair<String, Integer>> {

	@Override
	public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
		// TODO Auto-generated method stub
		if (o1.getRight() < o2.getRight()) {
			return -1;
		} else if (o1.getRight() > o2.getRight()) {
			return 1;
		}
		return 0;
	}

}

class Pair<L, R> {

	private final L left;
	private final R right;

	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		return left.hashCode() ^ right.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair)) {
			return false;
		}
		Pair pairo = (Pair) o;
		return this.left.equals(pairo.getLeft()) && this.right.equals(pairo.getRight());
	}

}