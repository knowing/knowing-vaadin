package de.lmu.ifi.dbs.knowing.vaadin.components;

import java.net.URISyntaxException;
import java.net.URL;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

import de.lmu.ifi.dbs.knowing.vaadin.Activator;
import de.lmu.ifi.dbs.knowing.vaadin.MainApplication;
import de.lmu.ifi.dbs.knowing.vaadin.ui.ComponentFactory;

import de.lmu.ifi.dbs.knowing.core.graph.xml.DataProcessingUnit;
import de.lmu.ifi.dbs.knowing.core.service.*;

public class DPUSelection extends CustomComponent {

	@AutoGenerated
	private GridLayout mainLayout;
	private Table dpuTable;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public DPUSelection() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		mainLayout.addComponent(buildDPUTable(), 0, 0);
		mainLayout.addComponent(buildInformationFrame(), 1, 0);
		mainLayout.addComponent(buildButtonBar(), 1, 1);

		Component c = mainLayout.getComponent(1, 0);
		mainLayout.setComponentAlignment(c, Alignment.TOP_RIGHT);
		
	}

	@AutoGenerated
	private void buildMainLayout() {
		// the main layout and components will be created here
		mainLayout = new GridLayout(2, 2);
		mainLayout.setColumnExpandRatio(0, 1);
		mainLayout.setColumnExpandRatio(1, 4);
		// mainLayout.setMargin(true);
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);
	}

	private Component buildInformationFrame() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setSpacing(true);
		Panel descriptionPanel = new Panel("Description");
		TextArea description = new TextArea();
		description.setReadOnly(true);
		descriptionPanel.addComponent(description);

		Table properties = new Table();
		properties.addContainerProperty("Node", String.class, "-");
		properties.addContainerProperty("Type", String.class, "-");
		properties.setSizeFull();

		layout.addComponent(descriptionPanel);
		layout.addComponent(properties);

		return layout;
	}

	private Component buildButtonBar() {
		HorizontalLayout bar = new HorizontalLayout();
		bar.setSizeFull();
		bar.setSpacing(true);
		Button ok = new Button("OK");
		Button cancel = new Button("Cancel");
		bar.addComponent(ok);
		bar.addComponent(cancel);

		bar.setExpandRatio(ok, 0);
		bar.setExpandRatio(cancel, 1);
		bar.setComponentAlignment(ok, Alignment.BOTTOM_LEFT);
		bar.setComponentAlignment(cancel, Alignment.BOTTOM_LEFT);
		return bar;
	}
	
	private Component buildDPUTable() {
		dpuTable = ComponentFactory.dpuTable();
		dpuTable.setImmediate(true);
		dpuTable.setSizeFull();
		
		dpuTable.addListener(new Property.ValueChangeListener() {
		    public void valueChange(ValueChangeEvent event) {
		    	String dpuName = dpuTable.getValue().toString();
		    	mainLayout.getWindow().showNotification("Selected: " + dpuName, Notification.TYPE_TRAY_NOTIFICATION);
		    	
		    	DataProcessingUnit dpu = Activator.getRegisteredDPU(dpuName);
		    	if(dpu == null) {
		    		mainLayout.getWindow().showNotification("Not Registered: " + dpuName, Notification.TYPE_ERROR_MESSAGE);
		    	}
		    	URL execPath = Activator.getRegisteredDPUPath(dpuName);
		    	IEvaluateService evaluateService = Activator.getEvaluateService();
		    	try {
					evaluateService.evaluate(dpu, MainApplication.uiFactory, execPath.toURI());
				} catch (URISyntaxException e) {
					e.printStackTrace();
					mainLayout.getWindow().showNotification("Malformed URI: " + e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
				}
		    	
		    }
		});
		return dpuTable;
	}

}
