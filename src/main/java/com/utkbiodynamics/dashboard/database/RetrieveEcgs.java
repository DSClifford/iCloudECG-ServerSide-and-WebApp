package com.utkbiodynamics.dashboard.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.utkbiodynamics.dashboard.data.EcgRecord;



public class RetrieveEcgs {
    public static List<EcgRecord> getUserRecds(String email, String basePath) {
        List<EcgRecord> userRecds = new ArrayList<EcgRecord> ();
        
        Connection con=null;
        Statement st=null;
        PreparedStatement pst=null;
        ResultSet rs=null;
       con = DBConnect.getConnection();
       
       String sql;
       
        try {
        	
        	if (email.equals("DBSearch")) {
         	   
            	sql = ("select * from records");
            	st = con.createStatement();
            	rs = st.executeQuery(sql);
            	   
               }else {
                sql = ("select * from records where email=? or email=?");
                pst = con.prepareStatement(sql);

                pst.setString(1, email);
                pst.setString(2, "Sample@utk.edu");
                
                rs = pst.executeQuery();
               }
        
        while(rs.next()) { 
  EcgRecord ecgRecord = new EcgRecord();
  			ecgRecord.setPatientName(rs.getString("patname"));
        	ecgRecord.setDiagnosis(rs.getString("diagnosis"));
            ecgRecord.setId(rs.getString("fid"));
            ecgRecord.setPatientAge(Integer.parseInt(rs.getString("age")));
            ecgRecord.setPatientGender(rs.getString("gender"));
            ecgRecord.setSamplingRate(Integer.parseInt(rs.getString("samprate")));
            ecgRecord.setQualityGrade(rs.getString("qualgrade"));
            ecgRecord.setSuspectedMisplacement(rs.getString("suspmisp"));
            ecgRecord.setupDate(rs.getTimestamp("regdate"));
            ecgRecord.setRecord_notes(rs.getString("recordnotes"));

            

            ecgRecord.setRelativeFilePath("/var/lib/tomcat7/webapps/resources/records/"+ ecgRecord.getId() +".txt");
            
            userRecds.add(ecgRecord);
            
          }
        
        con.close();
        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userRecds;
        

    }
    public static EcgRecord getAnDummyECG(String id, String email, String basePath) {
        EcgRecord ecgRecord = null;
        List<EcgRecord> sampleRecords = getUserRecds(email, basePath);
        for( EcgRecord r : sampleRecords ) {
            if( r.getId() == id ) {
                ecgRecord = r;
                break;
            }
        }
        if( ecgRecord == null ) {
            return ecgRecord;
        }
        List<Double> [] ecgData = new ArrayList [12];
        for(int i = 0 ; i < 12 ; i ++ ) {
            ecgData[i] = new ArrayList<Double> ();
        }
        try {
            FileReader fr = new FileReader(ecgRecord.getRelativeFilePath());
            BufferedReader br = new BufferedReader(fr);
            String tmp = br.readLine();
            while(tmp!=null) {
                String [] datas = tmp.split(" ");
                int lNum = -1;
                for( String volt : datas ) {
                    if( volt.equals("") || volt.contains(" ") ) 
                        continue;
                    lNum ++;
                    if( lNum == 0 ) continue;                    
                    ecgData[lNum-1].add(Double.parseDouble(volt));
                }

                tmp=br.readLine();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ecgRecord.setEcgData(ecgData);
        return ecgRecord;
    }


public static List<EcgRecord> getQueryRecds(String dselected, String aselected, String gselected, String basePath) {
    List<EcgRecord> queryRecds = new ArrayList<EcgRecord> ();
    
    Connection con=null;
    Statement st=null;
   con = DBConnect.getConnection();
   
   List<String> dselectlist = new ArrayList<String>(Arrays.asList(dselected.split(", ")));
   List<String> gselectlist = new ArrayList<String>(Arrays.asList(gselected.split(", ")));
   
   StringBuilder dlist = new StringBuilder("(\""+dselectlist.get(0)+ "\"");
   for (int i = 1; i < dselectlist.size(); i++)
     dlist.append(",\"").append(dselectlist.get(i)).append("\"");
   dlist.append(")");
   
   StringBuilder glist = new StringBuilder("(\""+gselectlist.get(0)+ "\"");
   for (int i = 1; i < gselectlist.size(); i++)
     glist.append(",\"").append(gselectlist.get(i)).append("\"");
   glist.append(")");
   
   
    String sql = ("select * from records where diagnosis in " + dlist.toString() + " and age in " + aselected + " and gender in " + glist.toString());
    
    try {
		st = con.createStatement();
		
    
    ResultSet rs = st.executeQuery(sql);
    
    while(rs.next()) { 
EcgRecord ecgRecord = new EcgRecord();
			ecgRecord.setPatientName(rs.getString("patname"));
    	ecgRecord.setDiagnosis(rs.getString("diagnosis"));
        ecgRecord.setId(rs.getString("fid"));
        ecgRecord.setPatientAge(Integer.parseInt(rs.getString("age")));
        ecgRecord.setPatientGender(rs.getString("gender"));
        ecgRecord.setSamplingRate(Integer.parseInt(rs.getString("samprate")));
        ecgRecord.setQualityGrade(rs.getString("qualgrade"));
        ecgRecord.setSuspectedMisplacement(rs.getString("suspmisp"));
        ecgRecord.setupDate(rs.getTimestamp("regdate"));

        

        ecgRecord.setRelativeFilePath(basePath + "/records/"+ ecgRecord.getId() +".txt");
        
        queryRecds.add(ecgRecord);

      }
    
    con.close();
    
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
	return queryRecds;
    

}
public static EcgRecord getDbECG(Long currentRecordId, String basepath) {
	// TODO Auto-generated method stub
	return null;
}
}
