import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class User {
    private final String username;
    private String[][] messages;

    public User(String username) throws IOException {
        this.username = username;
        messages = parseMessages(username);
    }

    public String getUsername() {
        return username;
    }

    public String[][] getMessages() {
        return messages;
    }

    public void setMessages(String[][] messages) {
        this.messages = messages;
    }

    private String[][] parseMessages(String username) throws IOException {
        ArrayList<ArrayList<String>> wholeFile = readWholeFile();

        ArrayList<ArrayList<String>> temp = new ArrayList<>();
        for (ArrayList<String> line : wholeFile) {
            if (line.get(0).equals(username) || line.get(1).equals(username)) {
                temp.add(line);
            }
        }

        String[][] answer = new String[temp.size()][temp.get(0).size()];
        for (int i = 0; i < temp.size(); i++) {
            for (int j = 0; j < temp.get(0).size(); j++) {
                answer[i][j] = temp.get(i).get(j);
            }
        }
        return answer;
    }

    private ArrayList<ArrayList<String>> readWholeFile() throws IOException {
        ArrayList<ArrayList<String>> fileContent = new ArrayList<>();
        BufferedReader bfr = new BufferedReader(new FileReader(new File("messages.csv")));
        String st;
        int i = 0;
        while ((st = bfr.readLine()) != null) {
            fileContent.set(i, customSplitSpecific(st));
            i++;
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
}
