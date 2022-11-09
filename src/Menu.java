import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    public static void main(String[] args) {
        System.out.println("Login: ");
        System.out.println("Password: ");
    }

    public static void writeMessage(User sender, User receiver, String message) throws SameTypeException, IOException {
        if (sender instanceof Buyer && receiver instanceof Buyer) {
            throw new SameTypeException("Buyers can't write to buyers");
        }
        if (sender instanceof Seller && receiver instanceof Seller) {
            throw new SameTypeException("Sellers can't to sellers");
        }
        PrintWriter pw = new PrintWriter(new FileWriter(new File("messages.csv")), true);

        String[] allValues = new String[4];

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);

        allValues[0] = "\"" + formattedDate + "\"";
        allValues[1] = "\"" + sender.getUsername() + "\"";
        allValues[2] = "\"" + receiver.getUsername() + "\"";
        allValues[3] = "\"" + message + "\"";

        pw.write(String.join(",", allValues) + "\n");
        pw.flush();

    }
}

