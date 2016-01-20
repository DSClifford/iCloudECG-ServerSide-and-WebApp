package com.utkbiodynamics.dashboard;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.eventbus.Subscribe;
import com.utkbiodynamics.dashboard.database.Authentication;
import com.utkbiodynamics.dashboard.database.RandomID;
import com.utkbiodynamics.dashboard.database.RetrieveUser;
import com.utkbiodynamics.dashboard.event.DashboardEventBus;
import com.utkbiodynamics.dashboard.event.DashboardEvent.BrowserResizeEvent;
import com.utkbiodynamics.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.utkbiodynamics.dashboard.event.DashboardEvent.UserLoggedOutEvent;
import com.utkbiodynamics.dashboard.event.DashboardEvent.UserLoginRequestedEvent;
import com.utkbiodynamics.dashboard.views.LoginView;
import com.utkbiodynamics.dashboard.views.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.utkbiodynamics.dashboard.data.User;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@Theme("dashboard")
@Widgetset("com.utkbiodynamics.dashboard.DashboardWidgetSet")
@Title("iCloudECG")
@SuppressWarnings("serial")
public final class DashboardUI extends UI {

    private final DashboardEventBus dashboardEventbus = new DashboardEventBus();
    private boolean android;

    @Override
    protected void init(final VaadinRequest request) {
        setLocale(Locale.US);
        android = Page.getCurrent().getWebBrowser().isAndroid();    
        String randomID = RandomID.generateString();
		System.out.println(randomID);
        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(
                new BrowserWindowResizeListener() {
                    @Override
                    public void browserWindowResized(
                            final BrowserWindowResizeEvent event) {
                        DashboardEventBus.post(new BrowserResizeEvent());
                    }
                });
    }
    
    protected void doGet(
    	    HttpServletRequest request,
    	    HttpServletResponse response)
    	      throws ServletException, IOException {

    	    

    	}
    
    private void updateContent() {
        User user = (User) VaadinSession.getCurrent().getAttribute(
                User.class.getName());
        if (user != null) {
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) throws SQLException {
            	
    	boolean authenticated = Authentication.userAuthentication(
    			event.getUserName(), event.getPassword());
            if(!authenticated) {
            	updateContent();
            	 Notification notification = new Notification(
                         "LOGIN FAILED");
                 notification
                         .setDescription("LOGIN FAILED");
                 notification.setHtmlContentAllowed(true);
                 notification.setStyleName("tray dark small closable login-help");
                 notification.setPosition(Position.BOTTOM_CENTER);
                 notification.setDelayMsec(20000);
                 notification.show(Page.getCurrent());
            }
            else if(authenticated) {
            	System.out.println("Login Successful: " + event.getUserName());
            	VaadinSession.getCurrent().setAttribute(User.class.getName(), event.getUserName());
            	RetrieveUser.retrieveUser(event.getUserName());
            	System.out.println("User.class.getName() = " + User.class.getName());
            	updateContent();
            	VaadinSession.getCurrent().setAttribute("android",android);
             }
    }

    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }
   
    public static DashboardEventBus getDashboardEventbus() {
        return ((DashboardUI) getCurrent()).dashboardEventbus;
    }

}
