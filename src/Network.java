import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Network {
	
	protected int[][] adjWeighted; //adj[i][j] is the weight btwn i and j
	
	/**
	 * Constructor for Network
	 * 
	 * @param file
	 * @throws IOException
	 */
	
	public Network(String file) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
		
		String curr = reader.readLine();
		
		int max = 0;
		
		while (curr != null) { //loop to find the max
			String[] nodes = curr.split(" ");
			
			int a = Integer.parseInt(nodes[0]);
			int b = Integer.parseInt(nodes[1]);
			
			max = Math.max(max, Math.max(a, b));
			
			curr = reader.readLine();
		}
		
		System.out.println("-- Found the max: " + max);
		
		reader = new BufferedReader(new FileReader(new File(file)));
		
		int[][] adj = new int[max + 1][max + 1]; //unweighted graph
		
		for (int i = 0; i < adj.length; i++) { //values to 0
			for (int j = 0; j < adj.length; j++) {
				adj[i][j] = 0;
			}
		}
		
		System.out.println("-- Assigned dummy values");
		
		curr = reader.readLine();
		
		while (curr != null) { //set up relationship between nodes.
			String[] nodes = curr.split(" ");
			
			int a = Integer.parseInt(nodes[0]);
			int b = Integer.parseInt(nodes[1]);
			
			adj[a][b] = 1;
			adj[b][a] = 1;
			
			curr = reader.readLine();
		}
		
		System.out.println("-- Set up connections between nodes (unweighted)");
		
		//maps node to the number of their connections
		HashMap<Integer, Integer> numConnections = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < adj.length; i++) {
			int cnt = 0;
			
			for (int j = 0; j < adj.length; j++) {
				if (adj[i][j] == 1) {
					cnt++;
				}
			}
			
			numConnections.put(i, cnt);
		}
		
		System.out.println("-- Found num connections for each node");
		
		adjWeighted = adj.clone(); //building weighted graph
		
		//we denote strong ties with 4, weak ties with 1
		
		double strongFactor = 1.25; //strength of strong tie.
		
		for (int i = 0; i < adj.length; i++) {
			for (int j = 0; j < adj.length; j++) {
				
				if (adj[i][j] == 1) {
					
					if (adjWeighted[i][j] != 4) {
						int threshold = (int) (Math.min(numConnections.get(i), 
								numConnections.get(j)) / strongFactor);
						int mutual = 0;
						for (int k = 0; k < adj.length; k++) {
							if (adj[i][k] == 1 && adj[j][k] == 1) {
								mutual++;
							}
						}
						
						if (mutual > threshold) {
							adjWeighted[i][j] = 4;
							adjWeighted[j][i] = 4;
						}
					}
				}
			}
		}
		
		System.out.println("-- Assigned strong ties");
		
		reader.close();
		
		System.out.println("completed");
	}
	
	/**
	 * Gets all ties for specified node
	 * @param n
	 * @return ArrayList of strong ties for n
	 */
	
	public ArrayList<Integer> getTies(int n) {
		
		ArrayList<Integer> ties = new ArrayList<Integer>();
		
		for (int i = 0; i < adjWeighted.length; i++) {
			if (adjWeighted[n][i] == 4) {
				ties.add(i);
			}
		}
		
		return ties;
	}
	
	/**
	 * Gets weak ties for specified node
	 * @param n
	 * @return ArrayList of strong ties for n
	 */
	
	public ArrayList<Integer> getWeakTies(int n) {
		
		ArrayList<Integer> ties = new ArrayList<Integer>();
		
		for (int i = 0; i < adjWeighted.length; i++) {
			if (adjWeighted[n][i] == 1) {
				ties.add(i);
			}
		}
		
		return ties;
	}
	
	/**
	 * Gets strong ties for specified node
	 * @param n
	 * @return ArrayList of strong ties for n
	 */
	
	public ArrayList<Integer> getStrongTies(int n) {
		
		ArrayList<Integer> ties = new ArrayList<Integer>();
		
		for (int i = 0; i < adjWeighted.length; i++) {
			if (adjWeighted[n][i] == 4) {
				ties.add(i);
			}
		}
		
		return ties;
	}
	
	/**
	 * Removes a specified tie between a and b.
	 * @param a
	 * @param b
	 */
	
	public void removeTie(int a, int b) {
		adjWeighted[a][b] = 0;
		adjWeighted[b][a] = 0;
	}
	
	/**
	 * Removes all weak ties for n.
	 * @param a
	 * @param b
	 */
	
	public void removeWeakTies(int n) {
		for (int i = 0; i < adjWeighted.length; i++) {
			if (adjWeighted[n][i] == 1) {
				adjWeighted[n][i] = 0;
				adjWeighted[i][n] = 0;
			}
		}
		
		System.out.println("Removed all weak ties for " + n);
	}
	
	/**
	 * Removes all weak ties for n.
	 * @param a
	 * @param b
	 */
	
	public void removeStrongTies(int n) {
		for (int i = 0; i < adjWeighted.length; i++) {
			if (adjWeighted[n][i] == 4) {
				adjWeighted[n][i] = 0;
				adjWeighted[i][n] = 0;
			}
		}
		
		System.out.println("Removed all Strong ties for " + n);
	}
}
