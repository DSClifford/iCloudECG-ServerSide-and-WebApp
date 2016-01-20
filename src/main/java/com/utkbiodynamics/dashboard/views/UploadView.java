package com.utkbiodynamics.dashboard.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.utkbiodynamics.dashboard.DashboardUI;
import com.utkbiodynamics.dashboard.data.EcgRecord;
import com.utkbiodynamics.dashboard.database.AnalyzeEcg;
import com.utkbiodynamics.dashboard.database.RandomID;
import com.utkbiodynamics.dashboard.database.UpdateEcgs;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.PropertyId;
import com.utkbiodynamics.dashboard.data.User;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class UploadView extends VerticalLayout implements View {

	public static final String TITLE_ID = "dashboard-title";
	public static User currentUser;
	
	private Label titleLabel;
    private CssLayout uploadPanels;
    private Component content;
    private String randomID = RandomID.generateString();
    private Upload upload;
    
    @PropertyId("patientName")
    private TextField patientNameField;
    @PropertyId("patientGender")
    private OptionGroup genderField;
    @PropertyId("record_notes")
    private TextArea notesField;
    private EcgRecord newRecord;
	
	public UploadView() {

        setSizeFull();
        addStyleName("sales");
        System.out.println("MCR_CACHE_ROOT: "+ System.getenv("MCR_CACHE_ROOT"));
        System.out.println("JAVA_HOME: "+ System.getenv("JAVA_HOME"));
        System.out.println("HOME: "+ System.getenv("HOME"));
        
        addComponent(buildHeader());
        
        content = buildContent("info");
        addComponent(content);
        setExpandRatio(content, 1);
        setComponentAlignment(content, Alignment.TOP_CENTER);
        content.addStyleName("upldContent");
        
        currentUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        newRecord = new EcgRecord();
     }

   	private Component buildHeader() {
   	 HorizontalLayout header = new HorizontalLayout();
     header.addStyleName("viewheader");
     header.setSpacing(true);
     Responsive.makeResponsive(header);
     
     HorizontalLayout logoTitle = new HorizontalLayout();
     logoTitle.addStyleName("logoTitle");
     logoTitle.setSpacing(false);
     
     Image logoImage = new Image(null,
     	    new ThemeResource("img/logoCloudOnly.png"));
     logoImage.addStyleName("logoImage");
     titleLabel = new Label("Upload Records");
     titleLabel.setId(TITLE_ID);
     titleLabel.setSizeUndefined();
     titleLabel.addStyleName(ValoTheme.LABEL_H1);
     titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
     titleLabel.addStyleName("dashtitle");
     logoTitle.addComponent(logoImage);
     logoTitle.addComponent(titleLabel);
     logoTitle.setComponentAlignment(titleLabel, Alignment.MIDDLE_CENTER);
     header.addComponent(logoTitle);
     header.setComponentAlignment(logoTitle, Alignment.MIDDLE_LEFT);

        return header;    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private Component buildContent(String stage) {
    	
    	uploadPanels = new CssLayout();
    	uploadPanels.addStyleName("uploadPanels");
    	Responsive.makeResponsive(uploadPanels);
    	uploadPanels.setCaption("New Record Upload");
    	
    	if(stage.equals("info")){
    	uploadPanels.addComponent(buildUploadForm());
    	uploadPanels.addComponent(buildFooter("info"));
    	}
    	else {    	
    	Component fileUploader = buildUploadFile();
    	fileUploader.addStyleName("uploader");
    	Responsive.makeResponsive(fileUploader);
    	uploadPanels.addComponent(fileUploader);
    	uploadPanels.addComponent(buildFooter("file"));
    	}
		return CreateWrapper.createContentWrapperHelp(uploadPanels);
	}

    private Component buildUploadForm() {

    	HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Patient Information");
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");
        
        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);
        
        patientNameField = new TextField("First Name");
        details.addComponent(patientNameField);
        
        genderField = new OptionGroup("Sex");
        genderField.addItem("female");
        genderField.setItemCaption("female", "Female");
        genderField.addItem("male");
        genderField.setItemCaption("male", "Male");
        genderField.addStyleName("horizontal");
        details.addComponent(genderField);
        
        notesField = new TextArea("Notes");
        notesField.setWidth("100%");
        notesField.setRows(4);
        notesField.setNullRepresentation("");
        details.addComponent(notesField);
        
        return root;
    }
    
    private Component buildUploadFile() {
    	
    	VerticalLayout root = new VerticalLayout();
        root.setWidth(100.0f, Unit.PERCENTAGE);

        
        HorizontalLayout infoRecap = new HorizontalLayout();
        infoRecap.setCaption("Patient Information");
        infoRecap.setIcon(FontAwesome.USER);
        infoRecap.setWidth(100.0f, Unit.PERCENTAGE);
        infoRecap.setSpacing(true);
        infoRecap.setMargin(false);
        infoRecap.addStyleName("profile-form");
        
        Label patName = new Label(newRecord.getPatientName());
    	Label patGender = new Label(newRecord.getPatientGender());
    	Label patNotes = new Label(newRecord.getRecord_notes());
        
        Table table = new Table();
        table.setHeightUndefined();
        table.setWidth("90%");
        table.addStyleName(ValoTheme.TABLE_BORDERLESS);
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectable(true);

        table.addContainerProperty("Name", String.class, null);
        table.addContainerProperty("Gender", String.class, null);
        table.addContainerProperty("Notes", String.class, null);
        
        Object newItemId = table.addItem();
        Item row1 = table.getItem(newItemId);
        row1.getItemProperty("Name").setValue(patName.getValue().toString());
        row1.getItemProperty("Gender").setValue(patGender.getValue().toString());
        row1.getItemProperty("Notes").setValue(patNotes.getValue().toString());

        table.setPageLength(table.size());
        
    	
    	infoRecap.addComponents(table);
    	infoRecap.setComponentAlignment(table, Alignment.TOP_CENTER);
        
    	HorizontalLayout upform = new HorizontalLayout();
        upform.setWidth(100.0f, Unit.PERCENTAGE);
        upform.setSpacing(true);
        upform.setMargin(true);
        upform.addStyleName("profile-form");
    	upload = new Upload("Choose your ECG file", null);
        upload.setButtonCaption(null);
        upform.addComponent(upload);
        upform.setComponentAlignment(upload, Alignment.TOP_CENTER);
        
        root.addComponent(infoRecap);
        root.addComponent(upform);
        root.setComponentAlignment(infoRecap, Alignment.TOP_CENTER);
        root.setComponentAlignment(upform, Alignment.BOTTOM_CENTER);
        Component panel = CreateWrapper.createContentWrapper(root);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        final String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        
        
        class ImageUploader implements Receiver, SucceededListener {
            private static final long serialVersionUID = -1276759102490466761L;

            public File file;
            //public File file2;
            
            public OutputStream receiveUpload(String filename, String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Output stream to write to
              //  FileOutputStream fos2 = null;
                try {
                    // Open the file for writing.
                	//file = new File(basePath + "/records/" + randomID + ".txt");
                	file = new File("/var/lib/tomcat7/webapps/resources/records/"+ randomID + ".txt");
                	//file = new File("/var/lib/tomcat7/webapps/resources/records/" + randomID + ".txt");
                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                	Notification.show(
                            "Could not open file<br/>", e.getMessage(),
                            Notification.TYPE_ERROR_MESSAGE);
                    return null;
                }
                return fos; // Return the output stream to write to
            }

            public void uploadSucceeded(SucceededEvent event) {
                // Show the uploaded file in the image viewer
            	System.out.println("New File Uploaded: " + basePath + "/records/" + randomID + ".txt");
            	System.out.println("C:"+File.separator+"Users"+File.separator+"DSClifford"+File.separator+"Downloads"+File.separator+"dashboard-demo-master"+File.separator+"dashboard-demo-master"+File.separator+"src"+File.separator+"main"+File.separator+"webapp"+File.separator+"records"+File.separator+randomID + ".txt");
                
                UpdateEcgs.saveEcg(newRecord);
                DashboardUI.getCurrent().setPollInterval(500);
                Runnable r = new Runnable() {
                    public void run() {
                    	
                        AnalyzeEcg.main(randomID,false);
                        
                        DashboardUI.getCurrent().setPollInterval(-1);
                    }
                };

                new Thread(r).start();
                     
                DashboardUI.getCurrent().addWindow(postUploadWindow());
                
                //UI.getCurrent().getNavigator()
                //.navigateTo("upload");
            }
        };
        final ImageUploader uploader = new ImageUploader(); 
        upload.setReceiver(uploader);
        upload.addListener(uploader);
        
//////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        
        
        
        return panel;
    }
    
    public Window postUploadWindow() {
    	Window postUploadWindow = new Window();
    	VerticalLayout layout = new VerticalLayout();
    	
    	postUploadWindow.setStyleName("post-upload-window");
    	postUploadWindow.setWidth("400px");
    	postUploadWindow.setHeight("400px");
    	postUploadWindow.center();
    	
    	layout.setSpacing(true);
    	
    	Label success = new Label("Record Uploaded Successfully!");
    	success.addStyleName(ValoTheme.LABEL_SUCCESS);
    	layout.addComponent(success);
    	layout.setComponentAlignment(success, Alignment.TOP_CENTER);
    	Button upld = new Button("Upload Another");
    	Button viz = new Button("Visualize Record");
    	upld.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    	viz.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    	layout.addComponent(viz);
    	layout.setComponentAlignment(viz, Alignment.MIDDLE_CENTER);
    	layout.addComponent(upld);
    	layout.setComponentAlignment(upld, Alignment.BOTTOM_CENTER);
    	postUploadWindow.setContent(layout);

    	upld.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                UI.getCurrent().getNavigator()
                .navigateTo("upload");

            	
            }
        });
    	
    	viz.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                UI.getCurrent().getNavigator()
                .navigateTo("my records");
          
                MyRecordsView.vizNewUpload();

                    
            }
        });
    	
		return postUploadWindow;
	}

	private Component buildFooter(String stage) {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        if(stage.equals("info")){
        Button next = new Button("Next");
        next.addStyleName(ValoTheme.BUTTON_PRIMARY);
        next.setIcon(FontAwesome.ARROW_CIRCLE_RIGHT);
        next.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	
                newRecord.setPatientName(patientNameField.getValue());
                newRecord.setPatientGender(genderField.getValue().toString());
                newRecord.setPatientAge(21);
                newRecord.setRecord_notes(notesField.getValue());
                newRecord.setDiagnosis("Not Yet Determined");
                newRecord.setQualityGrade("Not Yet Determined");
                newRecord.setSuspectedMisplacement("Not Yet Determined");
                newRecord.setSamplingRate(250);
                newRecord.setId(randomID);
                
             removeComponent(content);
                content = null;
                content = buildContent("file");
             addComponent(content);
                setExpandRatio(content, 1);
                content.addStyleName("upldContent");
                setComponentAlignment(content, Alignment.TOP_CENTER);
                

            }
        });
        next.focus();
        footer.addComponent(next);
        footer.setComponentAlignment(next, Alignment.TOP_RIGHT);
        }
        else{
            final Button upld = new Button("Confirm & Upload");
            upld.addStyleName(ValoTheme.BUTTON_PRIMARY);
            upld.setIcon(FontAwesome.ARROW_CIRCLE_RIGHT);
            upld.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                	
                	upld.setImmediate(true);
                    
                    upload.submitUpload();
                    
                        // Updated user should also be persisted to database. But
                        // not in this demo.
                        
                }
            });
            upld.focus();
            footer.addComponent(upld);
            footer.setComponentAlignment(upld, Alignment.TOP_RIGHT);
        }
        return footer;
    }
}

