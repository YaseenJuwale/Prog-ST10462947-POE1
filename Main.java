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

    /**
     * Method 1: checkUserName()
     * Ensures that any username contains an underscore (_) and is no more than 5 characters long.
     */
    public boolean checkUserName(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Method 2: checkPasswordComplexity()
     * Ensures passwords meet the following password complexity rules:
     * - At least eight characters long
     * - Contain a capital letter
     * - Contain a number
     * - Contain a special character
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";
        return Pattern.matches(regex, password);
    }

    /**
     * Method 3: checkCellPhoneNumber()
     * Ensures the cell phone is the correct length and contains the international country code.
     * Format: +27 followed by 9 digits (total 12 characters including +)
     * Mobile prefixes: 6, 7, or 8
     */
    public boolean checkCellPhoneNumber(String cellNumber) {
        if (cellNumber == null || cellNumber.isEmpty()) {
            return false;
        }
        String saPhoneRegex = "^\\+27[6-8][0-9]{8}$";
        return Pattern.matches(saPhoneRegex, cellNumber);
    }

    /**
     * Method 4: registerUser()
     * Returns the necessary registration messaging
     */
    public String registerUser(String username, String password, String cellNumber) {
        // Check username only
        if (username != null && !username.isEmpty()) {
            if (!checkUserName(username)) {
                return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
            }
            return "Username successfully captured.";
        }
        
        // Check password only
        if (password != null && !password.isEmpty()) {
            if (!checkPasswordComplexity(password)) {
                return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
            }
            return "Password successfully captured.";
        }
        
        // Check cell number only
        if (cellNumber != null && !cellNumber.isEmpty()) {
            if (!checkCellPhoneNumber(cellNumber)) {
                return "Cell phone number incorrectly formatted or does not contain international code.";
            }
            return "Cell phone number successfully added.";
        }
        
        return "Registration failed. Please check your inputs.";
    }

    /**
     * Method 5: loginUser()
     * Verifies that the login details entered match stored credentials
     */
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (enteredUsername == null || enteredPassword == null) {
            return false;
        }
        if (storedUsername == null || storedPassword == null) {
            return false;
        }
        return storedUsername.equals(enteredUsername) && storedPassword.equals(enteredPassword);
    }

    /**
     * Method 6: returnLoginStatus()
     * Returns the necessary messaging for login success or failure
     */
    public String returnLoginStatus(boolean isSuccess, String firstName, String lastName) {
        if (isSuccess) {
            return "Welcome " + firstName + " " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    /**
     * Stores user credentials after successful registration
     */
    public void storeUserCredentials(String username, String password, String firstName, 
        String lastName, String phoneNumber) {
        this.storedUsername = username;
        this.storedPassword = password;
        this.storedFirstName = firstName;
        this.storedLastName = lastName;
        this.storedPhoneNumber = phoneNumber;
    }

    // Getter methods for testing
    public String getStoredUsername() { return storedUsername; }
    public String getStoredFirstName() { return storedFirstName; }
    public String getStoredLastName() { return storedLastName; }
    public String getStoredPhoneNumber() { return storedPhoneNumber; }
}

// ── Message Class for Part 2 ─────────────────────────────────────────────────────
class Message {
    private String messageID;
    private int numMessagesSent;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String messageStatus;
    
    private static int totalMessagesSent = 0;
    private static List<Message> allMessages = new ArrayList<>();
    
    // Constructor
    public Message(int messageNumber, String recipient, String messageText) {
        this.numMessagesSent = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = generateMessageHash();
        this.messageStatus = "Created";
    }
    
    // Generates random 10-digit Message ID
    private String generateMessageID() {
        Random rand = new Random();
        long tenDigitNumber = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(tenDigitNumber);
    }
    
    // Method 1: checkMessageID()
    public boolean checkMessageID() {
        boolean isValid = messageID != null && messageID.length() == 10;
        return isValid;
    }
    
    // Method 2: checkRecipientCell()
    public String checkRecipientCell() {
        if (recipient == null || recipient.isEmpty()) {
            String errorMsg = "Cell phone number incorrectly formatted or does not contain international code. Please correct the number and try again.";
            return errorMsg;
        }
        String saPhoneRegex = "^\\+27[6-8][0-9]{8}$";
        if (Pattern.matches(saPhoneRegex, recipient)) {
            return "Cell phone number successfully captured.";
        } else {
            String errorMsg = "Cell phone number incorrectly formatted or does not contain international code. Please correct the number and try again.";
            return errorMsg;
        }
    }
    
    // Method 3: generateMessageHash()
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
    
    // Public method to get message hash
    public String getCreatedMessageHash() {
        return messageHash;
    }
    
    // Validate message length
    public String validateMessageLength() {
        if (messageText.length() > 250) {
            int excess = messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        } else {
            return "Message ready to send.";
        }
    }
    
    // Method 4: sendMessageOption() - allows user to choose send, store, or disregard
    public String sendMessageOption(int choice) {
        if (choice == 1) {
            this.messageStatus = "Sent";
            totalMessagesSent++;
            allMessages.add(this);
            storeMessageInJSON();
            return "Message successfully sent";
        } else if (choice == 2) {
            this.messageStatus = "Disregarded";
            return "Press 0 to delete the message - Message disregarded";
        } else if (choice == 3) {
            this.messageStatus = "Stored";
            allMessages.add(this);
            storeMessageInJSON();
            return "Message successfully stored";
        }
        return "Invalid option";
    }
    
    // Method to store message in JSON file (for full marks)
    public void storeMessageInJSON() {
        try {
            File file = new File("messages.json");
            StringBuilder jsonArrayContent = new StringBuilder();
            
            // Read existing content if file exists
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonArrayContent.append(line);
                }
                reader.close();
            }
            
            // Create JSON object for this message
            String jsonMessage = "{";
            jsonMessage += "\"messageID\":\"" + messageID + "\",";
            jsonMessage += "\"numMessagesSent\":" + numMessagesSent + ",";
            jsonMessage += "\"recipient\":\"" + recipient + "\",";
            jsonMessage += "\"message\":\"" + messageText.replace("\"", "\\\"") + "\",";
            jsonMessage += "\"messageHash\":\"" + messageHash + "\",";
            jsonMessage += "\"status\":\"" + messageStatus + "\"";
            jsonMessage += "}";
            
            // Write to file
            FileWriter writer = new FileWriter(file, true);
            writer.write(jsonMessage + "\n");
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Error storing message in JSON: " + e.getMessage());
        }
    }
    
    // Method 5: printMessages()
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
    
    // Method 6: returnTotalMessages()
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    // Getters
    public String getMessageID() { return messageID; }
    public int getNumMessagesSent() { return numMessagesSent; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    
    @Override
    public String toString() {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + messageText + "\n" +
               "Status: " + messageStatus;
    }
}

