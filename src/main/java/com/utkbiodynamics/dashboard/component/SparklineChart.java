package com.utkbiodynamics.dashboard.component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import com.utkbiodynamics.dashboard.data.EcgRecord;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SparklineChart extends VerticalLayout {


	public SparklineChart(final int index) {
        setSizeUndefined();
        addStyleName("spark");
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        
        @SuppressWarnings("unchecked")
		List<EcgRecord> userRecds = (List<EcgRecord>) VaadinSession.getCurrent().getAttribute("userRecds");

        System.out.println(userRecds.size());
        
        if(userRecds.size()-1 >= index) {
        EcgRecord currentrecord = userRecds.get(index);
        
        Timestamp ts = Timestamp.valueOf(currentrecord.getupDate());
                
        DateFormat time = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH);
        DateFormat date = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
        
        String rectime = time.format(ts);
        String recdate = date.format(ts);
        
        Label current = new Label(recdate);
        current.setSizeUndefined();
        current.addStyleName(ValoTheme.LABEL_LARGE);
        addComponent(current);

        Label title = new Label(rectime);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_SMALL);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        addComponent(title);

        String dashDiag = "Diagnosis: " + currentrecord.getDiagnosis();
        Label diagnosis = new Label(dashDiag);
        addComponent(diagnosis);
        diagnosis.setSizeUndefined();
        

        Label highLow = new Label(userRecds.get(index).getPatientName());
        highLow.addStyleName(ValoTheme.LABEL_TINY);
        highLow.addStyleName(ValoTheme.LABEL_LIGHT);
        highLow.setSizeUndefined();
        addComponent(highLow);

    }
        else if(index == userRecds.size()) {
        	String noRecMes;
        	if(userRecds.size()==0) {
        		noRecMes = "You Have No Record";
        	}
        	else if(userRecds.size()==1){
        		noRecMes = "You Only Have " + userRecds.size() + " Record";
        	}
        	else {
        		noRecMes = "You Only Have " + userRecds.size() + " Records";
        	}
        	Button button = new Button("Upload Records");  
        	button.addClickListener(new Button.ClickListener() {
        	    public void buttonClick(ClickEvent event) {
        	    	UI.getCurrent().getNavigator()
                    .navigateTo("sales");
        	    }
        	});
        	       	
        	Label noreclab = new Label(noRecMes);
        	noreclab.setSizeUndefined();
        	//noreclab.addStyleName("noreclab");
        	noreclab.addStyleName(ValoTheme.LABEL_SMALL);
        	addComponent(noreclab);
        	
            Label current = new Label(" ");
            current.setSizeUndefined();
            current.addStyleName(ValoTheme.LABEL_LARGE);
            addComponent(current);
        	
        	button.setSizeUndefined();
        	//button.addStyleName("norecbtn");
        	addComponent(button);
        	
        	  Label highLow = new Label(" ");
              highLow.addStyleName(ValoTheme.LABEL_TINY);
              highLow.addStyleName(ValoTheme.LABEL_LIGHT);
              highLow.setSizeUndefined();
              addComponent(highLow);
        
        }
	}
}

