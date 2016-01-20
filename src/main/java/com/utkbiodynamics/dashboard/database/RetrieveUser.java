package com.utkbiodynamics.dashboard.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import com.utkbiodynamics.dashboard.data.EcgRecord;
import com.utkbiodynamics.dashboard.data.User;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;


public class RetrieveUser {
	public static void retrieveUser(String login) throws SQLException {
    Connection con=null;
    PreparedStatement st=null;
    User user = new User();
    con = DBConnect.getConnection();
    
    String sql = ("select * from members where email=?");
    
    st = con.prepareStatement(sql);
    st.setString(1, login);
    
    ResultSet rs = st.executeQuery();
    
    if(rs.next()) { 
     user.setFirstName(rs.getString("first_name")); 
     user.setLastName(rs.getString("last_name"));
     user.setbirthDate(rs.getDate("bdate"));
     user.setGender(rs.getString("gender"));
     user.setEmail(rs.getString("email"));
     user.setInstitution(rs.getString("institution"));
     user.setFirstrun(rs.getInt("firstrun"));
     user.setRole(rs.getString("userrole"));
     user.setUniqueID(rs.getString("uniqueID"));
    }

    if (user.getbirthDate() != null){
    Calendar dob = Calendar.getInstance();  
    dob.setTime(user.getbirthDate());  
    Calendar today = Calendar.getInstance();  
    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);  
    if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
      age--;  
    } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)+1
        && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
      age--;  
    }
    user.setPatAge((long) age);
    user.setIntPatAge(age);
    
    } else {
    	System.out.println("Birthdate is null");
        
    }
    con.close();
    
    VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
    userRecds();
    
	}
	 private static void userRecds() {
			
	    	String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	        
	        List<EcgRecord> userRecds = RetrieveEcgs.getUserRecds(getCurrentUser().getEmail(), basepath);
	    	
	        VaadinSession.getCurrent().setAttribute("userRecds", userRecds);
	        
	        if(userRecds.size() != 0){	        
	        VaadinSession.getCurrent().setAttribute("currentRecd", userRecds.get(0).getId());
	        }
	    }
	 private static User getCurrentUser() {
	        return (User) VaadinSession.getCurrent().getAttribute(
	                User.class.getName());
	    }
}
