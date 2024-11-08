package account;
public class Account {
	 public long accountNumber;
	public String userName;
    public String accountType;
    public double balance;
    public String password;
    public long phoneNumber;
    public String address;
    public String branchName;
     public Account(long accountNumber,String userName, String accountType, double balance, String password,
			long phoneNumber, String address, String branchName) {
		this.userName = userName;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.balance = balance;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.branchName = branchName;
	}
     public Account() {}
     public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
}
