import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		Network net = new Network("facebook_combined.txt"); //must be a txt file of edges

		VirusSimulation vs = new VirusSimulation(net, 0.3, 14, 0.1,
				60, (int) (net.getSize() * 0.001), true, 0.3);

		vs.simulate();
	}
}
