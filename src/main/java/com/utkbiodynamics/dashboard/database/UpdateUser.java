package com.utkbiodynamics.dashboard.database;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.utkbiodynamics.dashboard.data.User;
import com.vaadin.server.VaadinSession;



public class UpdateUser {
    public static void updateUser() {
    	 
    	User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
    	
    	System.out.println("Updating User");
    	System.out.println(user.getEmail());
    	System.out.println(user.getFirstName());
    	System.out.println(user.getLastName());
    	System.out.println(user.getLocation());
    	System.out.println(user.getUniqueID());
    	 Connection con=null;
         PreparedStatement memstmt = null;
         PreparedStatement loginstmt = null;
         try
         {
          con=DBConnect.getConnection();
             memstmt = con.prepareStatement("UPDATE members SET first_name = ?, last_name = ?, gender = ?, bdate = ?, email = ?, Institution = ?, firstrun = ?, userrole = ? WHERE email=?");
             memstmt.setString(1, user.getFirstName());
             memstmt.setString(2, user.getLastName());
             memstmt.setString(3, user.getGender());
             memstmt.setDate(4, user.getbirthDate());
             memstmt.setString(5, user.getEmail());
             memstmt.setString(6, user.getInstitution());
             memstmt.setString(7, Integer.toString((int) user.getFirstrun()));
             memstmt.setString(8, user.getRole());
             memstmt.setString(9, user.getEmail());
 
             memstmt.executeUpdate();
            
         } catch (Exception e) {
             e.printStackTrace();
         } 
         
             
              finally {
                 if (loginstmt != null) {
                     try {
                        loginstmt.close();
                     } catch (SQLException ex) {
                     }
                 }
                 
                     if (memstmt != null) {
                         try {
                            memstmt.close();
                         } catch (SQLException ex) {
                         }
                     }
                     if (con != null) {
                         try {
                            con.close();                    
                         } catch (SQLException ex) {
                         }
                 }
                 }
             
        
    }
    public static void saveNewUser(User user ) throws Exception {
        Connection con=null;
        PreparedStatement memstmt = null;
        PreparedStatement loginstmt = null;
        try
        {
         con=DBConnect.getConnection();
            memstmt = con.prepareStatement("INSERT INTO members (first_name,last_name,email,Institution,firstrun,gender,bdate,userrole,uniqueID) VALUES (?,?,?,?,?,?,?,?,?);");
            memstmt.setString(1, user.getFirstName());
            memstmt.setString(2, user.getLastName());
            memstmt.setString(3, user.getEmail());
            memstmt.setString(4, user.getInstitution());
            memstmt.setString(5, "1");
            memstmt.setString(6, user.getGender());
            memstmt.setDate(7, user.getbirthDate());
            memstmt.setString(8, "unset");
            memstmt.setString(9, user.getUniqueID());
            memstmt.executeUpdate();
            
            loginstmt = con.prepareStatement("INSERT INTO memberlogins (email,password) VALUES (?,?);");
            loginstmt.setString(1, user.getEmail());
            loginstmt.setString(2, user.getPassword());
            loginstmt.executeUpdate();
            //con.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
            
             finally {
                if (loginstmt != null) {
                    try {
                       loginstmt.close();
                    } catch (SQLException ex) {
                    }
                }
                
                    if (memstmt != null) {
                        try {
                           memstmt.close();
                        } catch (SQLException ex) {
                        }
                    }
                    if (con != null) {
                        try {
                           con.close();                    
                        } catch (SQLException ex) {
                        }
                }
                }
            }
    
        
        
    
   // public static void addAnEcgRecord( User user, EcgRecord ecgrecord ) {
        
   // }
}
