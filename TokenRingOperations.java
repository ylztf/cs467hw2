import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TokenRingOperations extends JApplet implements ActionListener {
   // public static void main(String[] args) {
   //     new TokenRingOperations();
   // }
   
	private JButton startButton, stopButton, moveButton, requestButton;
	private JTextField source, destination;
	private JTextArea log;
	private Container content;
	private JPanel controlPanel, controlPanel1;
	private JList schemeList;
	private JTextArea [] mssArea;
   
    public void init() {
		//create the network
        Network TRnet = new Network();

        //create some MSS
        //This is not very dynamic, but this also not what the project focuses on either
        ArrayList MSSlist = new ArrayList();
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
		
		stopButton = new JButton("Stop the simulation");
		stopButton.addActionListener(this);
		
		requestButton = new JButton("Request the Token");
		requestButton.addActionListener(this);
		
		moveButton = new JButton("Move MH's");
		moveButton.addActionListener(this);
		
		String[] selections = {"MH's Only","Inform Strategy","Replication Strategy"};
		schemeList = new JList(selections);
		source = new JTextField("Mobile ID", 10);
		destination = new JTextField("destination", 10);
		
		JLabel label = new JLabel("to");
		
		controlPanel = new JPanel();
		controlPanel.add(startButton);
		controlPanel.add(stopButton);
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
			mssArea[i].setRows(10);
			mssArea[i].setColumns(10);
			mssArea[i].setText("");
			mssArea[i].setVisible(true);
			controlPanel1.add(new JScrollPane(mssArea[i]));
			mssArea[i].append(cell.getID());
			for (int j=0; j<cell.getLocalMHs().size(); j++) {
				mssArea[i].append( ((MH)cell.getLocalMHs().get(j)).getID()  );
			}
		}
        content.add(controlPanel1, BorderLayout.CENTER);
		content.add(new JLabel("dummy"), BorderLayout.SOUTH);

        //run a few tests of MSS facilitated token passing
        mobile_1.request();   
        mobile_2.request();
        mobile_2.move(cell_c);
        mobile_8.request();

        //TRnet.traverse();
		//MHsOnly algo = new MHsOnly(TRnet);
		//Inform algo = new Inform(TRnet);
		Replication algo = new Replication(TRnet);
    }
	
	public void actionPerformed(ActionEvent e){}
}