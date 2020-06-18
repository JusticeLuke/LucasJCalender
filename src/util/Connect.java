/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.gluonhq.impl.charm.a.b.b.e;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Lucas
 */
public class Connect {
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U063aP";
    
    private static final String jdbcURL = protocol + vendorName + ipAddress;
    
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;
    
    private static final String username = "U063aP";
    private static String password = "53688677096";
    
    public static Connection startConnection(){
        try{
            Class.forName(MYSQLJDBCDriver);            
            conn = (Connection)DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful");
        }catch(ClassNotFoundException e){
            System.out.println("Error: " + e.getMessage());
                    
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        
        return conn;
        
    }
    
    public static void closeConnection() 
    {
        try{
            conn.close();
            System.out.println("Connection closed.");
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        
        
    }
    
    public static Connection getConnection()
    {        
        return conn;       
    }
    
}
