package com.utkbiodynamics.dashboard.component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Credits;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.PlotOptionsSeries;
import com.vaadin.addon.charts.model.Title;
import com.vaadin.addon.charts.model.ZoomType;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.data.Item;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ECGDetailViewer12Lead extends Window {


private	int selectedLead = 0;
private List<Double> [] ecgData = new ArrayList [12];
private Item currentRec;
private List<Float> [] scrollHeights = new ArrayList[14];
private Panel panel;
	public ECGDetailViewer12Lead(Item currentRecd) {
    	panel = new Panel();
    	panel.setImmediate(true);
		
    	currentRec = currentRecd;
    	getEcgData();
    	configureEcgChart();
    	
    	VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        setContent(subContent);
        
        VerticalLayout plotContent = new VerticalLayout();
        plotContent.setMargin(true);
        panel.setContent(plotContent);
		        
        subContent.addComponent(showRecordInfo(),0);
        subContent.addComponent(getLeadSelection(),1);
        subContent.addComponent(panel);

        
        for(int i=0; i<(ecgData.length); i++){
        	plotContent.addComponent(configureEcgChart(),i);
        	selectedLead++;
        }
        
        center();
        int pageHeight = Page.getCurrent().getBrowserWindowHeight();
        int pageWidth = Page.getCurrent().getBrowserWindowWidth();
        System.out.println(pageHeight);
        System.out.println(pageWidth);
        setHeight(Double.toString(pageHeight*0.91));
        setWidth("90%");
        panel.setHeight(Double.toString(pageHeight*0.74));
        panel.setWidth("100%");
        
        scrollHeights[0] = new ArrayList<Float>();
        
        for(int i=0; i<(ecgData.length); i++){
        	scrollHeights[0].add(plotContent.getComponent(i).getHeight());

        }
    }
	
	private Chart configureEcgChart() {
		
			Chart ecgChart = new Chart();
			String chartTitle = new String("Lead "+Integer.toString(selectedLead+1));
		 	ecgChart.getConfiguration().setTitle(chartTitle);
		 	ecgChart.getConfiguration().getTitle().setHorizontalAlign(HorizontalAlign.LEFT);;
	        ecgChart.getConfiguration().getChart().setType(ChartType.LINE);
	        ecgChart.getConfiguration().getChart().setAnimation(false);
	        ecgChart.getConfiguration().getxAxis().setTitle("Time");
	        ecgChart.getConfiguration().getxAxis().setTickWidth(0);
	        ecgChart.getConfiguration().getyAxis().setTitle("mV");
	        ecgChart.getConfiguration().getyAxis().setTickPixelInterval(10);
	        ecgChart.getConfiguration().getyAxis().setMaxPadding(0);
	        ecgChart.getConfiguration().getyAxis().setMinPadding(0);
	        ecgChart.getConfiguration().getyAxis().setTickWidth(0);
	        ecgChart.getConfiguration().getChart().setZoomType(ZoomType.X);
	        ecgChart.getConfiguration().getTooltip().setEnabled(false);
	        ecgChart.getConfiguration().getLegend().setEnabled(false);
	        
	        Credits c = new Credits("");
	        ecgChart.getConfiguration().setCredits(c);
	        ecgChart.setHeight("300px");
	        ecgChart.addStyleName("detail-charts");
	        PlotOptionsLine opts = new PlotOptionsLine();
	        opts.setMarker(new Marker(false));
	        ecgChart.getConfiguration().setPlotOptions(opts);
	        
	        Title t = ecgChart.getConfiguration().getTitle();  // Obtain the object representing the title (or Subtitle for subtitle).
	        if ( t.getStyle() == null ) {  // If not yet existing…
	            t.setStyle( new Style() );  // Instantiate a Style object and install on the Title object.
	        }
	        Style st = t.getStyle();  // Obtain the Style object (whether new or pre-existing).
	        st.setFontSize( "1em" ); 
	        
	        return getChartData(ecgChart);
		
	}

	private void getEcgData() {
		
		for(int i = 0 ; i < 12 ; i ++ ) {
            ecgData[i] = new ArrayList<Double> ();
        }
        try {
        	String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();  
        	String filePath = new String(/*"/var/lib/tomcat7/webapps/resources/records/"*/basePath+"/records/"+ currentRec.getItemProperty("fid").getValue().toString() +".txt");
        	FileReader fr = new FileReader(filePath);
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
            br.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
	}

	private Component showRecordInfo() {
		
		HorizontalLayout recordInfo = new HorizontalLayout();
		Timestamp ts = Timestamp.valueOf(currentRec.getItemProperty("regdate").getValue().toString());
            	
        DateFormat time = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH);
        DateFormat date = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
        
        String rectime = time.format(ts);
        String recdate = date.format(ts);
        String title = new String("Selected Record: " + recdate + " " + rectime);
        Label titleLabel = new Label(title);
        titleLabel.addStyleName(ValoTheme.LABEL_BOLD);
        recordInfo.addComponents(titleLabel);
        
		return recordInfo;
	}
	
	private Chart getChartData(Chart ecgChart){
 	   DataSeries testseries = new DataSeries();
       testseries.setName("Lead " + (selectedLead+1));

       List<Double> data = ecgData[selectedLead];
    	
       	for(int i = 0 ;i < data.size();i++) {           
    		testseries.add(new DataSeriesItem((double)(i), 
    				(double)(data.get(i))));
    }
    ecgChart.getConfiguration().setSeries(testseries);
    PlotOptionsSeries opts1 = new PlotOptionsSeries();
    opts1.setEnableMouseTracking(false);
    testseries.setPlotOptions(opts1);
    double max = Collections.max(data);
    double min = Collections.min(data);

    System.out.println("Max: " + max);
    System.out.println("Min: " + min);
    
   // ecgChart.getConfiguration().getyAxis().setExtremes(min, max);
    
    return ecgChart;
	}
	
	private Component getLeadSelection() {
		
		VerticalLayout leadSelectWrap = new VerticalLayout();
		leadSelectWrap.addComponent(new Label("Jump to Lead: "),0);
		leadSelectWrap.getComponent(0).addStyleName("lead-select-label");
		HorizontalLayout leadSelection = new HorizontalLayout();
		leadSelectWrap.addComponent(leadSelection);
		
		final int topCompCount = 0;
		final Button lead1Btn = new Button("1");
        lead1Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 0;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
            }
        });
		final Button lead2Btn = new Button("2");
        lead2Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 1;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead+topCompCount;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
                }
        });
		final Button lead3Btn = new Button("3");
        lead3Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 2;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead+topCompCount;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
            }
        });
		final Button lead4Btn = new Button("4");
        lead4Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 3;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead+topCompCount;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
            }
        });
		final Button lead5Btn = new Button("5");
        lead5Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 4;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead+topCompCount;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
            }
        });
		final Button lead6Btn = new Button("6");
        lead6Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 5;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead+topCompCount;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
            }
        });
		final Button lead7Btn = new Button("7");
        lead7Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 6;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead+topCompCount;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
            }
        });
		final Button lead8Btn = new Button("8");
        lead8Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 7;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead+topCompCount;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
            }
        });
		final Button lead9Btn = new Button("9");
        lead9Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 8;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead+topCompCount;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
           }
        });
		final Button lead10Btn = new Button("10");
        lead10Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 9;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead+topCompCount;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
            }
        });
		final Button lead11Btn = new Button("11");
        lead11Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 10;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead+topCompCount;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
            }
        });
		final Button lead12Btn = new Button("12");
        lead12Btn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                selectedLead = 11;
                float scrollHeight = 0;
                for (int i=0;i<selectedLead+topCompCount;i++){
                	scrollHeight = (-16*i)+scrollHeight+scrollHeights[0].get(i);
                }
                panel.setScrollTop((int) scrollHeight);
            }
        });
        lead1Btn.setImmediate(true);
        lead2Btn.setImmediate(true);
        lead3Btn.setImmediate(true);
        lead4Btn.setImmediate(true);
        lead5Btn.setImmediate(true);
        lead6Btn.setImmediate(true);
        lead7Btn.setImmediate(true);
        lead8Btn.setImmediate(true);
        
		leadSelection.addComponents(
				lead1Btn,
				lead2Btn,
				lead3Btn,
				lead4Btn,
				lead5Btn,
				lead6Btn,
				lead7Btn,
				lead8Btn,
				lead9Btn,
				lead10Btn,
				lead11Btn,
				lead12Btn);

		return leadSelectWrap;
	}
	
	
}


