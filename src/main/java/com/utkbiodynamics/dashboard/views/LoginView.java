package com.utkbiodynamics.dashboard.views;

import com.utkbiodynamics.dashboard.event.DashboardEventBus;
import com.utkbiodynamics.dashboard.event.DashboardEvent.UserLoginRequestedEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

    public LoginView() {
        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

/*        Notification notification = new Notification(
                "Welcome to Dashboard Demo");
        notification
                .setDescription("<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(20000);
        notification.show(Page.getCurrent());
*/
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        final HorizontalLayout signupGuest = new HorizontalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        //signupGuest.setSizeUndefined();
        signupGuest.setSpacing(true);
        signupGuest.setMargin(false);
        Responsive.makeResponsive(signupGuest);
        
        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(new CheckBox("Remember me", true));
        loginPanel.addComponent(signupGuest);
        loginPanel.setComponentAlignment(signupGuest, Alignment.TOP_CENTER);
        
        Button signupButton = buildSignup();
        signupGuest.addComponent(signupButton);
        signupGuest.setComponentAlignment(signupButton,Alignment.TOP_LEFT);
        Button guestButton = buildGuestLogin();
        signupGuest.addComponent(guestButton);
        signupGuest.setComponentAlignment(guestButton,Alignment.TOP_RIGHT);
        return loginPanel;
    }

    private Button buildSignup() {
    	
    	final Button guestBtn = new Button("New User");
        guestBtn.addStyleName(ValoTheme.BUTTON_SMALL);
        guestBtn.addStyleName("guest-btn");
        
        guestBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	SignUpView.open();
            }
        });

    	
		return guestBtn;
	}

	private Button buildGuestLogin() {
		
    	final Button guestBtn = new Button("Guest Login");
        guestBtn.addStyleName(ValoTheme.BUTTON_SMALL);
        guestBtn.addStyleName("guest-btn");
        
        guestBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                DashboardEventBus.post(new UserLoginRequestedEvent("Guest@utk.edu", "GuestPASSiCloudECG"));
            }
        });

    	
		return guestBtn;
	}

	private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Username");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        username.focus();

        final PasswordField password = new PasswordField("Password");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Sign In");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);


        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                DashboardEventBus.post(new UserLoginRequestedEvent(username
                        .getValue(), password.getValue()));
            }
        });
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        return labels;
    }

}
