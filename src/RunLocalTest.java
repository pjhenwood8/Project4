import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Timeout;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static org.junit.Assert.*;

public class RunLocalTest {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    /**
     * A set of public test cases.
     *
     * <p>Purdue University -- CS18000 -- Summer 2022</p>
     *
     * @author Purdue CS
     * @version June 13, 2022
     */
    public static class TestCase {
        private final PrintStream originalOutput = System.out;
        private final InputStream originalSysin = System.in;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayInputStream testIn;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayOutputStream testOut;

        @Before
        public void outputStart() {
            testOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(testOut));
        }

        public void restoreInputAndOutput() {
            System.setIn(originalSysin);
            System.setOut(originalOutput);
        }

        private String getOutput() {
            return testOut.toString();
        }

        @SuppressWarnings("SameParameterValue")
        private void receiveInput(String str) {
            testIn = new ByteArrayInputStream(str.getBytes());
            System.setIn(testIn);
        }

        @Test(timeout = 1000)
        public void testCustomSplitSpecific(){
            User user = new User("a", "a@gmail.com", "123");
            String s = "hi,I,am,a,snail";
            String[] list = s.split(",");
            ArrayList<String> sList = new ArrayList<>(Arrays.asList(list));
            assertEquals("Wrong", sList,user.customSplitSpecific(s));

        }

        @Test(timeout = 1000)
        public void testReadWholeFile() throws IOException {
            User user1 = new User("a", "a@gmail.com", "123");
            User user2 = new User("b", "b@gmail.com", "1234");
            ArrayList<Message> mList1 = user1.readWholeFile();
            ArrayList<Message> mList2 = user2.readWholeFile();
            assertEquals( mList1.toString(), mList2.toString());
        }

        @Test(timeout = 1000)
        public void testParseMessages() throws IOException {
            User user1 = new User("Buyer1", "buyer1@purdue.edu", "password1234");
            ArrayList<Message> mList = new ArrayList<>();
            mList.add(new Message(169, "13-11-2022 02:23:31", "Buyer1", "Seller1", "Hi there", false, false));
            mList.add(new Message(183, "13-11-2022 02:24:59", "Buyer1", "Seller1", "Yes i want to buy", false, false));
            assertEquals( mList.toString(), user1.parseMessages().toString());
        }

        @Test(timeout = 1000)
        public void testBlockUser(){
            User user1 = new User("Buyer1", "buyer1@purdue.edu", "password1234");
            User user2 = new User("a", "a@gmail.com", "123");
            User user3 = new User("b", "b@gmail.com", "1234");
            ArrayList<User> uList = new ArrayList<>();
            uList.add(user1);
            uList.add(user2);
            uList.add(user3);
            assertTrue(user1.blockUser("a", uList));
            assertFalse(user1.blockUser("c", uList));
        }

        @Test(timeout = 1000)
        public void testUnblockUser(){
            User user1 = new User("Buyer1", "buyer1@purdue.edu", "password1234");
            User user2 = new User("a", "a@gmail.com", "123");
            User user3 = new User("b", "b@gmail.com", "1234");
            ArrayList<User> uList = new ArrayList<>();
            uList.add(user1);
            uList.add(user2);
            uList.add(user3);
            user1.blockUser("a", uList);
            user1.blockUser("c", uList);
            assertTrue(user1.unblockUser("a", user1.getBlockedUsers()));
            assertFalse(user1.unblockUser("c", user1.getBlockedUsers()));
        }
    }
}
