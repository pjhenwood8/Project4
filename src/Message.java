import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private int id;
    private static int count = 0;
    private final String time;
    private String sender;
    private String receiver;
    private String message;

    public Message(int id, String time, String sender, String receiver, String message) {
        count++;
        this.id = id;
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Message(String sender, String receiver, String message) {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.time = myDateObj.format(myFormatObj);
        count++;
        this.id = count;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;

    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        String ans = String.format("%s   (%s -> %s)%n%s%n", time, sender, receiver, message);
        return ans;
    }
}
