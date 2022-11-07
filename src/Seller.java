import java.util.ArrayList;

public class Seller extends User{

    private String store;

    public Seller(String username, String password) {
        super(username, password);
    }

    public void createMessage(Buyer buyer, String message) {
        if (buyer != null) {
            addMessage(message);
            buyer.addMessage(message);
        }
    }

    public void createMessage(String name, String message, ArrayList<Buyer> buyers) {
        for (Buyer b : buyers) {
            if (b.getUsername().equalsIgnoreCase(name)) {
                addMessage(message);
                b.addMessage(message);
            }
        }
    }

    public void editMessage(String message) {
        int index = getMessages().indexOf(message);
        getMessages().remove(message);
        getMessages().add(index, message);
        // Edit buyer message
    }

    public void createStore(String storeName) {
        store = storeName;
    }

    public String getStore() {
        return store;
    }
}
