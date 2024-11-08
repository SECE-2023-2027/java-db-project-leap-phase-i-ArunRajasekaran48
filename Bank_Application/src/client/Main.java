package client;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
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
                            account = checkAccountExist(userName, password, details);

                            if (account != null) {
                                System.out.println("User has been logged in with Account no " + account.accountNumber);
                                TransactionDetails.put(account.userName, new ArrayList<Transaction>());
                                
                                // User-specific options after login
                                boolean userFlag = true;
                       
                                while (userFlag) {
                                    System.out.println("1. Check Balance\n2. Deposit\n3. Withdraw\n4. Transfer\n5. Transaction History\n6. Logout");
                                    int userChoice = sc.nextInt();

                                    switch (userChoice) {
                                        case 1:
                                            System.out.println("Your Account Balance is: " + account.balance);
                                            break;
                                        case 2:
                                            System.out.println("Enter deposit amount: ");
                                            double depositAmount = sc.nextDouble();
                                            if (depositAmount > 0) {
                                                account.balance += depositAmount;
                                                System.out.println("Deposit successful. New balance: " + account.balance);
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
                                                System.out.println("Withdrawal successful. New balance: " + account.balance);
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
                            Ad_account = checkAccountExistAdmin(adminName, password, Admin_details);

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
                                            System.out.println("Enter username:");
                                            String newUserName = sc.next();
                                            System.out.println("Enter password:");
                                            String newPassword = sc.next();
                                            System.out.println("Enter balance:");
                                            double newBalance = sc.nextDouble();
                                            Account newAccount = new Account(100 + details.size(), newUserName, "Savings", newBalance, newPassword, 70123456, "Default Address", "Default Branch");
                                            details.put(newUserName, newAccount);
                                            System.out.println("Account created successfully.");
                                            break;
                                        case 2:
                                            System.out.println("Displaying all accounts:");
                                            for (String userName : details.keySet()) {
                                                Account acc = details.get(userName);
                                                System.out.println("User: " + acc.getUserName() + " | Balance: " + acc.getBalance());
                                            }
                                            break;
                                        case 3:
                                            System.out.println("Enter account number to deactivate: ");
                                            long deactivateAccountNumber = sc.nextLong();
                                            boolean accountFound = false;
                                            for (String userName : details.keySet()) {
                                                Account acc = details.get(userName);
                                                if (acc.getAccountNumber() == deactivateAccountNumber) {
                                                    details.remove(userName);  
                                                    System.out.println("Account with account number " + deactivateAccountNumber + " has been deactivated.");
                                                    accountFound = true;
                                                    break;
                                                }
                                            }
                                            
                                            if (!accountFound) {
                                                System.out.println("Account not found.");
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
                    System.out.println("Logged out.");
                    account = null;
                    Ad_account = null;
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static Account checkAccountExist(String userName, String password, HashMap<String, Account> details) {
        Account account = details.get(userName);
        if (account != null && account.password.equals(password)) {
            return account;
        }
        return null;
    }

    private static Admin checkAccountExistAdmin(String adminName, String password, HashMap<String, Admin> Admin_details) {
        Admin admin = Admin_details.get(adminName);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
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
