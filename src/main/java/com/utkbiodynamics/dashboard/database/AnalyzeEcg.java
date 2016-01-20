package com.utkbiodynamics.dashboard.database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.utkbiodynamics.dashboard.DashboardUI;
import com.utkbiodynamics.dashboard.views.MyRecordsView;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;


public class AnalyzeEcg
{
private static String recordID;
private static final String GOOGLE_SERVER_KEY = "AIzaSyCCpTj7IpIWp-2F-FenxBgOXSOet9JVqfo";
static final String REGISTER_NAME = "name";
static final String MESSAGE_KEY = "message";
static final String TO_NAME = "toName";
	private static ArrayList<Double> [] ecgData = new ArrayList [12];
   public static void main(String randomID, Boolean mobile)
   {
	  recordID = randomID;
	
	  //Removed for IP reasons

         //MWArray.disposeArray(n);
         //MWArray.disposeArray(heights);
         //MWArray.disposeArray(indices);
         //findPeaks.dispose();
   }

	private static void calcHR(Object[] indices, int length) {
	
		//Removed for IP reasons
		
}

    public static Window postUploadWindow(CssLayout analysisResults) {
    	Window postUploadWindow = new Window();
    	VerticalLayout layout = new VerticalLayout();
    	
    	postUploadWindow.setStyleName("post-upload-window");
    	postUploadWindow.setWidth("400px");
    	postUploadWindow.setHeight("400px");
    	postUploadWindow.center();
    	
    	layout.setSpacing(true);
    	
    	
    	postUploadWindow.setContent(analysisResults);

    	return postUploadWindow;
	}
	
	private static void getEcgData(String randomID) {
		
		for(int i = 0 ; i < 12 ; i ++ ) {
            ecgData[i] = new ArrayList<Double> ();
        }
        try {
        	final String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        	//String filePath = new String(basePath + "/records/" + randomID + ".txt");
        	String filePath = new String("/var/lib/tomcat7/webapps/resources/records/"+ randomID + ".txt");
        	FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String tmp = br.readLine();
            while(tmp!=null) {
                
                ecgData[0].add(Double.parseDouble(tmp));
                

                tmp=br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
	}

   
}
