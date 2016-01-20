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
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ECGDetailViewer extends Window {



private List<Double> [] ecgData = new ArrayList [1];
private Item currentRec;

private Panel panel;
	public ECGDetailViewer(Item currentRecd) {
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
        subContent.addComponent(panel);

        plotContent.addComponent(configureEcgChart());

        
        center();
        int pageHeight = Page.getCurrent().getBrowserWindowHeight();
        int pageWidth = Page.getCurrent().getBrowserWindowWidth();
        System.out.println(pageHeight);
        System.out.println(pageWidth);
        setHeight(Double.toString(pageHeight*0.91));
        setWidth("90%");
        panel.setHeight(Double.toString(pageHeight*0.74));
        panel.setWidth("100%");
    }
	
	private Chart configureEcgChart() {
		
			Chart ecgChart = new Chart();
			String chartTitle = new String("ECG Data");
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
		
        ecgData[0] = new ArrayList<Double> ();

        try {
        	String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();  
        	String filePath = new String("/var/lib/tomcat7/webapps/resources/records/"+ currentRec.getItemProperty("fid").getValue().toString() +".txt");
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
 	   DataSeries series = new DataSeries();
       series.setName("Data");

       List<Double> data = ecgData[0];
    	
       	for(int i = 0 ;i < data.size();i++) {           
    		series.add(new DataSeriesItem((double)(i), 
    				(double)(data.get(i))));
    }
    ecgChart.getConfiguration().setSeries(series);
    PlotOptionsSeries opts1 = new PlotOptionsSeries();
    opts1.setEnableMouseTracking(false);
    series.setPlotOptions(opts1);
    double max = Collections.max(data);
    double min = Collections.min(data);

    System.out.println("Max: " + max);
    System.out.println("Min: " + min);
    
   // ecgChart.getConfiguration().getyAxis().setExtremes(min, max);
    
    return ecgChart;
	}
	
	
}


