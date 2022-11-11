import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<User> users = readUsers("login.csv");
        User currUser = null;
        /*for (User u : users) {
            if (u instanceof Buyer) {
                System.out.println("Buyer:");
            } else {
                System.out.println("Seller:");
            }
            System.out.println(u.getUsername());
            System.out.println(u.getPassword());
        } */
        //ArrayList<User> users = new ArrayList<>();
        //User u1 = new User("tfh", "pass");
        //User currUser = new User("user1", "password");
        //users.add(currUser);
        //users.add(new User("Den"));
        //System.out.println(users.get(0).getUsername());
        //System.out.println(users.get(0).getMessages());
        boolean loggedIn = false;
        boolean online = true;
        /* MENU EXAMPLE */
        /*while (online) {
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
                        System.out.println("username: ");
                        username = scanner.nextLine();
                        System.out.println("Password: ");
                        password = scanner.nextLine();
                        for (User u : users) {
                            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                                loggedIn = true;
                                System.out.println("Successfully Logged in");
                                currUser = u;

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
        } */
        /* Edit or delete User */
        /*System.out.println(currUser.getUsername());
        System.out.println(currUser.getPassword());
        while (online) {
            System.out.println("[3] Account");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 3) {
                    do {
                        System.out.println("[1] Edit Account\n[2] Delete Account\n[3] Exit");
                        choice = scanner.nextInt();
                        if (choice > 3) {
                            throw new InputMismatchException();
                        }
                        switch (choice) {
                            case 1:
                                scanner.nextLine();
                                System.out.println("[1] Change Username\n[2] Change Password\n[3] Exit");
                                choice = scanner.nextInt();
                                String newAccountInfo = "";
                                if (choice > 3) {
                                    throw new InputMismatchException();
                                }
                                switch (choice) {
                                    case 1 -> {
                                        scanner.nextLine();
                                        System.out.println("Enter new username:");
                                        newAccountInfo = scanner.nextLine();
                                        currUser.setUsername(newAccountInfo);
                                        System.out.printf("Username changed to: %s%n", newAccountInfo);
                                    }
                                    case 2 -> {
                                        scanner.nextLine();
                                        System.out.println("Enter new password:");
                                        newAccountInfo = scanner.nextLine();
                                        currUser.setPassword(newAccountInfo);
                                        System.out.printf("Password changed to: %s%n", newAccountInfo);
                                    }
                                    case 3 -> scanner.nextLine();
                                }
                                break;
                            case 2:
                                scanner.nextLine();
                                System.out.println("Are you sure you want to delete this user? [Y/N]");
                                String yesNo = scanner.nextLine();
                                if (yesNo.equalsIgnoreCase("Y")) {
                                    System.out.printf("User [%s] successfully deleted%n", currUser.getUsername());
                                    currUser.removeUser();
                                    users.remove(currUser);
                                    choice = 3;
                                }
                                break;
                            case 3:
                                break;
                        }
                    } while (choice != 3);
                    System.out.println(currUser.getUsername());
                    System.out.println(currUser.getPassword());
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
                scanner.nextLine();
            }
        } */

        /* User logs in */
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

    /*
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
    } */

    public static ArrayList<User> readUsers(String filename) throws FileNotFoundException {
        File f = new File(filename);
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader bfr = null;
        ArrayList<User> users = new ArrayList<>();
        if (!f.exists()) {
            throw new FileNotFoundException();
        } else {
            try {
                bfr = new BufferedReader(new FileReader(f));
                String read = bfr.readLine();
                while (read != null) {
                    lines.add(read);
                    read = bfr.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (bfr != null) {
                        bfr.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        for (String line : lines) {
            String userString = line;
            String username = userString.substring(0, userString.indexOf(','));
            userString = userString.substring(userString.indexOf(',') + 1);
            String password = userString.substring(0, userString.indexOf(','));
            userString = userString.substring(userString.indexOf(',') + 1);
            boolean isBuyer = userString.equalsIgnoreCase("b");
            if (isBuyer) {
                users.add(new Buyer(username, password));
            } else {
                users.add(new Seller(username, password));
            }
        }
        return users;
    }

    public static void writeUsers(String filename, ArrayList<User> users) {
        File f = new File(filename);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(f, false));
            for (User u : users) {
                if (u instanceof Buyer) {
                    pw.println(u.getUsername() + ',' + u.getPassword() + ",b");
                } else {
                    pw.println(u.getUsername() + ',' + u.getPassword() + ",s");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}

