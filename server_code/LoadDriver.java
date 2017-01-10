import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoadDriver {
    public static void main(String[] args) {
        try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
		System.out.println("register unsuccessful");
	}
}
}
            
