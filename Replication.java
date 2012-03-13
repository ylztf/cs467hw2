import java.util.*;
import javax.swing.*;

class Replication implements Runnable {
	private Network net;
	private JTextArea ta;
	
	public Replication(Network n, JTextArea t) {
		net = n;
		ta = t;
	}
	
	public void run() {
		ArrayList allMSSs = net.getAllMSSs();
		MSS currentMSS;
		
		for(int i = 0; i < allMSSs.size(); ++i) {
			currentMSS = (MSS)allMSSs.get(i);
			currentMSS.setAlgorithm(MSS.REPLICATION);
		}
			
		while(true) {
			for(int i = 0; i < allMSSs.size(); ++i) {
				currentMSS = (MSS)allMSSs.get(i);
				
				if(currentMSS.hasPendingRequests()) {
					MH host = (MH)currentMSS.getRequestQueue().get(0);
					ta.append(host.getID() + " is scheduled to get the token next\n");
					if(net.search(host.getID(), ta) != currentMSS)
						ta.append("sending token to " + host.getCurrentCell().getID() + "\n");
					
					host.tokenUse(ta);
					
					net.globalRelease(host);
					ta.append(host.getCurrentCell().getID() + " sends global release to all other MSSs (cost is Cf for each)\n");
				}
			}
		}
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
				ta.append(MHhome + " sends token back to " + mss.getID() + " (cost is Cf)\n");
			}
		}
	}
}	
