import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.*;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        /* Test to update login.csv*/
        ArrayList<User> users = readUsers("login.csv");/*
        addBlockedUsers(users);
        User newUser = new Buyer("Buyer3", "email4@email.com", "password4");
        users.add(newUser);
        writeUsers("login.csv", users); */


        boolean loggedIn = false;
        boolean online = true;

        /* Edit or delete User */
        User currUser = new Buyer("user1", "email@email.com", "password");
        users.add(currUser);
        System.out.println(currUser.getUsername());
        System.out.println(currUser.getPassword());
        if (currUser != null) {
                if (currUser instanceof Buyer) {
                    System.out.println("[3] Account");
                } else {
                    System.out.println("[3] Account\n[4] Create New Store\n[5] Delete Store");
                }

                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 3:
                            do {
                                System.out.println("[1] Edit Account\n[2] Delete Account\n[3] Exit");
                                choice = scanner.nextInt();
                                if (choice > 3) {
                                    throw new InputMismatchException();
                                }
                                switch (choice) {
                                    case 1:
                                        scanner.nextLine();
                                        System.out.println("[1] Change Email\n[2] Change Password\n[3] Exit");
                                        choice = scanner.nextInt();
                                        String newAccountInfo;
                                        if (choice > 3) {
                                            throw new InputMismatchException();
                                        }
                                        switch (choice) {
                                            case 1 -> {
                                                scanner.nextLine();
                                                System.out.println("Enter new email:");
                                                newAccountInfo = scanner.nextLine();
                                                currUser.setEmail(newAccountInfo);
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
                            break;
                        case 4:
                            if (currUser instanceof Buyer) {
                                break;
                            } else if (currUser instanceof Seller) {
                                System.out.println("Enter name for new store");
                                String storeName = scanner.nextLine();
                                ((Seller) currUser).createStore(storeName);
                                break;
                            }
                        case 5:
                            if (currUser instanceof Buyer) {
                                break;
                            } else if (currUser instanceof Seller) {
                                System.out.println("Enter store name to delete");
                                String storeName = scanner.nextLine();
                                try {
                                    ((Seller) currUser).deleteStore(storeName);
                                } catch (IllegalArgumentException il) {
                                    System.out.println(il.getMessage());
                                }
                                break;
                            }
                        default:
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input");
                    scanner.nextLine();
                }
            }

        /* User logs in */
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        boolean buy = false;
        User user;
        if (buy)
            user = new Buyer(username, email, password);
        else
            user = new Seller(username, email, password);


        //We show him list of people with whom he had conversations before
        boolean insideMessageHistory = true;
        boolean insideUsersList = true;
        while (insideUsersList) {
            ArrayList<Message> messageHistory;
            String[] listOfUsers = parseUsers(user);
            for (int i = 0; i < listOfUsers.length; i++) {
                System.out.printf("[%d] %s%n", i + 1, listOfUsers[i]);
            }
            System.out.printf("[%d] %s%n", 0, "Start new dialog");           // We provide an option to start new dialog
            System.out.printf("[%d] %s%n", -1, "Exit");
            int receiveUser = Integer.parseInt(scanner.nextLine());          // He makes the choice
            if (receiveUser == -1) {
                break;
            }
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
            } else {
                while (insideMessageHistory) {
                    messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                    for (int i = 0; i < messageHistory.size(); i++) {
                        System.out.print(messageHistory.get(i).toString());
                    }
                    System.out.println();
                    System.out.println("[1] Write message                         [2] Edit message");
                    System.out.println("[3] Delete message                        [0] Exit");
                    int optionChoice = Integer.parseInt(scanner.nextLine());
                    if (optionChoice == 1) {
                        System.out.println("Enter message: ");
                        String mes = scanner.nextLine();
                        ArrayList<Message> temp = user.getMessages();
                        temp.add(new Message(user.getUsername(), listOfUsers[receiveUser - 1], mes));
                        user.setMessages(temp);
                    }
                    if (optionChoice == 2) {
                        messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                        ArrayList<Message> userIsSender = new ArrayList<>();
                        int i = 0;
                        while (i < messageHistory.size()) {
                            if (messageHistory.get(i).getSender().equals(user.getUsername())) {
                                userIsSender.add(messageHistory.get(i));
                                System.out.printf("[%d] " + messageHistory.get(i).toString(), i + 1);
                                i++;
                            } else
                                System.out.print(messageHistory.get(i).toString());
                        }
                        System.out.println("Choose message to edit");
                        int choice = Integer.parseInt(scanner.nextLine());
                        System.out.println("To which message you want to change it?");
                        String msg = scanner.nextLine();
                        Message temp = userIsSender.get(choice - 1);
                        for (int j = 0; j < messageHistory.size(); j++) {
                            if (messageHistory.get(j).getId() == temp.getId()) {
                                messageHistory.get(j).setMessage(msg);
                            }
                        }
                    }
                    if (optionChoice == 3) {
                        messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                        ArrayList<Message> userIsSender = new ArrayList<>();
                        int i = 0;
                        while (i < messageHistory.size()) {
                            userIsSender.add(messageHistory.get(i));
                            System.out.printf("[%d] " + messageHistory.get(i).toString(), i + 1);
                            i++;
                        }
                        System.out.println("Choose message to delete");
                        int choice = Integer.parseInt(scanner.nextLine());
                        Message temp = userIsSender.get(choice - 1);
                        ArrayList<Message> allUserMessages = user.getMessages();
                        for (int j = 0; j < allUserMessages.size(); j++) {
                            if (allUserMessages.get(j).getId() == temp.getId()) {
                                if (temp.getSender().equals(user.getUsername()))
                                    allUserMessages.get(j).setDelBySender(true);
                                else
                                    allUserMessages.get(j).setDelByReceiver(true);
                                user.setMessages(allUserMessages);
                                break;
                            }
                        }
                        user.refreshMessages();
                    }
                    if (optionChoice == 0) {
                        //insideMessageHistory = false;
                        break;
                    }
                }
            }
        }
        saveMessages(user);
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
            if (!line.isEmpty()) {
                ArrayList<String> user = customSplitSpecific(line);
                String username = user.get(0);
                String email = user.get(1);
                String password = user.get(2);
                boolean isBuyer = user.get(3).equalsIgnoreCase("b");
                String blockedUsers = user.get(4);
                ArrayList<String> blockedUsernames = new ArrayList<>();
                do {

                    if (!blockedUsers.contains(",")) {
                        blockedUsernames.add(blockedUsers);
                        blockedUsers = "";
                    } else {
                        blockedUsernames.add(blockedUsers.substring(0, blockedUsers.indexOf(",")));
                        blockedUsers = blockedUsers.substring(blockedUsers.indexOf(",") + 1);
                    }
                } while (!blockedUsers.isEmpty());
                if (isBuyer) {
                    users.add(new Buyer(username, email, password, blockedUsernames));
                } else {
                    users.add(new Seller(username, email, password, blockedUsernames));
                }
            }
        }
        return users;
    }

    public static void writeUsers(String filename, ArrayList<User> users) {
        File f = new File(filename);
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(f, false))) {
            for (User u : users) {
                pw.print("\"" + u.getUsername() + "\",\"" + u.getEmail() + "\",\"" + u.getPassword());
                if (u instanceof Buyer) {
                    pw.print("\",\"b\",\"");
                } else {
                    pw.print("\",\"s\",\"");
                }
                if (u.getBlockedUsers().size() > 0) {
                    ArrayList<User> blockedUsers = u.getBlockedUsers();
                    ArrayList<String> blockedUsernames = new ArrayList<>();
                    for (User bUser : blockedUsers) {
                        blockedUsernames.add(bUser.getUsername());
                    }
                    for (int i = 0; i < blockedUsernames.size(); i++) {
                        if (i != blockedUsers.size() - 1) {
                            pw.print(blockedUsernames.get(i) + ",");
                        } else {
                            pw.print(blockedUsernames.get(i) + "\"");
                        }
                    }
                } else {
                    pw.print("\"");
                }
                pw.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> customSplitSpecific(String s)
    {
        ArrayList<String> words = new ArrayList<>();
        boolean notInsideComma = true;
        int start =0, end=0;
        for(int i=0; i<s.length()-1; i++)
        {
            if(s.charAt(i)==',' && notInsideComma)
            {
                words.add(s.substring(start + 1,i - 1));
                start = i+1;
            }
            else if(s.charAt(i)=='"')
                notInsideComma=!notInsideComma;
        }
        words.add(s.substring(start + 1, s.length() - 1));
        return words;
    }

    public static void addBlockedUsers(ArrayList<User> users) {
        for (User u : users) {
            ArrayList<String> blockedUsernames = u.getBlockedUsernames();
            for (String bUser : blockedUsernames) {
                u.blockUser(bUser, users);
            }
        }
    }


    public static void saveMessages(User user) throws IOException {
        ArrayList<Message> allMessages = user.getMessages();
        ArrayList<String> temp = new ArrayList<>();
        BufferedReader bfr = new BufferedReader(new FileReader(new File("messages.csv")));
        String st;
        while ((st = bfr.readLine())!=null) {
            ArrayList<String> mesInfo = user.customSplitSpecific(st);
            if (!(mesInfo.get(2).equals("\"" + user.getUsername() + "\"") || mesInfo.get(3).equals("\"" + user.getUsername()+ "\"")))
                temp.add(st);
        }

        PrintWriter pw = new PrintWriter(new FileOutputStream(new File("messages.csv"),false));
        for (int i = 0; i < temp.size(); i++) {
            pw.write(temp.get(i));
            pw.println();
            pw.flush();
        }
        for (int i = 0; i < allMessages.size(); i++) {
            Message msg = allMessages.get(i);
            String ans = String.format("\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"", msg.getId(), msg.getTime(), msg.getSender(), msg.getReceiver(), msg.getMessage(), msg.isDelBySender(), msg.isDelByReceiver());
            pw.write(ans);
            pw.println();
            pw.flush();
        }
    }
}

