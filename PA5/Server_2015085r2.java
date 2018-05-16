import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
	@Sanidhya Singal, 2015085
*/

//Implementation of Distance-Vector Algorithm (Bellman Ford)
public class Server_2015085r2 {
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

		for (int i = 0; i < nodes.size(); i++) {
			bellmanFord(nodes.get(i));
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

			}

			for (int i = 0; i < nodes.size(); i++) {
				bellmanFord(nodes.get(i));
			}

		}
	}

	public static void bellmanFord(String t) { // t = start node
		HashMap<String, Integer> d = new HashMap<>(); // Distance to the node
		// from t
		HashMap<String, String> successor = new HashMap<>(); // Parent node to
																// any
		// node

		for (int i = 0; i < nodes.size(); i++) { // Initialize distances to
			// infinity
			if (nodes.get(i).equals(t) == false) {
				if (d.containsKey(nodes.get(i)) == false) {
					d.put(nodes.get(i), INT_MAX);
				} else {
					d.replace(nodes.get(i), INT_MAX);
				}
			}
		}
		if (d.containsKey(t) == false) { // Initialize distance of t from t to
			// be 0
			d.put(t, 0);
		} else {
			d.replace(t, 0);
		}

		System.out.println("d(v)");
		Iterator it = d.entrySet().iterator();
		while (it.hasNext() == true) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + ": " + pair.getValue());
		}
		System.out.println();

		for (int i = 0; i < nodes.size(); i++) { // Initialize successor node
													// for
			// each node to be null
			successor.put(nodes.get(i), "");
		}

		for (int i = 0; i < nodes.size() - 1; i++) {
			Iterator j = edges.entrySet().iterator();
			String u, v;
			Integer d_uv, d_u, d_v;
			while (j.hasNext()) {
				Map.Entry pair = (Map.Entry) j.next();
				u = ((Pair<String, String>) pair.getKey()).getLeft();
				v = ((Pair<String, String>) pair.getKey()).getRight();
				d_uv = (Integer) pair.getValue();

				d_u = d.get(u);
				d_v = d.get(v);

				if (d_v > d_u + d_uv) {
					d_v = d_u + d_uv;
					d.replace(v, d_v);
					successor.replace(v, u);
				}
			}

			System.out.println("Iteration: " + (i + 1));
			System.out.println("d(v), successor");
			it = d.entrySet().iterator();
			while (it.hasNext() == true) {
				Map.Entry pair = (Map.Entry) it.next();
				System.out.println(pair.getKey() + ": " + pair.getValue() + ", " + successor.get(pair.getKey()));
			}
			System.out.println();
		}

		System.out.println("Forwarding table for " + t);
		System.out.println("Destination: Link");
		for (int i = 0; i < nodes.size(); i++) {
			String v = nodes.get(i);
			if (v.equals(t) == true) {
				continue;
			}

			String pr = successor.get(v);
			while (pr.equals(t) == false) { // Find link for forwarding table
				v = pr;
				pr = successor.get(v);
			}
			System.out.println(nodes.get(i) + ": (" + t + ", " + v + ")");
		}
		System.out.println();
		System.out.println("------------------------------------------------------------------------");

	}

}
