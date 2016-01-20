package com.utkbiodynamics.dashboard.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnect {
 
    public static Connection getConnection()
    {
     Connection con = null;
     
     try 
     {
    Class.forName("com.mysql.jdbc.Driver");
    
    //Production Server
    //con  =DriverManager.getConnection("jdbc:mysql://localhost:3307/cloudecg?autoReconnect=true","cloudecg","cloudecg123");
    //Testing Server
    con  =DriverManager.getConnection("jdbc:mysql://localhost/cloudecg?autoReconnect=true","root","iCloudECG4238272483");

     } 
     catch (ClassNotFoundException e) 
     {
   // TODO Auto-generated catch block
      e.printStackTrace();
     } 
     catch (SQLException e) 
     {
   // TODO Auto-generated catch block
      e.printStackTrace();
     }
     
     return con; 
     
    }
 
     public static void closeConnection(Connection con){      
      if(con!=null){
       try{
       con.close();
       }catch(SQLException e){}
      }      
     }   
       public static void closePreparedStatement(PreparedStatement ps){      
      if(ps!=null){
       try{
       ps.close();
       }catch(SQLException e){}
      }
      
     }

}
