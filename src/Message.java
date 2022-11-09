import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private final String time;
    private User sender;
    private User receiver;
    private String message;

    public Message(String time, User sender, User receiver, String message) {
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Message(User sender, User receiver, String message) {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.time = myDateObj.format(myFormatObj);
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
