package com.utkbiodynamics.dashboard.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckExists {
    public static boolean checkExists(String login ) throws Exception {
        boolean exists = false;
        Connection connection=null;
        PreparedStatement stmt=null;
        
        try{
            connection=DBConnect.getConnection();
           stmt = connection.prepareStatement("select * from memberlogins where email=?");
           stmt.setString(1, login);
                          
                 try {                   
                     ResultSet rs = stmt.executeQuery();                                     
                     try {                      
                         if (rs.next()) {                             
                          exists = true;                            
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
        
        return exists;
    }
    }
        
        
    


