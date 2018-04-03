package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectDB {

    private final static String DRIVER_NAME = "com.mysql.jdbc.Driver";
	private final static String URL = "jdbc:mysql://localhost:3306/";
	private final static String USERNAME = "root";
	private final static String PASSWORD = "root";
	private final static String DATABASE = "clinic_tool";
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection con = null; 
        
		try {
			Class.forName(DRIVER_NAME);
                    con = DriverManager.getConnection(URL + DATABASE + 
                    "?autoReconnect=true&useSSL=false", USERNAME, PASSWORD);
            System.out.println("SUCCESS! DATABASE CONNECTION ESTABLISHED");
		} catch (ClassNotFoundException | SQLException ex){
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
		return con; 
	}
	
}
