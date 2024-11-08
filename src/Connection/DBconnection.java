package Connection;
import java.sql.Connection;
import java.sql.DriverManager;
public class DBconnection {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "Arun@1209#");
            System.out.println("Connection established successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static void main(String[] args) {
        Connection conn = DBconnection.getConnection();
    }
}
