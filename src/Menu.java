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
        boolean LoggingIn = true;
        String response;
        User user = null;
        User currUser = null;
        ArrayList<User> users = readUsers("login.csv");
        addBlockedUsers(users);
        System.out.println("Please enter the number corresponding with your option");
        while(LoggingIn) {
            System.out.println("1. Login\n2. Create Account\n3. Exit");
            response = scanner.nextLine();
            switch (response) {
                case "1":
                    user = login(scanner);
                    if(user != null)
                        LoggingIn = false;
                    break;
                case "2":
                    user = createAccount(scanner);
                    if(user != null) {
                        LoggingIn = false;
                        currUser = user;
                        users.add(user);
                    }
                    break;
                case "3":
                    user = null;
                    LoggingIn = false;
                    break;
                default:
                    System.out.println("Please enter a valid input");
                    user = null;
            }
        }
        System.out.println("Finished");
        if(user != null) {
            for (User u : users) {
                if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
                    currUser = u;
                }
            }
        }

        boolean online = true;
        while (online) {
            if (currUser != null) {
                try {
                    System.out.println("[1] Messages\n[2] Statistics\n[3] Account\n[0] Exit");
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 1:
                            System.out.printf("%s - Message Log%n", currUser.getUsername());
                            System.out.println("--------------");
                            if (currUser instanceof Seller) {
                                while (true) {
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
                                        boolean flag = true;
                                        boolean flag1 = true;
                                        for (int i = 0; i < users.size(); i++) {
                                            if (users.get(i).getUsername().equals(newUser)) {
                                                flag1 = false;
                                                if (users.get(i) instanceof Seller && currUser instanceof Seller) {
                                                    System.out.println("You can't write to Seller, because you are Seller yourself");
                                                    flag = false;
                                                }
                                                if (users.get(i) instanceof Buyer && currUser instanceof Buyer) {
                                                    System.out.println("You can't write to Buyer, because you are Buyer yourself");
                                                    flag = false;
                                                }
                                            }
                                        }
                                        if (flag1) {
                                            System.out.println("USER DOES NOT EXIST");
                                        } else if (flag) {
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
                                    } else {
                                        while (true) {
                                            messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                                            for (int i = 0; i < messageHistory.size(); i++) {
                                                System.out.print(messageHistory.get(i).toString());
                                            }
                                            System.out.println();
                                            System.out.println("[1] Write message                         [2] Edit message");
                                            System.out.println("[3] Delete message                        [0] Exit");
                                            System.out.println("[-1] Export this message history to csv file");
                                            int optionChoice = Integer.parseInt(scanner.nextLine());
                                            if (optionChoice == -1) {
                                                System.out.println("Enter name of the file to which you want to export your message history");
                                                String fileName = scanner.nextLine();
                                                PrintWriter pw = new PrintWriter(new FileOutputStream(new File(fileName),false));
                                                for (int i = 0; i < messageHistory.size(); i++) {
                                                    Message msg = messageHistory.get(i);
                                                    String ans = String.format("\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"", msg.getId(), msg.getTime(), msg.getSender(), msg.getReceiver(), msg.getMessage(), msg.isDelBySender(), msg.isDelByReceiver());
                                                    pw.write(ans);
                                                    pw.println();
                                                    pw.flush();
                                                }
                                            }
                                            if (optionChoice == 1) {
                                                System.out.println("You want to send a message or upload a txt file?\n[1] Send message\n[2] Upload file");
                                                int fileOrText = Integer.parseInt(scanner.nextLine());
                                                if (fileOrText == 1) {
                                                    System.out.println("Enter message: ");
                                                    String mes = scanner.nextLine();
                                                    ArrayList<Message> temp = user.getMessages();
                                                    temp.add(new Message(user.getUsername(), listOfUsers[receiveUser - 1], mes));
                                                    user.setMessages(temp);
                                                }
                                                else if (fileOrText == 2) {
                                                    System.out.println("Enter name of txt file: ");
                                                    String fileName = scanner.nextLine();
                                                    String mes = "";
                                                    try {
                                                        BufferedReader bfr = new BufferedReader(new FileReader(new File(fileName)));
                                                        String st;
                                                        while ((st = bfr.readLine()) != null) {
                                                            mes += st + "\n";
                                                        }
                                                    }
                                                    catch (FileNotFoundException e) {
                                                        System.out.println("I'm sorry but that file does not exist");
                                                    }
                                                    ArrayList<Message> temp = user.getMessages();
                                                    temp.add(new Message(user.getUsername(), listOfUsers[receiveUser - 1], mes));
                                                    user.setMessages(temp);
                                                }
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
                                                choice = Integer.parseInt(scanner.nextLine());
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
                                                choice = Integer.parseInt(scanner.nextLine());
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
                            else if (currUser instanceof Buyer) {
                                System.out.println("[1] Write to store\n[2] Write to seller\n[0] Exit");
                                int makeChoice = Integer.parseInt(scanner.nextLine());
                                if (makeChoice == 0) {

                                }
                                if (makeChoice == 1) {
                                    System.out.println("Enter name of the store");
                                    String store = scanner.nextLine();
                                    for (int i = 0; i < users.size(); i++) {
                                        if (users.get(i) instanceof Seller) {
                                            for (int j = 0; j < ((Seller) users.get(i)).getStores().size(); j++) {
                                                if (((Seller) users.get(i)).getStores().get(j).equals(store)) {
                                                    System.out.println("Enter message you want to send to that store");
                                                    String msg = scanner.nextLine();
                                                    ArrayList<Message> temp = currUser.getMessages();
                                                    temp.add(new Message(currUser.getUsername(), users.get(i).getUsername(), msg));              // We should check if user exists in the future
                                                    user.setMessages(temp);
                                                    System.out.println("Store manager's username is "+users.get(i).getUsername());
                                                    System.out.println("Please wait for his message");
                                                    ArrayList<Message> messageHistory = parseMessageHistory(currUser, users.get(i).getUsername());
                                                    for (int k = 0; k < messageHistory.size(); k++) {
                                                        System.out.print(messageHistory.get(k).toString());                     //we print their message history
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    saveMessages(currUser);
                                }
                                if (makeChoice == 2) {
                                    while (true) {
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
                                            boolean flag = true;
                                            boolean flag1 = true;
                                            for (int i = 0; i < users.size(); i++) {
                                                if (users.get(i).getUsername().equals(newUser)) {
                                                    flag1 = false;
                                                    if (users.get(i) instanceof Seller && currUser instanceof Seller) {
                                                        System.out.println("You can't write to Seller, because you are Seller yourself");
                                                        flag = false;
                                                    }
                                                    if (users.get(i) instanceof Buyer && currUser instanceof Buyer) {
                                                        System.out.println("You can't write to Buyer, because you are Buyer yourself");
                                                        flag = false;
                                                    }
                                                }
                                            }
                                            if (flag1) {
                                                System.out.println("USER DOES NOT EXIST");
                                            } else if (flag) {
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
                                        } else {
                                            while (true) {
                                                messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                                                for (int i = 0; i < messageHistory.size(); i++) {
                                                    System.out.print(messageHistory.get(i).toString());
                                                }
                                                System.out.println();
                                                System.out.println("[1] Write message                         [2] Edit message");
                                                System.out.println("[3] Delete message                        [0] Exit");
                                                System.out.println("[-1] Export this message history to csv file");
                                                int optionChoice = Integer.parseInt(scanner.nextLine());
                                                if (optionChoice == -1) {
                                                    System.out.println("Enter name of the file to which you want to export your message history");
                                                    String fileName = scanner.nextLine();
                                                    PrintWriter pw = new PrintWriter(new FileOutputStream(new File(fileName),false));
                                                    for (int i = 0; i < messageHistory.size(); i++) {
                                                        Message msg = messageHistory.get(i);
                                                        String ans = String.format("\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"", msg.getId(), msg.getTime(), msg.getSender(), msg.getReceiver(), msg.getMessage(), msg.isDelBySender(), msg.isDelByReceiver());
                                                        pw.write(ans);
                                                        pw.println();
                                                        pw.flush();
                                                    }
                                                }
                                                if (optionChoice == 1) {
                                                    System.out.println("You want to send a message or upload a txt file?\n[1] Send message\n[2] Upload file");
                                                    int fileOrText = Integer.parseInt(scanner.nextLine());
                                                    if (fileOrText == 1) {
                                                        System.out.println("Enter message: ");
                                                        String mes = scanner.nextLine();
                                                        ArrayList<Message> temp = user.getMessages();
                                                        temp.add(new Message(user.getUsername(), listOfUsers[receiveUser - 1], mes));
                                                        user.setMessages(temp);
                                                    }
                                                    else if (fileOrText == 2) {
                                                        System.out.println("Enter name of txt file: ");
                                                        String fileName = scanner.nextLine();
                                                        String mes = "";
                                                        try {
                                                            BufferedReader bfr = new BufferedReader(new FileReader(new File(fileName)));
                                                            String st;
                                                            while ((st = bfr.readLine()) != null) {
                                                                mes += st + "\n";
                                                            }
                                                        }
                                                        catch (FileNotFoundException e) {
                                                            System.out.println("I'm sorry but that file does not exist");
                                                        }
                                                        ArrayList<Message> temp = user.getMessages();
                                                        temp.add(new Message(user.getUsername(), listOfUsers[receiveUser - 1], mes));
                                                        user.setMessages(temp);
                                                    }
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
                                                    choice = Integer.parseInt(scanner.nextLine());
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
                                                    choice = Integer.parseInt(scanner.nextLine());
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
                            }
                            break;
                        case 2:
                            // Statistics
                            break;
                        case 3:
                            do {
                                System.out.printf("%s - Account Details%n", currUser.getUsername());
                                System.out.println("--------------");
                                System.out.printf("Email: %s%nPassword: %s%n", currUser.getEmail(), currUser.getPassword());
                                if (currUser instanceof Seller) {
                                    System.out.println("[1] Edit Account\n[2] Delete Account\n[3] Create New Store\n[4] Delete Store\n[0] Exit");
                                } else {
                                    System.out.println("[1] Edit Account\n[2] Delete Account\n[0] Exit");
                                }
                                choice = scanner.nextInt();
                                if (choice > 4) {
                                    throw new InputMismatchException();
                                }
                                switch (choice) {
                                    case 1:
                                        scanner.nextLine();
                                        System.out.println("[1] Change Email\n[2] Change Password\n[0] Exit");
                                        choice = scanner.nextInt();
                                        String newAccountInfo;
                                        if (choice > 2) {
                                            throw new InputMismatchException();
                                        }
                                        switch (choice) {
                                            case 1 -> {
                                                scanner.nextLine();
                                                do {
                                                    System.out.println("Enter new email:");
                                                    newAccountInfo = scanner.nextLine();
                                                    if (newAccountInfo.contains("@") && newAccountInfo.contains(".")) {
                                                        currUser.setEmail(newAccountInfo);
                                                    } else {
                                                        System.out.println("Error: Enter a valid email!");
                                                        newAccountInfo = "";
                                                    }
                                                } while (newAccountInfo.isEmpty());
                                                System.out.printf("Email changed to: %s%n", newAccountInfo);
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
                                            currUser = null;
                                        }
                                        break;
                                    case 3:
                                        if (currUser instanceof Buyer) {
                                            break;
                                        } else if (currUser instanceof Seller) {
                                            System.out.println("Enter name for new store");
                                            String storeName = scanner.nextLine();
                                            ((Seller) currUser).createStore(storeName);
                                            break;
                                        }
                                    case 4:
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
                            } while (choice != 0 && currUser != null);
                            break;
                        default:
                            online = false;
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input");
                    scanner.nextLine();
                }
            }
        }
        writeUsers("login.csv", users);
        System.out.println("Successfully Logged out");

        //store.csv test
        /*ArrayList<User> users = readUsers("login.csv");
        ArrayList<Store> stores = readStores("stores.csv", users);
        ArrayList<Buyer> buyers = new ArrayList<>();
        Buyer exBuyer = new Buyer("exBuyer", "ebuyer@purdue.edu", "9876");
        buyers.add(exBuyer);
        stores.add(new Store("Nike", 34, buyers));
        writeStores("stores.csv", stores);*/
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
                    if (u instanceof Seller) {
                        pw.print("\",");
                    } else {
                        pw.print("\"");
                    }
                }
                if (u instanceof Seller) {
                    if (((Seller) u).getStores().size() > 0) {
                        for (int i = 0; i < ((Seller) u).getStores().size(); i++) {
                            if (i != ((Seller) u).getStores().size() - 1) {
                                pw.print(",\"" + ((Seller) u).getStores().get(i) + ",");
                            } else {
                                pw.print(((Seller) u).getStores().get(i) + "\"");
                            }
                        }
                    } else {
                        pw.print("\"\"");
                    }
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
public static User login(Scanner scanner) {
        ArrayList<String[]> users = new ArrayList<>();
        ArrayList<String> tempArrayList = new ArrayList<>();
        String[] tempArray;
        ArrayList<String> transferList;
        boolean invEmail = true;
        String email, pass;
        //Add users from file to arraylist
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("login.csv"));
            String line = bfr.readLine();
            while (line != null) {
                tempArrayList.add(line);
                line = bfr.readLine();
            }
            for(int i = 0; i < tempArrayList.size(); i++) {
                transferList = customSplitSpecific(tempArrayList.get(i));
                tempArray = new String[transferList.size()];
                for(int j = 0; j < tempArray.length; j++) {
                     tempArray[j] = transferList.get(j);
                }
                users.add(tempArray);
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            System.out.println("Please enter your email:");
            email = scanner.nextLine();
            System.out.println("Please enter you password:");
            pass = scanner.nextLine();
            for (int i = 0; i < users.size(); i++) {
                if (email.equals(users.get(i)[1])) {
                    invEmail = false;
                    if (pass.equals(users.get(i)[2])) {
                        if (users.get(i)[3].equals("b"))
                            return new Buyer(users.get(i)[0],email,pass);
                        if (users.get(i)[3].equals("s"))
                            return new Seller(users.get(i)[0],email,pass);
                    }
                }
            }
            if (invEmail) {
                System.out.println("Your email was incorrect");
            } else {
                System.out.println("Your password was incorrect");
            }
            System.out.println("Would you like to try again?\n1.Yes\n2.No");
            switch (scanner.nextLine()) {
                case "2":
                    return null;
            }
        }
    }

    public static User createAccount(Scanner scanner) {
        ArrayList<String[]> userFile = new ArrayList<>();
        User user = null;
        String email = "";
        String pass = "";
        String userType = "";
        String userName = "";
        ArrayList<String> tempArrayList = new ArrayList<>();
        String[] tempArray;
        ArrayList<String> transferList;
        boolean repeatUser = false;
        boolean invUsername = true;
        boolean invEmail = true;
        boolean invPass = true;
        boolean invBuyer = true;
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("login.csv"));
            String line = bfr.readLine();
            while (line != null) {
                tempArrayList.add(line);
                line = bfr.readLine();
            }
            for(int i = 0; i < tempArrayList.size(); i++) {
                transferList = customSplitSpecific(tempArrayList.get(i));
                tempArray = new String[transferList.size()];
                for(int j = 0; j < tempArray.length; j++) {
                    tempArray[j] = transferList.get(j);
                }
                userFile.add(tempArray);
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("A valid email contains an @ sign and has no commas");
        while(invEmail) {
            System.out.print("Please enter a valid email:");
            email = scanner.nextLine();
            if (email.contains(",") || (!(email.contains("@"))) || email == null || email.equals("")) {
                System.out.println("That email is not valid");
            } else {
                invEmail = false;
            }
        }
        System.out.println("A valid username contains no commas");
        while(invUsername){
            System.out.print("Please enter a valid username:");
            userName = scanner.nextLine();
            if (userName.contains(",") || userName == null || userName.equals("")) {
                System.out.println("That user name was not valid");
                for(int i = 0; i < userFile.size(); i++) {
                    if(userName.equals(userFile.get(i)[0]))
                        System.out.println("Someone else has that username please try a different one");
                        repeatUser = true;
                }
            } else if (repeatUser) {
                System.out.println("Someone else has that user name please enter a different one.");
                repeatUser = false;
            } else {
                invUsername = false;
            }
        }
        while(invPass){
            System.out.print("Please enter a password:");
            pass = scanner.nextLine();
            if (pass == null || pass == "") {
                System.out.println("That password was not valid");
            } else {
                invPass = false;
            }
        }
        System.out.println("A valid user type is either Buyer or Seller");
        while(invBuyer){
            System.out.print("Please enter a valid user type:");
            userType = scanner.nextLine();
            if (userType.equalsIgnoreCase("Buyer")) {
                userType = "b";
                user = new Buyer(userName,email,pass);
                invBuyer = false;
            }else if (userType.equalsIgnoreCase("Seller")) {
                userType = "s";
                user = new Seller(userName,email,pass);
                invBuyer = false;
            } else {
                System.out.println("That user type was not valid");
            }

        }
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("login.csv", false));
            for (int i = 0; i < userFile.size(); i++) {
                pw.println("\"" + userFile.get(i)[0] + "\"" + "," + "\"" + userFile.get(i)[1] + "\"" + "," + "\"" + userFile.get(i)[2] + "\"" + "," + "\"" + userFile.get(i)[3] + "\"" + "," + userFile.get(i)[4]);
            }
            pw.println("\"" + userName + "\"" + "," + "\"" + email + "\"" + "," + "\"" + pass + "\"" + "," + "\"" +userType + "\"" + ",\"\"");
            pw.close();
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return user;
    }
    
    public static ArrayList<User> readUsers(String filename) throws FileNotFoundException {
        File f = new File(filename);
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader bfr = null;
        ArrayList<User> users = new ArrayList<>();
        if (!f.exists()) {
            throw new FileNotFoundException("File doesn't exist");
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
                    Seller seller = new Seller(username, email, password, blockedUsernames);
                    String strStores = user.get(5);
                    if (strStores == null || strStores.isEmpty()) {

                    } else {
                        do {
                            if (!strStores.contains(",")) {
                                seller.createStore(strStores);
                                strStores = "";
                            } else {
                                seller.createStore(strStores.substring(0, strStores.indexOf(",")));
                                strStores = strStores.substring(strStores.indexOf(",") + 1);
                            }
                        } while (!strStores.isEmpty());
                    }
                    users.add(seller);
                }
            }
        }
        return users;
    }

    public static ArrayList<Store> readStores(String filename, ArrayList<User> users) throws FileNotFoundException {
        File f = new File(filename);
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader bfr = null;
        ArrayList<Store> stores = new ArrayList<>();
        if (!f.exists()) {
            throw new FileNotFoundException("File doesn't exist");
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
                ArrayList<String> strStores = customSplitSpecific(line);
                String storeName = strStores.get(0);
                int messagesReceived = Integer.parseInt(strStores.get(1));
                String messagedUsers = strStores.get(2);
                ArrayList<String> messageUsers = new ArrayList<>();
                ArrayList<Buyer> buyers = new ArrayList<>();
                do {

                    if (!messagedUsers.contains(",")) {
                        messageUsers.add(messagedUsers);
                        messagedUsers = "";
                    } else {
                        messageUsers.add(messagedUsers.substring(0, messagedUsers.indexOf(",")));
                        messagedUsers = messagedUsers.substring(messagedUsers.indexOf(",") + 1);
                    }
                } while (!messagedUsers.isEmpty());
                for (String s : messageUsers) {
                    for (User u : users) {
                        if (u instanceof Buyer && u.getUsername().equals(s)) {
                            buyers.add((Buyer) u);
                        }
                    }
                }
                stores.add(new Store(storeName, messagesReceived, buyers));
            }
        }
        return stores;
    }

    public static void writeStores(String filename, ArrayList<Store> stores) {
        File f = new File(filename);
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(f, false))) {
            for (Store s : stores) {
                pw.print("\"" + s.getStoreName() + "\",\"" + s.getMessagesReceived() + "\",\"");
                if (s.getUserMessaged().size() > 0) {
                    //ArrayList<User> blockedUsers = u.getBlockedUsers();
                    ArrayList<String> userMessaged = new ArrayList<>();
                    for (Buyer b : s.getUserMessaged()) {
                        userMessaged.add(b.getUsername());
                    }
                    for (int i = 0; i < userMessaged.size(); i++) {
                        if (i != userMessaged.size() - 1) {
                            pw.print(userMessaged.get(i) + ",");
                        } else {
                            pw.print(userMessaged.get(i) + "\"");
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
}

