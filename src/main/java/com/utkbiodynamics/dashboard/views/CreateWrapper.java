package com.utkbiodynamics.dashboard.views;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

public class CreateWrapper {

	 public static Component createContentWrapperHelp(final Component content) {
	        final CssLayout slot = new CssLayout();
	        slot.setWidth("100%");
	        slot.addStyleName("dashboard-panel-slot");

	        CssLayout card = new CssLayout();
	        card.setWidth("100%");
	        card.addStyleName(ValoTheme.LAYOUT_CARD);

	        HorizontalLayout toolbar = new HorizontalLayout();
	        toolbar.addStyleName("dashboard-panel-toolbar");
	        toolbar.setWidth("100%");

	        Label caption = new Label(content.getCaption());
	        caption.addStyleName(ValoTheme.LABEL_H4);
	        caption.addStyleName(ValoTheme.LABEL_COLORED);
	        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        content.setCaption(null);

	        MenuBar tools = new MenuBar();
	        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
	        
	        MenuItem root = tools.addItem("", FontAwesome.QUESTION_CIRCLE, null);
	        root.addItem("Configure", new Command() {
	            @Override
	            public void menuSelected(final MenuItem selectedItem) {
	                Notification.show("Function Pending");
	            }
	        });
	        root.addSeparator();
	        root.addItem("Close", new Command() {
	            @Override
	            public void menuSelected(final MenuItem selectedItem) {
	                Notification.show("Function Pending");
	            }
	        });

	        toolbar.addComponents(caption, tools);
	        toolbar.setExpandRatio(caption, 1);
	        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

	        card.addComponents(toolbar, content);
	        slot.addComponent(card);
	        return slot;
	    }

	 public static Component createContentWrapper(final Component content) {
	        final CssLayout slot = new CssLayout();
	        slot.setWidth("100%");
	        slot.addStyleName("dashboard-panel-slot");

	        CssLayout card = new CssLayout();
	        card.setWidth("100%");
	        card.addStyleName(ValoTheme.LAYOUT_CARD);

	        HorizontalLayout toolbar = new HorizontalLayout();
	        toolbar.addStyleName("dashboard-panel-toolbar");
	        toolbar.setWidth("100%");

	        Label caption = new Label(content.getCaption());
	        caption.addStyleName(ValoTheme.LABEL_H4);
	        caption.addStyleName(ValoTheme.LABEL_COLORED);
	        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        content.setCaption(null);

	        toolbar.addComponents(caption);
	        toolbar.setExpandRatio(caption, 1);
	        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

	        card.addComponents(toolbar, content);
	        slot.addComponent(card);
	        return slot;
	    }

}
