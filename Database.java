package no.tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database{
    public static Connection connect() {
    	String url = "jdbc:sqlite:C:/sqlite/db/cards.db";
        Connection conn = null;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return conn;
    }
    
    
//    public void insert
    
    
    
    
    
    
    
    
    
    
}
//        finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException ex) {
//                System.out.println(ex.getMessage());
//            }
//        }