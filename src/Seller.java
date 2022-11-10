import java.io.IOException;
import java.util.ArrayList;

public class Seller extends User {

    private ArrayList<String> stores;

    public Seller(String username, String password) {
        super(username, password);
    }
    public void createStore(String storeName) {
        stores.add(storeName);
    }

    public ArrayList<String> getStore() {
        return stores;
    }

}