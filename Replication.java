import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Replication implements Runnable {
	private Network net;
	
	public Replication(Network n) {
		net = n;
		
		// will move this out soon
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(this);
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
					System.out.println(host.getID() + " is scheduled to get the token next");
					if(net.search(host.getID()) != currentMSS)
						System.out.println("sending token to " + host.getCurrentCell().getID());
					
					host.tokenUse();
					
					net.globalRelease(host);
					System.out.println(host.getCurrentCell().getID() + " sends global release to all other MSSs (cost is Cf for each)");
				}
			}
		}
	}
	
	public void grantRequests(MSS mss) {
		mss.prepareRequests();
		ArrayList grantingQueue = mss.getGrantingQueue();
		
		for (int i = 0; i < grantingQueue.size(); i++) {
			MH currentMH = (MH)grantingQueue.get(i);
			System.out.println(mss.getID() + " is processing request from " + ((MH)grantingQueue.get(i)).getID());
			
			MSS MHhome = net.search(currentMH.getID());
			if(MHhome == mss) { // local
				System.out.println("no forwarding needed");
				currentMH.tokenUse();                           
			} else {
				System.out.println("need to forward token to " + MHhome.getID() + " (cost is Cf)");
				currentMH.tokenUse();
				System.out.println(MHhome + " sends token back to " + mss.getID() + " (cost is Cf)");
			}
			
			System.out.println();
		}
	}
}	
