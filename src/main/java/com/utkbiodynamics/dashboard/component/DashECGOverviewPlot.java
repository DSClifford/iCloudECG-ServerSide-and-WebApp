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
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.PlotOptionsSeries;
import com.vaadin.addon.charts.model.ZoomType;
import com.utkbiodynamics.dashboard.data.EcgRecord;
import com.vaadin.server.VaadinSession;

@SuppressWarnings("serial")
public class DashECGOverviewPlot extends Chart {

    public DashECGOverviewPlot(int recIndex) {
    	List<EcgRecord> userRecds = (List<EcgRecord>) VaadinSession.getCurrent().getAttribute("userRecds");
    	
    	if(userRecds.size()!=0){
    	EcgRecord currentRecd = userRecds.get(recIndex);
    	
    	Timestamp ts = Timestamp.valueOf(currentRecd.getupDate());
        
        DateFormat time = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH);
        DateFormat date = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
        
        String rectime = time.format(ts);
        String recdate = date.format(ts);
            	
        String recDate = "Selected Record: " + recdate + "  " + rectime;
        setCaption(recDate);
        getConfiguration().setTitle("");
        getConfiguration().getChart().setType(ChartType.LINE);
        getConfiguration().getChart().setAnimation(false);
        getConfiguration().getxAxis().getLabels().setEnabled(false);
        getConfiguration().getxAxis().setTickWidth(0);
        getConfiguration().getyAxis().getLabels().setEnabled(false);
        getConfiguration().getyAxis().setTickWidth(0);
        getConfiguration().getChart().setZoomType(ZoomType.Y);
        getConfiguration().getTooltip().setEnabled(false);
        getConfiguration().getLegend().setEnabled(false);
        
        setSizeFull();
        
        List<Double> [] ecgData = new ArrayList [1];

            ecgData[0] = new ArrayList<Double> ();

        try {
            FileReader fr = new FileReader(currentRecd.getRelativeFilePath());
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
       final DataSeries maxMin = new DataSeries();
    	   DataSeries testseries = new DataSeries();
           testseries.setName("Data");
        
           List<Double> data = ecgData[0];
           maxMin.add(new DataSeriesItem(Collections.max(data),
        		   Collections.min(data)));
        	
           	for(int i = 0 ;i < data.size();i++) {           
        		testseries.add(new DataSeriesItem((double)(i), 
        				data.get(i)));
        }
        getConfiguration().addSeries(testseries);
        PlotOptionsSeries opts1 = new PlotOptionsSeries();
        opts1.setEnableMouseTracking(false);
        testseries.setPlotOptions(opts1);
       
        double overallMin = (Double) maxMin.get(0).getY()+10;
        double overallMax = (Double) maxMin.get(0).getX()+10;
        getConfiguration().getyAxis().setExtremes(overallMin, overallMax);
        Credits c = new Credits("");
        getConfiguration().setCredits(c);

        PlotOptionsLine opts = new PlotOptionsLine();
        opts.setMarker(new Marker(false));
        getConfiguration().setPlotOptions(opts);

    }else if(userRecds.size()==0){
    	getConfiguration().setTitle("No Record to Display");
    }
    }
}
