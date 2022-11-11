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
        // User logs in
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


        //We show him list of people with whom he had conversations before
        ArrayList<Message> messageHistory;
        String[] listOfUsers = parseUsers(user);
        for (int i = 0; i < listOfUsers.length; i++) {
            System.out.printf("[%d] %s%n", i+1, listOfUsers[i]);
        }
        System.out.printf("[%d] %s%n", 0, "Start new dialog");           // We provide an option to start new dialog
        int receiveUser = Integer.parseInt(scanner.nextLine());          // He makes the choice
        if (receiveUser == 0) {                                          // dialog with new user
            System.out.println("Enter name of user:");
            String newUser = scanner.nextLine();
            System.out.println("Write your hello message first!");
            String mes = scanner.nextLine();
            ArrayList<Message> temp = user.getMessages();
            temp.add(new Message(user.getUsername(), newUser, mes));              // We should check if user exists in the future
            user.setMessages(temp);
            messageHistory = parseMessageHistory(user, newUser);
            for (int i = 0; i < messageHistory.size(); i++) {
                System.out.print(messageHistory.get(i).toString());                     //we print their message history
            }
        }
        else {
            messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
            for (int i = 0; i < messageHistory.size(); i++) {
                System.out.print(messageHistory.get(i).toString());
            }
            System.out.println();
            System.out.println("[1] Write message                         [2] Edit message");
            System.out.println("[3] Delete message                        [0] Exit");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                System.out.println("Enter message: ");
                String mes = scanner.nextLine();
                ArrayList<Message> temp = user.getMessages();
                temp.add(new Message(user.getUsername(), listOfUsers[receiveUser - 1], mes));
                user.setMessages(temp);
                messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                for (int i = 0; i < messageHistory.size(); i++) {
                    System.out.print(messageHistory.get(i).toString());
                }
            }
            if (choice == 2) {
                messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                ArrayList<Message> userIsSender = new ArrayList<>();
                int i = 0;
                while (i < messageHistory.size()) {
                    if (messageHistory.get(i).getSender().equals(user.getUsername())) {
                        userIsSender.add(messageHistory.get(i));
                        System.out.printf("[%d] " + messageHistory.get(i).toString(), i+1);
                        i++;
                    }
                    else
                        System.out.print(messageHistory.get(i).toString());
                }
                System.out.println("Choose message to edit");
                choice = Integer.parseInt(scanner.nextLine());
                System.out.println("To which message you want to change it?");
                String msg = scanner.nextLine();
                Message temp = userIsSender.get(choice-1);
                for (int j = 0; j < messageHistory.size(); j++) {
                    if (messageHistory.get(j).getId() == temp.getId()) {
                        messageHistory.get(j).setMessage(msg);
                    }
                }
                messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                for (int j = 0; j < messageHistory.size(); j++) {
                    System.out.print(messageHistory.get(j).toString());
                }
            }
            if (choice == 3) {
                messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                ArrayList<Message> userIsSender = new ArrayList<>();
                int i = 0;
                while (i < messageHistory.size()) {
                    if (messageHistory.get(i).getSender().equals(user.getUsername())) {
                        userIsSender.add(messageHistory.get(i));
                        System.out.printf("[%d] " + messageHistory.get(i).toString(), i+1);
                        i++;
                    }
                    else
                        System.out.print(messageHistory.get(i).toString());
                }
                System.out.println("Choose message to delete");
                choice = Integer.parseInt(scanner.nextLine());
                Message temp = userIsSender.get(choice-1);
                ArrayList<Message> allUserMessages = user.getMessages();
                for (int j = 0; j < allUserMessages.size(); j++) {
                    if (allUserMessages.get(j).getId() == temp.getId()) {
                        allUserMessages.remove(j);
                        break;
                    }
                }
                messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                for (int j = 0; j < messageHistory.size(); j++) {
                    System.out.print(messageHistory.get(j).toString());
                }
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
}

