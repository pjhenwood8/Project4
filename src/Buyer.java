import java.util.ArrayList;

public class Buyer extends User{
    public Buyer (String username, String email, String password) {
        super(username, email, password);
    }

    public Buyer (String username, String email, String password, ArrayList<String> blockedUsernames) {
        super(username, email, password, blockedUsernames);
    }
}
