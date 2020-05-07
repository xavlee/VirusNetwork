import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class VirusSimulation {

    Network net;
    Status[] infectionStatus;
    Double infectionRate;
    Integer daysContagious;
    Double mortalityRate;
    Integer vaccineDays;
    Integer testingCapacity;
    Boolean socialDistancing;
    Double essentialWorkerRate;
    List<Integer> recovered;
    List<Integer> dead;
    List<Integer> vaccinated;
    List<InfectedNode> transmitting;
    List<Integer> recoveredThisTurn;
    List<Integer> deadThisTurn;
    List<Integer> vaccinatedThisTurn;
    List<Integer> transmittingThisTurn;
    List<Integer> quarantinedThisTurn;
    Set<Integer> testingNotNeeded;
    int maxTransmitting;
    List<Integer> infectionsBeforeAware;
    List<Integer> infectionsAfterAware;
    int daysPassed;
    boolean distancingMeasuresTaken;
    boolean aware;
    int totalTested;
    

    enum Status {HEALTHY, TRANSMITTING, RECOVERED, DEAD, VACCINATED}

    public VirusSimulation(Network net, Double infectionRate, Integer daysContagious, Double mortalityRate,
                           Integer vaccineDays, Integer testingCapacity, Boolean socialDistancing,
                           Double essentialWorkerRate) {
        this.net = net;

        this.infectionRate = infectionRate;
        this.daysContagious = daysContagious;
        this.mortalityRate = mortalityRate;
        this.vaccineDays = vaccineDays;
        this.testingCapacity = testingCapacity;
        this.socialDistancing = socialDistancing;
        this.essentialWorkerRate = essentialWorkerRate;
        this.recovered = new ArrayList<>();
        this.dead = new ArrayList<>();
        this.vaccinated = new ArrayList<>();
        this.recoveredThisTurn = new ArrayList<>();
        this.deadThisTurn = new ArrayList<>();
        this.vaccinatedThisTurn = new ArrayList<>();
        this.transmittingThisTurn = new ArrayList<>();
        this.quarantinedThisTurn = new ArrayList<>();
        this.testingNotNeeded = new HashSet<>();
        this.maxTransmitting = 1;
        this.infectionsBeforeAware = new ArrayList<>();
        this.infectionsAfterAware = new ArrayList<>();

        infectionStatus = new Status[net.getSize()];
        for (int i = 0; i < infectionStatus.length; i++) {
            infectionStatus[i] = Status.HEALTHY;
        }
        Integer patientZero = (int) (Math.random() * net.getSize());
        infectionStatus[patientZero] = Status.TRANSMITTING;

        this.transmitting = new ArrayList<>();
        transmitting.add(new InfectedNode(patientZero));
        transmittingThisTurn.add(patientZero);

        this.daysPassed = 0;
        this.distancingMeasuresTaken = false;
        this.aware = false;
        this.totalTested = 0;
    }

    public void simulate() {
        
        recoveredThisTurn = new ArrayList<>();
        deadThisTurn = new ArrayList<>();
        vaccinatedThisTurn = new ArrayList<>();
        transmittingThisTurn = new ArrayList<>();
        quarantinedThisTurn = new ArrayList<>();

        // while there are still people transmitting the disease
        List<InfectedNode> newInfection = new ArrayList<>();
        List<InfectedNode> toRemove = new ArrayList<>();
        int testedToday = 0;

        if (transmitting.size() + dead.size() + recovered.size() / 2 >= net.getSize() * 0.1) {
            aware = true;
        }
        
        if (!aware) {
            vaccineDays++;
        }

        if (daysPassed >= vaccineDays) {
            int numVaccinesToday = net.getSize() / 200;
            for (int i = 0; i < net.getSize(); i++) {
                if (numVaccinesToday > 0 && infectionStatus[i] == Status.HEALTHY) {
                    infectionStatus[i] = Status.VACCINATED;
                    vaccinated.add(i);
                    testingNotNeeded.add(i);
                    vaccinatedThisTurn.add(i);
                    numVaccinesToday--;
                }

                if (Math.random() > 0.5) {
                    i += 5;
                }
            }
        }

        // add social distancing measures after the infected population reaches a certain size
        if (aware && socialDistancing && !distancingMeasuresTaken) {
            // this person is not an essential worker, they can remove their ties
            if (Math.random() >= essentialWorkerRate) {
                for (int i = 0; i < net.getSize(); i++) {
                    // remove 70% all weak ties
                    for (Integer weakTie : net.getWeakTies(i)) {
                        if (Math.random() < 0.7) {
                            net.removeTie(i, weakTie);
                        }
                    }

                    // remove about 20% of strong ties
                    for (Integer strongTie : net.getStrongTies(i)) {
                        if (Math.random() < 0.2) {
                            net.removeTie(i, strongTie);
                        }
                    }

                }
            }
            distancingMeasuresTaken = true;
        }

        // go through the ties of each infected person
        for (InfectedNode person : transmitting) {
            // if they have had the disease for the full time, they no longer are transmitting so remove them
            if (person.daysInfected >= daysContagious) {
                toRemove.add(person);

                // they recover with rate 1 - mortality rate
                if (Math.random() < mortalityRate) {
                    infectionStatus[person.num] = Status.DEAD;
                    net.removeStrongTies(person.num);
                    net.removeWeakTies(person.num);
                    dead.add(person.num);
                    testingNotNeeded.add(person.num);
                    deadThisTurn.add(person.num);

                } else {
                    infectionStatus[person.num] = Status.RECOVERED;
                    recovered.add(person.num);
                    recoveredThisTurn.add(person.num);
                }
            } else {
                 /*
                 - the person is still infectious, so see if they pass the disease on in this round
                 - assume that Person sees 30% of their friends on any given day
                 */
                for (Integer friend : net.getStrongTies(person.num)) {
                    if (infectionStatus[friend] == Status.HEALTHY && Math.random() < 0.3) {
                        // strong ties are more likely to get it than weak ties
                        if (Math.random() < infectionRate) {
                            infectionStatus[friend] = Status.TRANSMITTING;
                            newInfection.add(new InfectedNode(friend));
                            transmittingThisTurn.add(friend);
                        }
                    }
                }

                // weak ties get it with 1/4 the transmission rate
                for (Integer friend : net.getWeakTies(person.num)) {
                    if (infectionStatus[friend] == Status.HEALTHY && Math.random() < 0.3) {
                        if (Math.random() < infectionRate / 4.0) {
                            infectionStatus[friend] = Status.TRANSMITTING;
                            newInfection.add(new InfectedNode(friend));
                            transmittingThisTurn.add(friend);
                        }
                    }
                }

                // if testing is active, there are still tests available on the given day, person is able to access
                // testing -- assume num available tests/population chance they can access testing on this given day
                if (aware && testedToday < testingCapacity && Math.random() < testingCapacity * 1.0
                        / (net.getSize() - testingNotNeeded.size()) &&
                        !testingNotNeeded.contains(person.num)) {
                    // if the person tests positive, assume they go into quarantine and remove all ties
                    net.removeWeakTies(person.num);
                    net.removeStrongTies(person.num);
                    testingNotNeeded.add(person.num);
                    testedToday++;
                    quarantinedThisTurn.add(person.num);
                }

                person.daysInfected++;
            }
        }

        transmitting.addAll(newInfection);
        transmitting.removeAll(toRemove);
        totalTested += testedToday;

        if (daysPassed % 3 == 0) {
            /*System.out.println("day: " + daysPassed + ", new infections: " + newInfection.size());
            System.out.println("aware: " + aware);
            System.out.println("number transmitting: " + transmitting.size());
            System.out.println("number recovered: " + recovered.size());
            System.out.println("number dead: " + dead.size());
            System.out.println("total tested: " + totalTested);
            System.out.println("number vaccinated: " + vaccinated.size());
            System.out.println(); */
        }
        

        maxTransmitting = Math.max(transmitting.size(), maxTransmitting);
        if (aware) {
            infectionsAfterAware.add(transmittingThisTurn.size());
        } else {
            infectionsBeforeAware.add(transmittingThisTurn.size());
        }

        daysPassed++;
        
    }

    private class InfectedNode {
        int daysInfected;
        int num;

        public InfectedNode(int n) {
            daysInfected = 0;
            num = n;
        }
    }
}
