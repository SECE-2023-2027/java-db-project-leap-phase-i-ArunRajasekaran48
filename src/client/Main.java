package client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import Connection.DBconnection;
import account.Account;
import admin.Admin;
import transaction.Transaction;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashMap<String, Account> details = new HashMap<>();
        HashMap<String, Admin> Admin_details = new HashMap<>();
        HashMap<String, ArrayList<Transaction>> TransactionDetails = new HashMap<>();

        // Predefined user and admin details
        details.put("Arun", new Account(0001,"Arun","Savings",1000,"arun@123",6374181274L,"Coimbatore","Kinathukadavu"));
        Admin_details.put("Boss", new Admin(102, "Boss", "12345"));
        
        Admin Ad_account = null;
        Account account = null;
        boolean flag = true;

        while (flag) {
        	System.out.println("============================");
        	System.out.println("Welcome to Bank Application:");
        	System.out.println("============================");
            System.out.println("1. Login\n2. Logout");
            int eventType = sc.nextInt();

            switch (eventType) {
                case 1:
                    System.out.println("Login (Admin/User):");
                    String ch = sc.next();
                    
                    // Login for User
                    if (ch.equals("User")) {
                        if (account == null) {
                            System.out.println("Enter Username :");
                            String userName = sc.next();
                            System.out.println("Enter Password :");
                            String password = sc.next();
                            account = checkAccountExist(userName, password);

                            if (account != null) {
                                System.out.println("User has been logged in with Account ID " + account.accountNumber);
                                TransactionDetails.put(account.userName, new ArrayList<Transaction>());
                                
                                // User-specific options after login
                                boolean userFlag = true;
                       
                                while (userFlag) {
                                    System.out.println("1. Check Balance\n2. Deposit\n3. Withdraw\n4. Transfer\n5. Transaction History\n6. Logout");
                                    int userChoice = sc.nextInt();

                                    switch (userChoice) {
                                    case 1:
                                        // Display current account balance from the database
                                        try (Connection conn = DBconnection.getConnection()) {
                                            String query = "SELECT balance FROM account WHERE name = ?";
                                            PreparedStatement pstmt = conn.prepareStatement(query);
                                            pstmt.setString(1, account.getUserName());
                                            ResultSet rs = pstmt.executeQuery();
                                            if (rs.next()) {
                                                double balance = rs.getDouble("balance");
                                                System.out.println("Your Account Balance is: " + balance);
                                                account.setBalance(balance); // Update account object with latest balance
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Error fetching account balance: " + e.getMessage());
                                        }
                                        break;

                                    case 2:
                                        System.out.println("Enter deposit amount: ");
                                        double depositAmount = sc.nextDouble();
                                        if (depositAmount > 0) {
                                            account.balance += depositAmount;

                                            // Update balance in the database
                                            try (Connection conn = DBconnection.getConnection()) {
                                                String updateQuery = "UPDATE account SET balance = ? WHERE name = ?";
                                                PreparedStatement pstmt = conn.prepareStatement(updateQuery);
                                                pstmt.setDouble(1, account.balance);
                                                pstmt.setString(2, account.getUserName());
                                                pstmt.executeUpdate();
                                                System.out.println("Deposit successful. New balance: " + account.balance);
                                            } catch (Exception e) {
                                                System.out.println("Error updating balance: " + e.getMessage());
                                            }

                                            addTransaction(TransactionDetails, account, "Deposit", depositAmount);
                                        } else {
                                            System.out.println("Invalid deposit amount.");
                                        }
                                        break;

                                    case 3:
                                        System.out.println("Enter withdrawal amount: ");
                                        double withdrawAmount = sc.nextDouble();
                                        if (account.balance >= withdrawAmount) {
                                            account.balance -= withdrawAmount;

                                            // Update balance in the database
                                            try (Connection conn = DBconnection.getConnection()) {
                                                String updateQuery = "UPDATE account SET balance = ? WHERE name = ?";
                                                PreparedStatement pstmt = conn.prepareStatement(updateQuery);
                                                pstmt.setDouble(1, account.balance);
                                                pstmt.setString(2, account.getUserName());
                                                pstmt.executeUpdate();
                                                System.out.println("Withdrawal successful. New balance: " + account.balance);
                                            } catch (Exception e) {
                                                System.out.println("Error updating balance: " + e.getMessage());
                                            }

                                            addTransaction(TransactionDetails, account, "Withdrawal", withdrawAmount);
                                        } else {
                                            System.out.println("Insufficient balance.");
                                        }
                                        break;
                                        case 4:
                                            System.out.println("Enter transfer amount: ");
                                            double transferAmount = sc.nextDouble();

                                            if (account.balance >= transferAmount) {
                                                System.out.println("Enter recipient account number: ");
                                                long recipientAccount = sc.nextLong();

                                                // Check if the recipient account is the same as the sender account
                                                if (account.accountNumber == recipientAccount) {
                                                    System.out.println("You cannot transfer money to your own account.");
                                                } else {
                                                    account.balance -= transferAmount;
                                                    System.out.println("Transfer successful. New balance: " + account.balance);
                                                    addTransaction(TransactionDetails, account, "Transfer", transferAmount);
                                                }
                                            } else {
                                                System.out.println("Insufficient balance.");
                                            }
                                            break;
                                        case 5: // Transaction History
                                            System.out.println("Transaction History:");
                                            ArrayList<Transaction> userTransactions = TransactionDetails.get(account.getUserName());
                                            if (userTransactions != null && !userTransactions.isEmpty()) {
                                                for (Transaction trans : userTransactions) {
                                                    System.out.println("Transaction ID: " + trans.getTransactionId() + ", Date: " + trans.getDate() + ", Type: " + trans.getTransactionType() + ", Amount: " + trans.getTransactionAccount());
                                                }
                                            } else {
                                                System.out.println("No transactions found.");
                                            }
                                            break;
                                        case 6:
                                            System.out.println("User has been logged out.");
                                            userFlag = false;
                                            account = null;
                                            break;
                                        default:
                                            System.out.println("Invalid choice.");
                                    }
                                }
                            } else {
                                System.out.println("Invalid login credentials.");
                            }
                        } else {
                            System.out.println("User is already logged in.");
                        }
                    }

                    // Login for Admin
                    else if (ch.equals("Admin")) {
                        if (Ad_account == null) {
                            System.out.println("Enter Admin Name :");
                            String adminName = sc.next();
                            System.out.println("Enter Password :");
                            String password = sc.next();
                            Ad_account = checkAccountExistAdmin(adminName, password);

                            if (Ad_account != null) {
                                System.out.println("Admin has been logged in with ID " + Ad_account.getId());

                                // Admin-specific options after login
                                boolean adminFlag = true;
                                while (adminFlag) {
                                    System.out.println("1. Create Account\n2. View All Accounts\n3. Deactivate Account\n4. Logout");
                                    int adminChoice = sc.nextInt();

                                    switch (adminChoice) {
                                    case 1:
                                        System.out.println("Enter Account details...");
                                        System.out.print("Enter username: ");
                                        String newUserName = sc.next();

                                        System.out.print("Enter password: ");
                                        String newPassword = sc.next();

                                        System.out.print("Enter balance: ");
                                        double newBalance = sc.nextDouble();

                                        System.out.print("Enter phone number: ");
                                        String phone = sc.next();

                                        System.out.print("Enter address: ");
                                        sc.nextLine();  
                                        String address = sc.nextLine();

                                        System.out.print("Enter branch: ");
                                        String branch = sc.next();

                                        try (Connection conn = DBconnection.getConnection()) {
                                            String insertQuery = "INSERT INTO account (name, acctype, balance, pass, phone, address, branch) VALUES (?, 'Savings', ?, ?, ?, ?, ?)";
                                            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                                            pstmt.setString(1, newUserName);
                                            pstmt.setDouble(2, newBalance);
                                            pstmt.setString(3, newPassword);
                                            pstmt.setString(4, phone);
                                            pstmt.setString(5, address);
                                            pstmt.setString(6, branch);
                                            
                                            pstmt.executeUpdate();
                                            System.out.println("Account created successfully in database.");
                                        } catch (Exception e) {
                                            System.out.println("Error creating account: " + e.getMessage());
                                        }
                                        break;

                                        case 2:
                                            System.out.println("Displaying all accounts:");
                                            // Fetch all accounts from the account table
                                            try (Connection conn = DBconnection.getConnection()) {
                                                String selectQuery = "SELECT * FROM account";
                                                PreparedStatement pstmt = conn.prepareStatement(selectQuery);
                                                ResultSet rs = pstmt.executeQuery();
                                                if (!rs.isBeforeFirst()) { 
                                                    System.out.println("No accounts found in the database.");
                                                } else {
                                                    while (rs.next()) {
                                                        String userName = rs.getString("name");
                                                        double balance = rs.getDouble("balance");
                                                        System.out.println("User: " + userName + " | Balance: " + balance);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Error fetching accounts: " + e.getMessage());
                                            }
                                            break;


                                        case 3:
                                            System.out.println("Enter account Id to deactivate: ");
                                            int deactivateAccountNumber = sc.nextInt();
                                            // Delete the account from the account table
                                            try (Connection conn = DBconnection.getConnection()) {
                                                String deleteQuery = "DELETE FROM account WHERE accid = ?";
                                                PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                                                pstmt.setInt(1, deactivateAccountNumber);
                                                int rowsAffected = pstmt.executeUpdate();
                                                if (rowsAffected > 0) {
                                                    System.out.println("Account with accountId " + deactivateAccountNumber + " has been deactivated.");
                                                } else {
                                                    System.out.println("Account not found.");
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Error deactivating account: " + e.getMessage());
                                            }
                                            break;

                                        case 4:
                                            System.out.println("Admin has been logged out.");
                                            adminFlag = false;
                                            Ad_account = null;
                                            break;

                                        default:
                                            System.out.println("Invalid choice.");
                                    }
                                }
                            } else {
                                System.out.println("Invalid admin credentials.");
                            }
                        } else {
                            System.out.println("Admin is already logged in.");
                        }
                    }
                    break;
                case 2:
                    System.out.println(" Bank Application Logged out.\n\tThankYou!!");
                    account = null;
                    Ad_account = null;
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static Account checkAccountExist(String userName, String password) {
        Account account = null;
        try (Connection conn = DBconnection.getConnection()) {  
            String query = "SELECT * FROM account WHERE name = ? AND pass = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, userName);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int accountNumber = rs.getInt("accid");
                String name = rs.getString("name");
                String accountType = rs.getString("acctype");
                double balance = rs.getDouble("balance");
                long phoneNumber = rs.getLong("phone");
                String address = rs.getString("address");
                String branch = rs.getString("branch");
                account = new Account(accountNumber, name, accountType, balance, password, phoneNumber, address, branch);
            }
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
        return account;  
    }
    private static Admin checkAccountExistAdmin(String adminName, String password) {
        Admin admin = null;
        try (Connection conn = DBconnection.getConnection()) { 
            String query = "SELECT * FROM admin WHERE username = ? AND pass = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, adminName);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("adminid");
                admin = new Admin(id, adminName, password);
            }
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
        return admin;
    }
    private static void addTransaction(HashMap<String, ArrayList<Transaction>> TransactionDetails, Account account, String type, double amount) {
        int fromAccountId = 123;
        int toAccountId = 101;  
        int transactionId = 4567;
        String date = "12-Nov-2023";
        Transaction transaction = new Transaction(fromAccountId, toAccountId, transactionId++, account.getAccountNumber(), date, type);
        ArrayList<Transaction> prevTrans = TransactionDetails.get(account.getUserName());
        prevTrans.add(transaction);
        TransactionDetails.put(account.getUserName(), prevTrans);
    }
}
