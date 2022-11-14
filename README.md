# Project4
Project 4 for CS 180 
In order to run the project code, you need to run the main method in the Menu.java class.
After starting the program, the follow-ups and tips will be shown to user, 
which will guide him on how to use the program

Student 1 was responsible for submitting the report on the project on BrightSpace
Student 2 was responsible for submitting the code or whatever it is//edit later

Menu.java is main class of the project. There you will be able to log in with existing account, create
a new account, delete accounts. Once you logged in, you will be able to write messages, edit them, delete them,
view statistics of those messages, change password of your account, change email of your account, and 
stuff like that.

Message.java is file for managing messages from the messages.csv file. Message.java allows us to convert
each line in the messages.csv into the Message object with specific field, using methods written in
Message.java

RunLocalTest.java is the file for testing functionality of the code. It allows us to quickly check,
whether new additions to the code messed up the overall logic.

User.java is the main class for creating users in our program. The most important field in that class
is ArrayList<Message> messages field, which saves all messages that are available to the user. After
each time user logs off from the program, these field is read and being saves to messages.csv.

Buyer.java is a subclass of User class, which has new method called viewStatistics, which allows user 
to view most used words, and most messaged stores.

Seller.java is a subclass of User class, which has additional field that saves all stores that are 
owned by the seller. Just like Buyer.java it has a viewStatistics method, which allows them to see which 
store is getting the highest amount of messages.

Store.java is a class that helps to keep track of statistics, specifically amount of messages that 
store receives. With that class it's much easier to construct statistics for the Seller class.