// ── Main class with built-in unit tests ───────────────────────────────────────
public class Main {
    
    /**
     * Runs all unit tests for the Login class (Part 1)
     */
    public static void runAllTests() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           RUNNING UNIT TESTS");
        System.out.println("=".repeat(60));
        
        Login login = new Login();
        int passedTests = 0;
        int totalTests = 0;
        
        // assertEquals Tests
        System.out.println("\n--- assertEquals Tests ---\n");
        
        // Test 1: Username correctly formatted
        totalTests++;
        System.out.print("Test 1: Username 'kyl_1' - ");
        String username = "kyl_1";
        boolean result1 = login.checkUserName(username);
        String message1 = login.registerUser(username, "", "");
        if (result1 && message1.equals("Username successfully captured.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 2: Username incorrectly formatted
        totalTests++;
        System.out.print("Test 2: Username 'kyle !!!!!!!' - ");
        String badUsername = "kyle !!!!!!!";
        boolean result2 = login.checkUserName(badUsername);
        String message2 = login.registerUser(badUsername, "", "");
        if (!result2 && message2.equals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 3: Password meets complexity
        totalTests++;
        System.out.print("Test 3: Password 'Ch&&sec@ke99!' - ");
        String password = "Ch&&sec@ke99!";
        boolean result3 = login.checkPasswordComplexity(password);
        String message3 = login.registerUser("", password, "");
        if (result3 && message3.equals("Password successfully captured.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 4: Password does not meet complexity
        totalTests++;
        System.out.print("Test 4: Password 'password' - ");
        String badPassword = "password";
        boolean result4 = login.checkPasswordComplexity(badPassword);
        String message4 = login.registerUser("", badPassword, "");
        if (!result4 && message4.equals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 5: Cell phone correctly formatted
        totalTests++;
        System.out.print("Test 5: Phone '+27838968976' - ");
        String phone = "+27838968976";
        boolean result5 = login.checkCellPhoneNumber(phone);
        String message5 = login.registerUser("", "", phone);
        if (result5 && message5.equals("Cell phone number successfully added.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 6: Cell phone incorrectly formatted
        totalTests++;
        System.out.print("Test 6: Phone '08966553' - ");
        String badPhone = "08966553";
        boolean result6 = login.checkCellPhoneNumber(badPhone);
        String message6 = login.registerUser("", "", badPhone);
        if (!result6 && message6.equals("Cell phone number incorrectly formatted or does not contain international code.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // assertTrue/False Tests
        System.out.println("\n--- assertTrue/False Tests ---\n");
        
        // Test 7: Login Successful
        totalTests++;
        System.out.print("Test 7: Login Successful - ");
        login.storeUserCredentials("john_1", "Pass@1234", "John", "Doe", "+27831234567");
        boolean loginSuccess = login.loginUser("john_1", "Pass@1234");
        if (loginSuccess) {
            System.out.println("PASS - Expected: true, Actual: true");
            passedTests++;
        } else {
            System.out.println("FAIL - Expected: true, Actual: false");
        }
        
        // Test 8: Login Failed
        totalTests++;
        System.out.print("Test 8: Login Failed - ");
        boolean loginFail = login.loginUser("john_1", "WrongPass");
        if (!loginFail) {
            System.out.println("PASS - Expected: false, Actual: false");
            passedTests++;
        } else {
            System.out.println("FAIL - Expected: false, Actual: true");
        }
        
        // Test 9: Username correctly formatted assertTrue
        totalTests++;
        System.out.print("Test 9: Username correctly formatted - ");
        boolean usernameValid = login.checkUserName("kyl_1");
        if (usernameValid) {
            System.out.println("PASS - Expected: true, Actual: true");
            passedTests++;
        } else {
            System.out.println("FAIL - Expected: true, Actual: false");
        }
        
        // Test 10: Username incorrectly formatted assertFalse
        totalTests++;
        System.out.print("Test 10: Username incorrectly formatted - ");
        boolean usernameInvalid = login.checkUserName("kyle");
        if (!usernameInvalid) {
            System.out.println("PASS - Expected: false, Actual: false");
            passedTests++;
        } else {
            System.out.println("FAIL - Expected: false, Actual: true");
        }
        
        // Test 11: Password meets complexity assertTrue
        totalTests++;
        System.out.print("Test 11: Password meets complexity - ");
        boolean passwordValid = login.checkPasswordComplexity("Ch&&sec@ke99!");
        if (passwordValid) {
            System.out.println("PASS - Expected: true, Actual: true");
            passedTests++;
        } else {
            System.out.println("FAIL - Expected: true, Actual: false");
        }
        
        // Test 12: Password does not meet complexity assertFalse
        totalTests++;
        System.out.print("Test 12: Password does not meet complexity - ");
        boolean passwordInvalid = login.checkPasswordComplexity("password");
        if (!passwordInvalid) {
            System.out.println("PASS - Expected: false, Actual: false");
            passedTests++;
        } else {
            System.out.println("FAIL - Expected: false, Actual: true");
        }
        
        // Test 13: Cell phone correctly formatted assertTrue
        totalTests++;
        System.out.print("Test 13: Cell phone correctly formatted - ");
        boolean phoneValid = login.checkCellPhoneNumber("+27838968976");
        if (phoneValid) {
            System.out.println("PASS - Expected: true, Actual: true");
            passedTests++;
        } else {
            System.out.println("FAIL - Expected: true, Actual: false");
        }
        
        // Test 14: Cell phone incorrectly formatted assertFalse
        totalTests++;
        System.out.print("Test 14: Cell phone incorrectly formatted - ");
        boolean phoneInvalid = login.checkCellPhoneNumber("08966553");
        if (!phoneInvalid) {
            System.out.println("PASS - Expected: false, Actual: false");
            passedTests++;
        } else {
            System.out.println("FAIL - Expected: false, Actual: true");
        }
        
        // Test Summary
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           TEST SUMMARY");
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
        System.out.print("\nTest 1: Message length (success - under 250 chars): ");
        Message testMsg1 = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String result1 = testMsg1.validateMessageLength();
        if (result1.equals("Message ready to send.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL - Got: " + result1);
        }
        
        // Test 2: Message length validation - Failure
        totalTests++;
        System.out.print("Test 2: Message length (failure - over 250 chars): ");
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 260; i++) {
            longText.append("a");
        }
        Message testMsg2 = new Message(2, "+27718693002", longText.toString());
        String result2 = testMsg2.validateMessageLength();
        if (result2.contains("exceeds 250 characters by")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL - Got: " + result2);
        }
        
        // Test 3: Recipient cell number validation - Success
        totalTests++;
        System.out.print("Test 3: Recipient cell number (success - +27718693002): ");
        Message testMsg3 = new Message(3, "+27718693002", "Test message");
        String result3 = testMsg3.checkRecipientCell();
        if (result3.equals("Cell phone number successfully captured.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL - Got: " + result3);
        }
        
        // Test 4: Recipient cell number validation - Failure
        totalTests++;
        System.out.print("Test 4: Recipient cell number (failure - 08575975889): ");
        Message testMsg4 = new Message(4, "08575975889", "Test message");
        String result4 = testMsg4.checkRecipientCell();
        if (result4.equals("Cell phone number incorrectly formatted or does not contain international code. Please correct the number and try again.")) {
            System.out.println("PASS");
            passedTests++;
        } else {
            System.out.println("FAIL - Got: " + result4);
        }
        
        // Test 5: Message Hash creation
        totalTests++;
        System.out.print("Test 5: Message Hash creation: ");
        Message testMsg5 = new Message(1, "+27718693002", "Hi Mike");
        String hash5 = testMsg5.getCreatedMessageHash();
        if (hash5.length() > 0) {
            System.out.println("PASS - Hash: " + hash5);
            passedTests++;
        } else {
            System.out.println("FAIL");
        }
        
        // Test 6: Message ID creation
        totalTests++;
        System.out.print("Test 6: Message ID created: ");
        if (testMsg5.checkMessageID() && testMsg5.getMessageID().length() == 10) {
            System.out.println("PASS - ID: " + testMsg5.getMessageID());
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
        System.out.println("Success Rate: " + (passedTests * 100 / totalTests) + "%");
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Main method - Entry point of the application
     */
    public static void main(String[] args) {
        
        // Run all unit tests first
        runAllTests();
        
        // Run Part 2 tests
        runPart2Tests();
        
        // Wait for user to acknowledge tests
        System.out.print("Press Enter to continue to the Registration System...");
        try {
            System.in.read();
            System.in.skip(System.in.available());
        } catch (IOException e) {
            // Continue without waiting if there's an error
        }
        
        // Main application starts here
        try (Scanner scanner = new Scanner(System.in)) {
            Login loginSystem = new Login();
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("    WELCOME TO REGISTRATION SYSTEM");
            System.out.println("=".repeat(50));
            System.out.println("\n           REGISTRATION");
            System.out.println("-".repeat(50));
            
            System.out.println("Enter First Name: ");
            String firstName = scanner.nextLine();

            System.out.println("Enter Last Name: ");
            String lastName = scanner.nextLine();

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
            
            // ========== PART 2: QUICK CHAT SYSTEM ==========
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
            
            // Main menu loop
            boolean running = true;
            while (running) {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("           QUICK CHAT MENU");
                System.out.println("=".repeat(50));
                System.out.println("1. Send Messages");
                System.out.println("2. Show recently sent messages");
                System.out.println("3. Quit");
                System.out.print("\nEnter your choice (1-3): ");
                
                String choice = scanner.nextLine();
                
                if (choice.equals("1")) {
                    System.out.println("\n" + "=".repeat(50));
                    System.out.println("           SEND MESSAGES");
                    System.out.println("=".repeat(50));
                    
                    // FOR loop for messages
                    for (int i = 1; i <= numMessages; i++) {
                        System.out.println("\n--- Message " + i + " of " + numMessages + " ---");
                        
                        // Get recipient
                        String recipient = "";
                        boolean validRecipient = false;
                        while (!validRecipient) {
                            System.out.print("Enter recipient cell number (e.g., +27718693002): ");
                            recipient = scanner.nextLine();
                            
                            Message tempMsg = new Message(i, recipient, "Temp");
                            String validationResult = tempMsg.checkRecipientCell();
                            if (validationResult.equals("Cell phone number successfully captured.")) {
                                System.out.println(validationResult);
                                validRecipient = true;
                            } else {
                                System.out.println(validationResult);
                            }
                        }
                        
                        // Get message
                        String messageText = "";
                        boolean validMessage = false;
                        while (!validMessage) {
                            System.out.print("Enter your message (max 250 characters): ");
                            messageText = scanner.nextLine();
                            
                            Message tempMsg = new Message(i, recipient, messageText);
                            String validationResult = tempMsg.validateMessageLength();
                            if (validationResult.equals("Message ready to send.")) {
                                System.out.println(validationResult);
                                validMessage = true;
                            } else {
                                System.out.println(validationResult);
                            }
                        }
                        
                        // Create message
                        Message currentMessage = new Message(i, recipient, messageText);
                        System.out.println("\nMessage Hash generated: " + currentMessage.getCreatedMessageHash());
                        System.out.println("Message ID generated: " + currentMessage.getMessageID());
                        
                        // Options menu
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
                        
                        // Display message details if sent or stored
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
                    System.out.println("\n" + "=".repeat(50));
                    System.out.println("Coming Soon - This feature is still in development.");
                    System.out.println(Message.printMessages());
                    System.out.println("=".repeat(50));
                    
                } else if (choice.equals("3")) {
                    System.out.println("\n" + "=".repeat(50));
                    System.out.println("      THANK YOU FOR USING QUICK CHAT");
                    System.out.println("=".repeat(50));
                    running = false;
                    
                } else {
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
        }
    }
}