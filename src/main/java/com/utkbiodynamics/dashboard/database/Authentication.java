package com.utkbiodynamics.dashboard.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// This class controls the authentication to the database
// current it is hard-coded
// need to implement a true authentication for mySQL database
public class Authentication {
    public static boolean userAuthentication(String login, String password) {
        boolean authenticated = false;
        Connection connection=null;
        PreparedStatement stmt=null;
        
        try{
            connection=DBConnect.getConnection();
           stmt = connection.prepareStatement("select * from memberlogins where email=? and password=?");
           stmt.setString(1, login);
           stmt.setString(2, password);                 
                 try {                   
                     ResultSet rs = stmt.executeQuery();                                     
                     try {                      
                         if (rs.next()) {                             
                          authenticated = true;                            
                         } //end of while()                      
                     } finally {                     
                         rs.close();                     
                     }                   
                 } finally {
                     stmt.close();                  
                 }

             } catch(SQLException e) {               
                 //System.out.println("Could not login from dataabse:" + e.getMessage());                
             }             
                                       
                               
         //end of if (connection != null)      
        return authenticated;   
    }
}
