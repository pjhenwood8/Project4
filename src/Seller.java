import java.io.IOException;
import java.util.ArrayList;

public class Seller extends User {

    private ArrayList<String> stores;

    public Seller(String username, String email, String password) {
        super(username, email, password);
    }

    public Seller (String username, String email, String password, ArrayList<String> blockedUsernames) {
        super(username, email, password, blockedUsernames);
    }
    public void createStore(String storeName) {
        stores.add(storeName);
    }

    public void deleteStore(String storeName) {
        if (stores.contains(storeName)) {
            stores.remove(storeName);
        } else {
            throw new IllegalArgumentException(String.format("%s is not this user store", storeName));
        }
    }

    public ArrayList<String> getStore() {
        return stores;
    }

}