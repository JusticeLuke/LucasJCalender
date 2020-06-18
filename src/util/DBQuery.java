/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Lucas
 */
public class DBQuery {
    
    private static PreparedStatement statement;
    
    //Create Statement Object
    public static void setPreparedStatement(Connection conn, String sqlStatemetnt) throws SQLException
    {        
       statement = conn.prepareStatement(sqlStatemetnt); 
    }
    
    //Return statement object
    public static PreparedStatement getPreparedStatement()
    {
        return statement;
    }
}
