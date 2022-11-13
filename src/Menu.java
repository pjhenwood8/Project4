import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args) throws IOException {
        boolean LoggingIn = true;
        String response;
        User user = null;
        Scanner scanner = new Scanner(System.in);
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
                    if(user != null)
                        LoggingIn = false;
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
        if(user != null) {
            /*String[] listOfUsers = parseUsers(user);
            for (int i = 0; i < listOfUsers.length; i++) {
                System.out.printf("[%d] %s%n", i + 1, listOfUsers[i]);
            }
            */
            System.out.printf("[%d] %s%n", 0, "Start new dialog");
        }
        System.out.println("Goodbye");
    }

    public static User login(Scanner scanner) {
        ArrayList<String[]> users = new ArrayList<>();
        boolean invEmail = true;
        String email, pass;
        //Add users from file to arraylist
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("login.csv"));
            String line = bfr.readLine();
            while (line != null) {
                users.add(line.split(","));
                line = bfr.readLine();
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
                if (email.equals(users.get(i)[0])) {
                    invEmail = false;
                    if (pass.equals(users.get(i)[2])) {
                        if (users.get(i)[3].equals("buyer"))
                            return new Buyer(email,users.get(i)[1],pass);
                        if (users.get(i)[3].equals("seller"))
                            return new Seller(email,users.get(i)[1],pass);
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
        boolean repeatUser = false;
        boolean invUsername = true;
        boolean invEmail = true;
        boolean invPass = true;
        boolean invBuyer = true;
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("login.csv"));
            String line = bfr.readLine();
            while (line != null) {
                userFile.add(line.split(","));
                line = bfr.readLine();
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
                    if(userName.equals(userFile.get(i)[1])) {
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
        System.out.println("A valid password contains no commas");
        while(invPass){
            System.out.print("Please enter a valid password:");
            pass = scanner.nextLine();
            if (pass.contains(",") || pass == null || pass.equals("")) {
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
                user = new Buyer(email,userName,pass);
                invBuyer = false;
            }else if (userType.equalsIgnoreCase("Seller")) {
                user = new Seller(email,userName,pass);
                invBuyer = false;
            } else {
                System.out.println("That userType was not valid");
            }

        }
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("login.csv", false));
            for (int i = 0; i < userFile.size(); i++) {
                pw.println(userFile.get(i)[0] + "," + userFile.get(i)[1] + "," + userFile.get(i)[2] + "," + userFile.get(i)[3]);
            }
            pw.println(email + "," + userName + "," + pass + "," + userType);
            pw.close();
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return user;
    }

    //Creates a list of all users the passed user has received messages from or sent messages to
    /*public static String[] parseUsers(User user) {
        ArrayList<ArrayList<String>> messages = user.getMessages();
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).get(1).equals("\"" + user.getUsername() + "\"")) {
                if (!temp.contains(messages.get(i).get(2)))
                    temp.add(messages.get(i).get(2));
            }
            else {
                if (!temp.contains(messages.get(i).get(1)))
                    temp.add(messages.get(i).get(1));
            }
        }
        String[] answer = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            answer[i] = temp.get(i);
        }
        return answer;
    }*/

    public static void writeMessage(User sender, User receiver, String message) throws SameTypeException, IOException {
        if (sender instanceof Buyer && receiver instanceof Buyer) {
            throw new SameTypeException("Buyers can't write to buyers");
        }
        if (sender instanceof Seller && receiver instanceof Seller) {
            throw new SameTypeException("Sellers can't to sellers");
        }
        PrintWriter pw = new PrintWriter(new FileWriter("messages.csv"), true);

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
    }
}

