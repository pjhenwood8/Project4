import java.util.ArrayList;

public class Buyer extends User{
    public Buyer(String username, String password) {
        super(username, password);
    }

    public void createMessage(Seller seller, String message) {
        if (seller != null) {
            this.addMessage(message);
            seller.addMessage(message);
        }
    }

    public void createMessage(String name, String message, ArrayList<Seller> sellers) {
        for (Seller s : sellers) {
            if (s.getUsername().equalsIgnoreCase(name)) {
                this.addMessage(message);
                s.addMessage(message);
            }
        }
    }

    public void editMessage(String message) {
        int index = getMessages().indexOf(message);
        getMessages().remove(message);
        getMessages().add(index, message);
        // Edit Seller message
    }
}
