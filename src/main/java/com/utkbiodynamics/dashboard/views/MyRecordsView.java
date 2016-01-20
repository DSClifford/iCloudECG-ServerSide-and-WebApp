package com.utkbiodynamics.dashboard.views;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import com.utkbiodynamics.dashboard.data.User;
import com.google.common.eventbus.Subscribe;
import com.utkbiodynamics.dashboard.DashboardUI;
import com.utkbiodynamics.dashboard.component.ECGDetailViewer;
import com.utkbiodynamics.dashboard.database.AnalyzeEcg;
import com.utkbiodynamics.dashboard.event.DashboardEventBus;
import com.utkbiodynamics.dashboard.event.DashboardEvent.BrowserResizeEvent;
import com.utkbiodynamics.dashboard.event.DashboardEvent.TransactionReportEvent;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial", "unchecked" })
public final class MyRecordsView extends VerticalLayout implements View {

    private static Table table;
    private static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "MM/dd/yyyy hh:mm:ss a");
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    private static final String[] DEFAULT_COLLAPSIBLE = { "country", "city",
            "theater", "room", "title", "seats" };

    // creating a simple SQLContainer that represents 
    // a table on our database 
    SQLContainer contactContainer = createMySQLContainer(); 

    // creating a simple layout 
    VerticalLayout layout = new VerticalLayout(); 



    public MyRecordsView() {
        setSizeFull();
        addStyleName("transactions");
        DashboardEventBus.register(this);

        addComponent(buildToolbar());

        layout.addComponent(buildEditToggle());
        layout.addStyleName("record-table");
        Responsive.makeResponsive(table);
        Responsive.makeResponsive(layout);
        layout.addComponent(new LazyLoadWrapper(buildTable()));
        
        addComponent(layout);
        setExpandRatio(layout, 1);
        setComponentAlignment(layout, Alignment.TOP_CENTER);
        
        if(Page.getCurrent().getBrowserWindowWidth() > 570){
        	layout.setMargin(true);
        }
        
    }

    @Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }

    private Component buildToolbar() {
    	 HorizontalLayout header = new HorizontalLayout();
         header.addStyleName("viewheader");
         header.setSpacing(false);
         Responsive.makeResponsive(header);
         
         HorizontalLayout logoTitle = new HorizontalLayout();
         logoTitle.addStyleName("logoTitle");
         logoTitle.setSpacing(false);
         
         Image logoImage = new Image(null,
         	    new ThemeResource("img/logoCloudOnly.png"));
         logoImage.addStyleName("logoImage");
         Label titleLabel = new Label("My Records");
         titleLabel.setId("my-records");
         titleLabel.setSizeUndefined();
         titleLabel.addStyleName(ValoTheme.LABEL_H1);
         titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
         titleLabel.addStyleName("dashtitle");
         logoTitle.addComponent(logoImage);
         logoTitle.addComponent(titleLabel);
         logoTitle.setComponentAlignment(titleLabel, Alignment.MIDDLE_CENTER);
         header.addComponent(logoTitle);
         header.setComponentAlignment(logoTitle, Alignment.MIDDLE_LEFT);

        return header;
    }


    private HorizontalLayout buildEditToggle() {
    	
    	HorizontalLayout editToggle = new HorizontalLayout();
    	
    	final Button deleteBtn = new Button(FontAwesome.TRASH_O);
        deleteBtn
                .setDescription("Delete selected records");
        deleteBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                System.out.println(table.getValue()+" deleted");
                
                try {
					contactContainer.commit();
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        deleteBtn.setEnabled(false);
        deleteBtn.setVisible(true);
        deleteBtn.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        
        final Button analyzeBtn = new Button("Analyze Sample");
        analyzeBtn
                .setDescription("Delete selected records");
        analyzeBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                System.out.println(table.getValue()+" deleted");
                DashboardUI.getCurrent().setPollInterval(500);  
                Runnable r = new Runnable() {
                    public void run() {
                    	try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        AnalyzeEcg.main("39815e5918", false);
                        try {
							Thread.sleep(4000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        DashboardUI.getCurrent().setPollInterval(-1);
                    }
                };

                new Thread(r).start();
                
                Notification notification = new Notification(
                        "Analysis Initiated");
                notification
                        .setDescription("<span>Results will be displayed shortly</span>");
                notification.setHtmlContentAllowed(true);
                notification.setStyleName("tray dark small closable login-help");
                notification.setPosition(Position.MIDDLE_CENTER);
                notification.setDelayMsec(2000);
                notification.show(Page.getCurrent());
                
                try {
					contactContainer.commit();
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        analyzeBtn.setEnabled(true);
        analyzeBtn.setVisible(true);
        analyzeBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
    	    	
    	final CheckBox switchEditable = new CheckBox("Select to Edit");
    	switchEditable.addValueChangeListener(
    	        new Property.ValueChangeListener() {
    	    public void valueChange(ValueChangeEvent event) {
    	        table.setMultiSelect(((Boolean)event.getProperty()
    	                             .getValue()).booleanValue());
    	        deleteBtn.setEnabled(true);
    	        deleteBtn.setVisible(true);
    	    }
    	});
    	switchEditable.setImmediate(true);
    	
    	editToggle.addComponent(switchEditable);
    	editToggle.addComponent(deleteBtn);
    	editToggle.addComponent(analyzeBtn);

        return editToggle;
    	
    }
    

    private static SQLContainer createMySQLContainer() 
    { 
            TableQuery query = null; 
            SQLContainer temp = null; 
            User currentUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
            try 
            { 

                    SimpleJDBCConnectionPool connectionPool = new 
SimpleJDBCConnectionPool( 
                                    "com.mysql.jdbc.Driver", 
                                    "jdbc:mysql://localhost/cloudecg?autoReconnect=true","root","iCloudECG4238272483",2,5); 
                    query = new TableQuery("records", connectionPool);
                    query.setVersionColumn("fid"); 

                    temp = new SQLContainer(query);
                    Filter user_filter = new SimpleStringFilter("email", currentUser.getEmail(),false,false);
                    temp.addContainerFilter(user_filter);
                    
            } 
            catch (SQLException e) 
            { 
                    // TODO Auto-generated catch block 
                    e.printStackTrace(); 
            } 
            return temp; 

    } 

    
    @SuppressWarnings("deprecation")
	private Table buildTable() {
        table = new Table() {
            @Override
            protected String formatPropertyValue(final Object rowId,
                    final Object colId, final Property<?> property) {
                String result = super.formatPropertyValue(rowId, colId,
                        property);
                if (colId.equals("time")) {
                    result = DATEFORMAT.format(((Date) property.getValue()));
                } else if (colId.equals("price")) {
                    if (property != null && property.getValue() != null) {
                        return "$" + DECIMALFORMAT.format(property.getValue());
                    } else {
                        return "";
                    }
                }
                return result;
            }
        };
        table.setWidth("100%");
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectable(true);
        table.setPageLength(10);

        table.setColumnCollapsingAllowed(true);
        table.setNullSelectionAllowed(false);
        
        table.setColumnReorderingAllowed(true);
        table.setContainerDataSource(contactContainer);
        table.setSortContainerPropertyId("regdate");
        table.setSortAscending(false);

        table.addGeneratedColumn("propertyName", new Table.ColumnGenerator() {
            public Component generateCell(Table source, Object itemId, Object columnId) {
              CheckBox cb = new CheckBox(); 
              
              return cb;
            }
         });

        table.setVisibleColumns("regdate", "patname", "age", "gender","diagnosis");
        table.setColumnHeaders("Date","Name","Age","Gender","Diagnosis");
        table.setMultiSelect(false);

        table.addActionHandler(new TransactionsActionHandler());

        table.setImmediate(true);
        table.setEditable(false);
        
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            private static final long serialVersionUID = 2068314108919135281L;

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                     System.out.println(table.getValue());
                    Item item = table.getItem(table.getValue());
                    
                	DashboardUI.getCurrent().addWindow(new ECGDetailViewer(item));
                    
                }
            }
        });

        return table;
    }

    private boolean defaultColumnsVisible() {
        boolean result = true;
        for (String propertyId : DEFAULT_COLLAPSIBLE) {
            if (table.isColumnCollapsed(propertyId) == Page.getCurrent()
                    .getBrowserWindowWidth() < 800) {
                result = false;
            }
        }
        return result;
    }

    @Subscribe
    public void browserResized(final BrowserResizeEvent event) {
        // Some columns are collapsed when browser window width gets small
        // enough to make the table fit better.
        if (defaultColumnsVisible()) {
            for (String propertyId : DEFAULT_COLLAPSIBLE) {
                table.setColumnCollapsed(propertyId, Page.getCurrent()
                        .getBrowserWindowWidth() < 800);
            }
        }
        if(Page.getCurrent().getBrowserWindowWidth() > 570){
        	layout.setMargin(true);
        }else if (Page.getCurrent().getBrowserWindowWidth() <= 570){
        	layout.setMargin(new MarginInfo(true,false,true,false));
        }
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private class TransactionsActionHandler implements Handler {

        private final Action discard = new Action("Discard");

        private final Action details = new Action("View ECG");

        @Override
        public void handleAction(final Action action, final Object sender,
                final Object target) {
            if (action == discard) {
                Notification.show("Not implemented in this demo");
            } else if (action == details) {
                Item item = ((Table) sender).getItem(target);
                if (item != null) {
                	DashboardUI.getCurrent().addWindow(new ECGDetailViewer(item));
                    
                }
            }
        }

        @Override
        public Action[] getActions(final Object target, final Object sender) {
            return new Action[] { details, discard };
        }
    }

	public static void vizNewUpload() {
		
		table.setValue(table.firstItemId());
        Item item = table.getItem(table.getValue());
        
    	DashboardUI.getCurrent().addWindow(new ECGDetailViewer(item));
		
	}

}
