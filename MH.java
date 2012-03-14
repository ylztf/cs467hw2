import javax.swing.*;

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
	}

	public void move(MSS destination, JTextArea ta) {
		if(currentCell.getAlgorithm() == MSS.INFORM)
			ta.append(mobileID + " is moved to " + destination.getID() + ". Informing " + currentCell.getID() + " of the move (cost is Cf)\n");
			
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

	public void request(JTextArea ta) {
		currentCell.addRequest(this, ta);
	}

	public void passTokenToPeer(String peerID, MSS peerCell, JTextArea ta)  {
		ta.append("Token passed to " + peerID + " in " + peerCell.getID() + "\n");
	}

	public void tokenUse(JTextArea ta) {
		ta.append("Token received by " + mobileID + " from " + currentCell.getID() + " (cost is Cw)\n");
		ta.append("Using token.\n");
		
		try {
			Thread.sleep(2000); // simulate using token
		} catch(InterruptedException e) {}
		
		ta.append("Token released to " + currentCell.getID() + " (cost is Cw)\n");
	}
}