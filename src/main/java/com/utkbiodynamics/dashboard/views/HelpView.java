package com.utkbiodynamics.dashboard.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.common.eventbus.Subscribe;
import com.utkbiodynamics.dashboard.DashboardUI;
import com.utkbiodynamics.dashboard.event.DashboardEventBus;
import com.utkbiodynamics.dashboard.event.DashboardEvent.BrowserResizeEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventResize;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.MoveEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class HelpView extends CssLayout implements View {

    private Calendar calendar;
    public static final String TITLE_ID = "help-title";

    public HelpView() {
    	setSizeFull();
        addStyleName("transactions");
        DashboardEventBus.register(this);

        addComponent(buildHeader());
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
        Label titleLabel = new Label("Help");
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

        return header;
    }
    
    @Override
    public void detach() {
        super.detach();
        // A new instance of ScheduleView is created every time it's navigated
        // to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }

    @Subscribe
    public void browserWindowResized(final BrowserResizeEvent event) {
        if (Page.getCurrent().getBrowserWindowWidth() < 800) {
            calendar.setEndDate(calendar.getStartDate());
        }
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }


}
