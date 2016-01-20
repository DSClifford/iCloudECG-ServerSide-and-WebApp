package com.utkbiodynamics.dashboard.views;

import java.util.Collection;

import com.google.common.eventbus.Subscribe;
import com.utkbiodynamics.dashboard.DashboardUI;
import com.utkbiodynamics.dashboard.component.DashECGOverviewPlot;
import com.utkbiodynamics.dashboard.component.SparklineChart;
import com.utkbiodynamics.dashboard.data.DashboardNotification;
import com.utkbiodynamics.dashboard.event.DashboardEventBus;
import com.utkbiodynamics.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.utkbiodynamics.dashboard.event.DashboardEvent.NotificationsCountUpdatedEvent;
import com.utkbiodynamics.dashboard.views.DashboardEdit.DashboardEditListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class DashboardView extends Panel implements View,
        DashboardEditListener {

    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";

    private Label titleLabel;
    private NotificationsButton notificationsButton;
    private CssLayout dashboardPanels;
    private final VerticalLayout root;
    private final CssLayout dashEcgContainer;
    private Window notificationsWindow;

    public DashboardView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        
        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);
        
        dashEcgContainer = new CssLayout();
        dashEcgContainer.setWidth("100%");
        dashEcgContainer.addStyleName("dashboard-ecg");
        Responsive.makeResponsive(dashEcgContainer);
        dashEcgContainer.setImmediate(true);

        root.addComponent(buildHeader());
        root.addComponent(buildSparklines());
        root.addComponent(dashEcgContainer);

        dashEcgContainer.addComponent(new LazyLoadWrapper(buildTopGrossingMovies(0)));
       
        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                DashboardEventBus.post(new CloseOpenWindowsEvent());
            }
        });
    }

    private Component buildSparklines() {
        final CssLayout sparks = new CssLayout();
        sparks.addStyleName("sparks");
        sparks.setWidth("100%");
        Responsive.makeResponsive(sparks);

        final SparklineChart s1 = new SparklineChart(0);
        sparks.addComponent(s1);
        s1.addStyleName("miniECG-clicked");
        
        final SparklineChart s2 = new SparklineChart(1);
        sparks.addComponent(s2);

        final SparklineChart s3 = new SparklineChart(2);
        sparks.addComponent(s3);

        final SparklineChart s4 = new SparklineChart(3);
        sparks.addComponent(s4);
        
        s1.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                DashboardEventBus.post(new CloseOpenWindowsEvent());
                miniecgreset(s2,s3,s4);
                s1.addStyleName("miniECG-clicked");
                dashEcgContainer.removeAllComponents();
                dashEcgContainer.addComponent(buildTopGrossingMovies(0));
                
            }
        });
        s2.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                DashboardEventBus.post(new CloseOpenWindowsEvent());
                miniecgreset(s1,s3,s4);
                s2.addStyleName("miniECG-clicked");
                dashEcgContainer.removeAllComponents();
                dashEcgContainer.addComponent(buildTopGrossingMovies(1));
            }
        });
        s3.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                DashboardEventBus.post(new CloseOpenWindowsEvent());
                miniecgreset(s2,s1,s4);
                s3.addStyleName("miniECG-clicked");
                dashEcgContainer.removeAllComponents();
                dashEcgContainer.addComponent(buildTopGrossingMovies(2));
            }
        });
        s4.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                DashboardEventBus.post(new CloseOpenWindowsEvent());
                miniecgreset(s2,s3,s1);
                s4.addStyleName("miniECG-clicked");
                dashEcgContainer.removeAllComponents();
                dashEcgContainer.addComponent(buildTopGrossingMovies(3));
            }
        });

        return sparks;
    }

    private void miniecgreset(SparklineChart ss1, SparklineChart ss2, SparklineChart ss3) {
    	ss1.removeStyleName("miniECG-clicked");
    	ss2.removeStyleName("miniECG-clicked");
    	ss3.removeStyleName("miniECG-clicked");
    	
		
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
        titleLabel = new Label("Dashboard");
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

        notificationsButton = buildNotificationsButton();
        Component edit = buildEditButton();
        HorizontalLayout tools = new HorizontalLayout(notificationsButton, edit);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private NotificationsButton buildNotificationsButton() {
        NotificationsButton result = new NotificationsButton();
        result.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                openNotificationsPopup(event);
            }
        });
        return result;
    }

    private Component buildEditButton() {
        Button result = new Button();
        result.setId(EDIT_ID);
        result.setIcon(FontAwesome.EDIT);
        result.addStyleName("icon-edit");
        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        result.setDescription("Edit Settings");

        return result;
    }

    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

        //dashboardPanels.addComponent(buildTopGrossingMovies());
        return dashboardPanels;
    }

    private Component buildTopGrossingMovies(int recIndex) {
        DashECGOverviewPlot dashEcgOverviewPlot = new DashECGOverviewPlot(recIndex);
        dashEcgOverviewPlot.setSizeFull();
        return CreateWrapper.createContentWrapperHelp(dashEcgOverviewPlot);
    }

    private void openNotificationsPopup(final ClickEvent event) {
        VerticalLayout notificationsLayout = new VerticalLayout();
        notificationsLayout.setMargin(true);
        notificationsLayout.setSpacing(true);

        Label title = new Label("Notifications");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        notificationsLayout.addComponent(title);

        Collection<DashboardNotification> notifications = DashboardUI
                .getDataProvider().getNotifications();
        DashboardEventBus.post(new NotificationsCountUpdatedEvent());

        for (DashboardNotification notification : notifications) {
            VerticalLayout notificationLayout = new VerticalLayout();
            notificationLayout.addStyleName("notification-item");

            Label titleLabel = new Label(notification.getFirstName() + " "
                    + notification.getLastName() + " "
                    + notification.getAction());
            titleLabel.addStyleName("notification-title");

            Label timeLabel = new Label(notification.getPrettyTime());
            timeLabel.addStyleName("notification-time");

            Label contentLabel = new Label(notification.getContent());
            contentLabel.addStyleName("notification-content");

            notificationLayout.addComponents(titleLabel, timeLabel,
                    contentLabel);
            notificationsLayout.addComponent(notificationLayout);
        }

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth("100%");
        Button showAll = new Button("View All Notifications",
                new ClickListener() {
                    @Override
                    public void buttonClick(final ClickEvent event) {
                        Notification.show("Not implemented in this demo");
                    }
                });
        showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showAll.addStyleName(ValoTheme.BUTTON_SMALL);
        footer.addComponent(showAll);
        footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
        notificationsLayout.addComponent(footer);

        if (notificationsWindow == null) {
            notificationsWindow = new Window();
            notificationsWindow.setWidth(300.0f, Unit.PIXELS);
            notificationsWindow.addStyleName("notifications");
            notificationsWindow.setClosable(false);
            notificationsWindow.setResizable(false);
            notificationsWindow.setDraggable(false);
            notificationsWindow.setCloseShortcut(KeyCode.ESCAPE, null);
            notificationsWindow.setContent(notificationsLayout);
        }

        if (!notificationsWindow.isAttached()) {
            notificationsWindow.setPositionY(event.getClientY()
                    - event.getRelativeY() + 40);
            getUI().addWindow(notificationsWindow);
            notificationsWindow.focus();
        } else {
            notificationsWindow.close();
        }
    }

    @Override
    public void enter(final ViewChangeEvent event) {
        notificationsButton.updateNotificationsCount(null);
    }

    @Override
    public void dashboardNameEdited(final String name) {
        titleLabel.setValue(name);
    }

    public static final class NotificationsButton extends Button {
        private static final String STYLE_UNREAD = "unread";
        public static final String ID = "dashboard-notifications";

        public NotificationsButton() {
            setIcon(FontAwesome.BELL);
            setId(ID);
            addStyleName("notifications");
            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            DashboardEventBus.register(this);
        }

        @Subscribe
        public void updateNotificationsCount(
                final NotificationsCountUpdatedEvent event) {
            setUnreadCount(DashboardUI.getDataProvider()
                    .getUnreadNotificationsCount());
        }

        public void setUnreadCount(final int count) {
            setCaption(String.valueOf(count));

            String description = "Notifications";
            if (count > 0) {
                addStyleName(STYLE_UNREAD);
                description += " (" + count + " unread)";
            } else {
                removeStyleName(STYLE_UNREAD);
            }
            setDescription(description);
        }
    }

}
