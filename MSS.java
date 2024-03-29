import java.util.*;
import javax.swing.*;

class MSS {
	//chose String for IDs instead of int, so it can be more expressive and user friendly
	private String cellID;
	private int currentAlgorithm = MH_ONLY;
	private ArrayList localMHs;
	private Network ntwk;
	//when MSS receives a request, it puts it in the request queue.  When it's the MSS's turn to pass
	//out tokens, it moves all requests in the requestingQueue to grantingQueue.  Any new requests arriving
	//after this is put into requestQuene and not considered until next round.
	private ArrayList requestQueue;   
	private ArrayList grantingQueue;

	public MSS(String id, Network wholeNet) {
		cellID = id;
		localMHs = new ArrayList();
		requestQueue = new ArrayList();
		grantingQueue = new ArrayList();
		ntwk = wholeNet;
		ntwk.addMSS(this);
	}

	public ArrayList getLocalMHs() {
		return localMHs;
	}

	public String getID() {
		return cellID;
	}
	
	public void addRequest(MH host, JTextArea ta) {
		if(!requestQueue.contains(host)) {
			if(currentAlgorithm == MH_ONLY) 
				return;
			
			ta.append(host.getID() + " sent request to " + cellID + " (cost is Cw)\n");
			
			if(currentAlgorithm == INFORM) {
				add(host);
			}
			else if(currentAlgorithm == REPLICATION){
				ArrayList list = ntwk.getAllMSSs();
				MSS m;
				for(int i = 0; i < list.size(); ++i) {
					m = (MSS)list.get(i);
					m.add(host);
				}
				
				ta.append(cellID + " sending request of " + host.getID() + " to all MSSs (cost of Cf for each)\n");
				ta.append("MSSs queue request for " + host.getID() + " and send priority to " + cellID + "(cost of Cf for each)\n");
				ta.append(cellID + " sending priority of " + requestQueue.indexOf(host) + " to all MSSs (cost of Cf for each)\n");
			}
		}
	}
	
	public void add(MH host) {
		requestQueue.add(host);
	}
	
	public boolean hasPendingRequests() {
		return !requestQueue.isEmpty();
	}
	
	public ArrayList getRequestQueue() {
		return requestQueue;
	}
	
	public void removeRequest(MH host) {
		requestQueue.remove(host);
	}
	
	public ArrayList getGrantingQueue() {
		return grantingQueue;
	}
	
	public void prepareRequests() {
		grantingQueue = (ArrayList)requestQueue.clone();
		requestQueue.clear();
	}
	
	public int getAlgorithm() {
		return currentAlgorithm;
	}
	
	public void setAlgorithm(int a) {
		currentAlgorithm = a;
	}

	public void grantRequests(JTextArea ta) {
		boolean local = false;
		if(requestQueue.isEmpty() == false) {
			grantingQueue = requestQueue;
			requestQueue = new ArrayList();
			for (int i = 0; i<grantingQueue.size(); i++) {
				local = false;
				ta.append("Processing request from " + ((MH)grantingQueue.get(i)).getID() + "\n");
				for (int j = 0; j< localMHs.size(); j++) {
					if (grantingQueue.get(i).equals(localMHs.get(j))) {
						ta.append("Only local search is needed \n");
						((MH)grantingQueue.get(i)).tokenUse(ta);
						local = true;                           
					}
				}
				if (!local) {
					ta.append("Wide area search is needed \n");
					MSS location = ntwk.search(((MH)grantingQueue.get(i)).getID(), ta);
					tokenPassFixedLink(location, ta);
					((MH)grantingQueue.get(i)).tokenUse(ta);
					location.tokenPassFixedLink(this, ta);
				}
				System.out.println();
			}
		}
	}

	void tokenPassFixedLink(MSS destination, JTextArea ta) {
		ta.append("Passing token from " + cellID + " to " + destination.getID() + "\n");
	}
	
	// these are so the MSSs know how to handle requests
	public static final int INFORM = 1;
	public static final int REPLICATION = 2;
	public static final int MH_ONLY = 0;
}