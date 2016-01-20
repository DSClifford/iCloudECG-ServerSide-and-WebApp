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
public class DashECGOverviewPlot12Lead extends Chart {

    public DashECGOverviewPlot12Lead(int recIndex) {
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
        
        List<Double> [] ecgData = new ArrayList [12];
        for(int i = 0 ; i < 12 ; i ++ ) {
            ecgData[i] = new ArrayList<Double> ();
        }
        try {
            FileReader fr = new FileReader(currentRecd.getRelativeFilePath());
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
       final DataSeries maxMin = new DataSeries();
       for(int lead = 1; lead < 13; lead++){
    	   DataSeries testseries = new DataSeries();
           testseries.setName("Lead " + (lead));
        
           List<Double> data = ecgData[lead-1];
           maxMin.add(new DataSeriesItem((double)(30+((Collections.max(data)-(lead-1)*4000))/200),
        		   (30+((Collections.min(data)-(lead-1)*4000))/200)));
        	
           	for(int i = 0 ;i < data.size();i++) {           
        		testseries.add(new DataSeriesItem((double)(i), 
        				(double)(30+((data.get(i)-(lead-1)*4000))/200)));
        }
        getConfiguration().addSeries(testseries);
        PlotOptionsSeries opts1 = new PlotOptionsSeries();
        opts1.setEnableMouseTracking(false);
        testseries.setPlotOptions(opts1);
       }
        double overallMin = (Double) maxMin.get(11).getY()+10;
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
