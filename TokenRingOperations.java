
import java.util.*;

public class TokenRingOperations {
    public static void main(String[] args) {
        new TokenRingOperations();
    }

    public TokenRingOperations() {

        //create the network
        Network TRnet = new Network();

        //create some MSS
        //This is not very dynamic, but this also not what the project focuses on either
        ArrayList MSSlist = new ArrayList();
        MSS cell_a = new MSS("Cell_A", TRnet);
        MSSlist.add(cell_a);

        MSS cell_b = new MSS("Cell_B", TRnet);
        MSSlist.add(cell_b);

        MSS cell_c = new MSS("Cell_C", TRnet);
        MSSlist.add(cell_c);

        //then create some MHs.  Again, not very dynamic but that's not the focus of the project       
        MH mobile_1 = new MH("mobile_1", cell_a);

        MH mobile_2 = new MH("mobile_2", cell_a);

        MH mobile_3 = new MH("mobile_3", cell_a);

        MH mobile_4 = new MH("mobile_4", cell_b);

        MH mobile_5 = new MH("mobile_5", cell_b);

        MH mobile_6 = new MH("mobile_6", cell_b);

        MH mobile_7 = new MH("mobile_7", cell_c);

        MH mobile_8 = new MH("mobile_8", cell_c);

        MH mobile_9 = new MH("mobile_9", cell_c);

        TRnet.update();

        //run a few tests of MSS facilitated token passing
        mobile_1.request();   
        mobile_2.request();
        mobile_2.move(cell_c);
        mobile_8.request();

        TRnet.traverse();
    }

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
            currentCell.localMHs.add(this);
        }

        public void move(MSS destination) {
            currentCell.localMHs.remove(this);
            currentCell = destination;
            register();
        }

        public String getID() {
            return mobileID;
        }

        public void request() {
            currentCell.requestQueue.add(this);
        }

        public void passTokenToPeer(String peerID, MSS peerCell)  {
            System.out.println("Token passed to " + peerID + " in " + peerCell.getID());
        }

        public void tokenUse() {
            System.out.println("Token received by " + mobileID + " from " + currentCell.getID());
            System.out.println("Using token.");
            System.out.println("Token returned to " + currentCell.getID());
        }
    }


    class MSS {
        //chose String for IDs instead of int, so it can be more expressive and user friendly
        private String cellID;
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
            ntwk.allMSS.add(this);
        }

        public ArrayList getLocalMHs() {
            return localMHs;
        }

        public String getID() {
            return cellID;
        }

        public void grantRequest() {
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
    }


    class Network {
        private ArrayList allMH;
        private ArrayList allMSS;

        public Network() {
            allMSS = new ArrayList();
            allMH = new ArrayList();
        }

        public void update() {
            //go through the list of MSS and put their local MHs into allMH
            for ( int i = 0; i < allMSS.size() ; i++) {
                List<MH> locals = ((MSS)allMSS.get(i)).getLocalMHs();
                allMH.addAll(locals);
            }

        }
        public MSS search(String mobileID) {
            for (int i = 0; i < allMSS.size(); i++) {
                ArrayList locals = ((MSS)allMSS.get(i)).getLocalMHs();
                for (int j = 0; j<locals.size(); j++){
                    if(((MH)locals.get(j)).getID() == mobileID) {
                        System.out.println(mobileID + " found in " + ((MSS)allMSS.get(i)).getID());
                        return (MSS)allMSS.get(i);
                    }
                }
            }
            System.out.println(mobileID + " not found");
            return null;
        }

        public void traverse() {
            for (int i = 0; i<allMSS.size(); i++) {
                System.out.println(((MSS)allMSS.get(i)).getID() + "'s turn");
                ((MSS)allMSS.get(i)).grantRequest();
                System.out.println();
            }
        }

    }

}