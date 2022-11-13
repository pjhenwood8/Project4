/**
 * A class to block other users
 *
 *
 **/


public class Blocking {
    private Buyer buyer;
    private Seller seller;
    private boolean blocked;

    public Blocking(Buyer buyer, Seller seller, boolean blocked){
        this.buyer = buyer;
        this.seller = seller;
        this.blocked = blocked;
    }

    public void block(){
        blocked = true;
    }

    public void unBlock() {
        blocked = false;
    }
}
