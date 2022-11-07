import java.util.ArrayList;

public class User {
    private String username;
    private String password;

    private ArrayList<String> messages;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void deleteMessage(String message) {
        messages.remove(message);
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void blockUser() {

    }

}
