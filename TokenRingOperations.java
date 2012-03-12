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

        //TRnet.traverse();
		//MHsOnly algo = new MHsOnly(TRnet);
		//Inform algo = new Inform(TRnet);
		Replication algo = new Replication(TRnet);
    }
}