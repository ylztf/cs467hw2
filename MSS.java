import java.util.*;

class MSS {
	//chose String for IDs instead of int, so it can be more expressive and user friendly
	private String cellID;
	private int currentAlgorithm = REPLICATION;
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
	
	public void addRequest(MH host) {
		if(currentAlgorithm == INFORM)
			add(host);
		else {
			ArrayList list = ntwk.getAllMSSs();
			MSS m;
			for(int i = 0; i < list.size(); ++i) {
				m = (MSS)list.get(i);
				m.add(host);
			}
				
			System.out.println(cellID + " sending request of " + host.getID() + " to all MSSs (cost of Cf for each)");
			System.out.println("MSSs queue request for " + host.getID() + " and send priority to " + cellID + "(cost of Cf for each)");
			System.out.println(cellID + " sending priority of " + requestQueue.indexOf(host) + " to all MSSs (cost of Cf for each)");
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
	
	public void setAlgorithm(int a) {
		currentAlgorithm = a;
	}

	public void grantRequests() {
		boolean local = false;
		if(requestQueue.isEmpty() == false) {
			grantingQueue = requestQueue;
			requestQueue = new ArrayList();
			for (int i = 0; i<grantingQueue.size(); i++) {
				local = false;
				System.out.println("Processing request from " + ((MH)grantingQueue.get(i)).getID());
				for (int j = 0; j< localMHs.size(); j++) {
					if (grantingQueue.get(i).equals(localMHs.get(j))) {
						System.out.println("Only local search is needed ");
						((MH)grantingQueue.get(i)).tokenUse();
						local = true;                           
					}
				}
				if (!local) {
					System.out.println("Wide area search is needed ");
					MSS location = ntwk.search(((MH)grantingQueue.get(i)).getID());
					tokenPassFixedLink(location);
					((MH)grantingQueue.get(i)).tokenUse();
					location.tokenPassFixedLink(this);
				}
				System.out.println();
			}
		}
	}

	void tokenPassFixedLink(MSS destination) {
		System.out.println("Passing token from " + cellID + " to " + destination.getID());
	}
	
	// these are so the MSSs know how to handle requests
	public static final int INFORM = 0;
	public static final int REPLICATION = 1;
}