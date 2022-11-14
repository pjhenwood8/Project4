import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Project 4 - Message
 * This class creates a message class that contains an id for a message, number of total messages, time, sender,
 * receiver, message string, and booleans if deleted. Contains constructors, getters and setters, and toString method.
 *
 * @author Kevin Zhang, Jalen Mann, Alimzhan Sultanov, Kyle Griffin, and PJ Henwood, lab sec LC2
 *
 * @version November 15, 2022
 *
 */
public class Message {
    private final int id;
    private static int count = 0;
    private final String time;
    private String sender;
    private String receiver;
    private String message;
    private boolean delBySender;
    private boolean delByReceiver;

    public Message(int id, String time, String sender, String receiver, String message, boolean delBySender, boolean delByReceiver) {
        count++;
        this.id = id;
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.delBySender = delBySender;
        this.delByReceiver = delByReceiver;
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
        delByReceiver = false;
        delBySender = false;
    }

    public boolean isDelBySender() {
        return delBySender;
    }

    public boolean isDelByReceiver() {
        return delByReceiver;
    }

    public void setDelBySender(boolean delBySender) {
        this.delBySender = delBySender;
    }

    public void setDelByReceiver(boolean delByReceiver) {
        this.delByReceiver = delByReceiver;
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
        return String.format("%s   (%s -> %s)%n%s%n", time, sender, receiver, message);
    }
}
