import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Inform implements Runnable {
	private Network net;
	
	public Inform(Network n) {
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
			currentMSS.setAlgorithm(MSS.INFORM);
		}
			
		while(true) {
			for(int i = 0; i < allMSSs.size(); ++i) {
				currentMSS = (MSS)allMSSs.get(i);
				
				if(currentMSS.hasPendingRequests()) {
					System.out.println("now servicing requests in " + currentMSS.getID());
					grantRequests(currentMSS);
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
				System.out.println(MHhome.getID() + " sends token back to " + mss.getID() + " (cost is Cf)");
			}
			
			System.out.println();
		}
	}
}	
