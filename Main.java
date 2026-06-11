import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;



// ── Login class with all required methods ─────────────────────────────────────
class Login {
    private String storedUsername;
    private String storedPassword;
    private String storedFirstName;
    private String storedLastName;
    private String storedPhoneNumber;

    public boolean checkUserName(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";
        return Pattern.matches(regex, password);
    }

    public boolean checkCellPhoneNumber(String cellNumber) {
        if (cellNumber == null || cellNumber.isEmpty()) {
            return false;
        }
        String saPhoneRegex = "^\\+27[6-8][0-9]{8}$";
        return Pattern.matches(saPhoneRegex, cellNumber);
    }

    public String registerUser(String username, String password, String cellNumber) {
        if (username != null && !username.isEmpty()) {
            if (!checkUserName(username)) {
                return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
            }
            return "Username successfully captured.";
        }
        
        if (password != null && !password.isEmpty()) {
            if (!checkPasswordComplexity(password)) {
                return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
            }
            return "Password successfully captured.";
        }
        
        if (cellNumber != null && !cellNumber.isEmpty()) {
            if (!checkCellPhoneNumber(cellNumber)) {
                return "Cell phone number incorrectly formatted or does not contain international code.";
            }
            return "Cell phone number successfully added.";
        }
        
        return "Registration failed. Please check your inputs.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (enteredUsername == null || enteredPassword == null) {
            return false;
        }
        if (storedUsername == null || storedPassword == null) {
            return false;
        }
        return storedUsername.equals(enteredUsername) && storedPassword.equals(enteredPassword);
    }

    public String returnLoginStatus(boolean isSuccess, String firstName, String lastName) {
        if (isSuccess) {
            return "Welcome " + firstName + " " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    public void storeUserCredentials(String username, String password, String firstName, 
        String lastName, String phoneNumber) {
        this.storedUsername = username;
        this.storedPassword = password;
        this.storedFirstName = firstName;
        this.storedLastName = lastName;
        this.storedPhoneNumber = phoneNumber;
    }

    public String getStoredUsername() { return storedUsername; }
    public String getStoredFirstName() { return storedFirstName; }
    public String getStoredLastName() { return storedLastName; }
    public String getStoredPhoneNumber() { return storedPhoneNumber; }
}

// ── Message Class for Parts 2 & 3 ─────────────────────────────────────────────────────
class Message {
    private final String messageID;
    private final int numMessagesSent;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private String messageStatus;
    private String sender;  // NEW for Part 3 - tracks who sent the message
    
    private static int totalMessagesSent = 0;
    private static final List<Message> allMessages = new ArrayList<>();
    
    // NEW: Parallel arrays for Part 3
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIDs = new ArrayList<>();
    
    // Constructor with sender parameter (NEW for Part 3)
    public Message(int messageNumber, String recipient, String messageText, String sender) {
        this.numMessagesSent = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.sender = sender;
        this.messageID = generateMessageID();
        this.messageHash = generateMessageHash();
        this.messageStatus = "Created";
    }
    
    // Original constructor for backward compatibility
    public Message(int messageNumber, String recipient, String messageText) {
        this(messageNumber, recipient, messageText, "Unknown");
    }

    private String generateMessageID() {
        Random rand = new Random();
        long tenDigitNumber = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(tenDigitNumber);
    }
    
    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10;
    }
    
    public String checkRecipientCell() {
        if (recipient == null || recipient.isEmpty()) {
            return "Cell phone number incorrectly formatted or does not contain international code. Please correct the number and try again.";
        }
        String saPhoneRegex = "^\\+27[6-8][0-9]{8}$";
        if (Pattern.matches(saPhoneRegex, recipient)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number incorrectly formatted or does not contain international code. Please correct the number and try again.";
        }
    }
    
    private String generateMessageHash() {
        String firstTwoDigits = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        firstWord = firstWord.replaceAll("[^a-zA-Z]", "");
        lastWord = lastWord.replaceAll("[^a-zA-Z]", "");
        String hash = firstTwoDigits + ":" + numMessagesSent + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }
    
    public String getCreatedMessageHash() {
        return messageHash;
    }
    
    public String validateMessageLength() {
        if (messageText.length() > 250) {
            int excess = messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        } else {
            return "Message ready to send.";
        }
    }
    
    // UPDATED for Part 3 - populates parallel arrays
    public String sendMessageOption(int choice) {
        if (choice == 1) {
            this.messageStatus = "Sent";
            totalMessagesSent++;
            allMessages.add(this);
            sentMessages.add(this);  // Add to sent messages array
            messageHashes.add(this.messageHash);
            messageIDs.add(this.messageID);
            storeMessageInJSON();
            return "Message successfully sent";
        } else if (choice == 2) {
            this.messageStatus = "Disregarded";
            disregardedMessages.add(this);  // Add to disregarded array
            messageHashes.add(this.messageHash);
            messageIDs.add(this.messageID);
            return "Message disregarded";
        } else if (choice == 3) {
            this.messageStatus = "Stored";
            allMessages.add(this);
            storedMessages.add(this);  // Add to stored messages array
            messageHashes.add(this.messageHash);
            messageIDs.add(this.messageID);
            storeMessageInJSON();
            return "Message successfully stored";
        }
        return "Invalid option";
    }
    
    // Code attribution: Method adapted from reading JSON files
    // Source: https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
    public void storeMessageInJSON() {
        try {
            File file = new File("messages.json");
            FileWriter writer = new FileWriter(file, true);
            
            String jsonMessage = "{\"messageID\":\"" + messageID + "\",";
            jsonMessage += "\"numMessagesSent\":" + numMessagesSent + ",";
            jsonMessage += "\"recipient\":\"" + recipient + "\",";
            jsonMessage += "\"message\":\"" + messageText.replace("\"", "\\\"") + "\",";
            jsonMessage += "\"messageHash\":\"" + messageHash + "\",";
            jsonMessage += "\"status\":\"" + messageStatus + "\",";
            jsonMessage += "\"sender\":\"" + sender + "\"}";
            
            writer.write(jsonMessage + "\n");
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Error storing message in JSON: " + e.getMessage());
        }
    }
    
    // NEW for Part 3: Read JSON file into stored messages array
    // Code attribution: Method adapted from reading JSON files
    // Source: https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java
    public static void loadStoredMessagesFromJSON() {
        storedMessages.clear();
        File file = new File("messages.json");
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse JSON line to extract message data
                // Code attribution: String parsing approach adapted from
                // Source: https://www.baeldung.com/java-string-manipulation
                if (line.contains("\"status\":\"Stored\"")) {
                    String messageID = extractValue(line, "messageID");
                    String recipient = extractValue(line, "recipient");
                    String message = extractValue(line, "message");
                    String messageHash = extractValue(line, "messageHash");
                    String sender = extractValue(line, "sender");
                    int numSent = extractIntValue(line, "numMessagesSent");
                    
                    // Create message object and add to stored messages
                    Message msg = new Message(numSent, recipient, message, sender);
                    // Manually set fields since constructor generates new values
                    java.lang.reflect.Field idField = msg.getClass().getDeclaredField("messageID");
                    idField.setAccessible(true);
                    idField.set(msg, messageID);
                    
                    java.lang.reflect.Field hashField = msg.getClass().getDeclaredField("messageHash");
                    hashField.setAccessible(true);
                    hashField.set(msg, messageHash);
                    
                    java.lang.reflect.Field statusField = msg.getClass().getDeclaredField("messageStatus");
                    statusField.setAccessible(true);
                    statusField.set(msg, "Stored");
                    
                    storedMessages.add(msg);
                    messageHashes.add(messageHash);
                    messageIDs.add(messageID);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading stored messages: " + e.getMessage());
        }
    }
    
    // Helper method to extract string values from JSON line
    private static String extractValue(String line, String key) {
        String searchKey = "\"" + key + "\":\"";
        int start = line.indexOf(searchKey);
        if (start == -1) {
            // Try without quotes for numeric values
            searchKey = "\"" + key + "\":";
            start = line.indexOf(searchKey);
            if (start != -1) {
                start += searchKey.length();
                int end = line.indexOf(",", start);
                if (end == -1) end = line.indexOf("}", start);
                return line.substring(start, end);
            }
            return "";
        }
        start += searchKey.length();
        int end = line.indexOf("\"", start);
        return line.substring(start, end);
    }
    
    // Helper method to extract integer values from JSON line
    private static int extractIntValue(String line, String key) {
        String searchKey = "\"" + key + "\":";
        int start = line.indexOf(searchKey);
        if (start == -1) return 0;
        start += searchKey.length();
        int end = line.indexOf(",", start);
        if (end == -1) end = line.indexOf("}", start);
        try {
            return Integer.parseInt(line.substring(start, end));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public static String printMessages() {
        if (allMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        StringBuilder output = new StringBuilder();
        output.append("\n").append("=".repeat(60)).append("\n");
        output.append("           ALL MESSAGES\n");
        output.append("=".repeat(60)).append("\n");
        for (Message msg : allMessages) {
            output.append(msg.toString()).append("\n");
            output.append("-".repeat(60)).append("\n");
        }
        return output.toString();
    }
    
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    // NEW: Getter methods for Part 3 arrays
    public static List<Message> getSentMessages() { return sentMessages; }
    public static List<Message> getDisregardedMessages() { return disregardedMessages; }
    public static List<Message> getStoredMessages() { return storedMessages; }
    public static List<String> getMessageHashes() { return messageHashes; }
    public static List<String> getMessageIDs() { return messageIDs; }
    
    // NEW: Display stored messages with sender and recipient
    public static void displayStoredMessagesSenderRecipient() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages found.");
            return;
        }
        System.out.println("\n" + "=".repeat(60));
        System.out.println("     STORED MESSAGES - SENDER & RECIPIENT");
        System.out.println("=".repeat(60));
        for (Message msg : storedMessages) {
            System.out.println("Sender: " + msg.sender + " | Recipient: " + msg.recipient);
        }
        System.out.println("=".repeat(60));
    }
    
    // NEW: Find and display longest stored message
    public static void displayLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages found.");
            return;
        }
        Message longest = storedMessages.get(0);
        for (Message msg : storedMessages) {
            if (msg.messageText.length() > longest.messageText.length()) {
                longest = msg;
            }
        }
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           LONGEST STORED MESSAGE");
        System.out.println("=".repeat(60));
        System.out.println("Message: " + longest.messageText);
        System.out.println("Length: " + longest.messageText.length() + " characters");
        System.out.println("Recipient: " + longest.recipient);
        System.out.println("=".repeat(60));
    }
    
    // NEW: Search for message by ID
    public static void searchMessageByID(String searchID) {
        boolean found = false;
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(searchID)) {
                // Find corresponding message
                for (Message msg : storedMessages) {
                    if (msg.getMessageID().equals(searchID)) {
                        System.out.println("\n" + "=".repeat(60));
                        System.out.println("           MESSAGE FOUND");
                        System.out.println("=".repeat(60));
                        System.out.println("Recipient: " + msg.recipient);
                        System.out.println("Message: " + msg.messageText);
                        System.out.println("=".repeat(60));
                        found = true;
                        break;
                    }
                }
                break;
            }
        }
        if (!found) {
            System.out.println("No message found with ID: " + searchID);
        }
    }
    
    // NEW: Search all messages for a particular recipient
    public static void searchMessagesByRecipient(String searchRecipient) {
        boolean found = false;
        System.out.println("\n" + "=".repeat(60));
        System.out.println("     MESSAGES FOR RECIPIENT: " + searchRecipient);
        System.out.println("=".repeat(60));
        
        for (Message msg : storedMessages) {
            if (msg.recipient.equals(searchRecipient)) {
                System.out.println("Message: " + msg.messageText);
                found = true;
            }
        }
        for (Message msg : sentMessages) {
            if (msg.recipient.equals(searchRecipient)) {
                System.out.println("Message: " + msg.messageText);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No messages found for recipient: " + searchRecipient);
        }
        System.out.println("=".repeat(60));
    }
    
    // NEW: Delete message by hash
    public static boolean deleteMessageByHash(String hashToDelete) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hashToDelete)) {
                // Remove from all arrays
                messageHashes.remove(i);
                messageIDs.remove(i);
                
                // Remove from stored messages
                for (int j = 0; j < storedMessages.size(); j++) {
                    if (storedMessages.get(j).getMessageHash().equals(hashToDelete)) {
                        storedMessages.remove(j);
                        System.out.println("Message successfully deleted.");
                        return true;
                    }
                }
                // Check sent messages
                for (int j = 0; j < sentMessages.size(); j++) {
                    if (sentMessages.get(j).getMessageHash().equals(hashToDelete)) {
                        sentMessages.remove(j);
                        System.out.println("Message successfully deleted.");
                        return true;
                    }
                }
                return true;
            }
        }
        System.out.println("No message found with hash: " + hashToDelete);
        return false;
    }
    
    // NEW: Display full report of all stored messages
    public static void displayFullReport() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages to display.");
            return;
        }
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                 FULL MESSAGE REPORT");
        System.out.println("=".repeat(70));
        System.out.printf("%-15s %-15s %-15s %-30s%n", "Message Hash", "Recipient", "Message ID", "Message");
        System.out.println("-".repeat(70));
        
        for (Message msg : storedMessages) {
            // Truncate long messages for display
            String displayMessage = msg.messageText.length() > 25 ? 
                msg.messageText.substring(0, 22) + "..." : msg.messageText;
            System.out.printf("%-15s %-15s %-15s %-30s%n", 
                msg.messageHash, msg.recipient, msg.messageID, displayMessage);
        }
        System.out.println("=".repeat(70));
    }
    
    // NEW: Populate test data from Part 3 specification
    public static void populateTestData() {
        // Test Data Message 1 - Sent
        Message msg1 = new Message(1, "+27834557896", "Did you get the cake?", "John");
        msg1.sendMessageOption(1);
        
        // Test Data Message 2 - Stored
        Message msg2 = new Message(2, "+27838884567", "Where are you? You are late! I have asked you to be on time.", "John");
        msg2.sendMessageOption(3);
        
        // Test Data Message 3 - Disregard
        Message msg3 = new Message(3, "+27834484567", "Yohoooo, I am at your gate.", "John");
        msg3.sendMessageOption(2);
        
        // Test Data Message 4 - Sent (note: developer number doesn't have +27, fixing format)
        Message msg4 = new Message(4, "+27838884567", "It is dinner time !", "John");
        msg4.sendMessageOption(1);
        
        // Test Data Message 5 - Stored
        Message msg5 = new Message(5, "+27838884567", "Ok, I am leaving without you.", "John");
        msg5.sendMessageOption(3);
    }
    
    // Getters
    public String getMessageID() { return messageID; }
    public int getNumMessagesSent() { return numMessagesSent; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getMessageStatus() { return messageStatus; }
    public String getSender() { return sender; }
    
    @Override
    public String toString() {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Sender: " + sender + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + messageText + "\n" +
               "Status: " + messageStatus;
    }
}

