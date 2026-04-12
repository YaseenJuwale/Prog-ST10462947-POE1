import java.io.IOException;
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
     * 
    
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

// ── Main class with built-in unit tests ───────────────────────────────────────
public class Main {
    
    /**
     * Runs all unit tests for the Login class
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
     * Main method - Entry point of the application
     */
    public static void main(String[] args) {
        
        // Run all unit tests first
        runAllTests();
        
        
       // Wait for user to acknowledge tests
System.out.print("Press Enter to continue to the Registration System...");
try {
    System.in.read();
    // Clear the newline character from the buffer
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
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("      THANK YOU FOR USING OUR SYSTEM");
            System.out.println("=".repeat(50));
        }
    }
}