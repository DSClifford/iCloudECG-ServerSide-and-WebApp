package com.utkbiodynamics.dashboard.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.utkbiodynamics.dashboard.data.EcgRecord;
import com.utkbiodynamics.dashboard.data.User;
import com.vaadin.server.VaadinSession;
public class UpdateEcgs {

    public static void saveEcg(EcgRecord newRecord) {
    	
    	User currentUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        Connection con=null;
        PreparedStatement memstmt = null;
        try
        {
         con=DBConnect.getConnection();
            memstmt = con.prepareStatement("INSERT INTO records (fid,email,patname,diagnosis,gender,age,samprate,fname,recordnotes) VALUES (?,?,?,?,?,?,?,?,?);");
            memstmt.setString(1, newRecord.getId());
            memstmt.setString(2, currentUser.getEmail());
            memstmt.setString(3, newRecord.getPatientName());
            memstmt.setString(4, newRecord.getDiagnosis());
            memstmt.setString(5, newRecord.getPatientGender());
            memstmt.setInt(6, newRecord.getPatientAge());
            memstmt.setDouble(7, newRecord.getSamplingRate());
            memstmt.setString(8, newRecord.getUserdefinedname());
            memstmt.setString(9, newRecord.getRecord_notes());

            memstmt.executeUpdate();
            
            //con.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
            
             finally {
                
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
    }
    
