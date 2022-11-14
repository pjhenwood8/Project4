import java.util.ArrayList;

public class Buyer extends User{
    public Buyer (String username, String email, String password) {
        super(username, email, password);
    }

    public Buyer (String username, String email, String password, ArrayList<String> blockedUsernames) {
        super(username, email, password, blockedUsernames);
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
