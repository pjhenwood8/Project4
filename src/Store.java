import java.util.ArrayList;

public class Store {

    private String storeName;
    
    private int messagesReceived;

    private ArrayList<Buyer> userMessaged = new ArrayList<>();

    public Store(String storeName) {
        this.storeName = storeName;
    }

    public Store(String storeName, int messagesReceived) {
        this.storeName = storeName;
        this.messagesReceived = messagesReceived;
    }

    public Store(String storeName, int messagesReceived, ArrayList<Buyer> userMessaged) {
        this.storeName = storeName;
        this.messagesReceived = messagesReceived;
        this.userMessaged = userMessaged;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getMessagesReceived() {
        return messagesReceived;
    }

    public ArrayList<Buyer> getUserMessaged() {
        return userMessaged;
    }

    public void addMessagesReceived() {
        messagesReceived++;
    }

    public void addUserMessaged(Buyer b) {
        userMessaged.add(b);
    }
}
