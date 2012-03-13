import java.util.*;
import javax.swing.*;

class Inform implements Runnable {
	private Network net;
	private JTextArea ta;
	
	public Inform(Network n, JTextArea t) {
		net = n;
		ta = t;
	}
	
	public void run() {
		ArrayList allMSSs = net.getAllMSSs();
		MSS currentMSS;
		
		for(int i = 0; i < allMSSs.size(); ++i) {
			currentMSS = (MSS)allMSSs.get(i);
			currentMSS.setAlgorithm(MSS.INFORM);
		}
			
		//while(true) {
			for(int i = 0; i < allMSSs.size(); ++i) {
				currentMSS = (MSS)allMSSs.get(i);
				
				if(currentMSS.hasPendingRequests()) {
					ta.append("now servicing requests in " + currentMSS.getID() + "\n");
					grantRequests(currentMSS);
				}
			}
		//}
	}
	
	public void grantRequests(MSS mss) {
		mss.prepareRequests();
		ArrayList grantingQueue = mss.getGrantingQueue();
		
		for (int i = 0; i < grantingQueue.size(); i++) {
			MH currentMH = (MH)grantingQueue.get(i);
			ta.append(mss.getID() + " is processing request from " + ((MH)grantingQueue.get(i)).getID() + "\n");
			
			MSS MHhome = net.search(currentMH.getID(), ta);
			if(MHhome == mss) { // local
				ta.append("no forwarding needed\n");
				currentMH.tokenUse(ta);                           
			} else {
				ta.append("need to forward token to " + MHhome.getID() + " (cost is Cf)\n");
				currentMH.tokenUse(ta);
				ta.append(MHhome.getID() + " sends token back to " + mss.getID() + " (cost is Cf)\n");
			}
		}
	}
}	
