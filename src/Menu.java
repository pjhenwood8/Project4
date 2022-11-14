import java.io.*;
import java.util.*;

@SuppressWarnings("ReassignedVariable")
public class Menu {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean online = true;
        String response;
        ArrayList<User> users = readUsers("login.csv");
        ArrayList<Store> stores = readStores("stores.csv", users);
        addBlockedUsers(users);
        while (online) {
            System.out.println("Welcome to the Marketplace Messaging System");
            System.out.println("--------------------------------------------");
            boolean LoggingIn = true;
            boolean loggedIn = false;
            User user = null;
            User currUser = null;
            System.out.println("Please enter the number corresponding with your option:");
            while (LoggingIn) {
                System.out.println("[1] Login\n[2] Create Account\n[3] Exit");
                response = scanner.nextLine();
                switch (response) {
                    case "1":
                        user = login(scanner);
                        if (user != null)
                            LoggingIn = false;
                        break;
                    case "2":
                        user = createAccount(scanner);
                        if (user != null) {
                            LoggingIn = false;
                            currUser = user;
                            users.add(user);
                        }
                        break;
                    case "3":
                        user = null;
                        LoggingIn = false;
                        online = false;
                        break;
                    default:
                        System.out.println("Please enter a valid input");
                        user = null;
                }
            }
            if (user != null) {
                System.out.println("Successfully logged in as " + user.getUsername());
                System.out.println();
                for (User u : users) {
                    if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
                        currUser = u;
                        loggedIn = true;
                    }
                }
            }
            writeUsers("login.csv",users);
            while (loggedIn) {
                if (currUser != null) {
                    try {
                        System.out.println("--Main Menu--");
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
                                            boolean alreadyMessaged = false;
                                            for (String u : listOfUsers) {
                                                if (u.equals(newUser)) {
                                                    alreadyMessaged = true;
                                                    System.out.println("You already messaged this user");
                                                }
                                            }
                                            boolean flag = true;
                                            boolean flag1 = true;
                                            boolean flag2 = true;
                                            for (User value : users) {
                                                if (value.getUsername().equals(newUser)) {
                                                    flag1 = false;
                                                    if (value instanceof Seller) {
                                                        System.out.println("You can't write to Seller, because you are Seller yourself");
                                                        flag = false;
                                                    } else if (currUser.getBlockedUsers().contains(value) || value.getBlockedUsers().contains(currUser)) {
                                                        System.out.println("You can't write to this user because they are blocked");
                                                        flag2 = false;
                                                    }
                                                }
                                            }
                                            if (flag1) {
                                                System.out.println("USER DOES NOT EXIST");
                                            } else if (flag && flag2 && !alreadyMessaged) {
                                                System.out.println("Write your hello message first!");
                                                String mes = scanner.nextLine();
                                                ArrayList<Message> temp = user.getMessages();
                                                temp.add(new Message(user.getUsername(), newUser, mes));              // We should check if user exists in the future
                                                user.setMessages(temp);
                                                messageHistory = parseMessageHistory(user, newUser);
                                                for (Message message : messageHistory) {
                                                    System.out.print(message.toString());                     //we print their message history
                                                }
                                            }
                                        } else {
                                            while (true) {
                                                messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                                                for (Message value : messageHistory) {
                                                    if (value.getMessage().contains("\\n")) {
                                                        String ansMes = value.getMessage().replaceAll("\\\\n", "\n");
                                                        String ans = String.format("%s   (%s -> %s)%n%s%n", value.getTime(), value.getSender(), value.getReceiver(), ansMes);
                                                        System.out.print(ans);
                                                    } else
                                                        System.out.print(value);
                                                }
                                                System.out.println();
                                                System.out.println("[1] Write message                         [2] Edit message");
                                                System.out.println("[3] Delete message                        [0] Exit");
                                                System.out.println("[-1] Export this message history to csv file");
                                                int optionChoice = Integer.parseInt(scanner.nextLine());
                                                if (optionChoice == -1) {
                                                    System.out.println("Enter name of the file to which you want to export your message history");
                                                    String fileName = scanner.nextLine();
                                                    PrintWriter pw = new PrintWriter(new FileOutputStream(fileName,false));
                                                    for (Message msg : messageHistory) {
                                                        String ans = String.format("\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"", msg.getId(), msg.getTime(), msg.getSender(), msg.getReceiver(), msg.getMessage(), msg.isDelBySender(), msg.isDelByReceiver());
                                                        pw.write(ans);
                                                        pw.println();
                                                        pw.flush();
                                                    }
                                                    System.out.println("Your message history was successfully saved to "+fileName);
                                                    System.out.println();
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
                                                    } else if (fileOrText == 2) {
                                                        System.out.println("Enter name of txt file: ");
                                                        String fileName = scanner.nextLine();
                                                        String mes = "";
                                                        ArrayList<String> tempArr = new ArrayList<>();
                                                        try {
                                                            BufferedReader bfr = new BufferedReader(new FileReader(fileName));
                                                            String st;
                                                            while ((st = bfr.readLine()) != null) {
                                                                tempArr.add(st);
                                                            }
                                                            mes = String.join("\\n",tempArr);
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
                                                    for (Message message : messageHistory) {
                                                        if (message.getId() == temp.getId()) {
                                                            message.setMessage(msg);
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
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    saveMessages(user);
                                } else if (currUser instanceof Buyer) {
                                    System.out.println("[1] Write to store\n[2] Write to seller\n[0] Exit");
                                    int makeChoice = Integer.parseInt(scanner.nextLine());
                                    if (makeChoice == 0) {
                                        break;
                                    } else if (makeChoice == 1) {
                                        System.out.println("List of Stores:");
                                        for (User value : users) {
                                            if (value instanceof Seller) {
                                                for (String storeName: ((Seller) value).getStores()) {
                                                    System.out.println(storeName);
                                                }
                                            }
                                        }
                                        System.out.println("Enter name of the store");
                                        String store = scanner.nextLine();
                                        boolean flag = false;
                                        for (User value : users) {
                                            if (value instanceof Seller) {
                                                for (int j = 0; j < ((Seller) value).getStores().size(); j++) {
                                                    if (((Seller) value).getStores().get(j).equals(store)) {
                                                        flag = true;
                                                        System.out.println("Enter message you want to send to that store");
                                                        String msg = scanner.nextLine();
                                                        ArrayList<Message> temp = currUser.getMessages();
                                                        temp.add(new Message(currUser.getUsername(), value.getUsername(), msg));              // We should check if user exists in the future
                                                        user.setMessages(temp);
                                                        System.out.println("Store manager's username is " + value.getUsername());
                                                        System.out.println("Please wait for his message");
                                                        for (Store s : ((Seller) value).getNewStores()) {
                                                            if (s.getStoreName().equalsIgnoreCase(store)) {
                                                                s.addMessagesReceived();
                                                                if (!s.getUserMessaged().contains(currUser)) {
                                                                    s.addUserMessaged((Buyer) currUser);
                                                                }
                                                                for (Store st : stores) {
                                                                    if (s.getStoreName().equalsIgnoreCase(st.getStoreName())) {
                                                                        st.addMessagesReceived();
                                                                        if (!st.getUserMessaged().contains(currUser)) {
                                                                            st.addUserMessaged((Buyer) currUser);
                                                                        }
                                                                    }
                                                                }

                                                                writeStores("stores.csv", stores);
                                                            }
                                                        }
                                                        ArrayList<Message> messageHistory = parseMessageHistory(user, value.getUsername());
                                                        for (Message message : messageHistory) {
                                                            if (message.getMessage().contains("\\n")) {
                                                                String ansMes = message.getMessage().replaceAll("\\\\n", "\n");
                                                                String ans = String.format("%s   (%s -> %s)%n%s%n", message.getTime(), message.getSender(), message.getReceiver(), ansMes);
                                                                System.out.print(ans);
                                                            } else {
                                                                System.out.print(message.toString());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (!flag) {
                                            System.out.println("That store doesn't exist!");
                                        }
                                        saveMessages(currUser);
                                    } else if (makeChoice == 2) {
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
                                                boolean alreadyMessaged = false;
                                                for (String u : listOfUsers) {
                                                    if (u.equals(newUser)) {
                                                        alreadyMessaged = true;
                                                        System.out.println("You already messaged this user");
                                                    }
                                                }
                                                boolean flag = true;
                                                boolean flag1 = true;
                                                boolean flag2 = true;
                                                for (User value : users) {
                                                    if (value.getUsername().equals(newUser)) {
                                                        flag1 = false;
                                                        if (value instanceof Buyer) {
                                                            System.out.println("You can't write to Buyer, because you are Buyer yourself");
                                                            flag = false;
                                                        } else if (currUser.getBlockedUsers().contains(value) || value.getBlockedUsers().contains(currUser)) {
                                                            System.out.println("You can't write to this user because they are blocked");
                                                            flag2 = false;
                                                        }
                                                    }
                                                }
                                                if (flag1) {
                                                    System.out.println("USER DOES NOT EXIST");
                                                } else if (flag && flag2 && !alreadyMessaged) {
                                                    System.out.println("Write your hello message first!");
                                                    String mes = scanner.nextLine();
                                                    ArrayList<Message> temp = user.getMessages();
                                                    temp.add(new Message(user.getUsername(), newUser, mes));              // We should check if user exists in the future
                                                    user.setMessages(temp);
                                                    messageHistory = parseMessageHistory(user, newUser);
                                                    for (Message message : messageHistory) {
                                                        if (message.getMessage().contains("\\n")) {
                                                            String ansMes = message.getMessage().replaceAll("\\\\n", "\n");
                                                            String ans = String.format("%s   (%s -> %s)%n%s%n", message.getTime(), message.getSender(), message.getReceiver(), ansMes);
                                                            System.out.print(ans);
                                                        } else
                                                            System.out.print(message.toString());
                                                    }
                                                }
                                            } else if (receiveUser >= 1) {
                                                while (true) {
                                                    messageHistory = parseMessageHistory(user, listOfUsers[receiveUser - 1]);
                                                    for (Message message : messageHistory) {
                                                        if (message.getMessage().contains("\\n")) {
                                                            String ansMes = message.getMessage().replaceAll("\\\\n", "\n");
                                                            String ans = String.format("%s   (%s -> %s)%n%s%n", message.getTime(), message.getSender(), message.getReceiver(), ansMes);
                                                            System.out.print(ans);
                                                        } else
                                                            System.out.print(message);
                                                    }
                                                    System.out.println();
                                                    System.out.println("[1] Write message                         [2] Edit message");
                                                    System.out.println("[3] Delete message                        [0] Exit");
                                                    System.out.println("[-1] Export this message history to csv file");
                                                    int optionChoice = Integer.parseInt(scanner.nextLine());
                                                    if (optionChoice == -1) {
                                                        System.out.println("Enter name of the file to which you want to export your message history");
                                                        String fileName = scanner.nextLine();
                                                        PrintWriter pw = new PrintWriter(new FileOutputStream(fileName,false));
                                                        for (Message msg : messageHistory) {
                                                            String ans = String.format("\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"", msg.getId(), msg.getTime(), msg.getSender(), msg.getReceiver(), msg.getMessage(), msg.isDelBySender(), msg.isDelByReceiver());
                                                            pw.write(ans);
                                                            pw.println();
                                                            pw.flush();
                                                        }
                                                        System.out.println("Your message history was successfully saved to "+fileName);
                                                        System.out.println();
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
                                                            ArrayList<String> tempArr = new ArrayList<>();
                                                            try {
                                                                BufferedReader bfr = new BufferedReader(new FileReader(fileName));
                                                                String st;
                                                                while ((st = bfr.readLine()) != null) {
                                                                    tempArr.add(st);
                                                                }
                                                                mes = String.join("\\n",tempArr);
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
                                                        for (Message message : messageHistory) {
                                                            if (message.getId() == temp.getId()) {
                                                                message.setMessage(msg);
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
                                                        break;
                                                    }
                                                }
                                            } else {
                                                System.out.println("Please enter a valid number");
                                            }
                                        }
                                        saveMessages(user);
                                    }
                                }
                                break;
                            case 2:
                                while (true) {
                                    System.out.printf("%s - Statistics%n", currUser.getUsername());
                                    System.out.println("--------------");
                                    System.out.println("Select in which order you want to sort\n[1] Alphabetical\n[2] Reverse alphabetical\n[3] Most common words\n[0] Exit");
                                    int alphabetical = Integer.parseInt(scanner.nextLine());
                                    if (currUser instanceof Buyer) {
                                        if (alphabetical == 1)
                                            currUser.viewStatistics(true);
                                        else if (alphabetical == 2)
                                            currUser.viewStatistics(false);
                                        else if (alphabetical == 3) {
                                            ArrayList<Message> allMessages = new ArrayList<>();
                                            String word = "";
                                            String secondWord = "";
                                            String thirdWord = "";
                                            int count;
                                            int maxCount = 0;
                                            int secondCount = 0;
                                            int thirdCount = 0;
                                            for (User u1 : users) {
                                                if (u1 != currUser) {
                                                    allMessages.addAll(parseMessageHistory(currUser, u1.getUsername()));
                                                }
                                            }
                                            String message = "";
                                            for (Message m : allMessages) {
                                                message += m.getMessage() + " ";
                                            }
                                            String[] wordArr = message.split(" ");
                                            for (int k = 0; k < wordArr.length; k++) {
                                                count = 1;
                                                for (int l = k + 1; l < wordArr.length; l++) {
                                                    if (wordArr[k].equals(wordArr[l])) {
                                                        count++;
                                                    }

                                                }
                                                if (count > maxCount) {
                                                    maxCount = count;
                                                    word = wordArr[k];
                                                }
                                            }
                                            String[] newWordArr = new String[wordArr.length - maxCount];
                                            int i = 0;
                                            for (String s : wordArr) {
                                                if (!s.equals(word)) {
                                                    newWordArr[i] = s;
                                                    i++;
                                                }
                                            }
                                            for (int k = 0; k < newWordArr.length; k++) {
                                                count = 1;
                                                for (int l = k + 1; l < newWordArr.length; l++) {
                                                    if (newWordArr[k].equals(newWordArr[l])) {
                                                        count++;
                                                    }

                                                }
                                                if (count > secondCount) {
                                                    secondWord = newWordArr[k];
                                                    secondCount = count;
                                                    //word = wordArr[k];
                                                }
                                            }
                                            String[] new2WordArr = new String[newWordArr.length - secondCount];
                                            i = 0;
                                            for (String s : newWordArr) {
                                                if (!s.equals(secondWord)) {
                                                    new2WordArr[i] = s;
                                                    i++;
                                                }
                                            }
                                            for (int k = 0; k < new2WordArr.length; k++) {
                                                count = 1;
                                                for (int l = k + 1; l < new2WordArr.length; l++) {
                                                    if (new2WordArr[k].equals(new2WordArr[l])) {
                                                        count++;
                                                    }

                                                }
                                                if (count > thirdCount) {
                                                    thirdCount = count;
                                                    thirdWord = new2WordArr[k];
                                                }
                                            }
                                            System.out.println("The most common word in Messages is " + word + " said " + maxCount + " times");
                                            System.out.println("The second common word in Messages is " + secondWord + " said " + secondCount + " times");
                                            System.out.println("The third most common word in Messages is " + thirdWord + " said " + thirdCount + " times");
                                            System.out.println();
                                        } else if (alphabetical == 0)
                                            break;
                                    } else if (currUser instanceof Seller) {
                                        Map<String, Integer> sentMessages = new HashMap<>();
                                        for (User u : users) {
                                            int count;
                                            ArrayList<Message> messages;
                                            if (!u.equals(currUser) && u instanceof Buyer) {
                                                 messages = parseStoreMessages(currUser, u.getUsername());
                                                count = messages.size();
                                                sentMessages.put(u.getUsername(), count);
                                            }
                                        }
                                        ArrayList<String> sortedSentMessages = new ArrayList<>(sentMessages.keySet());
                                        Collections.sort(sortedSentMessages);
                                        if (alphabetical == 1) {
                                            for (String s : sortedSentMessages) {
                                                System.out.printf("%s sent %d messages%n", s, sentMessages.get(s));
                                            }
                                        } else if (alphabetical == 2) {
                                            for (int j = sortedSentMessages.size() - 1; j >= 0; j--) {
                                                System.out.printf("%s sent %d messages%n", sortedSentMessages.get(j), sentMessages.get(sortedSentMessages.get(j)));
                                            }
                                        } else if (alphabetical == 0) {
                                            break;
                                        } else if (alphabetical == 3) {
                                            ArrayList<Message> allMessages = new ArrayList<>();
                                            String word = "";
                                            String secondWord = "";
                                            String thirdWord = "";
                                            int count;
                                            int maxCount = 0;
                                            int secondCount = 0;
                                            int thirdCount = 0;
                                            for (User u1 : users) {
                                                if (u1 != currUser) {
                                                    allMessages.addAll(parseMessageHistory(currUser, u1.getUsername()));
                                                }
                                            }
                                            String message = "";
                                            for (Message m : allMessages) {
                                                message += m.getMessage() + " ";
                                            }
                                            String[] wordArr = message.split(" ");
                                            for (int k = 0; k < wordArr.length; k++) {
                                                count = 1;
                                                for (int l = k + 1; l < wordArr.length; l++) {
                                                    if (wordArr[k].equals(wordArr[l])) {
                                                        count++;
                                                    }

                                                }
                                                if (count > maxCount) {
                                                    maxCount = count;
                                                    word = wordArr[k];
                                                }
                                            }
                                            String[] newWordArr = new String[wordArr.length - maxCount];
                                            int i = 0;
                                            for (String s : wordArr) {
                                                if (!s.equals(word)) {
                                                    newWordArr[i] = s;
                                                    i++;
                                                }
                                            }
                                            for (int k = 0; k < newWordArr.length; k++) {
                                                count = 1;
                                                for (int l = k + 1; l < newWordArr.length; l++) {
                                                    if (newWordArr[k].equals(newWordArr[l])) {
                                                        count++;
                                                    }

                                                }
                                                if (count > secondCount) {
                                                    secondWord = newWordArr[k];
                                                    secondCount = count;
                                                    //word = wordArr[k];
                                                }
                                            }
                                            String[] new2WordArr = new String[newWordArr.length - secondCount];
                                            i = 0;
                                            for (String s : newWordArr) {
                                                if (!s.equals(secondWord)) {
                                                    new2WordArr[i] = s;
                                                    i++;
                                                }
                                            }
                                            for (int k = 0; k < new2WordArr.length; k++) {
                                                count = 1;
                                                for (int l = k + 1; l < new2WordArr.length; l++) {
                                                    if (new2WordArr[k].equals(new2WordArr[l])) {
                                                        count++;
                                                    }

                                                }
                                                if (count > thirdCount) {
                                                    thirdCount = count;
                                                    thirdWord = new2WordArr[k];
                                                }
                                            }
                                            System.out.println("The most common word in Messages is " + word + " said " + maxCount + " times");
                                            System.out.println("The second common word in Messages is " + secondWord + " said " + secondCount + " times");
                                            System.out.println("The third most common word in Messages is " + thirdWord + " said " + thirdCount + " times");
                                            System.out.println();
                                        }
                                    }

                                }
                                break;
                            case 3:
                                do {
                                    System.out.printf("%s - Account Details%n", currUser.getUsername());
                                    System.out.println("--------------");
                                    System.out.printf("Email: %s%nPassword: %s%n", currUser.getEmail(), currUser.getPassword());
                                    if (currUser instanceof Seller) {
                                        System.out.println("[1] Edit Account\n[2] Delete Account\n[3] Block/Unblock User\n[4] Create New Store\n[0] Exit");
                                    } else {
                                        System.out.println("[1] Edit Account\n[2] Delete Account\n[3] Block/Unblock User\n[0] Exit");
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
                                            System.out.println("Blocked Users: ");
                                            for (User b : currUser.getBlockedUsers()) {
                                                System.out.println(b.getUsername());
                                            }
                                            System.out.println("--------------");
                                            System.out.println("[1] Block new User\n[2] Unblock Users\n[3] Exit");
                                            choice = scanner.nextInt();
                                            scanner.nextLine();
                                            switch (choice) {
                                                case 1:
                                                    System.out.println("Enter name of user to block:");
                                                    String blockUsername = scanner.nextLine();
                                                    if (currUser.blockUser(blockUsername, users)) {
                                                        System.out.println(blockUsername + " blocked");
                                                    } else {
                                                        System.out.println("That user doesn't exist");
                                                    }
                                                    break;
                                                case 2:
                                                    System.out.println("Enter name of user to unblock:");
                                                    String unblockUsername = scanner.nextLine();
                                                    if (currUser.unblockUser(unblockUsername, users)) {
                                                        System.out.println(unblockUsername + " unblocked");
                                                    } else {
                                                        System.out.println("That user doesn't exist in your blocked list");
                                                    }
                                                    break;
                                                case 3:
                                                    break;
                                            }
                                            writeUsers("login.csv", users);
                                            break;
                                        case 4:
                                            if (currUser instanceof Buyer) {
                                                break;
                                            } else if (currUser instanceof Seller) {
                                                System.out.println("Your Stores:");
                                                for (String storeName : ((Seller) currUser).getStores()) {
                                                    System.out.println(storeName);
                                                }
                                                System.out.println("--------------");
                                                scanner.nextLine();
                                                System.out.println("Enter name for new store");
                                                String storeName = scanner.nextLine();
                                                ((Seller) currUser).createStore(storeName);
                                                stores.add(new Store(storeName,0));
                                                writeStores("stores.csv", stores);
                                                writeUsers("login.csv", users);
                                                break;
                                            }
                                        default:
                                            break;
                                    }
                                } while (choice != 0 && currUser != null);
                                break;
                            default:
                                loggedIn = false;
                                break;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input");
                        scanner.nextLine();
                    }
                }
            }
            if (currUser != null) {
                System.out.println("Successfully Logged out\n");
            } else {
                System.out.println("Thank you for using the messaging service");
            }
            writeUsers("login.csv", users);
            writeStores("stores.csv", stores);
        }
    }

    public static String[] parseUsers(User user) {
        ArrayList<Message> messages = user.getMessages();
        ArrayList<String> temp = new ArrayList<>();
        for (Message message : messages) {
            if (message.getSender().equals(user.getUsername())) {
                if (!temp.contains(message.getReceiver()))
                    temp.add(message.getReceiver());
            } else {
                if (!temp.contains(message.getSender()))
                    temp.add(message.getSender());
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
        for (Message message : messages) {
            if (message.getSender().equals(thirdParty) || message.getReceiver().equals(thirdParty)) {
                temp.add(message);
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
                        if (((Seller) u).getStores().size() > 0) {
                            pw.print("\"");
                        } else {
                            pw.print("\",");
                        }
                    } else {
                        pw.print("\"");
                    }
                }
                if (u instanceof Seller) {
                    if (((Seller) u).getStores().size() > 1) {
                        for (int i = 0; i < ((Seller) u).getStores().size(); i++) {
                            if (i != ((Seller) u).getStores().size() - 1) {
                                pw.print(",\"" + ((Seller) u).getStores().get(i) + ",");
                            } else {
                                pw.print(((Seller) u).getStores().get(i) + "\"");
                            }
                        }
                    } else if (((Seller) u).getStores().size() == 1) {
                        pw.print(",\"" + ((Seller) u).getStores().get(0) + "\"");
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
        int start =0;
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
        BufferedReader bfr = new BufferedReader(new FileReader("messages.csv"));
        String st;
        while ((st = bfr.readLine())!=null) {
            ArrayList<String> mesInfo = user.customSplitSpecific(st);
            if (!(mesInfo.get(2).equals("\"" + user.getUsername() + "\"") || mesInfo.get(3).equals("\"" + user.getUsername()+ "\"")))
                temp.add(st);
        }

        PrintWriter pw = new PrintWriter(new FileOutputStream("messages.csv",false));
        for (String s : temp) {
            pw.write(s);
            pw.println();
            pw.flush();
        }
        for (Message msg : allMessages) {
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
            for (String s : tempArrayList) {
                transferList = customSplitSpecific(s);
                tempArray = new String[transferList.size()];
                for (int j = 0; j < tempArray.length; j++) {
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
            for (String[] user : users) {
                if (email.equals(user[1])) {
                    invEmail = false;
                    if (pass.equals(user[2])) {
                        if (user[3].equals("b"))
                            return new Buyer(user[0], email, pass);
                        if (user[3].equals("s"))
                            return new Seller(user[0], email, pass);
                    }
                }
            }
            if (invEmail) {
                System.out.println("Your email was incorrect");
            } else {
                System.out.println("Your password was incorrect");
            }
            String option;
            do {
                System.out.println("Would you like to try again?\n1.Yes\n2.No");
                option = scanner.nextLine();
                if (option.equals("2")) {
                    return null;
                } else if (!(option.equals("1"))) {
                    System.out.println("Please enter a valid option");
                }
            } while (!option.equals("1"));
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
            for (String s : tempArrayList) {
                transferList = customSplitSpecific(s);
                tempArray = new String[transferList.size()];
                for (int j = 0; j < tempArray.length; j++) {
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
            System.out.print("Please enter a valid email: ");
            email = scanner.nextLine();
            if (email.contains(",") || !email.contains("@")) {
                System.out.println("That email is not valid");
            } else {
                invEmail = false;
            }
        }
        System.out.println("A valid username contains no commas");
        while(invUsername){
            System.out.print("Please enter a valid username: ");
            userName = scanner.nextLine();
            if (userName.contains(",") || userName.equals("")) {
                System.out.println("That user name was not valid");
                for (String[] strings : userFile) {
                    if (userName.equals(strings[0])) {
                        System.out.println("Someone else has that username please try a different one");
                        repeatUser = true;
                    }
                }
            } else if (repeatUser) {
                System.out.println("Someone else has that user name please enter a different one.");
                repeatUser = false;
            } else {
                invUsername = false;
            }
        }
        while(invPass){
            System.out.print("Please enter a password: ");
            pass = scanner.nextLine();
            if (pass == null || pass.equals("")) {
                System.out.println("That password was not valid");
            } else {
                invPass = false;
            }
        }
        System.out.println("A valid user type is either Buyer or Seller");
        while(invBuyer){
            System.out.print("Please enter a valid user type: ");
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
            for (String[] strings : userFile) {
                pw.println("\"" + strings[0] + "\"" + "," + "\"" + strings[1] + "\"" + "," + "\"" + strings[2] + "\"" + "," + "\"" + strings[3] + "\"" + "," + strings[4]);
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
                    if (strStores != null && !strStores.isEmpty()) {
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

    public static ArrayList<Message> parseStoreMessages(User mainClient, String thirdParty) {
        ArrayList<Message> messages = mainClient.getMessages();
        ArrayList<Message> temp = new ArrayList<>();
        for (Message message : messages) {
            if (message.getSender().equals(thirdParty)) {
                temp.add(message);
            }
        }
        return temp;
    }
}

