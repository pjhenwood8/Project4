import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class User {
    private final String username;
    private ArrayList<Message> messages = new ArrayList<>();

    private String password;

    private ArrayList<User> blockedUsers;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        try {
            messages = parseMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
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
            if (line.getSender().equals(username) || line.getReceiver().equals(username)) {
                temp.add(line);
            }
        }
        return temp;
    }

    private ArrayList<Message> readWholeFile() throws IOException {
        ArrayList<Message> fileContent = new ArrayList<>();
        BufferedReader bfr = new BufferedReader(new FileReader(new File("messages.csv")));
        String st;
        while ((st = bfr.readLine()) != null) {
            ArrayList<String> temp = customSplitSpecific(st);
            for (int i = 0; i < temp.size(); i++) {
                temp.set(i, temp.get(i).substring(1, temp.get(i).length()-1));
            }
            fileContent.add(new Message(Integer.parseInt(temp.get(0)),temp.get(1),temp.get(2),temp.get(3),temp.get(4)));
        }
        return fileContent;
    }

    private ArrayList<String> customSplitSpecific(String s)
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

    public void blockUser(String username, ArrayList<User> users) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                blockedUsers.add(u);
            }
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
