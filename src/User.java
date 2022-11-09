import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class User {
    private final String username;
    private ArrayList<ArrayList<String>> messages;

    private ArrayList<User> blockedUsers;

    public User(String username) {
        this.username = username;
        try {
            messages = parseMessages(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<ArrayList<String>> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<ArrayList<String>> messages) {
        this.messages = messages;
    }

    private ArrayList<ArrayList<String>> parseMessages(String username) throws IOException {
        ArrayList<ArrayList<String>> wholeFile = readWholeFile();

        ArrayList<ArrayList<String>> temp = new ArrayList<>();
        for (ArrayList<String> line : wholeFile) {
            if (line.get(0).equals(username) || line.get(1).equals(username)) {
                temp.add(line);
            }
        }
        return temp;
    }

    private ArrayList<ArrayList<String>> readWholeFile() throws IOException {
        ArrayList<ArrayList<String>> fileContent = new ArrayList<>();
        BufferedReader bfr = new BufferedReader(new FileReader(new File("messages.csv")));
        String st;
        while ((st = bfr.readLine()) != null) {
            fileContent.add(customSplitSpecific(st));
        }
        return fileContent;
    }

    private ArrayList<String> customSplitSpecific(String s)
    {
        ArrayList<String> words = new ArrayList<String>();
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

}
