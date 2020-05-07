import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;


public class GUIVirus implements Runnable {

    private final JFrame frame = new JFrame("VirusSim");
    
    Double infectionRate;
    Integer daysContagious;
    Double mortalityRate;
    Integer vaccineDays;
    Integer testingCapacity;
    Boolean socialDistancing;
    Double essentialWorkerRate;
    Boolean paused;
    VirusSimulation vs;
    JLabel[] pop;
    
    public void run() {
        this.infectionRate = 0.3;
        this.daysContagious = 14;
        this.mortalityRate = 0.1;
        this.vaccineDays = 60;
        this.vs = null;
        this.pop = null;
        testingCapacity = 4;
        socialDistancing = true;
        essentialWorkerRate = 0.3;
        
        frame.setLayout(new BorderLayout());


        final JPanel menubar = new JPanel();
        final JButton infectRate  = new JButton("Set Infection Rate");      
        final JButton daysContag  = new JButton("Set Days Contagious");
        final JButton mortRate    = new JButton("Set Mortality Rate");
        final JButton vaccDays    = new JButton("Set Days to a Vaccine");
        final JButton testingCap  = new JButton("Set Testing Capacity");
        final JButton socialDist  = new JButton("Social Distancing?");
        final JButton essentWork  = new JButton("Set Rate of Essential Workers");
        final JButton run         = new JButton("Run Simulation");
        final JLabel infectRateLabel  = new JLabel(Double.toString(infectionRate));      
        final JLabel daysContagLabel  = new JLabel(Integer.toString(daysContagious));
        final JLabel mortRateLabel    = new JLabel(Double.toString(mortalityRate));
        final JLabel vaccDaysLabel    = new JLabel(Integer.toString(vaccineDays));
        final JLabel testingCapLabel  = new JLabel(Integer.toString(testingCapacity));
        final JLabel socialDistLabel  = new JLabel(Boolean.toString(socialDistancing));
        final JLabel essentWorkLabel  = new JLabel(Double.toString(essentialWorkerRate));
        final JButton advance       = new JButton("Advance");
        final JPanel statusbar = new JPanel();
        final JLabel healthy     = new JLabel("");
        final JLabel infected    = new JLabel("");
        final JLabel dead        = new JLabel("");
        final JLabel vaccinated  = new JLabel("");
        final JLabel recovered   = new JLabel("");
        final JLabel aware       = new JLabel("");
        final JLabel dayNumber   = new JLabel("");
        final JLabel daysToVacc  = new JLabel("");

        menubar.setLayout(new GridLayout(2,8));
        menubar.add(infectRate);
        menubar.add(daysContag);
        menubar.add(mortRate);
        menubar.add(vaccDays);
        menubar.add(testingCap);
        menubar.add(socialDist);
        menubar.add(essentWork);
        menubar.add(run);
        menubar.add(infectRateLabel);
        menubar.add(daysContagLabel);
        menubar.add(mortRateLabel);
        menubar.add(vaccDaysLabel);
        menubar.add(testingCapLabel);
        menubar.add(socialDistLabel);
        menubar.add(essentWorkLabel);
        menubar.add(advance);
        
        statusbar.setLayout(new GridLayout(1,8));
        statusbar.add(healthy);
        statusbar.add(infected);
        statusbar.add(dead);
        statusbar.add(vaccinated);
        statusbar.add(recovered);
        statusbar.add(dayNumber);
        statusbar.add(aware);
        statusbar.add(daysToVacc);

        infectRate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String result = (String)JOptionPane.showInputDialog(
                        frame,
                        "Set the rate at which people infect their friends.\n" +
                                "This must be a number between 1 (inclusive) and 0 (non-inclusive).",
                                "Set Infection Rate",
                                JOptionPane.PLAIN_MESSAGE
                        );
                try {
                    if (result != null) {
                        Double ir = Double.parseDouble(result);
                        if (ir > 0 && ir <= 1) {
                            infectionRate = ir;
                            infectRateLabel.setText(Double.toString(ir));
                        } else {
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Value not between 0 and 1", 
                                    "Alert",
                                    JOptionPane.ERROR_MESSAGE
                                    );
                        }
                    }
                } catch (NumberFormatException nu) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Not a number" + nu.getMessage(), 
                            "Alert",
                            JOptionPane.ERROR_MESSAGE
                            );
                }
            }
        });
        daysContag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String result = (String)JOptionPane.showInputDialog(
                        frame,
                        "Set the amount of days a person is contagious.\n" +
                                "This must be a positive integer value.",
                                "Set Days Contagious",
                                JOptionPane.PLAIN_MESSAGE
                        );
                try {
                    if (result != null) {
                        int dc = Integer.parseInt(result);
                        if (dc > 0) {
                            daysContagious = dc;
                            daysContagLabel.setText(Integer.toString(dc));
                        } else {
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Value not positive", 
                                    "Alert",
                                    JOptionPane.ERROR_MESSAGE
                                    );
                        }
                    }
                } catch (NumberFormatException nu) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Not an integer" + nu.getMessage(), 
                            "Alert",
                            JOptionPane.ERROR_MESSAGE
                            );
                }
            }
        });
        mortRate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String result = (String)JOptionPane.showInputDialog(
                        frame,
                        "Set the mortality rate.\n" +
                                "This must be a number between 0 and 1 (inclusive).",
                                "Set Mortality Rate",
                                JOptionPane.PLAIN_MESSAGE
                        );
                try {
                    if (result != null) {
                        Double mr = Double.parseDouble(result);
                        if (mr >= 0 && mr <= 1) {
                            mortalityRate = mr;
                            mortRateLabel.setText(Double.toString(mr));
                        } else {
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Value not between 1 and 0", 
                                    "Alert",
                                    JOptionPane.ERROR_MESSAGE
                                    );
                        }
                    }
                } catch (NumberFormatException nu) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Not an number" + nu.getMessage(), 
                            "Alert",
                            JOptionPane.ERROR_MESSAGE
                            );
                }
            }
        });
        vaccDays.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String result = (String)JOptionPane.showInputDialog(
                        frame,
                        "Set the amount of days needed to develop a vaccine.\n" +
                                "This must be a positive integer value.",
                                "Set Days to a Vaccine",
                                JOptionPane.PLAIN_MESSAGE
                        );
                try {
                    if (result != null) {
                        int vd = Integer.parseInt(result);
                        if (vd > 0) {
                            vaccineDays = vd;
                            vaccDaysLabel.setText(Integer.toString(vd));
                        } else {
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Value not positive", 
                                    "Alert",
                                    JOptionPane.ERROR_MESSAGE
                                    );
                        }
                    }
                } catch (NumberFormatException nu) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Not an integer" + nu.getMessage(), 
                            "Alert",
                            JOptionPane.ERROR_MESSAGE
                            );
                }
            }
        });
        testingCap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String result = (String)JOptionPane.showInputDialog(
                        frame,
                        "Set the amount of people that can be tested in a day.\n" +
                                "This must be a positive integer value.",
                                "Set Testing Capacity",
                                JOptionPane.PLAIN_MESSAGE
                        );
                try {
                    if (result != null) {
                        int tc = Integer.parseInt(result);
                        if (tc > 0) {
                            testingCapacity = tc;
                            testingCapLabel.setText(Integer.toString(tc));
                        } else {
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Value not positive", 
                                    "Alert",
                                    JOptionPane.ERROR_MESSAGE
                                    );
                        }
                    }
                } catch (NumberFormatException nu) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Not an integer" + nu.getMessage(), 
                            "Alert",
                            JOptionPane.ERROR_MESSAGE
                            );
                }
            }
        });
        socialDist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                socialDistancing = !socialDistancing;
                socialDistLabel.setText(Boolean.toString(socialDistancing));
            }
        });
        essentWork.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String result = (String)JOptionPane.showInputDialog(
                        frame,
                        "Set the rate of essential workers.\n" +
                                "This must be a number between 0 and 1 (inclusive).",
                                "Set Rate of Essential Workers",
                                JOptionPane.PLAIN_MESSAGE
                        );
                try {
                    if (result != null) {
                        Double ew = Double.parseDouble(result);
                        if (ew >= 0 && ew <= 1) {
                            essentialWorkerRate = ew;
                            essentWorkLabel.setText(Double.toString(ew));
                        } else {
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Value not between 1 and 0", 
                                    "Alert",
                                    JOptionPane.ERROR_MESSAGE
                                    );
                        }
                    }
                } catch (NumberFormatException nu) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Not an number" + nu.getMessage(), 
                            "Alert",
                            JOptionPane.ERROR_MESSAGE
                            );
                }
            }
        });
        advance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String result = (String)JOptionPane.showInputDialog(
                        frame,
                        "How many days do you want to advance? \n" +
                                "This must be a positive integer value.",
                                "Advance",
                                JOptionPane.PLAIN_MESSAGE
                        );
                try {
                    if (result != null) {
                        int ad = Integer.parseInt(result);
                        if (ad > 0) {
                            for (int i = 0; i < ad; i++) {
                                if (!vs.transmitting.isEmpty()) {
                                    vs.simulate();
                                    for (int j : vs.recoveredThisTurn) {
                                        pop[j].setBackground(Color.orange);
                                    }
                                    for (int j : vs.deadThisTurn) {
                                        pop[j].setBackground(Color.black);
                                    }
                                    for (int j : vs.vaccinatedThisTurn) {
                                        pop[j].setBackground(Color.blue);
                                    }
                                    for (int j : vs.transmittingThisTurn) {
                                        pop[j].setBackground(Color.green);
                                    }
                                    for (int j : vs.quarantinedThisTurn) {
                                        pop[j].setBackground(Color.red);
                                    }
                                    healthy.setText("healthy: " + (vs.net.getSize() -
                                            vs.transmitting.size() - vs.recovered.size() -
                                            vs.vaccinated.size() - vs.dead.size()));
                                    infected.setText("infected: " + vs.transmitting.size());
                                    recovered.setText("recovered: " + vs.recovered.size());
                                    dead.setText("dead: " + vs.dead.size());
                                    vaccinated.setText("vaccinated: " + vs.vaccinated.size());
                                    if (vs.aware) {
                                        aware.setText("aware");
                                        if (vs.vaccineDays - vs.daysPassed < 0) {
                                            daysToVacc.setText("vaccine deployed");
                                        } else {
                                            daysToVacc.setText("days to vaccine: " + (vs.vaccineDays - vs.daysPassed));
                                        }
                                    }
                                    dayNumber.setText("days passed: " + vs.daysPassed);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Value not positive", 
                                    "Alert",
                                    JOptionPane.ERROR_MESSAGE
                                    );
                        }
                    }
                } catch (NumberFormatException nu) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Not an integer" + nu.getMessage(), 
                            "Alert",
                            JOptionPane.ERROR_MESSAGE
                            );
                }
            }
        });
        frame.add(menubar, BorderLayout.PAGE_START);
        frame.add(statusbar, BorderLayout.PAGE_END);
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 600));
        frame.add(panel, BorderLayout.CENTER);

        run.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    vs = new VirusSimulation(new Network("facebook_combined.txt"), 
                            infectionRate, daysContagious, mortalityRate, vaccineDays, testingCapacity,
                            socialDistancing, essentialWorkerRate);
                    pop = new JLabel[vs.net.getSize()];
                    int dim = (int) Math.sqrt(vs.net.getSize()) + 1;
                    panel.removeAll();
                    panel.setLayout(new GridLayout(dim, dim));
                    for (int i = 0; i < pop.length; i++) {
                        pop[i] = new JLabel();
                        pop[i].setHorizontalAlignment(SwingConstants.CENTER);
                        pop[i].setBorder(new LineBorder(Color.black));
                        pop[i].setOpaque(true);
                        pop[i].setBackground(Color.white);
                        panel.add(pop[i]);
                    }
                    for (int i : vs.transmittingThisTurn) {
                        pop[i].setBackground(Color.green);
                    }
                    healthy.setText("healthy: " + (vs.net.getSize() - vs.transmitting.size() -
                            vs.recovered.size() - vs.dead.size()));
                    infected.setText("infected: " + vs.transmitting.size());
                    recovered.setText("recovered: " + vs.recovered.size());
                    dead.setText("dead: " + vs.dead.size());
                    vaccinated.setText("vaccinated: " + vs.vaccinated.size());
                    aware.setText("");
                    daysToVacc.setText("");
                    dayNumber.setText("days passed: " + vs.daysPassed);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        ;
        frame.pack();
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    public static void main (String[] args) {
        SwingUtilities.invokeLater(new GUIVirus());
    }

}
