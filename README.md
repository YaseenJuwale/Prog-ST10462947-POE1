**Q1 Registration:**
The username must contain ≤5 characters and an underscore (_). A password must have at least eight characters, one capital, one number, and one special character. For SA format (+27 then 9 digits),
cell phones utilize the regex ^\\+27[6-8][0-9]{8}$. Every success/failure message precisely complies with the requirements.

**Q2 Login:** Verifies entered credentials against stored information. Congratulations: "Welcome <firstname> <lastname> it is great to see you again." Failed: "Username or password incorrect,
please try again."

**Q3 Methods:** CheckUserName, checkPasswordComplexity, checkCellPhoneNumber, registerUser, loginUser, and returnLoginStatus are the six implemented methods.

**Q4 Unit Tests:** Using the necessary test data, all fourteen tests (six assertEquals and eight assertTrue/False) passed 100%.
**Link**:https://rb.gy/vwf3ub

**Link**:https://github.com/YaseenJuwale/Prog-ST10462947-POE1

**PART 2**
**Overview**
By enabling authenticated users to send, store, or ignore messages, the QuickChat Messaging System expands the login application. Message hashing, 
JSON file storage, and automated message ID generation are all elements of the system.

**Important Features**
Management of Messages:

-Create 10-digit unique message IDs at random.

-Make message hashes using the syntax XX:X:FIRSTLAST.

-Verify the message's maximum length of 250 characters.

-Verify the recipient's cell phone number (+27 format).

**User Choices:**
-Send Message: Sends and saves the message right away.
-Disregard Message: Removes and cancels the message.
-Store Message: Saves a message to a JSON file so it can be sent later.

**Automated Examination:**
-Six automated unit tests that cover every aspect of message functionality
-Tests are automatically launched when an application is launched.
-100% of test cases are passed.

**Technical Execution**
-For message entry loop (user-specified message count)
-Using string manipulation to create hashes
-Validating phone numbers by pattern matching
-interface that is menu-driven and loops until it is quit
