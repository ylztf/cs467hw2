class MH {
	//chose String for IDs instead of int, so it can be more expressive and user friendly
	private String mobileID;
	private MSS currentCell;
	private boolean dozing;
	private boolean connected;

	public MH(String id, MSS cell) {
		mobileID = id;
		currentCell = cell;
		dozing = false;
		connected = true;
		register();
	}

	public void register() {
		currentCell.getLocalMHs().add(this);
		currentCell.update();
	}

	public void move(MSS destination) {
		currentCell.getLocalMHs().remove(this);
		currentCell = destination;
		register();
	}

	public String getID() {
		return mobileID;
	}
	
	public MSS getCurrentCell() {
		return currentCell;
	}

	public void request() {
		currentCell.addRequest(this);
	}

	public void passTokenToPeer(String peerID, MSS peerCell)  {
		System.out.println("Token passed to " + peerID + " in " + peerCell.getID());
	}

	public void tokenUse() {
		System.out.println("Token received by " + mobileID + " from " + currentCell.getID() + " (cost is Cw)");
		System.out.println("Using token.");
		
		try {
			Thread.sleep(2000); // simulate using token
		} catch(InterruptedException e) {}
		
		System.out.println("Token released to " + currentCell.getID() + " (cost is Cw)");
	}
}