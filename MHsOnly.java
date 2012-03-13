import java.util.*;
import javax.swing.*;

class MHsOnly implements Runnable {
	private Network net;
	JTextArea ta;
	
	public MHsOnly(Network n, JTextArea t) {
		net = n;
		ta = t;
	}
	
	public void run() {
		ArrayList allHosts = net.getAllMHs();
		
		//while(true) {		
			for(int i = 0; i < allHosts.size(); ++i) {
				MH currentHost = (MH)allHosts.get(i);
				currentHost.tokenUse(ta);
				
				MH nextHost;
				if(i + 1 == allHosts.size())
					nextHost = (MH)allHosts.get(0);
				else
					nextHost = (MH)allHosts.get(i + 1);
					
				ta.append(nextHost.getID() + " is next to use the token\n");
				
				if(currentHost.getCurrentCell() != nextHost.getCurrentCell()) {
					net.search(nextHost.getID(), ta).getID();
					ta.append("(cost of search is Cs)\n");
					ta.append("moving token to " + nextHost.getCurrentCell().getID() + " (cost is Cf)\n");
				}
			}
		//}
	}
}	
