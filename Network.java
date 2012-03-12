import java.util.*;

class Network {
	private ArrayList allMH;
	private ArrayList allMSS;

	public Network() {
		allMSS = new ArrayList();
		allMH = new ArrayList();
	}

	public void update() {
		// this is to make sure nothing is left over from the last update
		allMH.clear();
	
		//go through the list of MSS and put their local MHs into allMH
		for ( int i = 0; i < allMSS.size() ; i++) {
			List<MH> locals = ((MSS)allMSS.get(i)).getLocalMHs();
			
			for(int j = 0; j < locals.size(); ++j)
				allMH.add(locals.get(j));
		}

	}
	
	public void addMSS(MSS mss) {
		allMSS.add(mss);
	}
	
	public ArrayList getAllMHs() {
		return allMH;
	}
	
	public ArrayList getAllMSSs() {
		return allMSS;
	}
	
	public void globalRelease(MH host) {
		for(int j = 0; j < allMSS.size(); ++j) {
			MSS m = (MSS)allMSS.get(j);
			m.removeRequest(host);
		}
	}
	
	public MSS search(String mobileID) {
		for (int i = 0; i < allMSS.size(); i++) {
			ArrayList locals = ((MSS)allMSS.get(i)).getLocalMHs();
			for (int j = 0; j<locals.size(); j++){
				if(((MH)locals.get(j)).getID() == mobileID) {
					System.out.println(mobileID + " is in " + ((MSS)allMSS.get(i)).getID());
					return (MSS)allMSS.get(i);
				}
			}
		}
		System.out.println(mobileID + " not found");
		return null;
	}

	/*public void traverse() {
		for (int i = 0; i<allMSS.size(); i++) {
			System.out.println(((MSS)allMSS.get(i)).getID() + "'s turn");
			((MSS)allMSS.get(i)).grantRequest();
			System.out.println();
		}
	}*/

}