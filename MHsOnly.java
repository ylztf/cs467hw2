import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MHsOnly implements Runnable {
	private Network net;
	
	public MHsOnly(Network n) {
		net = n;
		
		// will move this out soon
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(this);
	}
	
	public void run() {
		ArrayList allHosts = net.getAllMHs();
		
		//while(true) {		
			for(int i = 0; i < allHosts.size(); ++i) {
				MH currentHost = (MH)allHosts.get(i);
				currentHost.tokenUse();
				
				MH nextHost;
				if(i + 1 == allHosts.size())
					nextHost = (MH)allHosts.get(0);
				else
					nextHost = (MH)allHosts.get(i + 1);
					
				System.out.println(nextHost.getID() + " is next to use the token");
				
				if(currentHost.getCurrentCell() != nextHost.getCurrentCell()) {
					net.search(nextHost.getID()).getID();
					System.out.println("(cost of search is Cs)");
					System.out.println("moving token to " + nextHost.getCurrentCell().getID() + " (cost is Cf)");
				}
			}
		//}
	}
}	
