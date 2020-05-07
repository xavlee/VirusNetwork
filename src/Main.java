import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		Network net = new Network("facebook_combined.txt"); //must be a txt file of edges

		double irBeforeSum = 0;
		double irAfterSum = 0;
		double maxTransmittingSum = 0;
		double daysDistancedSum = 0;
		double totalInfectedSum = 0;
		double totalDeadSum = 0;
		
		for (int i = 0; i < 100; i++) {
		    VirusSimulation vs = new VirusSimulation(new Network("facebook_combined.txt"),
		            0.3, 14, 0.1, 60, (int) (net.getSize() * 0.001), true, 0.3);
	        while (!vs.transmitting.isEmpty()) {
	            vs.simulate();
	        }
	        int j = 0;
	        for (int k = 0; k < vs.infectionsBeforeAware.size(); k++) {
	            j += vs.infectionsBeforeAware.get(k);
	        }
	        double infectionRateBefore = (double) j / (double) vs.infectionsBeforeAware.size();
	        irBeforeSum += infectionRateBefore;
	        int l = 0;
            for (int k = 0; k < vs.infectionsAfterAware.size(); k++) {
                l += vs.infectionsAfterAware.get(k);
            }
            if (vs.infectionsAfterAware.size() > 0) {
                
            }
            double infectionRateAfter = (double) l / (double) vs.infectionsAfterAware.size();
            irAfterSum += infectionRateAfter;
            maxTransmittingSum += vs.maxTransmitting;
            daysDistancedSum += vs.infectionsAfterAware.size();
            totalInfectedSum += vs.recovered.size() + vs.dead.size();
            totalDeadSum += vs.dead.size();
		}
		double irBeforeAvg = irBeforeSum / 100;
		double irAfterAvg = irAfterSum / 100;
		double maxTransmittingAvg = maxTransmittingSum / 100;
		double daysDistancedAvg = daysDistancedSum / 100;
		double totalInfectedAvg = totalInfectedSum / 100;
		double totalDeadAvg = totalDeadSum / 100;
		System.out.println(irBeforeAvg);
		System.out.println(irAfterAvg);
		System.out.println(maxTransmittingAvg);
		System.out.println(daysDistancedAvg);
		System.out.println(totalInfectedAvg);
		System.out.println(totalDeadAvg);
	}
}
