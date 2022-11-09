import java.util.ArrayList;

public class Seller extends User {

    private String store;

    public Seller(String username) {
        super(username);
    }
    public void createStore(String storeName) {
        store = storeName;
    }

    public String getStore() {
        return store;
    }

}