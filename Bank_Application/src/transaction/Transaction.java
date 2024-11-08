package transaction;
import account.Account;
public class Transaction extends Account {
        public int fromAccountId;
        public int toAccountId;
        public int transactionId;
        public long transactionAccount;
        public String date;
        public String transactionType;
        
		public Transaction(int fromAccountId, int toAccountId, int transactionId, long accountNumber, String date,
				String transactionType) {
			this.fromAccountId = fromAccountId;
			this.toAccountId = toAccountId;
			this.transactionId = transactionId;
			this.transactionAccount = accountNumber;
			this.date = date;
			this.transactionType = transactionType;
		}
		public Transaction(){};
		public int getFromAccountId() {
			return fromAccountId;
		}
		public void setFromAccountId(int fromAccountId) {
			this.fromAccountId = fromAccountId;
		}
		public int getToAccountId() {
			return toAccountId;
		}
		public void setToAccountId(int toAccountId) {
			this.toAccountId = toAccountId;
		}
		public int getTransactionId() {
			return transactionId;
		}
		public void setTransactionId(int transactionId) {
			this.transactionId = transactionId;
		}
		public long getTransactionAccount() {
			return transactionAccount;
		}
		public void setTransactionAccount(long transactionAccount) {
			this.transactionAccount = transactionAccount;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getTransactionType() {
			return transactionType;
		}
		public void setTransactionType(String transactionType) {
			this.transactionType = transactionType;
		}
        
}
