import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Den"));
        System.out.println(users.get(0).getUsername());
        System.out.println(users.get(0).getMessages());
        /**
         * users.add(new Seller("seller"));
         * users.add(new Buyer("buyer"));
         */
        System.out.println("[1] Create Account\n[2] Login");

        System.out.println("Login: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        //some login stuff functionality
        boolean buy = false;
        User user;
        if (buy)
            user = new Buyer(username);
        else
            user = new Seller(username);
        String[] listOfUsers = parseUsers(user);
        for (int i = 0; i < listOfUsers.length; i++) {
            System.out.printf("[%d] %s%n", i+1, listOfUsers[i]);
        }
        System.out.printf("[%d] %s%n", 0, "Start new dialog");
        int choice = Integer.parseInt(scanner.nextLine());
        ArrayList<Message> messageHistory = parseMessageHistory(user, listOfUsers[choice-1]);


    }

    public static String[] parseUsers(User user) {
        ArrayList<Message> messages = user.getMessages();
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getSender().equals(user.getUsername())) {
                if (!temp.contains(messages.get(i).getReceiver()))
                    temp.add(messages.get(i).getReceiver());
            }
            else {
                if (!temp.contains(messages.get(i).getSender()))
                    temp.add(messages.get(i).getSender());
            }
        }
        String[] answer = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            answer[i] = temp.get(i);
        }
        return answer;
    }

    public static ArrayList<Message> parseMessageHistory(User mainClient, String thirdParty) {
        ArrayList<Message> messages = mainClient.getMessages();
        ArrayList<Message> temp = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getSender().equals(thirdParty) || messages.get(i).getReceiver().equals(thirdParty)) {
                temp.add(messages.get(i));
            }
        }
        return temp;
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

