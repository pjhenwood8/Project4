import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Seller extends User {

    private ArrayList<String> stores = new ArrayList<>();

    public Seller(String username, String email, String password) {
        super(username, email, password);
    }

    public Seller (String username, String email, String password, ArrayList<String> blockedUsernames) {
        super(username, email, password, blockedUsernames);
    }
    public void createStore(String storeName) {
        stores.add(storeName);
    }

    public void deleteStore(String storeName) {
        if (stores.contains(storeName)) {
            stores.remove(storeName);
        } else {
            throw new IllegalArgumentException(String.format("%s is not this user store", storeName));
        }
    }

    public ArrayList<String> getStores() {
        return stores;
    }

    public void viewStatistics(boolean alphabetical) throws IOException {
        FileReader fr = new FileReader("messages.csv");
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<String> senders = new ArrayList<>();
        ArrayList<Integer> messageCount = new ArrayList<Integer>();
        ArrayList<String> words = new ArrayList<String>();
        ArrayList<String> uniqueWords = new ArrayList<String>();
        ArrayList<Integer> wordUsage = new ArrayList<Integer>();
        int secondCommaIndex = -1;
        int thirdCommaIndex = -1;
        String sender = "";
        String reciever = "";
        String line = "";
        //creates ArrayLists messages and senders
        do {
             line = br.readLine();
             secondCommaIndex = line.indexOf(',', line.indexOf(','));
             thirdCommaIndex = line.indexOf(',', secondCommaIndex + 1);
             reciever = line.substring(secondCommaIndex, thirdCommaIndex);
             if(reciever.equals(getUsername())) {
                messages.add(line);
             }
             sender = line.substring(line.indexOf(','), secondCommaIndex);
             if(senders.contains(sender) == false) {
                 senders.add(sender);
             }
         } while(line != null);
        //creates ArrayList messageCount
        for (int i = 0; i < senders.size(); i++) {
            int messageCounter = 0;
            for (int j = 0; j < messages.size(); j++) {
                line = messages.get(j);
                sender = line.substring(line.indexOf(','), secondCommaIndex);
                if(senders.get(i).equals(sender)) {
                    messageCounter += 1;
                }
            }
            messageCount.add(messageCounter);
        }
        //TODO: most common words in overall messages.
        //creates ArrayList words
        for (int i = 0; i < messages.size(); i++) {
            line = messages.get(i);
            line = line.replaceAll("\\p{Punct}", "");
            String[] wordArray = line.split(" ");
            for (int j = 0; j < wordArray.length; j++) {
                words.add(wordArray[j]);
            }
        }
        //creates ArrayList uniqueWords
        for (int i = 0; i < words.size(); i++) {
            if (uniqueWords.contains(words.get(i)) == false) {
                uniqueWords.add(words.get(i));
            }
        }
        //creates ArrayList wordUsage
        for (int i = 0; i < uniqueWords.size(); i++) {
            int usageCount = 0;
            for (int j = 0; j < words.size(); j++) {
                if (uniqueWords.get(i).equals(words.get(j))) {
                    usageCount++;
                }
            }
            wordUsage.add(usageCount);
        }
        //finds the most frequent words
        int wordOneUses = 0;
        int wordTwoUses = 0;
        int wordThreeUses = 0;
        int wordOneIndex = 0;
        int wordTwoIndex = 0;
        int wordThreeIndex = 0;
        for (int i = 0; i < wordUsage.size(); i++) {
            if (wordUsage.get(i) > wordOneUses) {
                wordThreeUses = wordTwoUses;
                wordTwoUses = wordOneUses;
                wordOneUses = wordUsage.get(i);
                wordThreeIndex = wordTwoIndex;
                wordTwoIndex = wordOneIndex;
                wordOneIndex = i;
            } else if (wordUsage.get(i) > wordTwoUses) {
                wordThreeUses = wordTwoUses;
                wordTwoUses = wordUsage.get(i);
                wordThreeIndex = wordTwoIndex;
                wordTwoIndex = i;
            } else if (wordUsage.get(i) > wordThreeUses) {
                wordThreeUses = wordUsage.get(i);
                wordThreeIndex = i;
            }
        }
        String wordOne = uniqueWords.get(wordOneIndex);
        String wordTwo = uniqueWords.get(wordTwoIndex);
        String wordThree = uniqueWords.get(wordThreeIndex);
        //display
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < senders.size(); i++) {
            hashMap.put(senders.get(i), messageCount.get(i));
        }
        Collections.sort(senders);
        if (alphabetical == true) {
            //alphabetical
            for (int i = 0; i < senders.size(); i++) {
                System.out.println("Customers: " + senders.get(i) + " - Messages sent: " + hashMap.get(senders.get(i)));
            }
        } else if (alphabetical == false) {
            //reverse alphabetical
            for (int i = senders.size() - 1; i >= 0; i--) {
                System.out.println("Customers: " + senders.get(i) + " - Messages sent: " + hashMap.get(senders.get(i)));
            }
        }
        System.out.println("Most frequently used words: " + wordOne + ", " + wordTwo + ", and "  + wordThree);
    }

}