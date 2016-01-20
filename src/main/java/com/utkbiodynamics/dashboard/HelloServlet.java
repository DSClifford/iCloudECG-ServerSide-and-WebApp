package com.utkbiodynamics.dashboard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String recordID;
	private static final String GOOGLE_SERVER_KEY = "AIzaSyCCpTj7IpIWp-2F-FenxBgOXSOet9JVqfo";
	static final String REGISTER_NAME = "name";
	static final String MESSAGE_KEY = "message";
	static final String TO_NAME = "toName";
	private static ArrayList<Double> [] ecgData = new ArrayList [12];

	@Override
	public void init() throws ServletException {
		// Servlet initialization code here
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Set response content type
		response.setContentType("text/html");

		
		String param1 = request.getParameter("param1");
        System.out.println(param1);
        String param2 = request.getParameter("param2");
        System.out.println(param2);
        
        if(param1!=null){
        if(param1.equals("analysis")){
        	AnalyzeEcg(param2,true);
        	return;
        }}
	}

	private void AnalyzeEcg(String randomID, boolean mobile) {
				//Removed for IP reasons
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	@Override
	public void destroy() {
		// resource release
		super.destroy();
	}
	private static void calcHR(Object[] indices, int length) {
		
		//Removed for IP reasons
}

	
	private static void getEcgData(String randomID) {
		
		for(int i = 0 ; i < 12 ; i ++ ) {
            ecgData[i] = new ArrayList<Double> ();
        }
        try {
        	//final String basePath = HttpService.getCurrent().getBaseDirectory().getAbsolutePath();
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
