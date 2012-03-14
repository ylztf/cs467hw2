import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.concurrent.*;

public class TokenRingOperations extends JApplet implements ActionListener, ListSelectionListener {
   
	private JButton startButton, stopButton, moveButton, requestButton;
	private JTextField source, destination;
	private JTextArea log;
	private Container content;
	private JPanel controlPanel, controlPanel1;
	private JList schemeList;
	private JTextArea [] mssArea;
	private ArrayList MSSlist;
	private Network TRnet;
	private Thread runner;
   
    public void init() {
		//create the network
        TRnet = new Network();

        //create some MSS
        //This is not very dynamic, but this also not what the project focuses on either
        MSSlist = new ArrayList();
        MSS cell_a = new MSS("Cell_A", TRnet);
        MSSlist.add(cell_a);

        MSS cell_b = new MSS("Cell_B", TRnet);
        MSSlist.add(cell_b);

        MSS cell_c = new MSS("Cell_C", TRnet);
        MSSlist.add(cell_c);

		//then create some MHs.  Again, not very dynamic but that's not the focus of the project       
        MH mobile_1 = new MH("mobile_1", cell_a);

        MH mobile_2 = new MH("mobile_2", cell_a);

        MH mobile_3 = new MH("mobile_3", cell_a);

        MH mobile_4 = new MH("mobile_4", cell_b);

        MH mobile_5 = new MH("mobile_5", cell_b);

        MH mobile_6 = new MH("mobile_6", cell_b);

        MH mobile_7 = new MH("mobile_7", cell_c);

        MH mobile_8 = new MH("mobile_8", cell_c);

        MH mobile_9 = new MH("mobile_9", cell_c);

        TRnet.update();
		
		content = getContentPane();
		
		startButton = new JButton("Start the Simulation");
		startButton.addActionListener(this);
		
		requestButton = new JButton("Request the Token");
		requestButton.addActionListener(this);
		
		moveButton = new JButton("Move MH");
		moveButton.addActionListener(this);
		
		String[] selections = {"MH's Only","Inform Strategy","Replication Strategy"};
		schemeList = new JList(selections);
		schemeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		schemeList.addListSelectionListener(this);
		
		source = new JTextField("Mobile ID", 10);
		destination = new JTextField("destination", 10);
		
		JLabel label = new JLabel("to");
		
		controlPanel = new JPanel();
		controlPanel.add(startButton);
		controlPanel.add(moveButton);
		controlPanel.add(requestButton);
		controlPanel.add(source);
		controlPanel.add(label);
		controlPanel.add(destination);
		controlPanel.add(schemeList);
		
		content.add(controlPanel, BorderLayout.NORTH);
		
		mssArea = new JTextArea[MSSlist.size()];
		controlPanel1 = new JPanel();
		
		for(int i= 0; i<TRnet.getAllMSSs().size(); i++){
			MSS cell = (MSS)TRnet.getAllMSSs().get(i);
			mssArea[i] = new JTextArea("", 10, 10);
			mssArea[i].setVisible(true);
			mssArea[i].setEditable(false);
			controlPanel1.add(new JScrollPane(mssArea[i]));
		}
		
		updateTextAreas();
		log = new JTextArea("", 10, 10);
		log.setVisible(true);
		content.add(new JScrollPane(log), BorderLayout.CENTER);
        content.add(controlPanel1, BorderLayout.SOUTH);
		
		log.setText("");
    }
	
	// when someone clicks a button
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == moveButton) {
			String movefrom = source.getText();
			String moveto = destination.getText();
			(TRnet.getMHByName(movefrom)).move(TRnet.getMSSByName(moveto), log);
			updateTextAreas();
		}
		else if(e.getSource() == startButton) {
			log.setText("");
		
			if(schemeList.getSelectedIndex() == -1)
				log.append("no strategy selected\n");
			else {
				switch(schemeList.getSelectedIndex()) {
					case MSS.MH_ONLY:
						runner = new Thread(new MHsOnly(TRnet, log));
						runner.start();
						break;
					case MSS.INFORM:
						runner = new Thread(new Inform(TRnet, log));
						runner.start();
						break;
					case MSS.REPLICATION:
						runner = new Thread(new Replication(TRnet, log));
						runner.start();
						break;
				}
			}
		}
		else if(e.getSource() == requestButton) {
			TRnet.getMHByName(source.getText()).request(log);
		}
	}
	
	// when someone selects a scheme from the list
	public void valueChanged(ListSelectionEvent e) {
		TRnet.setAlgorithm(schemeList.getSelectedIndex());
	}
	
	public void updateTextAreas() {
		for(int i= 0; i<TRnet.getAllMSSs().size(); i++){
			mssArea[i].setText("");
			MSS cell = (MSS)TRnet.getAllMSSs().get(i);
			mssArea[i].append(cell.getID() + "\n--------\n");
			
			for (int j=0; j<cell.getLocalMHs().size(); j++) {
				mssArea[i].append( ((MH)cell.getLocalMHs().get(j)).getID() + "\n");
			}
		}
	}
}