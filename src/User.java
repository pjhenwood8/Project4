import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class User {

    private String email;
    private final String username;
    private ArrayList<Message> messages = new ArrayList<>();

    private String password;

    private ArrayList<User> blockedUsers = new ArrayList<>();

    private ArrayList<String> blockedUsernames = new ArrayList<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        try {
            messages = parseMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshMessages() {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getSender().equals(username) && messages.get(i).isDelBySender()) {
                messages.remove(i);
                i--;
            }
            else if (messages.get(i).getReceiver().equals(username) && messages.get(i).isDelByReceiver()) {
                messages.remove(i);
                i--;
            }
        }
    }

    public User(String username, String email, String password, ArrayList<String> blockedUsernames) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.blockedUsernames = blockedUsernames;
        try {
            messages = parseMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    private ArrayList<Message> parseMessages() throws IOException {
        ArrayList<Message> wholeFile = readWholeFile();

        ArrayList<Message> temp = new ArrayList<>();
        for (Message line : wholeFile) {
            if ((line.getSender().equals(username) && !line.isDelBySender()) || (line.getReceiver().equals(username) && !line.isDelBySender())) {
                temp.add(line);
            }
        }
        return temp;
    }

    private ArrayList<Message> readWholeFile() throws IOException {
        ArrayList<Message> fileContent = new ArrayList<>();
        BufferedReader bfr = new BufferedReader(new FileReader("messages.csv"));
        String st;
        while ((st = bfr.readLine()) != null) {
            ArrayList<String> temp = customSplitSpecific(st);
            for (int i = 0; i < temp.size(); i++) {
                temp.set(i, temp.get(i).substring(1, temp.get(i).length()-1));
            }
            fileContent.add(new Message(Integer.parseInt(temp.get(0)),temp.get(1),temp.get(2),temp.get(3),temp.get(4),Boolean.parseBoolean(temp.get(5)),Boolean.parseBoolean(temp.get(6))));
        }
        return fileContent;
    }

    public ArrayList<String> customSplitSpecific(String s)
    {
        ArrayList<String> words = new ArrayList<>();
        boolean notInsideComma = true;
        int start =0, end=0;
        for(int i=0; i<s.length()-1; i++)
        {
            if(s.charAt(i)==',' && notInsideComma)
            {
                words.add(s.substring(start,i));
                start = i+1;
            }
            else if(s.charAt(i)=='"')
                notInsideComma=!notInsideComma;
        }
        words.add(s.substring(start));
        return words;
    }

    public boolean blockUser(String username, ArrayList<User> users) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                blockedUsers.add(u);
                return true;
            }
        }
        return false;
    }

    public boolean unblockUser(String username, ArrayList<User> users) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                blockedUsers.remove(u);
                return true;
            }
        }
        return false;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void removeUser() {
        email = null;
        password = null;
        blockedUsers = null;
        messages = null;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getBlockedUsernames() {
        return blockedUsernames;
    }

    public ArrayList<User> getBlockedUsers() {
        return blockedUsers;
    }
    
    public void viewStatistics(boolean alphabetical) throws IOException {
        FileReader fr = new FileReader("stores.csv");
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<String> stores = new ArrayList<>();
        ArrayList<Integer> totalMessagesReceived = new ArrayList<>();
        ArrayList<Integer> messagesFromUser = new ArrayList<>();
        //creates ArrayLists messages and stores
        String line = "";
        String store = "";
        do {
            line = br.readLine();
            store = line.substring(0, line.indexOf(','));
            messages.add(line);
            if (!stores.contains(store)) {
                stores.add(store);
            }
        } while (line != null);
        //creates ArrayList totalMessagesReceived
        for (int i = 0; i < messages.size(); i++) {
            line = messages.get(i);
            store = line.substring(0, line.indexOf(","));
            int counter = 0;
            if (stores.get(i).equals(store)) {
                counter++;
            }
            totalMessagesReceived.add(counter);
        }
        //creates ArrayList messagesFromUser
        for (int i = 0; i < stores.size(); i++) {
            int counter = 0;
            for (int j = 0; j < messages.size(); j++) {
                line = messages.get(j);
                String user = line.substring(line.indexOf(',', line.indexOf(',')));
                store = line.substring(0, line.indexOf(','));
                if (user.equals(username) && store.equals(stores.get(i))) {
                    counter++;
                }
            }
            messagesFromUser.add(counter);
        }
        //create hashmaps listOne and listTwo
        HashMap<String, Integer> listOne = new HashMap<>();
        for (int i = 0; i < stores.size(); i++) {
            listOne.put(stores.get(i), totalMessagesReceived.get(i));
        }
        HashMap<String, Integer> listTwo = new HashMap<>();
        for (int i = 0; i < stores.size(); i++) {
            listOne.put(stores.get(i), messagesFromUser.get(i));
        }
        //sort alphabetically
        Collections.sort(stores);
        if (alphabetical == true) {
            //alphabetical
            for (int i = 0; i < stores.size(); i++) {
                System.out.println("Store: " + stores.get(i) + " - Number of messages received: " + listOne.get(stores.get(i)));
            }
        } else if (alphabetical == false) {
            //reverse alphabetical
            for (int i = stores.size() - 1; i >= 0; i--) {
                System.out.println("Store: " + stores.get(i) + " - Number of messages you've sent: " + listTwo.get(stores.get(i)));
            }
        }
    }

}
