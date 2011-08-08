package de.lmu.ifi.dbs.knowing.vaadin;

import akka.actor.TypedActor;
import akka.actor.TypedActorFactory;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.lmu.ifi.dbs.knowing.vaadin.ui.VaadinUIFactory;
import de.lmu.ifi.dbs.knowing.core.factory.*;

public class MainApplication extends Application {

	private Window main;
	private VerticalLayout mainLayout;
	private MenuBar.MenuItem homeMenu;
	private Window aboutWindow;

	@Override
	public void init() {
		main = new Window("Knowing Vaadin Application");
		mainLayout = (VerticalLayout) main.getContent();
		mainLayout.setMargin(false);
		// mainLayout.setStyleName("blue");
		setMainWindow(main);

		mainLayout.setSizeFull();
		mainLayout.addComponent(getMenu());

		TypedActor.newInstance(UIFactory.class, new TypedActorFactory() {
			@Override
			public TypedActor create() {
				return new VaadinUIFactory(main);
			}
		});
	}

	@SuppressWarnings("serial")
	private MenuBar getMenu() {
		MenuBar menubar = new MenuBar();
		menubar.setWidth("100%");
		homeMenu = menubar.addItem("Home", null);

		homeMenu.addItem("Run Process", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				main.showNotification("Run your own DPU soon!");
			}
		});

		homeMenu.addItem("Show Services", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				main.showNotification("See what you can do soon");
			}
		});

		homeMenu.addSeparator();

		homeMenu.addItem("Logout", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				main.showNotification("Run your own DPU soon!");
			}
		});

		final MenuBar.MenuItem viewMenu = menubar.addItem("Help", null);
		viewMenu.addItem("About...", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				 main.addWindow(getAboutDialog());
			}
		});

		return menubar;
	}

	private Window getAboutDialog() {
		if (aboutWindow == null) {
			aboutWindow = new Window("About...");
			aboutWindow.setModal(true);
			aboutWindow.setWidth("400px");

			VerticalLayout layout = (VerticalLayout) aboutWindow.getContent();
			layout.setMargin(true);
			layout.setSpacing(true);
			layout.setStyleName("grey");

			CssLayout titleLayout = new CssLayout();
			H2 title = new H2("Knowing Vaadin Application");
			titleLayout.addComponent(title);
			SmallText description = new SmallText("<br>Copyright (c) Nepomuk Seiler, LMU Database <br>"
					+ "Licensed under Apache Software Foundation 2.0 license (ASF).<br><br>" + "This software contains modules licenced under<br>"
					+ " the Apache Software Foundation 2.0 license (ASF) and EPL<br><br>"
					+ "Many thanks to Kai Tödter, Chris Brind, Neil Bartlett and<br>" + " Petter Holmström for their OSGi and Vaadin<br>"
					+ " related work, blogs, and bundles.<br><br>" + "The icons are from the Silk icon set by Mark James<br>"
					+ "<a href=\"http://www.famfamfam.com/lab/icons/silk/\">http://www.famfamfam.com/lab/icons/silk/</a>");
			description.setSizeUndefined();
			description.setContentMode(Label.CONTENT_XHTML);

			titleLayout.addComponent(description);
			aboutWindow.addComponent(titleLayout);

			@SuppressWarnings("serial")
			Button close = new Button("Close", new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					(aboutWindow.getParent()).removeWindow(aboutWindow);
				}

			});
			layout.addComponent(close);
			layout.setComponentAlignment(close, Alignment.TOP_RIGHT);
		}
		return aboutWindow;
	}

	@SuppressWarnings("serial")
	class H1 extends Label {
		public H1(String caption) {
			super(caption);
			setSizeUndefined();
			setStyleName(Reindeer.LABEL_H1);
		}
	}

	@SuppressWarnings("serial")
	class H2 extends Label {
		public H2(String caption) {
			super(caption);
			setSizeUndefined();
			setStyleName(Reindeer.LABEL_H2);
		}
	}

	@SuppressWarnings("serial")
	class SmallText extends Label {
		public SmallText(String caption) {
			super(caption);
			setStyleName(Reindeer.LABEL_SMALL);
		}
	}
}
