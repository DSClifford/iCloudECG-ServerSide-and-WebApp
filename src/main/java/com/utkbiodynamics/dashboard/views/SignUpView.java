package com.utkbiodynamics.dashboard.views;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import com.utkbiodynamics.dashboard.database.RandomID;
import com.utkbiodynamics.dashboard.database.UpdateUser;
import com.utkbiodynamics.dashboard.event.DashboardEventBus;
import com.utkbiodynamics.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.utkbiodynamics.dashboard.event.DashboardEvent.ProfileUpdatedEvent;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.utkbiodynamics.dashboard.data.User;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SignUpView extends Window {

    public static final String ID = "signupwindow";

    private final BeanFieldGroup<User> fieldGroup;
    private User newuser = new User();
    /*
     * Fields for editing the User object are defined here as class members.
     * They are later bound to a FieldGroup by calling
     * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
     * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
     * the fields with the user object.
     */
    @PropertyId("firstName")
    private TextField firstNameField;
    @PropertyId("lastName")
    private TextField lastNameField;
    @PropertyId("email")
    private TextField emailField;
    @PropertyId("password")
    private PasswordField passwordField;
    @PropertyId("institution")
    private TextField institutionField;
    @PropertyId("sexBoolean")
    private OptionGroup sexField;
    @PropertyId("passConfirm")
    private PasswordField conpassField;
    @PropertyId("bday")
    private NativeSelect bday;
    @PropertyId("bmonth")
    private NativeSelect bmonth;
    @PropertyId("byear")
    private NativeSelect byear;

    private SignUpView() {
        addStyleName("profile-window");
        setId(ID);
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        final TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildLoginTab());
        detailsWrapper.setSelectedTab(0);
        
        fieldGroup = new BeanFieldGroup<User>(User.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(newuser);
        
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        final Button confirm = new Button("Create Account");
        confirm.addStyleName(ValoTheme.BUTTON_PRIMARY);
        confirm.setVisible(false);
        confirm.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	
            	try {
                    newuser.setUniqueID(RandomID.generateString());
                    newuser.setFirstName(firstNameField.getValue());
                    newuser.setLastName(lastNameField.getValue());
                    newuser.setInstitution(institutionField.getValue());
            		if(sexField.getValue() != null && sexField.getValue().equals(true)){
            			newuser.setGender("male");            			
            		}else if (sexField.getValue() != null && sexField.getValue().equals(false)){
            			newuser.setGender("female");
            		}else{
            			newuser.setGender(" ");
            		}
            		if (bday.getValue() != null && bmonth.getValue() != null && byear != null) {
            			DecimalFormat formatter = new DecimalFormat("00");
            			java.sql.Date birthDate = java.sql.Date.valueOf(formatter.format(byear.getValue())+"-"+formatter.format(bmonth.getValue())+"-"+formatter.format(bday.getValue()));

                        newuser.setbirthDate(birthDate);
            		}
            		fieldGroup.commit();
                    // Updated user should also be persisted to database. But
                    // not in this demo.
                    
                    Notification success = new Notification(
                            "Account created successfully");
                    success.setDelayMsec(2000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.BOTTOM_CENTER);
                    success.show(Page.getCurrent());

                    DashboardEventBus.post(new ProfileUpdatedEvent());

                    UpdateUser.saveNewUser(newuser);
                    
                    close();
                } catch (CommitException e) {
                    Notification.show("Error while adding user",
                            Type.ERROR_MESSAGE);
                    e.printStackTrace();
                } catch (Exception e) {
					// TODO Auto-generated catch block
                	Notification.show("Error while adding user",
                            Type.ERROR_MESSAGE);
                	e.printStackTrace();
				}

            }
            
        });
        
        final Button ok = new Button("OK");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                
            	if(conpassField.getValue().equals(passwordField.getValue())){

            		ok.setVisible(false);
            		confirm.setVisible(true);
                    detailsWrapper.addComponent(buildProfileTab());
            		detailsWrapper.setSelectedTab(1);
            		
            	}else {
            		
           		 Notification.show("Password Confirmation Doesn't Match",
                            Type.ERROR_MESSAGE);
           		 Notification.show("Password Confirmation Doesn't Match "+conpassField.getValue()+" "+passwordField.getValue(),
                            Type.ERROR_MESSAGE);
           		 conpassField.setIcon(FontAwesome.EXCLAMATION_CIRCLE);
            }
            }
        });
        
        footer.addComponent(ok);
        footer.addComponent(confirm);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        footer.setComponentAlignment(confirm, Alignment.TOP_RIGHT);
        
        content.addComponent(footer);
    }

    private Component buildProfileTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Profile");
        root.setIcon(FontAwesome.USER);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();


        FormLayout loginInfo = new FormLayout();
        loginInfo.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(loginInfo);
        root.setExpandRatio(loginInfo, 1);

        firstNameField = new TextField("First Name");
        firstNameField.setRequired(true);
        firstNameField.setImmediate(true);
        loginInfo.addComponent(firstNameField);
        
        lastNameField = new TextField("Last Name");
        lastNameField.setRequired(true);
        loginInfo.addComponent(lastNameField);
        
        institutionField = new TextField("Company/Institution");
        institutionField.setRequired(true);
        loginInfo.addComponent(institutionField);

        sexField = new OptionGroup("Sex");
        sexField.addItem(Boolean.FALSE);
        sexField.setItemCaption(Boolean.FALSE, "Female");
        sexField.addItem(Boolean.TRUE);
        sexField.setItemCaption(Boolean.TRUE, "Male");
        sexField.addStyleName("horizontal");
        loginInfo.addComponent(sexField);

        Label section = new Label("Date of Birth");
        section.addStyleName(ValoTheme.LABEL_LIGHT);
        loginInfo.addComponent(section);
        loginInfo.setComponentAlignment(section, Alignment.TOP_CENTER);

        Date currentdate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentdate);  //use java.util.Date object as arguement
        int currentyear = cal.get(Calendar.YEAR);
       
        bday = new NativeSelect("Day");
        for (int i = 1; i < 32; i++) {
            bday.addItem(i);
        }
        bday.setNullSelectionAllowed(true);
        bday.setValue(null);
        bday.setImmediate(true);
        //day.addListener(this);

        
        bmonth = new NativeSelect("Month");
        for (int i = 1; i < 13; i++) {
            bmonth.addItem(i);
        }
        
        bmonth.setNullSelectionAllowed(true);
        bmonth.setValue(null);
        bmonth.setImmediate(true);

        byear = new NativeSelect("Year");
        for (int i = currentyear; i > 1900; i--) {
            byear.addItem(i);
        }
        
        byear.setNullSelectionAllowed(true);
        byear.setValue(null);
        byear.setImmediate(true);

        HorizontalLayout bdateLayout = new HorizontalLayout();
        
        bdateLayout.addComponent(bday);
        bdateLayout.addComponent(bmonth);
        bdateLayout.addComponent(byear);

        
        loginInfo.addComponent(bdateLayout);


        return root;

    }

    
       private Component buildLoginTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Login");
        root.setIcon(FontAwesome.KEY);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");

        VerticalLayout pic = new VerticalLayout();
        pic.setSizeUndefined();
        pic.setSpacing(true);
        Image profilePic = new Image(null, new ThemeResource(
                "img/profile-pic-300px.jpg"));
        profilePic.setWidth(100.0f, Unit.PIXELS);
        pic.addComponent(profilePic);

        Button upload = new Button("Change…", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Notification.show("Not yet implemented");
            }
        });
        upload.addStyleName(ValoTheme.BUTTON_TINY);
        pic.addComponent(upload);

        root.addComponent(pic);

        FormLayout loginInfo = new FormLayout();
        loginInfo.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(loginInfo);
        root.setExpandRatio(loginInfo, 1);

        Label section = new Label("Email/Password");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        loginInfo.addComponent(section);

        emailField = new TextField("Email");
        emailField.setWidth("100%");
        emailField.setRequired(true);
        emailField.setNullRepresentation("");
        loginInfo.addComponent(emailField);

        passwordField = new PasswordField("Password");
        passwordField.setWidth("100%");
        passwordField.setRequired(true);
        passwordField.setNullRepresentation("");
        loginInfo.addComponent(passwordField);
        
        conpassField = new PasswordField("Confirm Password");
        conpassField.setWidth("100%");
        conpassField.setRequired(true);
        conpassField.setNullRepresentation("");
        loginInfo.addComponent(conpassField);

        return root;
    }

    public static void open() {
        DashboardEventBus.post(new CloseOpenWindowsEvent());
        Window w = new SignUpView();
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}
