import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<User> users = new ArrayList<>();
        //users.add(new User("Den"));
        //System.out.println(users.get(0).getUsername());
        //System.out.println(users.get(0).getMessages());
        boolean loggedIn = false;
        boolean online = true;
        /** MENU EXAMPLE
        while (online) {
            while (!loggedIn) {
                System.out.println("[1] Create Account\n[2] Login\n[3] Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();
                String username = "";
                String password = "";
                switch (choice) {
                    case 1:
                        System.out.println("Create username: ");
                        username = scanner.nextLine();
                        System.out.println("Create password: ");
                        password = scanner.nextLine();
                        System.out.println("Are you a [1] buyer or [2] seller");
                        choice = scanner.nextInt();
                        scanner.nextLine();
                        if (choice == 1) {
                            users.add(new Buyer(username, password));
                        } else if (choice == 2) {
                            users.add(new Seller(username, password));
                        }
                        System.out.println("Account successfully created!");
                        break;
                    case 2:
                        System.out.println("Login: ");
                        username = scanner.nextLine();
                        System.out.println("Password: ");
                        password = scanner.nextLine();
                        for (User u : users) {
                            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                                loggedIn = true;
                                System.out.println("Successfully Logged in");
                            }
                        }
                        break;
                    case 3:
                        online = false;
                        break;
                }
                if (!online) {
                    break;
                }

            }
        } **/
        //some login stuff functionality
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        boolean buy = false;
        User user;
        if (buy)
            user = new Buyer(username, password);
        else
            user = new Seller(username, password);
        String[] listOfUsers = parseUsers(user);
        for (int i = 0; i < listOfUsers.length; i++) {
            System.out.printf("[%d] %s%n", i+1, listOfUsers[i]);
        }
        System.out.printf("[%d] %s%n", 0, "Start new dialog");
        int receiveUser = Integer.parseInt(scanner.nextLine());
        ArrayList<Message> messageHistory = parseMessageHistory(user, listOfUsers[receiveUser-1]);
        for (int i = 0; i < messageHistory.size(); i++) {
            System.out.printf("%s  (%s -> %s)%n", messageHistory.get(i).getTime(),messageHistory.get(i).getSender(),messageHistory.get(i).getReceiver());
            System.out.println(messageHistory.get(i).getMessage());
        }
        System.out.println();
        System.out.println("[1] Write message                         [2] Edit message");
        System.out.println("[3] Delete message                        [0] Exit");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1) {
            System.out.println("Enter message: ");
            String mes = scanner.nextLine();
            ArrayList<Message> temp = user.getMessages();
            temp.add(new Message(user.getUsername(), listOfUsers[receiveUser-1], mes));
            messageHistory = parseMessageHistory(user, listOfUsers[receiveUser-1]);
            for (int i = 0; i < messageHistory.size(); i++) {
                System.out.printf("%s  (%s -> %s)%n", messageHistory.get(i).getTime(),messageHistory.get(i).getSender(),messageHistory.get(i).getReceiver());
                System.out.println(messageHistory.get(i).getMessage());
            }
        }
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