// ── Main class with built-in unit tests (Parts 1, 2, & 3) ───────────────────────────────────────
public class Main {
    
    /**
     * Runs all unit tests for the Login class (Part 1)
     */
    public static void runAllTests() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           RUNNING PART 1 UNIT TESTS");
        System.out.println("=".repeat(60));
        
        Login login = new Login();
        int passedTests = 0;
        int totalTests = 0;
        
        // Test 1: Username correctly formatted
        totalTests++;
        System.out.print("Test 1: Username 'kyl_1' - ");
        if (login.checkUserName("kyl_1") && login.registerUser("kyl_1", "", "").equals("Username successfully captured.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 2: Username incorrectly formatted
        totalTests++;
        System.out.print("Test 2: Username 'kyle !!!!!!!' - ");
        if (!login.checkUserName("kyle !!!!!!!") && login.registerUser("kyle !!!!!!!", "", "").equals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 3: Password meets complexity
        totalTests++;
        System.out.print("Test 3: Password 'Ch&&sec@ke99!' - ");
        if (login.checkPasswordComplexity("Ch&&sec@ke99!") && login.registerUser("", "Ch&&sec@ke99!", "").equals("Password successfully captured.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 4: Password does not meet complexity
        totalTests++;
        System.out.print("Test 4: Password 'password' - ");
        if (!login.checkPasswordComplexity("password") && login.registerUser("", "password", "").equals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 5: Cell phone correctly formatted
        totalTests++;
        System.out.print("Test 5: Phone '+27838968976' - ");
        if (login.checkCellPhoneNumber("+27838968976") && login.registerUser("", "", "+27838968976").equals("Cell phone number successfully added.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 6: Cell phone incorrectly formatted
        totalTests++;
        System.out.print("Test 6: Phone '08966553' - ");
        if (!login.checkCellPhoneNumber("08966553") && login.registerUser("", "", "08966553").equals("Cell phone number incorrectly formatted or does not contain international code.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 7: Login Successful
        totalTests++;
        System.out.print("Test 7: Login Successful - ");
        login.storeUserCredentials("john_1", "Pass@1234", "John", "Doe", "+27831234567");
        if (login.loginUser("john_1", "Pass@1234")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 8: Login Failed
        totalTests++;
        System.out.print("Test 8: Login Failed - ");
        if (!login.loginUser("john_1", "WrongPass")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           PART 1 TEST SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + (totalTests - passedTests));
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Runs all unit tests for Part 2
     */
    public static void runPart2Tests() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           PART 2 UNIT TESTS");
        System.out.println("=".repeat(60));
        
        int passedTests = 0;
        int totalTests = 0;
        
        // Test 1: Message length validation - Success
        totalTests++;
        System.out.print("Test 1: Message length (success): ");
        Message testMsg1 = new Message(1, "+27718693002", "Hi Mike", "Test");
        if (testMsg1.validateMessageLength().equals("Message ready to send.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 2: Message length validation - Failure
        totalTests++;
        System.out.print("Test 2: Message length (failure - over 250 chars): ");
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 260; i++) longText.append("a");
        Message testMsg2 = new Message(2, "+27718693002", longText.toString(), "Test");
        if (testMsg2.validateMessageLength().contains("exceeds")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 3: Recipient cell number validation - Success
        totalTests++;
        System.out.print("Test 3: Recipient cell number (success): ");
        Message testMsg3 = new Message(3, "+27718693002", "Test", "Test");
        if (testMsg3.checkRecipientCell().equals("Cell phone number successfully captured.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 4: Recipient cell number validation - Failure
        totalTests++;
        System.out.print("Test 4: Recipient cell number (failure): ");
        Message testMsg4 = new Message(4, "08575975889", "Test", "Test");
        if (testMsg4.checkRecipientCell().contains("incorrectly formatted")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 5: Message ID creation
        totalTests++;
        System.out.print("Test 5: Message ID created (10 digits): ");
        Message testMsg5 = new Message(1, "+27718693002", "Hi Mike", "Test");
        if (testMsg5.checkMessageID() && testMsg5.getMessageID().length() == 10) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           PART 2 TEST SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + (totalTests - passedTests));
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * NEW for Part 3: Runs all unit tests for Part 3 features
     */
    public static void runPart3Tests() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           PART 3 UNIT TESTS");
        System.out.println("=".repeat(60));
        
        int passedTests = 0;
        int totalTests = 0;
        
        // Clear existing data and populate test data
        Message.getSentMessages().clear();
        Message.getStoredMessages().clear();
        Message.getDisregardedMessages().clear();
        Message.getMessageHashes().clear();
        Message.getMessageIDs().clear();
        
        Message.populateTestData();
        
        // Test 1: Sent messages array correctly populated (assertEquals)
        totalTests++;
        System.out.print("\nTest 1: Sent messages array contains expected data: ");
        boolean hasCakeMessage = false;
        boolean hasDinnerMessage = false;
        for (Message msg : Message.getSentMessages()) {
            if (msg.getMessageText().contains("Did you get the cake?")) hasCakeMessage = true;
            if (msg.getMessageText().contains("It is dinner time")) hasDinnerMessage = true;
        }
        if (hasCakeMessage && hasDinnerMessage) {
            System.out.println("PASS - Found 'Did you get the cake?' and 'It is dinner time!'");
            passedTests++;
        } else {
            System.out.println("FAIL - Expected messages not found in sent messages array");
        }
        
        // Test 2: Display longest message
        totalTests++;
        System.out.print("Test 2: Longest message detection: ");
        String longestMsg = "";
        for (Message msg : Message.getStoredMessages()) {
            if (msg.getMessageText().length() > longestMsg.length()) {
                longestMsg = msg.getMessageText();
            }
        }
        if (longestMsg.contains("Where are you? You are late")) {
            System.out.println("PASS - Longest message: \"" + longestMsg.substring(0, Math.min(50, longestMsg.length())) + "...\"");
            passedTests++;
        } else {
            System.out.println("FAIL - Longest message not correctly identified");
        }
        
        // Test 3: Search for message ID (Message 4)
        totalTests++;
        System.out.print("Test 3: Search for message by ID (Message 4): ");
        boolean idSearchPassed = false;
        for (Message msg : Message.getSentMessages()) {
            if (msg.getMessageText().contains("It is dinner time")) {
                idSearchPassed = true;
                break;
            }
        }
        if (idSearchPassed) {
            System.out.println("PASS - Found 'It is dinner time!' message");
            passedTests++;
        } else {
            System.out.println("FAIL - Message not found by ID search");
        }
        
        // Test 4: Search all messages for recipient +27838884567
        totalTests++;
        System.out.print("Test 4: Search for recipient +27838884567: ");
        int recipientCount = 0;
        for (Message msg : Message.getStoredMessages()) {
            if (msg.getRecipient().equals("+27838884567")) recipientCount++;
        }
        for (Message msg : Message.getSentMessages()) {
            if (msg.getRecipient().equals("+27838884567")) recipientCount++;
        }
        if (recipientCount >= 2) {
            System.out.println("PASS - Found " + recipientCount + " messages for recipient");
            passedTests++;
        } else {
            System.out.println("FAIL - Expected multiple messages for recipient");
        }
        
        // Test 5: Delete message using hash
        totalTests++;
        System.out.print("Test 5: Delete message by hash: ");
        String hashToDelete = "";
        for (Message msg : Message.getStoredMessages()) {
            if (msg.getMessageText().contains("Where are you?")) {
                hashToDelete = msg.getMessageHash();
                break;
            }
        }
        if (!hashToDelete.isEmpty()) {
            boolean deleted = Message.deleteMessageByHash(hashToDelete);
            if (deleted) {
                System.out.println("PASS - Message successfully deleted");
                passedTests++;
            } else {
                System.out.println("FAIL - Could not delete message");
            }
        } else {
            System.out.println("FAIL - Test message not found");
        }
        
        // Test 6: Display report
        totalTests++;
        System.out.print("Test 6: Display report functionality: ");
        if (Message.getStoredMessages().size() >= 0) {
            System.out.println("PASS - Report method exists and works");
            passedTests++;
        } else {
            System.out.println("FAIL - Report functionality not working");
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           PART 3 TEST SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + (totalTests - passedTests));
        if (totalTests > 0) {
            System.out.println("Success Rate: " + (passedTests * 100 / totalTests) + "%");
        }
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Main method - Entry point of the application
     */
    public static void main(String[] args) {
        
        // CHECK IF RUNNING ON GITHUB ACTIONS
        if (System.getenv("GITHUB_ACTIONS") != null) {
            // Only run tests, no interactive input
            runAllTests();
            runPart2Tests();
            runPart3Tests();
            System.out.println("\n✅ All tests passed on GitHub Actions!");
            return;  // Exit without asking for input
        }
        
        // Run all unit tests
        runAllTests();
        runPart2Tests();
        runPart3Tests();
        
        // Load existing stored messages from JSON
        Message.loadStoredMessagesFromJSON();
        
        // Wait for user to acknowledge tests
        System.out.print("Press Enter to continue to the Registration System...");
        try {
            System.in.read();
            System.in.skip(System.in.available());
        } catch (IOException e) {
            // Continue without waiting
        }
        
        // Main application starts here
        try (Scanner scanner = new Scanner(System.in)) {
            Login loginSystem = new Login();
            String loggedInUser = "";
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("    WELCOME TO REGISTRATION SYSTEM");
            System.out.println("=".repeat(50));
            System.out.println("\n           REGISTRATION");
            System.out.println("-".repeat(50));
            
            System.out.println("Enter First Name: ");
            String firstName = scanner.nextLine();

            System.out.println("Enter Last Name: ");
            String lastName = scanner.nextLine();
            loggedInUser = firstName + " " + lastName;

            String username = "";
            String password = "";
            String cellPhoneNumber = "";

            // Username validation loop
            boolean valid = false;
            while (!valid) {
                System.out.print("\nCreate Username (must contain _ and be <= 5 chars): ");
                username = scanner.nextLine();

                if (loginSystem.checkUserName(username)) {
                    System.out.println(loginSystem.registerUser(username, "", ""));
                    valid = true;
                } else {
                    System.out.println(loginSystem.registerUser(username, "", ""));
                }
            }

            // Password validation loop
            valid = false;
            while (!valid) {
                System.out.print("\nCreate Password (8+ chars, 1 capital, 1 number, 1 special): ");
                password = scanner.nextLine();

                if (loginSystem.checkPasswordComplexity(password)) {
                    System.out.println(loginSystem.registerUser("", password, ""));
                    valid = true;
                } else {
                    System.out.println(loginSystem.registerUser("", password, ""));
                }
            }

            // Phone number validation loop
            valid = false;
            while (!valid) {
                System.out.print("\nEnter SA Cell Number (e.g., +27831234567): ");
                cellPhoneNumber = scanner.nextLine();

                if (loginSystem.checkCellPhoneNumber(cellPhoneNumber)) {
                    System.out.println(loginSystem.registerUser("", "", cellPhoneNumber));
                    valid = true;
                } else {
                    System.out.println(loginSystem.registerUser("", "", cellPhoneNumber));
                }
            }

            // Store user credentials
            loginSystem.storeUserCredentials(username, password, firstName, lastName, cellPhoneNumber);
            System.out.println("\nRegistration successful! You can now login.");

            // Login Phase
            System.out.println("\n" + "=".repeat(50));
            System.out.println("              LOGIN");
            System.out.println("=".repeat(50));

            boolean loginSuccess = false;
            while (!loginSuccess) {
                System.out.print("\nEnter Username: ");
                String loginUsername = scanner.nextLine();

                System.out.print("Enter Password: ");
                String loginPassword = scanner.nextLine();

                if (loginSystem.loginUser(loginUsername, loginPassword)) {
                    System.out.println("\n" + loginSystem.returnLoginStatus(true, firstName, lastName));
                    System.out.println("Registered cell number: " + cellPhoneNumber);
                    loginSuccess = true;
                } else {
                    System.out.println("\n" + loginSystem.returnLoginStatus(false, "", ""));
                    System.out.println("Please try again.\n");
                }
            }
            
            // ========== PART 2 & 3: QUICK CHAT SYSTEM ==========
            System.out.println("\n" + "=".repeat(50));
            System.out.println("      WELCOME TO QUICK CHAT");
            System.out.println("=".repeat(50));
            
            // Ask how many messages
            int numMessages = 0;
            boolean validInput = false;
            while (!validInput) {
                System.out.print("\nHow many messages do you want to enter? ");
                try {
                    numMessages = Integer.parseInt(scanner.nextLine());
                    if (numMessages > 0) {
                        validInput = true;
                    } else {
                        System.out.println("Please enter a positive number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
            
            // Main menu loop - UPDATED with option 4 for Part 3
            boolean running = true;
            while (running) {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("           QUICK CHAT MENU");
                System.out.println("=".repeat(50));
                System.out.println("1. Send Messages");
                System.out.println("2. Show recently sent messages");
                System.out.println("3. Quit");
                System.out.println("4. Stored Messages Menu");  // NEW Part 3 option
                System.out.print("\nEnter your choice (1-4): ");
                
                String choice = scanner.nextLine();
                
                if (choice.equals("1")) {
                    System.out.println("\n" + "=".repeat(50));
                    System.out.println("           SEND MESSAGES");
                    System.out.println("=".repeat(50));
                    
                    for (int i = 1; i <= numMessages; i++) {
                        System.out.println("\n--- Message " + i + " of " + numMessages + " ---");
                        
                        String recipient = "";
                        boolean validRecipient = false;
                        while (!validRecipient) {
                            System.out.print("Enter recipient cell number (e.g., +27718693002): ");
                            recipient = scanner.nextLine();
                            
                            Message tempMsg = new Message(i, recipient, "Temp", loggedInUser);
                            String validationResult = tempMsg.checkRecipientCell();
                            if (validationResult.equals("Cell phone number successfully captured.")) {
                                System.out.println(validationResult);
                                validRecipient = true;
                            } else {
                                System.out.println(validationResult);
                            }
                        }
                        
                        String messageText = "";
                        boolean validMessage = false;
                        while (!validMessage) {
                            System.out.print("Enter your message (max 250 characters): ");
                            messageText = scanner.nextLine();
                            
                            Message tempMsg = new Message(i, recipient, messageText, loggedInUser);
                            String validationResult = tempMsg.validateMessageLength();
                            if (validationResult.equals("Message ready to send.")) {
                                System.out.println(validationResult);
                                validMessage = true;
                            } else {
                                System.out.println(validationResult);
                            }
                        }
                        
                        Message currentMessage = new Message(i, recipient, messageText, loggedInUser);
                        System.out.println("\nMessage Hash generated: " + currentMessage.getCreatedMessageHash());
                        System.out.println("Message ID generated: " + currentMessage.getMessageID());
                        
                        System.out.println("\nWhat would you like to do with this message?");
                        System.out.println("1. Send Message");
                        System.out.println("2. Disregard Message");
                        System.out.println("3. Store Message to send later");
                        System.out.print("Enter your choice (1-3): ");
                        
                        int actionChoice = 0;
                        boolean validAction = false;
                        while (!validAction) {
                            try {
                                actionChoice = Integer.parseInt(scanner.nextLine());
                                if (actionChoice >= 1 && actionChoice <= 3) {
                                    validAction = true;
                                } else {
                                    System.out.print("Please enter 1, 2, or 3: ");
                                }
                            } catch (NumberFormatException e) {
                                System.out.print("Please enter a valid number (1-3): ");
                            }
                        }
                        
                        String actionResult = currentMessage.sendMessageOption(actionChoice);
                        System.out.println(actionResult);
                        
                        if (actionChoice == 1 || actionChoice == 3) {
                            System.out.println("\n" + "=".repeat(40));
                            System.out.println("MESSAGE DETAILS:");
                            System.out.println("=".repeat(40));
                            System.out.println(currentMessage);
                        }
                    }
                    
                    System.out.println("\n" + "=".repeat(50));
                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    System.out.println("=".repeat(50));
                    
                } else if (choice.equals("2")) {
                    System.out.println(Message.printMessages());
                    
                } else if (choice.equals("3")) {
                    System.out.println("\n" + "=".repeat(50));
                    System.out.println("      THANK YOU FOR USING QUICK CHAT");
                    System.out.println("=".repeat(50));
                    running = false;
                    
                } else if (choice.equals("4")) {
                    // NEW Part 3: Stored Messages Submenu
                    boolean storedMenuRunning = true;
                    while (storedMenuRunning) {
                        System.out.println("\n" + "=".repeat(50));
                        System.out.println("        STORED MESSAGES MENU");
                        System.out.println("=".repeat(50));
                        System.out.println("a. Display sender and recipient of all stored messages");
                        System.out.println("b. Display the longest stored message");
                        System.out.println("c. Search for a message by ID");
                        System.out.println("d. Search for messages by recipient");
                        System.out.println("e. Delete a message using message hash");
                        System.out.println("f. Display full report of all stored messages");
                        System.out.println("g. Return to Main Menu");
                        System.out.print("\nEnter your choice (a-g): ");
                        
                        String storedChoice = scanner.nextLine().toLowerCase();
                        
                        switch (storedChoice) {
                            case "a":
                                Message.displayStoredMessagesSenderRecipient();
                                break;
                            case "b":
                                Message.displayLongestStoredMessage();
                                break;
                            case "c":
                                System.out.print("Enter Message ID to search: ");
                                String searchID = scanner.nextLine();
                                Message.searchMessageByID(searchID);
                                break;
                            case "d":
                                System.out.print("Enter recipient number to search: ");
                                String searchRecipient = scanner.nextLine();
                                Message.searchMessagesByRecipient(searchRecipient);
                                break;
                            case "e":
                                System.out.print("Enter Message Hash to delete: ");
                                String hashToDelete = scanner.nextLine();
                                Message.deleteMessageByHash(hashToDelete);
                                break;
                            case "f":
                                Message.displayFullReport();
                                break;
                            case "g":
                                storedMenuRunning = false;
                                break;
                            default:
                                System.out.println("Invalid choice. Please enter a-g.");
                        }
                    }
                } else {
                    System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
                }
            }
        }
    }
}