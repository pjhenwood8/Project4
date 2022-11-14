import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Project 4 - User
 * This class is the underlying structure of each user. It contains the information each
 * user stores such as the user's email, password, email, messages, and blocked users.
 *
 * @author Kevin Zhang, Jalen Mann, Alimzhan Sultanov, Kyle Griffin, and PJ Henwood, lab sec LC2
 *
 * @version November 15, 2022
 *
 */

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
        int start =0;
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

    }
}
